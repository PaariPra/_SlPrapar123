package com.chettapps.videoeditor.videocutermerger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

import java.io.IOException;

/* loaded from: classes.dex */
public class ScalingUtilities {

    /* loaded from: classes.dex */
    public enum ScalingLogic {
        CROP,
        FIT
    }

    public static Bitmap decodeResource(Resources res, int resId, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth, dstHeight, scalingLogic);
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap createScaledBitmap(Bitmap unscaledBitmap, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
        Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
        Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height(), Bitmap.Config.ARGB_8888);
        new Canvas(scaledBitmap).drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(2));
        return scaledBitmap;
    }

    public static int calculateSampleSize(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            if (srcWidth / srcHeight > dstWidth / dstHeight) {
                return srcWidth / dstWidth;
            }
            return srcHeight / dstHeight;
        } else if (srcWidth / srcHeight > dstWidth / dstHeight) {
            return srcHeight / dstHeight;
        } else {
            return srcWidth / dstWidth;
        }
    }

    public static Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (scalingLogic != ScalingLogic.CROP) {
            return new Rect(0, 0, srcWidth, srcHeight);
        }
        float dstAspect = dstWidth / dstHeight;
        if (srcWidth / srcHeight > dstAspect) {
            int srcRectWidth = (int) (srcHeight * dstAspect);
            int srcRectLeft = (srcWidth - srcRectWidth) / 2;
            return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight);
        }
        int srcRectHeight = (int) (srcWidth / dstAspect);
        int scrRectTop = (srcHeight - srcRectHeight) / 2;
        return new Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight);
    }

    public static Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (scalingLogic != ScalingLogic.FIT) {
            return new Rect(0, 0, dstWidth, dstHeight);
        }
        float srcAspect = srcWidth / srcHeight;
        if (srcAspect > dstWidth / dstHeight) {
            return new Rect(0, 0, dstWidth, (int) (dstWidth / srcAspect));
        }
        return new Rect(0, 0, (int) (dstHeight * srcAspect), dstHeight);
    }

    public static Bitmap ConvetrSameSize(Bitmap originalImage, int mDisplayWidth, int mDisplayHeight) {
        Bitmap cs = Bitmap.createBitmap(mDisplayWidth, mDisplayHeight, Bitmap.Config.ARGB_8888);
        Canvas comboImage = new Canvas(cs);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, mDisplayWidth, mDisplayHeight);
        paint.setAntiAlias(true);
        comboImage.drawARGB(0, 0, 0, 0);
        paint.setColor(-12434878);
        comboImage.drawRect(rect, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        comboImage.drawBitmap(newscaleBitmap(originalImage, mDisplayWidth, mDisplayHeight), 0.0f, 0.0f, paint);
        return cs;
    }

    public static Bitmap ConvetrSameSizeTransBg(Bitmap originalImage, int mDisplayWidth, int mDisplayHeight) {
        Bitmap cs = Bitmap.createBitmap(mDisplayWidth, mDisplayHeight, Bitmap.Config.ARGB_8888);
        Canvas comboImage = new Canvas(cs);
        Paint paint = new Paint();
        new Rect(0, 0, mDisplayWidth, mDisplayHeight);
        comboImage.drawBitmap(newscaleBitmap(originalImage, mDisplayWidth, mDisplayHeight), 0.0f, 0.0f, paint);
        return cs;
    }

    public static Bitmap ConvetrSameSize(Bitmap originalImage, Bitmap bgBitmap, int mDisplayWidth, int mDisplayHeight) {
        Canvas comboImage = new Canvas(bgBitmap);
        Paint paint = new Paint();
        new Rect(0, 0, mDisplayWidth, mDisplayHeight);
        comboImage.drawBitmap(newscaleBitmap(originalImage, mDisplayWidth, mDisplayHeight), 0.0f, 0.0f, paint);
        return bgBitmap;
    }

    public static Bitmap ConvetrSameSize(Bitmap originalImage, Bitmap bgBitmap, int mDisplayWidth, int mDisplayHeight, float x, float y) {
        Bitmap cs = bgBitmap.copy(bgBitmap.getConfig(), true);
        new Canvas(FastBlur.doBlur(cs, 25, true)).drawBitmap(newscaleBitmap(originalImage, mDisplayWidth, mDisplayHeight, x, y), 0.0f, 0.0f, new Paint());
        return cs;
    }

    public static Bitmap ConvetrSameSizeNew(Bitmap originalImage, Bitmap bgBitmap, int mDisplayWidth, int mDisplayHeight) {
        Bitmap cs = FastBlur.doBlur(bgBitmap, 25, true);
        Canvas comboImage = new Canvas(cs);
        Paint paint = new Paint();
        float originalWidth = originalImage.getWidth();
        float originalHeight = originalImage.getHeight();
        float scale = mDisplayWidth / originalWidth;
        float scaleY = mDisplayHeight / originalHeight;
        float xTranslation = 0.0f;
        float yTranslation = (mDisplayHeight - (originalHeight * scale)) / 2.0f;
        if (yTranslation < 0.0f) {
            yTranslation = 0.0f;
            scale = mDisplayHeight / originalHeight;
            xTranslation = (mDisplayWidth - (originalWidth * scaleY)) / 2.0f;
        }
        Matrix transformation = new Matrix();
        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scale, scale);
        comboImage.drawBitmap(originalImage, transformation, paint);
        return cs;
    }

    public static Bitmap scaleCenterCrop(Bitmap source, int newWidth, int newHeight) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();
        if (sourceWidth == newWidth && sourceHeight == newHeight) {
            return source;
        }
        float scale = Math.max(newWidth / sourceWidth, newHeight / sourceHeight);
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;
        float left = (newWidth - scaledWidth) / 2.0f;
        float top = (newHeight - scaledHeight) / 2.0f;
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        new Canvas(dest).drawBitmap(source, (Rect) null, targetRect, (Paint) null);
        return dest;
    }

    private static Bitmap newscaleBitmap(Bitmap originalImage, int width, int height, float x, float y) {
        Bitmap background = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        float originalWidth = originalImage.getWidth();
        float originalHeight = originalImage.getHeight();
        Canvas canvas = new Canvas(background);
        float scale = width / originalWidth;
        float scaleY = height / originalHeight;
        float xTranslation = 0.0f;
        float yTranslation = (height - (originalHeight * scale)) / 2.0f;
        if (yTranslation < 0.0f) {
            yTranslation = 0.0f;
            scale = height / originalHeight;
            xTranslation = (width - (originalWidth * scaleY)) / 2.0f;
        }
        Matrix transformation = new Matrix();
        transformation.postTranslate(xTranslation * x, yTranslation + y);
        Log.d("bbbbb....translation", "xTranslation :" + xTranslation + " yTranslation :" + yTranslation);
        transformation.preScale(scale, scale);
        canvas.drawBitmap(originalImage, transformation, new Paint());
        return background;
    }

    public static Bitmap addShadow(Bitmap bm, int color, int size, float dx, float dy) {
        Bitmap mask = Bitmap.createBitmap(bm.getWidth() + (size * 2), bm.getHeight(), Bitmap.Config.ALPHA_8);
        Matrix scaleToFit = new Matrix();
        scaleToFit.setRectToRect(new RectF(0.0f, 0.0f, bm.getWidth(), bm.getHeight()), new RectF(0.0f, 0.0f, (bm.getWidth() - dx) - size, bm.getHeight() - dy), Matrix.ScaleToFit.CENTER);
        Matrix dropShadow = new Matrix(scaleToFit);
        dropShadow.postTranslate(dx, dy);
        Canvas maskCanvas = new Canvas(mask);
        Paint paint = new Paint(1);
        maskCanvas.drawBitmap(bm, scaleToFit, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        maskCanvas.drawBitmap(bm, dropShadow, paint);
        BlurMaskFilter filter = new BlurMaskFilter(size, BlurMaskFilter.Blur.NORMAL);
        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setMaskFilter(filter);
        paint.setFilterBitmap(true);
        Bitmap ret = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas retCanvas = new Canvas(ret);
        retCanvas.drawBitmap(mask, 0.0f, 0.0f, paint);
        retCanvas.drawBitmap(bm, scaleToFit, null);
        mask.recycle();
        return ret;
    }

    @SuppressLint({"NewApi"})
    public static Bitmap blurBitmap(Bitmap bitmap, Context context) {
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
        blurScript.setRadius(25.0f);
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);
        allOut.copyTo(outBitmap);
        bitmap.recycle();
        rs.destroy();
        return outBitmap;
    }

    public static Bitmap overlay(Bitmap bitmap1, Bitmap bitmapOverlay, int opacity) {
        Bitmap resultBitmap = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(resultBitmap);
        c.drawBitmap(bitmap1, 0.0f, 0.0f, (Paint) null);
        Paint p = new Paint();
        p.setAlpha(opacity);
        c.drawBitmap(bitmapOverlay, 0.0f, 0.0f, p);
        return resultBitmap;
    }

    private static Bitmap newscaleBitmap(Bitmap originalImage, int width, int height) {
        Bitmap background = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        float originalWidth = originalImage.getWidth();
        float originalHeight = originalImage.getHeight();
        Canvas canvas = new Canvas(background);
        float scale = width / originalWidth;
        float xTranslation = 0.0f;
        float yTranslation = (height - (originalHeight * scale)) / 2.0f;
        if (yTranslation < 0.0f) {
            yTranslation = 0.0f;
            scale = height / originalHeight;
            xTranslation = (width - (originalWidth * scale)) / 2.0f;
        }
        Matrix transformation = new Matrix();
        transformation.postTranslate(xTranslation, yTranslation);
        Log.d("bbbbb...translation", "xTranslation :" + xTranslation + " yTranslation :" + yTranslation);
        transformation.preScale(scale, scale);
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        canvas.drawBitmap(originalImage, transformation, paint);
        return background;
    }

    public static Bitmap checkBitmap(String path) {
        int orientation = 1;
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bounds);
        Bitmap bm = BitmapFactory.decodeFile(path, new BitmapFactory.Options());
        try {
            String orientString = new ExifInterface(path).getAttribute("Orientation");
            if (orientString != null) {
                orientation = Integer.parseInt(orientString);
            }
            int rotationAngle = 0;
            if (orientation == 6) {
                rotationAngle = 90;
            }
            if (orientation == 3) {
                rotationAngle = 180;
            }
            if (orientation == 8) {
                rotationAngle = 270;
            }
            Matrix matrix = new Matrix();
            matrix.setRotate(rotationAngle, bm.getWidth() / 2.0f, bm.getHeight() / 2.0f);
            return Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
