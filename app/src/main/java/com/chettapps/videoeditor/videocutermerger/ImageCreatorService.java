package com.chettapps.videoeditor.videocutermerger;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.bumptech.glide.Glide;

import com.chettapps.videoeditor.R;
import com.chettapps.videoeditor.videocutermerger.activities.SlideShow_Video_Activity;
import com.chettapps.videoeditor.videocutermerger.mask.FinalMaskBitmap3D;
import com.chettapps.videoeditor.videocutermerger.utils.FileUtils;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;


public class ImageCreatorService extends IntentService

{
    public static final String ACTION_CREATE_NEW_THEME_IMAGES = "ACTION_CREATE_NEW_THEME_IMAGES";
    public static final String ACTION_UPDATE_THEME_IMAGES = "ACTION_UPDATE_THEME_IMAGES";
    public static final String EXTRA_SELECTED_IMAGE = "selected_image";
    public static final String EXTRA_SELECTED_THEME = "selected_theme";
    public static ArrayList<String> arrayList;
    public static boolean isImageComplate = false;
    public static final Object mLock = new Object();
    MyApplication application;
    private Notification.Builder mBuilder;
    private String selectedTheme;
    int totalImages;
    public static final int   MAX_VALUE = 0x7fffffff;



    public ImageCreatorService() {
        this(ImageCreatorService.class.getName());
    }



    public ImageCreatorService(String name)
    {
        super(name);
    }

    @Override // android.app.IntentService, android.app.Service
    public void onCreate() {
        super.onCreate();
        this.application = MyApplication.getInstance();
    }

    @Override // android.app.IntentService, android.app.Service
    @Deprecated
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override // android.app.IntentService, android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.IntentService
    protected void onHandleIntent(Intent intent) {
        Log.e("TAG", "onHandleIntent11");
        this.mBuilder = new Notification.Builder(this);
        this.mBuilder.setContentTitle("Preparing Video").setContentText("Making in progress").setSmallIcon(R.drawable.icon_play_video);
        this.selectedTheme = intent.getStringExtra(EXTRA_SELECTED_THEME);
        Log.e("TAG", "onHandleIntent22" + this.selectedTheme);
        arrayList = SlideShow_Video_Activity.paths;
        Log.e("TAG", "onHandleIntent33" + arrayList);
        Log.e("TAG", "onHandleIntent44" + arrayList.size());
        isImageComplate = false;
        createImages();
    }

    private void createImages() {
        Bitmap newFirstBmp;
        Log.e("TAG", "createImages11");
        Bitmap newSecondBmp2 = null;
        this.totalImages = arrayList.size();
        Log.e("TAG", "createImagesf"+arrayList.size());

        int l = 0;
        int i = 0;



        while (i < arrayList.size() - 1 && isSameTheme() && !MyApplication.isBreak) {
            l=i;
            Log.e("TAG", "createImagesf"+l);
            Log.e("TAG", "createImagesf"+l);
            Log.e("TAG", "createImagesf"+isSameTheme());
            Log.e("TAG", "createImagesf"+!MyApplication.isBreak);


            File imgDir = FileUtils.getImageDirectory(this.application.selectedTheme.toString(), i);


            Log.e("TAG", "createImagesf"+imgDir);

            if (l == 0) {
                Bitmap firstBitmap = ScalingUtilities.checkBitmap(arrayList.get(l));
                Bitmap temp = ScalingUtilities.scaleCenterCrop(firstBitmap, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
                newFirstBmp = ScalingUtilities.ConvetrSameSizeNew(firstBitmap, temp, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
                temp.recycle();
                firstBitmap.recycle();
                System.gc();
            }
            else {
                if (newSecondBmp2 == null || newSecondBmp2.isRecycled()) {
                    Bitmap firstBitmap2 = ScalingUtilities.checkBitmap(arrayList.get(l));
                    Bitmap temp2 = ScalingUtilities.scaleCenterCrop(firstBitmap2, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
                    newSecondBmp2 = ScalingUtilities.ConvetrSameSizeNew(firstBitmap2, temp2, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
                    temp2.recycle();
                    firstBitmap2.recycle();
                }
                newFirstBmp = newSecondBmp2;
            }




            Bitmap secondBitmap = ScalingUtilities.checkBitmap(arrayList.get(l + 1));
            Bitmap temp22 = ScalingUtilities.scaleCenterCrop(secondBitmap, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
            newSecondBmp2 = ScalingUtilities.ConvetrSameSizeNew(secondBitmap, temp22, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
            temp22.recycle();
            secondBitmap.recycle();
            System.gc();




            FinalMaskBitmap3D.EFFECT effect = this.application.selectedTheme.getTheme().get(l % this.application.selectedTheme.getTheme().size());


            Log.e("TAG", "createImages: "+newFirstBmp );
            Log.e("TAG", "createImages: "+newSecondBmp2 );


            effect.initBitmaps(newFirstBmp, newSecondBmp2);




            for (int j = 0; j < FinalMaskBitmap3D.ANIMATED_FRAME; j++) {

                Log.e("TAG", "createImages11"+FinalMaskBitmap3D.ANIMATED_FRAME);


                Log.e("TAG", "createImages: "+newFirstBmp );
                Log.e("TAG", "createImages: "+newSecondBmp2 );


                Bitmap bitmap3 = effect.getMask(newFirstBmp, newSecondBmp2, j);
                Log.e("TAG", "createImages: "+bitmap3 );






                if (isSameTheme()) {
                    File file = new File(imgDir, String.format("img%02d.jpg", Integer.valueOf(j)));
                    try {
                        if (file.exists()) {
                            file.delete();
                        }
                        OutputStream fileOutputStream = new FileOutputStream(file);
                        bitmap3.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                        fileOutputStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    boolean isBreak = false;
                    while (SlideShow_Video_Activity.isEditModeEnable) {
                        if (SlideShow_Video_Activity.min_pos != Integer.MAX_VALUE) {
                            i = SlideShow_Video_Activity.min_pos;
                            isBreak = true;
                        }
                        if (MyApplication.isBreak) {
                            isImageComplate = true;
                            stopSelf();
                            return;
                        }
                    }
                    if (isBreak) {
                        ArrayList<String> str = new ArrayList<>();
                        str.addAll(SlideShow_Video_Activity.videoImages);
                        Log.e("TAG", "createImages22" + str);
                        SlideShow_Video_Activity.videoImages.clear();
                        int size = Math.min(str.size(), Math.max(0, l - l) * 30);
                        for (int p = 0; p < size; p++) {
                            SlideShow_Video_Activity.videoImages.add(str.get(p));
                        }
                        SlideShow_Video_Activity.min_pos = MAX_VALUE;
                    }

                    Log.e("TAG", "createImages:66 "+isSameTheme());
                    Log.e("TAG", "createImages:77 "+!MyApplication.isBreak);

                    if (isSameTheme() && !MyApplication.isBreak) {


                        SlideShow_Video_Activity.videoImages.add(file.getAbsolutePath());
                        Log.e("TAG", "createImages33" + SlideShow_Video_Activity.f102i);

                        updateImageProgress();

                        Log.e("TAG", "createImages:88 "+(FinalMaskBitmap3D.ANIMATED_FRAME - 1.0f));
                        Log.e("TAG", "createImages:99 "+j);



                        if (j == FinalMaskBitmap3D.ANIMATED_FRAME - 1.0f) {


                            for (int m = 0; m < 8 && isSameTheme() && !MyApplication.isBreak; m++) {
                                Log.e("TAG", "createImages44" + file);
                                SlideShow_Video_Activity.videoImages.add(file.getAbsolutePath());
                                updateImageProgress();
                                Log.e("TAG", "createImages44" + SlideShow_Video_Activity.videoImages.size());

                            }





                        }
                    }
                    l++;
                }
            }



            i++;

            Log.e("TAG", "createImagesf2"+isSameTheme());
            Log.e("TAG", "createImagesf2"+!MyApplication.isBreak);




        }

        Log.e("TAG", "createImagesf3"+i);
        Log.e("TAG", "createImagesf3"+!MyApplication.isBreak);

        Glide.get(this).clearDiskCache();
        isImageComplate = true;
        stopSelf();
        isSameTheme();
    }

    private boolean isSameTheme() {
        Log.d("bbbbb....ImgCretSer....", ".................isSameTheme...............");
        return this.selectedTheme.equals(this.application.getCurrentTheme());
    }

    private void updateImageProgress() {
        Log.d("bbbbb....ImgCretSer....", ".................updateImageProgress...............");
        final float progress = (100.0f * SlideShow_Video_Activity.videoImages.size()) / ((this.totalImages - 1) * 30);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                OnProgressReceiver receiver = ImageCreatorService.this.application.getOnProgressReceiver();
                if (receiver != null) {
                    receiver.onImageProgressFrameUpdate(progress);
                }
            }
        });
    }
}
