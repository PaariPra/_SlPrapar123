package com.chettapps.videoeditor.videocutermerger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.chettapps.videoeditor.R;



/* loaded from: classes.dex */
public class PreviewImageView extends ImageView {
    public static int mAspectRatioHeight = 400;
    public static int mAspectRatioWidth = 640;

    public PreviewImageView(Context context) {
        super(context);
    }

    public PreviewImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init(context, attrs);
    }

    public PreviewImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(context, attrs);
    }

    @SuppressLint({"NewApi"})
    public PreviewImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Init(context, attrs);
    }

    private void Init(Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs, ja.burhanrashid52.photoeditor.R.styleable.PhotoEditorView);
        mAspectRatioWidth = a.getInt(0, MyApplication.VIDEO_WIDTH);
        mAspectRatioHeight = a.getInt(1, MyApplication.VIDEO_HEIGHT);
        Log.e("bbbbb..mAspectRaWidth", "****** .. ******** >>>> \nmAspectRatioWidth:" + mAspectRatioWidth + " mAspectRatioHeight:" + mAspectRatioHeight);
        a.recycle();


    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int finalWidth;
        int finalHeight;
        Log.e("bbbbb..onMeasure..", "****** .. ******** >>>> \nwidthMeasureSpec:" + heightMeasureSpec + " mAspectRatioHeight:" + heightMeasureSpec);
        int originalWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        int originalHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        int calculatedHeight = (int) ((mAspectRatioHeight * originalWidth) / mAspectRatioWidth);
        if (calculatedHeight > originalHeight) {
            finalWidth = (int) ((mAspectRatioWidth * originalHeight) / mAspectRatioHeight);
            finalHeight = originalHeight;
            Log.e("bbbbb..onMeasure..", "****** ..if.. ******** >>>> \nfinalWidth:" + finalWidth + " finalHeight:" + finalHeight);
        } else {
            finalWidth = originalWidth;
            finalHeight = calculatedHeight;
            Log.e("bbbbb..onMeasure..", "****** ..else.. ******** >>>> \nfinalWidth:" + finalWidth + " finalHeight:" + finalHeight);
        }
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(finalWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(finalHeight, 1073741824));
    }
}
