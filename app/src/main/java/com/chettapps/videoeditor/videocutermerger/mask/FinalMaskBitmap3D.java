package com.chettapps.videoeditor.videocutermerger.mask;

import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;


import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;

import com.chettapps.videoeditor.videocutermerger.MyApplication;

import java.lang.reflect.Array;
import java.util.Random;

/* loaded from: classes.dex */
public class FinalMaskBitmap3D {
    static final int HORIZONTAL = 0;
    static final int VERTICALE = 1;
    private static int averageHeight;
    private static int averageWidth;
    private static float axisX;
    private static float axisY;
    private static Bitmap[][] bitmaps;
    static int[][] randRect;
    public static EFFECT rollMode;
    private static float rotateDegree;
    public static float ANIMATED_FRAME = 22.0f;
    public static int ANIMATED_FRAME_CAL = (int) (ANIMATED_FRAME - 1.0f);
    public static int ORIGANAL_FRAME = 8;
    public static int TOTAL_FRAME = 30;
    private static Camera camera = new Camera();
    public static int direction = 0;
    private static Matrix matrix = new Matrix();
    static final Paint paint = new Paint();
    private static int partNumber = 8;
    static Random random = new Random();

    static {
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    /* loaded from: classes.dex */


    public enum EFFECT {
        Roll2D_TB("Roll2D Up Down") {


            @Override
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor) {

                FinalMaskBitmap3D.setRotateDegree(factor);

                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Bitmap.Config.ARGB_8888);
                Log.e("TAG", "getMask: " + bottom);
                Log.e("TAG", "getMask: " + top);


                FinalMaskBitmap3D.drawRollWhole3D(bottom, top, new Canvas(mask), true);
                Log.e("TAG", "getMask: " + mask);


                return mask;


            }

            @Override

            public void initBitmaps(Bitmap bottom, Bitmap top) {

                int unused = FinalMaskBitmap3D.partNumber = 8;
                FinalMaskBitmap3D.direction = 1;
                FinalMaskBitmap3D.rollMode = this;
                Camera unused2 = FinalMaskBitmap3D.camera = new Camera();
                Matrix unused3 = FinalMaskBitmap3D.matrix = new Matrix();


            }
        },


        Roll2D_BT("Roll2D Down Up") { // from class: com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT.2

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor) {
                FinalMaskBitmap3D.setRotateDegree(FinalMaskBitmap3D.ANIMATED_FRAME_CAL - factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Bitmap.Config.ARGB_8888);
                FinalMaskBitmap3D.drawRollWhole3D(top, bottom, new Canvas(mask), true);
                return mask;
            }

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public void initBitmaps(Bitmap bottom, Bitmap top) {
                int unused = FinalMaskBitmap3D.partNumber = 8;
                FinalMaskBitmap3D.direction = 1;
                FinalMaskBitmap3D.rollMode = this;
                Camera unused2 = FinalMaskBitmap3D.camera = new Camera();
                Matrix unused3 = FinalMaskBitmap3D.matrix = new Matrix();
            }
        },
        Roll2D_LR("Roll2D Left Right") { // from class: com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT.3

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor) {
                FinalMaskBitmap3D.setRotateDegree(factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Bitmap.Config.ARGB_8888);
                FinalMaskBitmap3D.drawRollWhole3D(bottom, top, new Canvas(mask), true);
                return mask;
            }

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public void initBitmaps(Bitmap bottom, Bitmap top) {
                int unused = FinalMaskBitmap3D.partNumber = 8;
                FinalMaskBitmap3D.direction = 0;
                FinalMaskBitmap3D.rollMode = this;
                Camera unused2 = FinalMaskBitmap3D.camera = new Camera();
                Matrix unused3 = FinalMaskBitmap3D.matrix = new Matrix();
            }
        },


        Roll2D_RL("Roll2D Right Left") {
            @Override
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor) {
                FinalMaskBitmap3D.setRotateDegree(FinalMaskBitmap3D.ANIMATED_FRAME_CAL - factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Bitmap.Config.ARGB_8888);
                FinalMaskBitmap3D.drawRollWhole3D(top, bottom, new Canvas(mask), true);
                return mask;
            }

            @Override
            public void initBitmaps(Bitmap bottom, Bitmap top) {
                int unused = FinalMaskBitmap3D.partNumber = 8;
                FinalMaskBitmap3D.direction = 0;
                FinalMaskBitmap3D.rollMode = this;
                Camera unused2 = FinalMaskBitmap3D.camera = new Camera();
                Matrix unused3 = FinalMaskBitmap3D.matrix = new Matrix();
            }
        },


        Whole3D_TB("Whole3D Up Down") { // from class: com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT.5

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor) {
                FinalMaskBitmap3D.setRotateDegree(factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                FinalMaskBitmap3D.rollMode = this;
                FinalMaskBitmap3D.drawRollWhole3D(bottom, top, canvas, false);
                return mask;
            }

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public void initBitmaps(Bitmap bottom, Bitmap top) {
                int unused = FinalMaskBitmap3D.partNumber = 8;
                FinalMaskBitmap3D.direction = 1;
                Camera unused2 = FinalMaskBitmap3D.camera = new Camera();
                Matrix unused3 = FinalMaskBitmap3D.matrix = new Matrix();
                FinalMaskBitmap3D.rollMode = this;
            }
        },
        Whole3D_BT("Whole3D Down Up") { // from class: com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT.6

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor) {
                FinalMaskBitmap3D.setRotateDegree(FinalMaskBitmap3D.ANIMATED_FRAME_CAL - factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                FinalMaskBitmap3D.rollMode = this;
                FinalMaskBitmap3D.drawRollWhole3D(top, bottom, canvas, false);
                return mask;
            }

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public void initBitmaps(Bitmap bottom, Bitmap top) {
                FinalMaskBitmap3D.direction = 1;
                int unused = FinalMaskBitmap3D.partNumber = 8;
                FinalMaskBitmap3D.rollMode = this;
                Camera unused2 = FinalMaskBitmap3D.camera = new Camera();
                Matrix unused3 = FinalMaskBitmap3D.matrix = new Matrix();
            }
        },
        Whole3D_LR("Whole3D Left Right") { // from class: com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT.7

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor) {
                FinalMaskBitmap3D.setRotateDegree(factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                FinalMaskBitmap3D.rollMode = this;
                FinalMaskBitmap3D.drawRollWhole3D(bottom, top, canvas, false);
                return mask;
            }

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public void initBitmaps(Bitmap bottom, Bitmap top) {
                int unused = FinalMaskBitmap3D.partNumber = 8;
                FinalMaskBitmap3D.direction = 0;
                FinalMaskBitmap3D.rollMode = this;
                Camera unused2 = FinalMaskBitmap3D.camera = new Camera();
                Matrix unused3 = FinalMaskBitmap3D.matrix = new Matrix();
            }
        },
        Whole3D_RL("Whole3D Right Left") { // from class: com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT.8

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor) {
                FinalMaskBitmap3D.setRotateDegree(FinalMaskBitmap3D.ANIMATED_FRAME_CAL - factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                FinalMaskBitmap3D.rollMode = this;
                FinalMaskBitmap3D.drawRollWhole3D(top, bottom, canvas, false);
                return mask;
            }

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public void initBitmaps(Bitmap bottom, Bitmap top) {
                int unused = FinalMaskBitmap3D.partNumber = 8;
                FinalMaskBitmap3D.rollMode = this;
                FinalMaskBitmap3D.direction = 0;
                Camera unused2 = FinalMaskBitmap3D.camera = new Camera();
                Matrix unused3 = FinalMaskBitmap3D.matrix = new Matrix();
            }
        },
        SepartConbine_TB("SepartConbine Up Down") { // from class: com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT.9

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor) {
                FinalMaskBitmap3D.rollMode = this;
                FinalMaskBitmap3D.setRotateDegree(factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Bitmap.Config.ARGB_8888);
                FinalMaskBitmap3D.drawSepartConbine(new Canvas(mask));
                return mask;
            }

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public void initBitmaps(Bitmap bottom, Bitmap top) {
                int unused = FinalMaskBitmap3D.partNumber = 4;
                FinalMaskBitmap3D.rollMode = this;
                FinalMaskBitmap3D.direction = 1;
                Camera unused2 = FinalMaskBitmap3D.camera = new Camera();
                Matrix unused3 = FinalMaskBitmap3D.matrix = new Matrix();
                FinalMaskBitmap3D.initBitmaps(bottom, top, this);
            }
        },
        SepartConbine_BT("SepartConbine Down Up") { // from class: com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT.10

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor) {
                FinalMaskBitmap3D.rollMode = this;
                FinalMaskBitmap3D.setRotateDegree(FinalMaskBitmap3D.ANIMATED_FRAME_CAL - factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Bitmap.Config.ARGB_8888);
                FinalMaskBitmap3D.drawSepartConbine(new Canvas(mask));
                return mask;
            }

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public void initBitmaps(Bitmap bottom, Bitmap top) {
                int unused = FinalMaskBitmap3D.partNumber = 4;
                FinalMaskBitmap3D.rollMode = this;
                FinalMaskBitmap3D.direction = 1;
                Camera unused2 = FinalMaskBitmap3D.camera = new Camera();
                Matrix unused3 = FinalMaskBitmap3D.matrix = new Matrix();
                FinalMaskBitmap3D.initBitmaps(top, bottom, this);
            }
        },
        SepartConbine_LR("SepartConbine Left Right") { // from class: com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT.11

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor) {
                FinalMaskBitmap3D.rollMode = this;
                FinalMaskBitmap3D.setRotateDegree(factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Bitmap.Config.ARGB_8888);
                FinalMaskBitmap3D.drawSepartConbine(new Canvas(mask));
                return mask;
            }

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public void initBitmaps(Bitmap bottom, Bitmap top) {
                int unused = FinalMaskBitmap3D.partNumber = 4;
                FinalMaskBitmap3D.rollMode = this;
                FinalMaskBitmap3D.direction = 0;
                Camera unused2 = FinalMaskBitmap3D.camera = new Camera();
                Matrix unused3 = FinalMaskBitmap3D.matrix = new Matrix();
                FinalMaskBitmap3D.initBitmaps(bottom, top, this);
            }
        },
        SepartConbine_RL("SepartConbine Right Left") { // from class: com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT.12

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor) {
                FinalMaskBitmap3D.rollMode = this;
                FinalMaskBitmap3D.setRotateDegree(FinalMaskBitmap3D.ANIMATED_FRAME_CAL - factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Bitmap.Config.ARGB_8888);
                FinalMaskBitmap3D.drawSepartConbine(new Canvas(mask));
                return mask;
            }

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public void initBitmaps(Bitmap bottom, Bitmap top) {
                int unused = FinalMaskBitmap3D.partNumber = 4;
                FinalMaskBitmap3D.rollMode = this;
                FinalMaskBitmap3D.direction = 0;
                Camera unused2 = FinalMaskBitmap3D.camera = new Camera();
                Matrix unused3 = FinalMaskBitmap3D.matrix = new Matrix();
                FinalMaskBitmap3D.initBitmaps(top, bottom, this);
            }
        },
        RollInTurn_TB("RollInTurn Up Down") { // from class: com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT.13

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor) {
                FinalMaskBitmap3D.rollMode = this;
                FinalMaskBitmap3D.setRotateDegree(factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Bitmap.Config.ARGB_8888);
                FinalMaskBitmap3D.drawRollInTurn(new Canvas(mask));
                return mask;
            }

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public void initBitmaps(Bitmap bottom, Bitmap top) {
                int unused = FinalMaskBitmap3D.partNumber = 8;
                FinalMaskBitmap3D.rollMode = this;
                FinalMaskBitmap3D.direction = 1;
                Camera unused2 = FinalMaskBitmap3D.camera = new Camera();
                Matrix unused3 = FinalMaskBitmap3D.matrix = new Matrix();
                FinalMaskBitmap3D.initBitmaps(bottom, top, this);
            }
        },
        RollInTurn_BT("RollInTurn Down Up") { // from class: com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT.14

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor) {
                FinalMaskBitmap3D.rollMode = this;
                FinalMaskBitmap3D.setRotateDegree(FinalMaskBitmap3D.ANIMATED_FRAME_CAL - factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Bitmap.Config.ARGB_8888);
                FinalMaskBitmap3D.drawRollInTurn(new Canvas(mask));
                return mask;
            }

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public void initBitmaps(Bitmap bottom, Bitmap top) {
                FinalMaskBitmap3D.rollMode = this;
                int unused = FinalMaskBitmap3D.partNumber = 8;
                FinalMaskBitmap3D.direction = 1;
                Camera unused2 = FinalMaskBitmap3D.camera = new Camera();
                Matrix unused3 = FinalMaskBitmap3D.matrix = new Matrix();
                FinalMaskBitmap3D.initBitmaps(top, bottom, this);
            }
        },
        RollInTurn_LR("RollInTurn Left Right") { // from class: com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT.15

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor) {
                FinalMaskBitmap3D.rollMode = this;
                FinalMaskBitmap3D.setRotateDegree(factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Bitmap.Config.ARGB_8888);
                FinalMaskBitmap3D.drawRollInTurn(new Canvas(mask));
                return mask;
            }

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public void initBitmaps(Bitmap bottom, Bitmap top) {
                FinalMaskBitmap3D.rollMode = this;
                int unused = FinalMaskBitmap3D.partNumber = 8;
                FinalMaskBitmap3D.direction = 0;
                Camera unused2 = FinalMaskBitmap3D.camera = new Camera();
                Matrix unused3 = FinalMaskBitmap3D.matrix = new Matrix();
                FinalMaskBitmap3D.initBitmaps(bottom, top, this);
            }
        },
        RollInTurn_RL("RollInTurn Right Left") { // from class: com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT.16

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor) {
                FinalMaskBitmap3D.rollMode = this;
                FinalMaskBitmap3D.setRotateDegree(FinalMaskBitmap3D.ANIMATED_FRAME_CAL - factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Bitmap.Config.ARGB_8888);
                FinalMaskBitmap3D.drawRollInTurn(new Canvas(mask));
                return mask;
            }

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public void initBitmaps(Bitmap bottom, Bitmap top) {
                FinalMaskBitmap3D.rollMode = this;
                int unused = FinalMaskBitmap3D.partNumber = 8;
                FinalMaskBitmap3D.direction = 0;
                Camera unused2 = FinalMaskBitmap3D.camera = new Camera();
                Matrix unused3 = FinalMaskBitmap3D.matrix = new Matrix();
                FinalMaskBitmap3D.initBitmaps(top, bottom, this);
            }
        },
        Jalousie_BT("Jalousie Down Up") { // from class: com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT.17

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor) {
                FinalMaskBitmap3D.rollMode = this;
                FinalMaskBitmap3D.setRotateDegree(FinalMaskBitmap3D.ANIMATED_FRAME_CAL - factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Bitmap.Config.ARGB_8888);
                FinalMaskBitmap3D.drawJalousie(new Canvas(mask));
                return mask;
            }

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public void initBitmaps(Bitmap bottom, Bitmap top) {
                FinalMaskBitmap3D.rollMode = this;
                int unused = FinalMaskBitmap3D.partNumber = 8;
                FinalMaskBitmap3D.direction = 1;
                Camera unused2 = FinalMaskBitmap3D.camera = new Camera();
                Matrix unused3 = FinalMaskBitmap3D.matrix = new Matrix();
                FinalMaskBitmap3D.initBitmaps(top, bottom, this);
            }
        },
        Jalousie_LR("Jalousie Left Right") { // from class: com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT.18

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor) {
                FinalMaskBitmap3D.rollMode = this;
                FinalMaskBitmap3D.setRotateDegree(factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Bitmap.Config.ARGB_8888);
                FinalMaskBitmap3D.drawJalousie(new Canvas(mask));
                return mask;
            }

            @Override
            // com.photovideomaker.pictovideditor.videocutermerger.mask.FinalMaskBitmap3D.EFFECT
            public void initBitmaps(Bitmap bottom, Bitmap top) {
                FinalMaskBitmap3D.rollMode = this;
                int unused = FinalMaskBitmap3D.partNumber = 8;
                FinalMaskBitmap3D.direction = 0;
                Camera unused2 = FinalMaskBitmap3D.camera = new Camera();
                Matrix unused3 = FinalMaskBitmap3D.matrix = new Matrix();
                FinalMaskBitmap3D.initBitmaps(bottom, top, this);
            }
        },


        CIRCLE_D("Circle") {


            @Override
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor) {

                FinalMaskBitmap3D.setRotateDegree(factor);

                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Bitmap.Config.ARGB_8888);
                Log.e("TAG", "getMask: " + bottom);
                Log.e("TAG", "getMask: " + top);


                FinalMaskBitmap3D.drawRollWhole3D(bottom, top, new Canvas(mask), true);
                Log.e("TAG", "getMask: " + mask);


                return mask;


            }

            @Override

            public void initBitmaps(Bitmap bottom, Bitmap top) {

                int unused = FinalMaskBitmap3D.partNumber = 8;
                FinalMaskBitmap3D.direction = 3;
                FinalMaskBitmap3D.rollMode = this;
                Camera unused2 = FinalMaskBitmap3D.camera = new Camera();
                Matrix unused3 = FinalMaskBitmap3D.matrix = new Matrix();


            }



        };


        String name;

        public abstract Bitmap getMask(Bitmap bitmap, Bitmap bitmap2, int i);

        public abstract void initBitmaps(Bitmap bitmap, Bitmap bitmap2);

        EFFECT(String name) {
            this.name = "";
            this.name = name;
        }


        public void drawText(Canvas canvas) {
            Paint paint = new Paint();
            paint.setTextSize(50.0f);
            paint.setColor(SupportMenu.CATEGORY_MASK);
        }


    }

    public static void reintRect() {
        randRect = (int[][]) Array.newInstance(Integer.TYPE, (int) ANIMATED_FRAME, (int) ANIMATED_FRAME);
        for (int i = 0; i < randRect.length; i++) {
            for (int j = 0; j < randRect[i].length; j++) {
                randRect[i][j] = 0;
            }
        }
    }

    public static Paint getPaint() {
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        return paint;
    }

    static float getRad(int w, int h) {
        return (float) Math.sqrt(((w * w) + (h * h)) / 4);
    }

    static Bitmap getHorizontalColumnDownMask(int w, int h, int factor) {
        Paint paint2 = new Paint();
        paint2.setColor(ViewCompat.MEASURED_STATE_MASK);
        paint2.setAntiAlias(true);
        paint2.setStyle(Paint.Style.FILL_AND_STROKE);
        Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mask);
        float factorX = ANIMATED_FRAME_CAL / 2.0f;
        canvas.drawRoundRect(new RectF(0.0f, 0.0f, (w / (ANIMATED_FRAME_CAL / 2.0f)) * factor, h / 2.0f), 0.0f, 0.0f, paint2);
        if (factor >= 0.5f + factorX) {
            canvas.drawRoundRect(new RectF(w - ((w / ((ANIMATED_FRAME_CAL - 1) / 2.0f)) * ((int) (factor - factorX))), h / 2.0f, w, h), 0.0f, 0.0f, paint2);
        }
        return mask;
    }

    static Bitmap getRandomRectHoriMask(int w, int h, int factor) {
        Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mask);
        Paint paint2 = new Paint();
        paint2.setColor(ViewCompat.MEASURED_STATE_MASK);
        paint2.setAntiAlias(true);
        paint2.setStyle(Paint.Style.FILL_AND_STROKE);
        float wf = w / 10.0f;
        float rectW = factor == 0 ? 0.0f : (factor * wf) / 9.0f;
        for (int i = 0; i < 10; i++) {
            canvas.drawRect(new Rect((int) (i * wf), 0, (int) ((i * wf) + rectW), h), paint2);
        }
        return mask;
    }

    static Bitmap getNewMask(int w, int h, int factor) {
        Paint paint2 = new Paint();
        paint2.setColor(ViewCompat.MEASURED_STATE_MASK);
        paint2.setAntiAlias(true);
        paint2.setStyle(Paint.Style.FILL_AND_STROKE);
        Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mask);
        float ratioX = (w / 18.0f) * factor;
        float ratioY = (h / 18.0f) * factor;
        Path path = new Path();
        path.moveTo(w / 2, h / 2);
        path.lineTo((w / 2) + ratioX, h / 2);
        path.lineTo(w, 0.0f);
        path.lineTo(w / 2, (h / 2) - ratioY);
        path.lineTo(w / 2, h / 2);
        path.moveTo(w / 2, h / 2);
        path.lineTo((w / 2) - ratioX, h / 2);
        path.lineTo(0.0f, 0.0f);
        path.lineTo(w / 2, (h / 2) - ratioY);
        path.lineTo(w / 2, h / 2);
        path.moveTo(w / 2, h / 2);
        path.lineTo((w / 2) - ratioX, h / 2);
        path.lineTo(0.0f, h);
        path.lineTo(w / 2, (h / 2) + ratioY);
        path.lineTo(w / 2, h / 2);
        path.moveTo(w / 2, h / 2);
        path.lineTo((w / 2) + ratioX, h / 2);
        path.lineTo(w, h);
        path.lineTo(w / 2, (h / 2) + ratioY);
        path.lineTo(w / 2, h / 2);
        path.close();
        canvas.drawCircle(w / 2.0f, h / 2.0f, (w / 18.0f) * factor, paint2);
        canvas.drawPath(path, paint2);
        return mask;
    }

    public static Bitmap getRadialBitmap(int w, int h, int factor) {
        Bitmap mask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        if (factor != 0) {
            Canvas canvas = new Canvas(mask);
            if (factor == 9) {
                canvas.drawColor(ViewCompat.MEASURED_STATE_MASK);
            } else {
                paint.setStyle(Paint.Style.STROKE);
                float radius = getRad(w, h);
                paint.setStrokeWidth((radius / 80.0f) * factor);
                for (int i = 0; i < 11; i++) {
                    canvas.drawCircle(w / 2.0f, h / 2.0f, (radius / 10.0f) * i, paint);
                }
            }
        }
        return mask;
    }

    public static void setRotateDegree(int factor) {

        int i = 90;

        if (rollMode == EFFECT.RollInTurn_BT || rollMode == EFFECT.RollInTurn_LR || rollMode == EFFECT.RollInTurn_RL || rollMode == EFFECT.RollInTurn_TB) {
            rotateDegree = ((((partNumber - 1) * 30.0f) + 90.0f) * factor) / ANIMATED_FRAME_CAL;
        } else if (rollMode == EFFECT.Jalousie_BT || rollMode == EFFECT.Jalousie_LR) {
            rotateDegree = (180.0f * factor) / ANIMATED_FRAME_CAL;
        } else {

            rotateDegree = (factor * 90.0f) / ANIMATED_FRAME_CAL;
        }


        if (direction == 1) {
            float f = rotateDegree;
            if (rollMode == EFFECT.Jalousie_BT || rollMode == EFFECT.Jalousie_LR) {
                i = 180;
            }
            axisY = (f / i) * MyApplication.VIDEO_HEIGHT;

            Log.e("TAG", "setRotateDegree1: " + axisY);
            Log.e("TAG", "setRotateDegree2: " + axisX);
            Log.e("TAG", "setRotateDegree3: " + rotateDegree);


            return;
        }
        if (direction == 3) {
            float f = rotateDegree;
            if (rollMode == EFFECT.Jalousie_BT || rollMode == EFFECT.Jalousie_LR) {
                i = 180;
            }
            axisY = (f / i) * MyApplication.VIDEO_HEIGHT;

            Log.e("TAG", "setRotateDegree1: " + axisY);
            Log.e("TAG", "setRotateDegree2: " + axisX);
            Log.e("TAG", "setRotateDegree3: " + rotateDegree);


            return;
        }

        float f2 = rotateDegree;
        if (rollMode == EFFECT.Jalousie_BT || rollMode == EFFECT.Jalousie_LR) {
            i = 180;
        }
        axisX = (f2 / i) * MyApplication.VIDEO_WIDTH;


    }

    public static void initBitmaps(Bitmap bottom, Bitmap top, EFFECT effect) {
        Bitmap partBitmap;
        Bitmap bitmap;
        Bitmap bitmap2;
        Bitmap bitmap3;
        rollMode = effect;
        if (MyApplication.VIDEO_HEIGHT > 0 || MyApplication.VIDEO_WIDTH > 0) {
            bitmaps = (Bitmap[][]) Array.newInstance(Bitmap.class, 2, partNumber);
            averageWidth = MyApplication.VIDEO_WIDTH / partNumber;
            averageHeight = MyApplication.VIDEO_HEIGHT / partNumber;
            int i = 0;
            while (i < 2) {
                for (int j = 0; j < partNumber; j++) {
                    if (rollMode == EFFECT.Jalousie_BT || rollMode == EFFECT.Jalousie_LR) {
                        if (direction == 1) {
                            Rect rect = new Rect(0, averageHeight * j, MyApplication.VIDEO_WIDTH, (j + 1) * averageHeight);
                            if (i == 0) {
                                bitmap2 = bottom;
                            } else {
                                bitmap2 = top;
                            }
                            partBitmap = getPartBitmap(bitmap2, 0, averageHeight * j, rect);
                        } else {
                            Rect rect2 = new Rect(averageWidth * j, 0, (j + 1) * averageWidth, MyApplication.VIDEO_HEIGHT);
                            if (i == 0) {
                                bitmap = bottom;
                            } else {
                                bitmap = top;
                            }
                            partBitmap = getPartBitmap(bitmap, averageWidth * j, 0, rect2);
                        }
                    } else if (direction == 1) {
                        Rect rect3 = new Rect(averageWidth * j, 0, (j + 1) * averageWidth, MyApplication.VIDEO_HEIGHT);
                        if (i == 0) {
                            bitmap3 = bottom;
                        } else {
                            bitmap3 = top;
                        }
                        partBitmap = getPartBitmap(bitmap3, averageWidth * j, 0, rect3);
                    } else {
                        partBitmap = getPartBitmap(i == 0 ? bottom : top, 0, averageHeight * j, new Rect(0, averageHeight * j, MyApplication.VIDEO_WIDTH, (j + 1) * averageHeight));
                    }
                    bitmaps[i][j] = partBitmap;
                }
                i++;
            }
        }
    }


    private static Bitmap getPartBitmap(Bitmap bitmap, int x, int y, Rect rect) {
        return Bitmap.createBitmap(bitmap, x, y, rect.width(), rect.height());
    }


    public static void drawRollWhole3D(Bitmap bottom, Bitmap top, Canvas canvas, boolean draw2D) {
        canvas.save();


        if (direction == 1) {


            camera.save();
            if (draw2D) {
                camera.rotateX(0.0f);
            } else {
                camera.rotateX(-rotateDegree);
            }

            camera.getMatrix(matrix);
            camera.restore();


            matrix.preTranslate((-MyApplication.VIDEO_WIDTH) / 2, 0.0f);
            matrix.postTranslate(MyApplication.VIDEO_WIDTH / 2, axisY);
            canvas.drawBitmap(bottom, matrix, null);


            camera.save();
            if (draw2D) {
                camera.rotateX(0.0f);
            } else {
                camera.rotateX(90.0f - rotateDegree);
            }
            camera.getMatrix(matrix);
            camera.restore();
            matrix.preTranslate((-MyApplication.VIDEO_WIDTH) / 2, -MyApplication.VIDEO_HEIGHT);
            matrix.postTranslate(MyApplication.VIDEO_WIDTH / 2, axisY);
            canvas.drawBitmap(top, matrix, null);


        }
        else if (direction == 3)

        {



//            camera.save();
//            if (draw2D) {
//                camera.rotateX(0.0f);
//            } else {
//                camera.rotateX(-rotateDegree);
//            }
//
//            camera.getMatrix(matrix);
//            camera.restore();
//            matrix.preTranslate((-MyApplication.VIDEO_WIDTH) / 2, 0.0f);
//            matrix.postTranslate(MyApplication.VIDEO_WIDTH / 2, 0);
//
//
//            canvas.drawBitmap(bottom, matrix, paint);
//
//


            camera.save();


            if (draw2D) {
                camera.rotateX(0.0f);
            } else {
                camera.rotateX(90.0f - rotateDegree);
            }
            camera.getMatrix(matrix);




            camera.restore();
            matrix.preTranslate((-MyApplication.VIDEO_WIDTH) / 2, -MyApplication.VIDEO_HEIGHT);
            matrix.postTranslate(MyApplication.VIDEO_WIDTH / 2, axisY);


            int squareBitmapWidth = Math.min(bottom.getWidth(), bottom.getHeight());

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            Rect rect = new Rect(0, 0, squareBitmapWidth, squareBitmapWidth);
            RectF rectF = new RectF(rect);




            canvas.drawBitmap(bottom, MyApplication.VIDEO_WIDTH,MyApplication.VIDEO_HEIGHT,null);

            canvas.drawCircle(MyApplication.VIDEO_WIDTH/2,MyApplication.VIDEO_HEIGHT/2,
                    (float) (MyApplication.VIDEO_HEIGHT /2),paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(top, matrix, paint);




        }



        else{
            camera.save();
            if (draw2D) {
                camera.rotateY(0.0f);
            } else {
                camera.rotateY(rotateDegree);
            }
            camera.getMatrix(matrix);
            camera.restore();
            matrix.preTranslate(0.0f, (-MyApplication.VIDEO_HEIGHT) / 2);
            matrix.postTranslate(axisX, MyApplication.VIDEO_HEIGHT / 2);
            canvas.drawBitmap(bottom, matrix, paint);
            camera.save();
            if (draw2D) {
                camera.rotateY(0.0f);
            } else {
                camera.rotateY(rotateDegree - 90.0f);
            }
            camera.getMatrix(matrix);
            camera.restore();
            matrix.preTranslate(-MyApplication.VIDEO_WIDTH, (-MyApplication.VIDEO_HEIGHT) / 2);
            matrix.postTranslate(axisX, MyApplication.VIDEO_HEIGHT / 2);
            canvas.drawBitmap(top, matrix, paint);
        }


        canvas.restore();
    }


    public static void drawSepartConbine(Canvas canvas) {
        for (int i = 0; i < partNumber; i++) {
            Bitmap currBitmap = bitmaps[0][i];
            Bitmap nextBitmap = bitmaps[1][i];
            canvas.save();
            if (direction == 1) {
                camera.save();
                camera.rotateX(-rotateDegree);
                camera.getMatrix(matrix);
                camera.restore();
                matrix.preTranslate((-currBitmap.getWidth()) / 2, 0.0f);
                matrix.postTranslate((currBitmap.getWidth() / 2) + (averageWidth * i), axisY);
                canvas.drawBitmap(currBitmap, matrix, paint);
                camera.save();
                camera.rotateX(90.0f - rotateDegree);
                camera.getMatrix(matrix);
                camera.restore();
                matrix.preTranslate((-nextBitmap.getWidth()) / 2, -nextBitmap.getHeight());
                matrix.postTranslate((nextBitmap.getWidth() / 2) + (averageWidth * i), axisY);
                canvas.drawBitmap(nextBitmap, matrix, paint);
            } else {
                camera.save();
                camera.rotateY(rotateDegree);
                camera.getMatrix(matrix);
                camera.restore();
                matrix.preTranslate(0.0f, (-currBitmap.getHeight()) / 2);
                matrix.postTranslate(axisX, (currBitmap.getHeight() / 2) + (averageHeight * i));
                canvas.drawBitmap(currBitmap, matrix, paint);
                camera.save();
                camera.rotateY(rotateDegree - 90.0f);
                camera.getMatrix(matrix);
                camera.restore();
                matrix.preTranslate(-nextBitmap.getWidth(), (-nextBitmap.getHeight()) / 2);
                matrix.postTranslate(axisX, (nextBitmap.getHeight() / 2) + (averageHeight * i));
                canvas.drawBitmap(nextBitmap, matrix, paint);
            }
            canvas.restore();
        }
    }


    public static void drawRollInTurn(Canvas canvas) {
        for (int i = 0; i < partNumber; i++) {
            Bitmap currBitmap = bitmaps[0][i];
            Bitmap nextBitmap = bitmaps[1][i];
            float tDegree = rotateDegree - (i * 30);
            if (tDegree < 0.0f) {
                tDegree = 0.0f;
            }
            if (tDegree > 90.0f) {
                tDegree = 90.0f;
            }
            canvas.save();
            if (direction == 1) {
                float tAxisY = (tDegree / 90.0f) * MyApplication.VIDEO_HEIGHT;
                if (tAxisY > MyApplication.VIDEO_HEIGHT) {
                    tAxisY = MyApplication.VIDEO_HEIGHT;
                }
                if (tAxisY < 0.0f) {
                    tAxisY = 0.0f;
                }
                camera.save();
                camera.rotateX(-tDegree);
                camera.getMatrix(matrix);
                camera.restore();
                matrix.preTranslate(-currBitmap.getWidth(), 0.0f);
                matrix.postTranslate(currBitmap.getWidth() + (averageWidth * i), tAxisY);
                canvas.drawBitmap(currBitmap, matrix, paint);
                camera.save();
                camera.rotateX(90.0f - tDegree);
                camera.getMatrix(matrix);
                camera.restore();
                matrix.preTranslate(-nextBitmap.getWidth(), -nextBitmap.getHeight());
                matrix.postTranslate(nextBitmap.getWidth() + (averageWidth * i), tAxisY);
                canvas.drawBitmap(nextBitmap, matrix, paint);
            } else {
                float tAxisX = (tDegree / 90.0f) * MyApplication.VIDEO_WIDTH;
                if (tAxisX > MyApplication.VIDEO_WIDTH) {
                    tAxisX = MyApplication.VIDEO_WIDTH;
                }
                if (tAxisX < 0.0f) {
                    tAxisX = 0.0f;
                }
                camera.save();
                camera.rotateY(tDegree);
                camera.getMatrix(matrix);
                camera.restore();
                matrix.preTranslate(0.0f, (-currBitmap.getHeight()) / 2);
                matrix.postTranslate(tAxisX, (currBitmap.getHeight() / 2) + (averageHeight * i));
                canvas.drawBitmap(currBitmap, matrix, paint);
                camera.save();
                camera.rotateY(tDegree - 90.0f);
                camera.getMatrix(matrix);
                camera.restore();
                matrix.preTranslate(-nextBitmap.getWidth(), (-nextBitmap.getHeight()) / 2);
                matrix.postTranslate(tAxisX, (nextBitmap.getHeight() / 2) + (averageHeight * i));
                canvas.drawBitmap(nextBitmap, matrix, paint);
            }
            canvas.restore();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void drawJalousie(Canvas canvas) {
        for (int i = 0; i < partNumber; i++) {
            Bitmap currBitmap = bitmaps[0][i];
            Bitmap nextBitmap = bitmaps[1][i];
            canvas.save();
            if (direction == 1) {
                if (rotateDegree < 90.0f) {
                    camera.save();
                    camera.rotateX(rotateDegree);
                    camera.getMatrix(matrix);
                    camera.restore();
                    matrix.preTranslate((-currBitmap.getWidth()) / 2, (-currBitmap.getHeight()) / 2);
                    matrix.postTranslate(currBitmap.getWidth() / 2, (currBitmap.getHeight() / 2) + (averageHeight * i));
                    canvas.drawBitmap(currBitmap, matrix, paint);
                } else {
                    camera.save();
                    camera.rotateX(180.0f - rotateDegree);
                    camera.getMatrix(matrix);
                    camera.restore();
                    matrix.preTranslate((-nextBitmap.getWidth()) / 2, (-nextBitmap.getHeight()) / 2);
                    matrix.postTranslate(nextBitmap.getWidth() / 2, (nextBitmap.getHeight() / 2) + (averageHeight * i));
                    canvas.drawBitmap(nextBitmap, matrix, paint);
                }
            } else if (rotateDegree < 90.0f) {
                camera.save();
                camera.rotateY(rotateDegree);
                camera.getMatrix(matrix);
                camera.restore();
                matrix.preTranslate((-currBitmap.getWidth()) / 2, (-currBitmap.getHeight()) / 2);
                matrix.postTranslate((currBitmap.getWidth() / 2) + (averageWidth * i), currBitmap.getHeight() / 2);
                canvas.drawBitmap(currBitmap, matrix, paint);
            } else {
                camera.save();
                camera.rotateY(180.0f - rotateDegree);
                camera.getMatrix(matrix);
                camera.restore();
                matrix.preTranslate((-nextBitmap.getWidth()) / 2, (-nextBitmap.getHeight()) / 2);
                matrix.postTranslate((nextBitmap.getWidth() / 2) + (averageWidth * i), nextBitmap.getHeight() / 2);
                canvas.drawBitmap(nextBitmap, matrix, paint);
            }
            canvas.restore();
        }
    }
}
