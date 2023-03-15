package com.chettapps.videoeditor.videocutermerger.music;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.ImageView;

/* loaded from: classes.dex */
public class MarkerView extends ImageView {
    private MarkerListener mListener = null;
    private int mVelocity = 0;

    /* loaded from: classes.dex */
    public interface MarkerListener {
        void markerDraw();

        void markerEnter(MarkerView markerView);

        void markerFocus(MarkerView markerView);

        void markerKeyUp();

        void markerLeft(MarkerView markerView, int i);

        void markerRight(MarkerView markerView, int i);

        void markerTouchEnd(MarkerView markerView);

        void markerTouchMove(MarkerView markerView, float f);

        void markerTouchStart(MarkerView markerView, float f);
    }

    public MarkerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
    }

    public void setListener(MarkerListener listener) {
        this.mListener = listener;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                requestFocus();
                this.mListener.markerTouchStart(this, event.getRawX());
                return true;
            case 1:
                this.mListener.markerTouchEnd(this);
                return true;
            case 2:
                this.mListener.markerTouchMove(this, event.getRawX());
                return true;
            default:
                return true;
        }
    }

    @Override // android.view.View
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        if (gainFocus && this.mListener != null) {
            this.mListener.markerFocus(this);
        }
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mListener != null) {
            this.mListener.markerDraw();
        }
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        this.mVelocity++;
        int v = (int) Math.sqrt((this.mVelocity / 2) + 1);
        if (this.mListener != null) {
            if (keyCode == 21) {
                this.mListener.markerLeft(this, v);
                return true;
            } else if (keyCode == 22) {
                this.mListener.markerRight(this, v);
                return true;
            } else if (keyCode == 23) {
                this.mListener.markerEnter(this);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        this.mVelocity = 0;
        if (this.mListener != null) {
            this.mListener.markerKeyUp();
        }
        return super.onKeyDown(keyCode, event);
    }
}
