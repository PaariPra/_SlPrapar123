package com.chettapps.videoeditor.videocutermerger.animoto.android.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;

import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.appcompat.widget.ActivityChooserView;

import com.google.android.exoplayer2.extractor.ts.PsExtractor;

import java.util.ArrayList;
import java.util.Collections;

/* loaded from: classes.dex */
public class DraggableGridView extends ViewGroup implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {
    public static int animT = 150;
    public static float childRatio = 0.9f;
    protected int childSize;
    protected int colCount;
    protected int dpi;
    private AdapterView.OnItemClickListener onItemClickListener;
    protected OnRearrangeListener onRearrangeListener;
    protected int padding;
    protected OnClickListener secondaryOnClickListener;
    protected int dragged = -1;
    protected boolean enabled = true;
    protected Handler handler = new Handler();
    protected float lastDelta = 0.0f;
    protected int lastTarget = -1;
    protected int lastX = -1;
    protected int lastY = -1;
    protected ArrayList<Integer> newPositions = new ArrayList<>();
    protected int scroll = 0;
    protected boolean touching = false;
    protected Runnable updateTask = new Runnable() { // from class: com.animoto.android.views.DraggableGridView.1
        @Override // java.lang.Runnable
        public void run() {
            if (DraggableGridView.this.dragged != -1) {
                if (DraggableGridView.this.lastY < DraggableGridView.this.padding * 3 && DraggableGridView.this.scroll > 0) {
                    DraggableGridView draggableGridView = DraggableGridView.this;
                    draggableGridView.scroll -= 20;
                } else if (DraggableGridView.this.lastY > (DraggableGridView.this.getBottom() - DraggableGridView.this.getTop()) - (DraggableGridView.this.padding * 3) && DraggableGridView.this.scroll < DraggableGridView.this.getMaxScroll()) {
                    DraggableGridView.this.scroll += 20;
                }
            } else if (DraggableGridView.this.lastDelta != 0.0f && !DraggableGridView.this.touching) {
                DraggableGridView draggableGridView2 = DraggableGridView.this;
                draggableGridView2.scroll = (int) (draggableGridView2.scroll + DraggableGridView.this.lastDelta);
                DraggableGridView draggableGridView3 = DraggableGridView.this;
                draggableGridView3.lastDelta = (float) (draggableGridView3.lastDelta * 0.9d);
                if (Math.abs(DraggableGridView.this.lastDelta) < 0.25d) {
                    DraggableGridView.this.lastDelta = 0.0f;
                }
            }
            DraggableGridView.this.clampScroll();
            DraggableGridView.this.onLayout(true, DraggableGridView.this.getLeft(), DraggableGridView.this.getTop(), DraggableGridView.this.getRight(), DraggableGridView.this.getBottom());
            DraggableGridView.this.handler.postDelayed(this, 25L);
        }
    };

    public DraggableGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setListeners();
        this.handler.removeCallbacks(this.updateTask);
        this.handler.postAtTime(this.updateTask, SystemClock.uptimeMillis() + 500);
        setChildrenDrawingOrderEnabled(true);
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        this.dpi = metrics.densityDpi;
    }

    protected void setListeners() {
        setOnTouchListener(this);
        super.setOnClickListener(this);
        setOnLongClickListener(this);
    }

    @Override // android.view.View
    public void setOnClickListener(OnClickListener l) {
        this.secondaryOnClickListener = l;
    }

    @Override // android.view.ViewGroup
    public void addView(View child) {
        super.addView(child);
        this.newPositions.add(-1);
    }

    @Override // android.view.ViewGroup
    public void removeViewAt(int index) {
        super.removeViewAt(index);
        this.newPositions.remove(index);
    }


    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        float w = (r - l) / (this.dpi / 160.0f);
        this.colCount = 2;
        int sub = PsExtractor.VIDEO_STREAM_MASK;
        float w2 = w - 280.0f;
        while (w2 > 0.0f) {
            this.colCount++;
            w2 -= sub;
            sub += 40;
        }
        this.childSize = (r - l) / this.colCount;
        this.childSize = Math.round(this.childSize * childRatio);
        this.padding = ((r - l) - (this.childSize * this.colCount)) / (this.colCount + 1);
        for (int i = 0; i < getChildCount(); i++) {
            if (i != this.dragged) {
                Point xy = getCoorFromIndex(i);
                getChildAt(i).layout(xy.x, xy.y, xy.x + this.childSize, xy.y + this.childSize);
            }
        }
    }

    @Override // android.view.ViewGroup
    protected int getChildDrawingOrder(int childCount, int i) {
        if (this.dragged == -1) {
            return i;
        }
        if (i == childCount - 1) {
            return this.dragged;
        }
        if (i >= this.dragged) {
            return i + 1;
        }
        return i;
    }

    public int getIndexFromCoor(int x, int y) {
        int col = getColOrRowFromCoor(x);
        int row = getColOrRowFromCoor(this.scroll + y);
        if (col == -1 || row == -1) {
            return -1;
        }
        int index = (this.colCount * row) + col;
        if (index >= getChildCount()) {
            return -1;
        }
        return index;
    }

    protected int getColOrRowFromCoor(int coor) {
        int coor2 = coor - this.padding;
        int i = 0;
        while (coor2 > 0) {
            if (coor2 < this.childSize) {
                return i;
            }
            coor2 -= this.childSize + this.padding;
            i++;
        }
        return -1;
    }

    protected int getTargetFromCoor(int x, int y) {
        if (getColOrRowFromCoor(this.scroll + y) == -1) {
            return -1;
        }
        int leftPos = getIndexFromCoor(x - (this.childSize / 4), y);
        int rightPos = getIndexFromCoor((this.childSize / 4) + x, y);
        if (!((leftPos == -1 && rightPos == -1) || leftPos == rightPos)) {
            int target = -1;
            if (rightPos > -1) {
                target = rightPos;
            } else if (leftPos > -1) {
                target = leftPos + 1;
            }
            if (this.dragged < target) {
                return target - 1;
            }
            return target;
        }
        return -1;
    }

    protected Point getCoorFromIndex(int index) {
        return new Point(this.padding + ((this.childSize + this.padding) * (index % this.colCount)), (this.padding + ((this.childSize + this.padding) * (index / this.colCount))) - this.scroll);
    }

    public int getIndexOf(View child) {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) == child) {
                return i;
            }
        }
        return -1;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (this.enabled) {
            if (this.secondaryOnClickListener != null) {
                this.secondaryOnClickListener.onClick(view);
            }
            if (this.onItemClickListener != null && getLastIndex() != -1) {
                this.onItemClickListener.onItemClick(null, getChildAt(getLastIndex()), getLastIndex(), getLastIndex() / this.colCount);
            }
        }
    }

    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View view) {
        int index;
        if (!this.enabled || (index = getLastIndex()) == -1) {
            return false;
        }
        this.dragged = index;
        animateDragged();
        return true;
    }

    @Override // android.view.View.OnTouchListener
    @SuppressLint({"WrongCall"})
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction() & 255) {
            case 0:
                this.enabled = true;
                this.lastX = (int) event.getX();
                this.lastY = (int) event.getY();
                this.touching = true;
                break;
            case 1:
                if (this.dragged != -1) {
                    View v = getChildAt(this.dragged);
                    if (this.lastTarget != -1) {
                        reorderChildren();
                    } else {
                        Point xy = getCoorFromIndex(this.dragged);
                        v.layout(xy.x, xy.y, xy.x + this.childSize, xy.y + this.childSize);
                    }
                    v.clearAnimation();
                    if (v instanceof ImageView) {
                        ((ImageView) v).setAlpha(255);
                    }
                    this.lastTarget = -1;
                    this.dragged = -1;
                }
                this.touching = false;
                break;
            case 2:
                int delta = this.lastY - ((int) event.getY());
                if (this.dragged != -1) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    int l = x - ((this.childSize * 3) / 4);
                    int t = y - ((this.childSize * 3) / 4);
                    getChildAt(this.dragged).layout(l, t, ((this.childSize * 3) / 2) + l, ((this.childSize * 3) / 2) + t);
                    int target = getTargetFromCoor(x, y);
                    if (!(this.lastTarget == target || target == -1)) {
                        animateGap(target);
                        this.lastTarget = target;
                    }
                } else {
                    this.scroll += delta;
                    clampScroll();
                    if (Math.abs(delta) > 2) {
                        this.enabled = false;
                    }
                    onLayout(true, getLeft(), getTop(), getRight(), getBottom());
                }
                this.lastX = (int) event.getX();
                this.lastY = (int) event.getY();
                this.lastDelta = delta;
                break;
        }
        if (this.dragged != -1) {
            return true;
        }
        return false;
    }

    protected void animateDragged() {
        View v = getChildAt(this.dragged);
        int l = (getCoorFromIndex(this.dragged).x + (this.childSize / 2)) - ((this.childSize * 3) / 4);
        int t = (getCoorFromIndex(this.dragged).y + (this.childSize / 2)) - ((this.childSize * 3) / 4);
        v.layout(l, t, ((this.childSize * 3) / 2) + l, ((this.childSize * 3) / 2) + t);
        AnimationSet animSet = new AnimationSet(true);
        ScaleAnimation scale = new ScaleAnimation(0.667f, 1.0f, 0.667f, 1.0f, (this.childSize * 3) / 4, (this.childSize * 3) / 4);
        scale.setDuration(animT);
        AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.5f);
        alpha.setDuration(animT);
        animSet.addAnimation(scale);
        animSet.addAnimation(alpha);
        animSet.setFillEnabled(true);
        animSet.setFillAfter(true);
        v.clearAnimation();
        v.startAnimation(animSet);
    }

    protected void animateGap(int target) {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (i != this.dragged) {
                int newPos = i;
                if (this.dragged < target && i >= this.dragged + 1 && i <= target) {
                    newPos--;
                } else if (target < this.dragged && i >= target && i < this.dragged) {
                    newPos++;
                }
                int oldPos = i;
                if (this.newPositions.get(i).intValue() != -1) {
                    oldPos = this.newPositions.get(i).intValue();
                }
                if (oldPos != newPos) {
                    Point oldXY = getCoorFromIndex(oldPos);
                    Point newXY = getCoorFromIndex(newPos);
                    Point oldOffset = new Point(oldXY.x - v.getLeft(), oldXY.y - v.getTop());
                    Point newOffset = new Point(newXY.x - v.getLeft(), newXY.y - v.getTop());
                    TranslateAnimation translate = new TranslateAnimation(0, oldOffset.x, 0, newOffset.x, 0, oldOffset.y, 0, newOffset.y);
                    translate.setDuration(animT);
                    translate.setFillEnabled(true);
                    translate.setFillAfter(true);
                    v.clearAnimation();
                    v.startAnimation(translate);
                    this.newPositions.set(i, Integer.valueOf(newPos));
                }
            }
        }
    }

    @SuppressLint({"WrongCall"})
    protected void reorderChildren() {
        if (this.onRearrangeListener != null) {
            this.onRearrangeListener.onRearrange(this.dragged, this.lastTarget);
        }
        ArrayList<View> children = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).clearAnimation();
            children.add(getChildAt(i));
        }
        removeAllViews();
        while (this.dragged != this.lastTarget) {
            if (this.lastTarget == children.size()) {
                children.add(children.remove(this.dragged));
                this.dragged = this.lastTarget;
            } else if (this.dragged < this.lastTarget) {
                Collections.swap(children, this.dragged, this.dragged + 1);
                this.dragged++;
            } else if (this.dragged > this.lastTarget) {
                Collections.swap(children, this.dragged, this.dragged - 1);
                this.dragged--;
            }
        }
        for (int i2 = 0; i2 < children.size(); i2++) {
            this.newPositions.set(i2, -1);
            addView(children.get(i2));
        }
        onLayout(true, getLeft(), getTop(), getRight(), getBottom());
    }

    public void scrollToTop() {
        this.scroll = 0;
    }

    public void scrollToBottom() {
        this.scroll = 0x7fffffff;
        clampScroll();
    }

    protected void clampScroll() {
        int overreach = getHeight() / 2;
        int max = Math.max(getMaxScroll(), 0);
        if (this.scroll < (-overreach)) {
            this.scroll = -overreach;
            this.lastDelta = 0.0f;
        } else if (this.scroll > max + overreach) {
            this.scroll = max + overreach;
            this.lastDelta = 0.0f;
        } else if (this.scroll < 0) {
            if (this.scroll >= -3) {
                this.scroll = 0;
            } else if (!this.touching) {
                this.scroll -= this.scroll / 3;
            }
        } else if (this.scroll <= max) {
        } else {
            if (this.scroll <= max + 3) {
                this.scroll = max;
            } else if (!this.touching) {
                this.scroll += (max - this.scroll) / 3;
            }
        }
    }

    protected int getMaxScroll() {
        int rowCount = (int) Math.ceil(getChildCount() / this.colCount);
        return ((this.childSize * rowCount) + ((rowCount + 1) * this.padding)) - getHeight();
    }

    public int getLastIndex() {
        return getIndexFromCoor(this.lastX, this.lastY);
    }

    public void setOnRearrangeListener(OnRearrangeListener l) {
        this.onRearrangeListener = l;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener l) {
        this.onItemClickListener = l;
    }
}
