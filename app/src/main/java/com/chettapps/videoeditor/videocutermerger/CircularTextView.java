package com.chettapps.videoeditor.videocutermerger;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/* loaded from: classes.dex */
public class CircularTextView extends TextView {
    int solidColor;
    int strokeColor;
    private float strokeWidth;

    public CircularTextView(Context context) {
        super(context);
    }

    public CircularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        int diameter;
        Paint circlePaint = new Paint();
        circlePaint.setColor(this.solidColor);
        circlePaint.setFlags(1);
        Paint strokePaint = new Paint();
        strokePaint.setColor(this.strokeColor);
        strokePaint.setFlags(1);
        int h = getHeight();
        int w = getWidth();
        if (h > w) {
            diameter = h;
        } else {
            diameter = w;
        }
        int radius = diameter / 2;
        setHeight(diameter);
        setWidth(diameter);
        canvas.drawCircle(diameter / 2, diameter / 2, radius, strokePaint);
        canvas.drawCircle(diameter / 2, diameter / 2, radius - this.strokeWidth, circlePaint);
        super.draw(canvas);
    }

    public void setStrokeWidth(int dp) {
        this.strokeWidth = dp * getContext().getResources().getDisplayMetrics().density;
    }

    public void setStrokeColor(String color) {
        this.strokeColor = Color.parseColor(color);
    }

    public void setSolidColor(String color) {
        this.solidColor = Color.parseColor(color);
    }
}
