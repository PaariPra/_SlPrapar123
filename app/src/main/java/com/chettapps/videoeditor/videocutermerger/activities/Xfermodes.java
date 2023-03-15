package com.chettapps.videoeditor.videocutermerger.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;


import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public class Xfermodes extends GraphicsActivity {
  // create a bitmap with a circle, used for the "dst" image
  static Bitmap makeDst(int w, int h) {
    Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    Canvas c = new Canvas(bm);
    Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

    p.setColor(0xFFFFCC44);
    c.drawOval(new RectF(0, 0, w * 3 / 4, h * 3 / 4), p);
    return bm;
  }

  // create a bitmap with a rect, used for the "src" image
  static Bitmap makeSrc(int w, int h) {
    Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    Canvas c = new Canvas(bm);
    Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

    p.setColor(0xFF66AAFF);
    c.drawRect(w / 3, h / 3, w * 19 / 20, h * 19 / 20, p);
    return bm;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(new SampleView(this));
  }

  private static class SampleView extends View {
    private static final int W = 64;
    private static final int H = 64;
    private static final int ROW_MAX = 4; // number of samples per row

    private Bitmap mSrcB;
    private Bitmap mDstB;
    private Shader mBG; // background checker-board pattern

    private static final Xfermode[] sModes = {
        new PorterDuffXfermode(PorterDuff.Mode.CLEAR),
        new PorterDuffXfermode(PorterDuff.Mode.SRC),
        new PorterDuffXfermode(PorterDuff.Mode.DST),
        new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER),
        new PorterDuffXfermode(PorterDuff.Mode.DST_OVER),
        new PorterDuffXfermode(PorterDuff.Mode.SRC_IN),
        new PorterDuffXfermode(PorterDuff.Mode.DST_IN),
        new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT),
        new PorterDuffXfermode(PorterDuff.Mode.DST_OUT),
        new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP),
        new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP),
        new PorterDuffXfermode(PorterDuff.Mode.XOR),
        new PorterDuffXfermode(PorterDuff.Mode.DARKEN),
        new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN),
        new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY),
        new PorterDuffXfermode(PorterDuff.Mode.SCREEN) };

    private static final String[] sLabels = { "Clear", "Src", "Dst",
        "SrcOver", "DstOver", "SrcIn", "DstIn", "SrcOut", "DstOut",
        "SrcATop", "DstATop", "Xor", "Darken", "Lighten", "Multiply",
        "Screen" };

    public SampleView(Context context) {
      super(context);

      mSrcB = makeSrc(W, H);
      mDstB = makeDst(W, H);

      // make a ckeckerboard pattern
      Bitmap bm = Bitmap.createBitmap(new int[] { 0xFFFFFFFF, 0xFFCCCCCC,
          0xFFCCCCCC, 0xFFFFFFFF }, 2, 2, Bitmap.Config.RGB_565);
      mBG = new BitmapShader(bm, Shader.TileMode.REPEAT,
          Shader.TileMode.REPEAT);
      Matrix m = new Matrix();
      m.setScale(6, 6);
      mBG.setLocalMatrix(m);
    }

    @Override
    protected void onDraw(Canvas canvas) {
      canvas.drawColor(Color.WHITE);

      Paint labelP = new Paint(Paint.ANTI_ALIAS_FLAG);
      labelP.setTextAlign(Paint.Align.CENTER);

      Paint paint = new Paint();
      paint.setFilterBitmap(false);

      canvas.translate(15, 35);

      int x = 0;
      int y = 0;
      for (int i = 0; i < 3; i++) {
        // draw the border
        paint.setStyle(Paint.Style.STROKE);
        paint.setShader(null);
        canvas.drawRect(x - 0.5f, y - 0.5f, x + W + 0.5f, y + H + 0.5f,
            paint);

        // draw the checker-board pattern
        paint.setStyle(Paint.Style.FILL);
        paint.setShader(mBG);
        canvas.drawRect(x, y, x + W, y + H, paint);


        canvas.translate(x, y);
        canvas.drawBitmap(mDstB, 0, 0, paint);
        paint.setXfermode(sModes[i]);
        canvas.drawBitmap(mSrcB, 0, 0, paint);
        paint.setXfermode(null);
        canvas.restoreToCount(5);

        // draw the label
        canvas.drawText(sLabels[i], x + W / 2, y - labelP.getTextSize()
            / 2, labelP);

        x += W + 10;

        // wrap around when we've drawn enough for one row
        if ((i % ROW_MAX) == ROW_MAX - 1) {
          x = 0;
          y += H + 30;
        }
      }
    }
  }
}

class GraphicsActivity extends Activity {
  // set to true to test Picture
  private static final boolean TEST_PICTURE = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void setContentView(View view) {
    if (TEST_PICTURE) {
      ViewGroup vg = new PictureLayout(this);
      vg.addView(view);
      view = vg;
    }

    super.setContentView(view);
  }
}

class PictureLayout extends ViewGroup {
  private final Picture mPicture = new Picture();

  public PictureLayout(Context context) {
    super(context);
  }

  public PictureLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public void addView(View child) {
    if (getChildCount() > 1) {
      throw new IllegalStateException(
          "PictureLayout can host only one direct child");
    }

    super.addView(child);
  }

  @Override
  public void addView(View child, int index) {
    if (getChildCount() > 1) {
      throw new IllegalStateException(
          "PictureLayout can host only one direct child");
    }

    super.addView(child, index);
  }

  @Override
  public void addView(View child, LayoutParams params) {
    if (getChildCount() > 1) {
      throw new IllegalStateException(
          "PictureLayout can host only one direct child");
    }

    super.addView(child, params);
  }

  @Override
  public void addView(View child, int index, LayoutParams params) {
    if (getChildCount() > 1) {
      throw new IllegalStateException(
          "PictureLayout can host only one direct child");
    }

    super.addView(child, index, params);
  }

  @Override
  protected LayoutParams generateDefaultLayoutParams() {
    return new LayoutParams(LayoutParams.MATCH_PARENT,
        LayoutParams.MATCH_PARENT);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    final int count = getChildCount();

    int maxHeight = 0;
    int maxWidth = 0;

    for (int i = 0; i < count; i++) {
      final View child = getChildAt(i);
      if (child.getVisibility() != GONE) {
        measureChild(child, widthMeasureSpec, heightMeasureSpec);
      }
    }

    maxWidth += getPaddingLeft() + getPaddingRight();
    maxHeight += getPaddingTop() + getPaddingBottom();

    Drawable drawable = getBackground();
    if (drawable != null) {
      maxHeight = Math.max(maxHeight, drawable.getMinimumHeight());
      maxWidth = Math.max(maxWidth, drawable.getMinimumWidth());
    }

    setMeasuredDimension(resolveSize(maxWidth, widthMeasureSpec),
        resolveSize(maxHeight, heightMeasureSpec));
  }

  private void drawPict(Canvas canvas, int x, int y, int w, int h, float sx,
      float sy) {
    canvas.save();
    canvas.translate(x, y);
    canvas.clipRect(0, 0, w, h);
    canvas.scale(0.5f, 0.5f);
    canvas.scale(sx, sy, w, h);
    canvas.drawPicture(mPicture);
    canvas.restore();
  }

  @Override
  protected void dispatchDraw(Canvas canvas) {
    super.dispatchDraw(mPicture.beginRecording(getWidth(), getHeight()));
    mPicture.endRecording();

    int x = getWidth() / 2;
    int y = getHeight() / 2;

    if (false) {
      canvas.drawPicture(mPicture);
    } else {
      drawPict(canvas, 0, 0, x, y, 1, 1);
      drawPict(canvas, x, 0, x, y, -1, 1);
      drawPict(canvas, 0, y, x, y, 1, -1);
      drawPict(canvas, x, y, x, y, -1, -1);
    }
  }

  @Override
  public ViewParent invalidateChildInParent(int[] location, Rect dirty) {
    location[0] = getLeft();
    location[1] = getTop();
    dirty.set(0, 0, getWidth(), getHeight());
    return getParent();
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    final int count = super.getChildCount();

    for (int i = 0; i < count; i++) {
      final View child = getChildAt(i);
      if (child.getVisibility() != GONE) {
        final int childLeft = getPaddingLeft();
        final int childTop = getPaddingTop();
        child.layout(childLeft, childTop,
            childLeft + child.getMeasuredWidth(),
            childTop + child.getMeasuredHeight());

      }
    }
  }
}

   
    
    
    