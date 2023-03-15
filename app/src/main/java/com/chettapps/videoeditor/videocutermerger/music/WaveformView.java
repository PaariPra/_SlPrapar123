package com.chettapps.videoeditor.videocutermerger.music;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.chettapps.videoeditor.R;
import com.chettapps.videoeditor.videocutermerger .music.audiocutter.soundfile.CheapSoundFile;

/* loaded from: classes.dex */
public class WaveformView extends View {
    private GestureDetector mGestureDetector;
    private float mInitialScaleSpan;
    private WaveformListener mListener;
    private int mNumZoomLevels;
    private int mSampleRate;
    private int mSamplesPerFrame;
    private ScaleGestureDetector mScaleGestureDetector;
    private double[] mZoomFactorByZoomLevel;
    private int mZoomLevel;
    private Paint mGridPaint = new Paint();
    private Paint mSelectedLinePaint = new Paint();
    private Paint mUnselectedLinePaint = new Paint();
    private Paint mUnselectedBkgndLinePaint = new Paint();
    private Paint mBorderLinePaint = new Paint();
    private Paint mPlaybackLinePaint = new Paint();
    private Paint mTimecodePaint = new Paint();
    private CheapSoundFile mSoundFile = null;
    private int[] mLenByZoomLevel = null;
    private double[][] mValuesByZoomLevel = null;
    private int[] mHeightsAtThisZoomLevel = null;
    private int mOffset = 0;
    private int mPlaybackPos = -1;
    private int mSelectionStart = 0;
    private int mSelectionEnd = 0;
    private float mDensity = 1.0f;
    private boolean mInitialized = false;

    /* loaded from: classes.dex */
    public interface WaveformListener {
        void waveformDraw();

        void waveformFling(float f);

        void waveformTouchEnd();

        void waveformTouchMove(float f);

        void waveformTouchStart(float f);

        void waveformZoomIn();

        void waveformZoomOut();
    }

    /* loaded from: classes.dex */
    class C02751 extends GestureDetector.SimpleOnGestureListener {
        C02751() {
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onFling(MotionEvent e1, MotionEvent e2, float vx, float vy) {
            WaveformView.this.mListener.waveformFling(vx);
            return true;
        }
    }

    /* loaded from: classes.dex */
    class C02762 extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        C02762() {
        }

        @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScaleBegin(ScaleGestureDetector d) {
            Log.v("Ringdroid", "ScaleBegin " + d.getCurrentSpanX());
            WaveformView.this.mInitialScaleSpan = Math.abs(d.getCurrentSpanX());
            return true;
        }

        @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector d) {
            float scale = Math.abs(d.getCurrentSpanX());
            Log.v("Ringdroid", "Scale " + (scale - WaveformView.this.mInitialScaleSpan));
            if (scale - WaveformView.this.mInitialScaleSpan > 40.0f) {
                WaveformView.this.mListener.waveformZoomIn();
                WaveformView.this.mInitialScaleSpan = scale;
            }
            if (scale - WaveformView.this.mInitialScaleSpan >= -40.0f) {
                return true;
            }
            WaveformView.this.mListener.waveformZoomOut();
            WaveformView.this.mInitialScaleSpan = scale;
            return true;
        }

        @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
        public void onScaleEnd(ScaleGestureDetector d) {
            Log.v("Ringdroid", "ScaleEnd " + d.getCurrentSpanX());
        }
    }

    public WaveformView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(false);
        Resources res = getResources();
        this.mGridPaint.setAntiAlias(false);
        this.mGridPaint.setColor(res.getColor(R.color.grid_line));
        this.mSelectedLinePaint.setAntiAlias(false);
        this.mSelectedLinePaint.setColor(res.getColor(R.color.waveform_selected));
        this.mUnselectedLinePaint.setAntiAlias(false);
        this.mUnselectedLinePaint.setColor(res.getColor(R.color.waveform_unselected));
        this.mUnselectedBkgndLinePaint.setAntiAlias(false);
        this.mUnselectedBkgndLinePaint.setColor(res.getColor(R.color.waveform_unselected_bkgnd_overlay));
        this.mBorderLinePaint.setAntiAlias(true);
        this.mBorderLinePaint.setStrokeWidth(1.5f);
        this.mBorderLinePaint.setPathEffect(new DashPathEffect(new float[]{3.0f, 2.0f}, 0.0f));
        this.mBorderLinePaint.setColor(res.getColor(R.color.selection_border));
        this.mPlaybackLinePaint.setAntiAlias(false);
        this.mPlaybackLinePaint.setColor(res.getColor(R.color.playback_indicator));
        this.mTimecodePaint.setTextSize(12.0f);
        this.mTimecodePaint.setAntiAlias(true);
        this.mTimecodePaint.setColor(res.getColor(R.color.timecode));
        this.mTimecodePaint.setShadowLayer(2.0f, 1.0f, 1.0f, res.getColor(R.color.timecode_shadow));
        this.mGestureDetector = new GestureDetector(context, new C02751());
        this.mScaleGestureDetector = new ScaleGestureDetector(context, new C02762());
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        this.mScaleGestureDetector.onTouchEvent(event);
        if (this.mGestureDetector.onTouchEvent(event)) {
            return true;
        }
        switch (event.getAction()) {
            case 0:
                this.mListener.waveformTouchStart(event.getX());
                return true;
            case 1:
                this.mListener.waveformTouchEnd();
                return true;
            case 2:
                this.mListener.waveformTouchMove(event.getX());
                return true;
            default:
                return true;
        }
    }

    public boolean hasSoundFile() {
        return this.mSoundFile != null;
    }

    public void setSoundFile(CheapSoundFile soundFile) {
        this.mSoundFile = soundFile;
        this.mSampleRate = this.mSoundFile.getSampleRate();
        this.mSamplesPerFrame = this.mSoundFile.getSamplesPerFrame();
        computeDoublesForAllZoomLevels();
        this.mHeightsAtThisZoomLevel = null;
    }

    public boolean isInitialized() {
        return this.mInitialized;
    }

    public int getZoomLevel() {
        return this.mZoomLevel;
    }

    public void setZoomLevel(int zoomLevel) {
        while (this.mZoomLevel > zoomLevel) {
            zoomIn();
        }
        while (this.mZoomLevel < zoomLevel) {
            zoomOut();
        }
    }

    public boolean canZoomIn() {
        return this.mZoomLevel > 0;
    }

    public void zoomIn() {
        if (canZoomIn()) {
            this.mZoomLevel--;
            this.mSelectionStart *= 2;
            this.mSelectionEnd *= 2;
            this.mHeightsAtThisZoomLevel = null;
            this.mOffset = ((this.mOffset + (getMeasuredWidth() / 2)) * 2) - (getMeasuredWidth() / 2);
            if (this.mOffset < 0) {
                this.mOffset = 0;
            }
            invalidate();
        }
    }

    public boolean canZoomOut() {
        return this.mZoomLevel < this.mNumZoomLevels + (-1);
    }

    public void zoomOut() {
        if (canZoomOut()) {
            this.mZoomLevel++;
            this.mSelectionStart /= 2;
            this.mSelectionEnd /= 2;
            this.mOffset = ((this.mOffset + (getMeasuredWidth() / 2)) / 2) - (getMeasuredWidth() / 2);
            if (this.mOffset < 0) {
                this.mOffset = 0;
            }
            this.mHeightsAtThisZoomLevel = null;
            invalidate();
        }
    }

    public int maxPos() {
        return this.mLenByZoomLevel[this.mZoomLevel];
    }

    public int secondsToFrames(double seconds) {
        return (int) ((((1.0d * seconds) * this.mSampleRate) / this.mSamplesPerFrame) + 0.5d);
    }

    public int secondsToPixels(double seconds) {
        return (int) ((((this.mZoomFactorByZoomLevel[this.mZoomLevel] * seconds) * this.mSampleRate) / this.mSamplesPerFrame) + 0.5d);
    }

    public double pixelsToSeconds(int pixels) {
        return (pixels * this.mSamplesPerFrame) / (this.mSampleRate * this.mZoomFactorByZoomLevel[this.mZoomLevel]);
    }

    public int millisecsToPixels(int msecs) {
        return (int) (((((msecs * 1.0d) * this.mSampleRate) * this.mZoomFactorByZoomLevel[this.mZoomLevel]) / (1000.0d * this.mSamplesPerFrame)) + 0.5d);
    }

    public int pixelsToMillisecs(int pixels) {
        return (int) (((pixels * (1000.0d * this.mSamplesPerFrame)) / (this.mSampleRate * this.mZoomFactorByZoomLevel[this.mZoomLevel])) + 0.5d);
    }

    public void setParameters(int start, int end, int offset) {
        this.mSelectionStart = start;
        this.mSelectionEnd = end;
        this.mOffset = offset;
    }

    public int getStart() {
        return this.mSelectionStart;
    }

    public int getEnd() {
        return this.mSelectionEnd;
    }

    public int getOffset() {
        return this.mOffset;
    }

    public void setPlayback(int pos) {
        this.mPlaybackPos = pos;
    }

    public void setListener(WaveformListener listener) {
        this.mListener = listener;
    }

    public void recomputeHeights(float density) {
        this.mHeightsAtThisZoomLevel = null;
        this.mDensity = density;
        this.mTimecodePaint.setTextSize((int) (12.0f * density));
        invalidate();
    }

    protected void drawWaveformLine(Canvas canvas, int x, int y0, int y1, Paint paint) {
        canvas.drawLine(x, y0, x, y1, paint);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        Paint paint;
        super.onDraw(canvas);
        if (this.mSoundFile != null) {
            if (this.mHeightsAtThisZoomLevel == null) {
                computeIntsForThisZoomLevel();
            }
            int measuredWidth = getMeasuredWidth();
            int measuredHeight = getMeasuredHeight();
            int start = this.mOffset;
            int width = this.mHeightsAtThisZoomLevel.length - start;
            int ctr = measuredHeight / 2;
            if (width > measuredWidth) {
                width = measuredWidth;
            }
            double onePixelInSecs = pixelsToSeconds(1);
            boolean onlyEveryFiveSecs = onePixelInSecs > 0.02d;
            double fractionalSecs = this.mOffset * onePixelInSecs;
            int integerSecs = (int) fractionalSecs;
            int i = 0;
            while (i < width) {
                int i2 = i + 1;
                fractionalSecs += onePixelInSecs;
                int integerSecsNew = (int) fractionalSecs;
                if (integerSecsNew != integerSecs) {
                    integerSecs = integerSecsNew;
                    if (!onlyEveryFiveSecs || integerSecs % 5 == 0) {
                        canvas.drawLine(i2, 0.0f, i2, measuredHeight, this.mGridPaint);
                    }
                }
                i = i2;
            }
            for (int i3 = 0; i3 < width; i3++) {
                if (i3 + start < this.mSelectionStart || i3 + start >= this.mSelectionEnd) {
                    drawWaveformLine(canvas, i3, 0, measuredHeight, this.mUnselectedBkgndLinePaint);
                    paint = this.mUnselectedLinePaint;
                } else {
                    paint = this.mSelectedLinePaint;
                }
                drawWaveformLine(canvas, i3, ctr - this.mHeightsAtThisZoomLevel[start + i3], ctr + 1 + this.mHeightsAtThisZoomLevel[start + i3], paint);
                if (i3 + start == this.mPlaybackPos) {
                    canvas.drawLine(i3, 0.0f, i3, measuredHeight, this.mPlaybackLinePaint);
                }
            }
            for (int i4 = width; i4 < measuredWidth; i4++) {
                drawWaveformLine(canvas, i4, 0, measuredHeight, this.mUnselectedBkgndLinePaint);
            }
            canvas.drawLine((this.mSelectionStart - this.mOffset) + 0.5f, 30.0f, (this.mSelectionStart - this.mOffset) + 0.5f, measuredHeight, this.mBorderLinePaint);
            canvas.drawLine((this.mSelectionEnd - this.mOffset) + 0.5f, 0.0f, (this.mSelectionEnd - this.mOffset) + 0.5f, measuredHeight - 30, this.mBorderLinePaint);
            double timecodeIntervalSecs = 1.0d;
            if (1.0d / onePixelInSecs < 50.0d) {
                timecodeIntervalSecs = 5.0d;
            }
            if (timecodeIntervalSecs / onePixelInSecs < 50.0d) {
                timecodeIntervalSecs = 15.0d;
            }
            double fractionalSecs2 = this.mOffset * onePixelInSecs;
            int integerTimecode = (int) (fractionalSecs2 / timecodeIntervalSecs);
            int i5 = 0;
            while (i5 < width) {
                i5++;
                fractionalSecs2 += onePixelInSecs;
                int integerSecs2 = (int) fractionalSecs2;
                int integerTimecodeNew = (int) (fractionalSecs2 / timecodeIntervalSecs);
                if (integerTimecodeNew != integerTimecode) {
                    integerTimecode = integerTimecodeNew;
                    String timecodeMinutes = String.valueOf(integerSecs2 / 60);
                    String timecodeSeconds = String.valueOf(integerSecs2 % 60);
                    if (integerSecs2 % 60 < 10) {
                        timecodeSeconds = "0" + timecodeSeconds;
                    }
                    String timecodeStr = String.valueOf(timecodeMinutes) + ":" + timecodeSeconds;
                    canvas.drawText(timecodeStr, i5 - ((float) (0.5d * this.mTimecodePaint.measureText(timecodeStr))), (int) (12.0f * this.mDensity), this.mTimecodePaint);
                }
            }
            if (this.mListener != null) {
                this.mListener.waveformDraw();
            }
        }
    }

    private void computeDoublesForAllZoomLevels() {
        int numFrames = this.mSoundFile.getNumFrames();
        int[] frameGains = this.mSoundFile.getFrameGains();
        double[] smoothedGains = new double[numFrames];
        if (numFrames == 1) {
            smoothedGains[0] = frameGains[0];
        } else if (numFrames == 2) {
            smoothedGains[0] = frameGains[0];
            smoothedGains[1] = frameGains[1];
        } else if (numFrames > 2) {
            smoothedGains[0] = (frameGains[0] / 2.0d) + (frameGains[1] / 2.0d);
            for (int i = 1; i < numFrames - 1; i++) {
                smoothedGains[i] = (frameGains[i - 1] / 3.0d) + (frameGains[i] / 3.0d) + (frameGains[i + 1] / 3.0d);
            }
            smoothedGains[numFrames - 1] = (frameGains[numFrames - 2] / 2.0d) + (frameGains[numFrames - 1] / 2.0d);
        }
        double maxGain = 1.0d;
        for (int i2 = 0; i2 < numFrames; i2++) {
            if (smoothedGains[i2] > maxGain) {
                maxGain = smoothedGains[i2];
            }
        }
        double scaleFactor = 1.0d;
        if (maxGain > 255.0d) {
            scaleFactor = 255.0d / maxGain;
        }
        double maxGain2 = 0.0d;
        int[] gainHist = new int[256];
        for (int i3 = 0; i3 < numFrames; i3++) {
            int smoothedGain = (int) (smoothedGains[i3] * scaleFactor);
            if (smoothedGain < 0) {
                smoothedGain = 0;
            }
            if (smoothedGain > 255) {
                smoothedGain = 255;
            }
            if (smoothedGain > maxGain2) {
                maxGain2 = smoothedGain;
            }
            gainHist[smoothedGain] = gainHist[smoothedGain] + 1;
        }
        double minGain = 0.0d;
        int sum = 0;
        while (minGain < 255.0d && sum < numFrames / 20) {
            sum += gainHist[(int) minGain];
            minGain += 1.0d;
        }
        int sum2 = 0;
        while (maxGain2 > 2.0d && sum2 < numFrames / 100) {
            sum2 += gainHist[(int) maxGain2];
            maxGain2 -= 1.0d;
        }
        double[] heights = new double[numFrames];
        double range = maxGain2 - minGain;
        for (int i4 = 0; i4 < numFrames; i4++) {
            double value = ((smoothedGains[i4] * scaleFactor) - minGain) / range;
            if (value < 0.0d) {
                value = 0.0d;
            }
            if (value > 1.0d) {
                value = 1.0d;
            }
            heights[i4] = value * value;
        }
        this.mNumZoomLevels = 5;
        this.mLenByZoomLevel = new int[5];
        this.mZoomFactorByZoomLevel = new double[5];
        this.mValuesByZoomLevel = new double[5][];
        this.mLenByZoomLevel[0] = numFrames * 2;
        this.mZoomFactorByZoomLevel[0] = 2.0d;
        this.mValuesByZoomLevel[0] = new double[this.mLenByZoomLevel[0]];
        if (numFrames > 0) {
            this.mValuesByZoomLevel[0][0] = 0.5d * heights[0];
            this.mValuesByZoomLevel[0][1] = heights[0];
        }
        for (int i5 = 1; i5 < numFrames; i5++) {
            this.mValuesByZoomLevel[0][i5 * 2] = 0.5d * (heights[i5 - 1] + heights[i5]);
            this.mValuesByZoomLevel[0][(i5 * 2) + 1] = heights[i5];
        }
        this.mLenByZoomLevel[1] = numFrames;
        this.mValuesByZoomLevel[1] = new double[this.mLenByZoomLevel[1]];
        this.mZoomFactorByZoomLevel[1] = 1.0d;
        for (int i6 = 0; i6 < this.mLenByZoomLevel[1]; i6++) {
            this.mValuesByZoomLevel[1][i6] = heights[i6];
        }
        for (int j = 2; j < 5; j++) {
            this.mLenByZoomLevel[j] = this.mLenByZoomLevel[j - 1] / 2;
            this.mValuesByZoomLevel[j] = new double[this.mLenByZoomLevel[j]];
            this.mZoomFactorByZoomLevel[j] = this.mZoomFactorByZoomLevel[j - 1] / 2.0d;
            for (int i7 = 0; i7 < this.mLenByZoomLevel[j]; i7++) {
                this.mValuesByZoomLevel[j][i7] = 0.5d * (this.mValuesByZoomLevel[j - 1][i7 * 2] + this.mValuesByZoomLevel[j - 1][(i7 * 2) + 1]);
            }
        }
        if (numFrames > 5000) {
            this.mZoomLevel = 3;
        } else if (numFrames > 1000) {
            this.mZoomLevel = 2;
        } else if (numFrames > 300) {
            this.mZoomLevel = 1;
        } else {
            this.mZoomLevel = 0;
        }
        this.mInitialized = true;
    }

    private void computeIntsForThisZoomLevel() {
        int halfHeight = (getMeasuredHeight() / 2) - 1;
        this.mHeightsAtThisZoomLevel = new int[this.mLenByZoomLevel[this.mZoomLevel]];
        for (int i = 0; i < this.mLenByZoomLevel[this.mZoomLevel]; i++) {
            this.mHeightsAtThisZoomLevel[i] = (int) (this.mValuesByZoomLevel[this.mZoomLevel][i] * halfHeight);
        }
    }
}
