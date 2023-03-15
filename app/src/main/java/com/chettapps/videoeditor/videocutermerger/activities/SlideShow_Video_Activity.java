package com.chettapps.videoeditor.videocutermerger.activities;


import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.chettapps.videoeditor.R;
import com.chettapps.videoeditor.videocutermerger.CircularTextView;
import com.chettapps.videoeditor.videocutermerger.EPreferences;
import com.chettapps.videoeditor.videocutermerger.ImageCreatorService;
import com.chettapps.videoeditor.videocutermerger.MusicData;
import com.chettapps.videoeditor.videocutermerger.MyApplication;
import com.chettapps.videoeditor.videocutermerger.ScalingUtilities;
import com.chettapps.videoeditor.videocutermerger.THEMES;
import com.chettapps.videoeditor.videocutermerger.adapters.Adapter_Animation;
import com.chettapps.videoeditor.videocutermerger.adapters.Adapter_Frame;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.chettapps.videoeditor.videocutermerger.utils.FileUtils;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;


public class SlideShow_Video_Activity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    public static ArrayList<String> paths;
    Adapter_Animation animationAdapter;
    MyApplication application;
    private BottomSheetBehavior<View> behavior;
    private LinearLayout flLoader;
    int frame;
    Adapter_Frame frameAdapter;
    private RequestManager glide;
    int id;
    LayoutInflater inflater;
    private ImageView ivFrame;
    private View ivPlayPause;
    private ImageView ivPreview;
    private ImageView iv_back;
    private MediaPlayer mPlayer;
    ProgressBar progressBar;
    RecyclerView rvAnimation;
    private RecyclerView rvDuration;
    RecyclerView rvFrame;
    private SeekBar seekBar;
    private TextView tvEndTime;
    private TextView tvTime;
    private TextView tv_next;
    public static ArrayList<String> lastData = new ArrayList<>();
    public static boolean isEditModeEnable = false;
    public static boolean isFromSdCardAudio = false;
    public static int min_pos = 0x7fffffff;
    private static float seconds = 2.0f;
    public static int f102i = 0;
    public static ArrayList<String> videoImages = new ArrayList<>();
    private LockRunnable lockRunnable = new LockRunnable();
    boolean isFromTouch = false;
    private Handler handler = new Handler();
    private Float[] duration = {Float.valueOf(1.0f), Float.valueOf(1.5f), Float.valueOf(2.0f), Float.valueOf(2.5f), Float.valueOf(3.0f), Float.valueOf(3.5f), Float.valueOf(4.0f), Float.valueOf(4.5f), Float.valueOf(5.0f)};
    boolean go = false;
    ProgressDialog p;

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        f102i = progress;
        Log.d("bbbbb....SSAV...", ".........onProgressChanged...isFromTouch.... ::::  " + this.isFromTouch);
        if (this.isFromTouch) {
            seekBar.setProgress(Math.min(progress, seekBar.getSecondaryProgress()));
            Log.d("bbbbb....SSAV...", ".........seekBar.getSecondaryProgress()).... ::::  " + seekBar.getSecondaryProgress());
            displayImage();
            seekMediaPlayer();
        }
    }

    private void seekMediaPlayer() {
        if (this.mPlayer != null) {
            try {
                this.mPlayer.seekTo(((int) (((f102i / 30.0f) * seconds) * 1000.0f)) % this.mPlayer.getDuration());
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStartTrackingTouch(SeekBar seekBar) {
        this.isFromTouch = true;
        Log.d("bbbbb....SSAV...", ".........onStartTrackingTouch..isFromTouch.... ::::  " + this.isFromTouch);
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStopTrackingTouch(SeekBar seekBar) {
        this.isFromTouch = false;
        Log.d("bbbbb....SSAV...", ".........onStopTrackingTouch..isFromTouch.... ::::  " + this.isFromTouch);
    }

    /* loaded from: classes.dex */
    class C09418 extends Thread {
        C09418() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Glide.get(SlideShow_Video_Activity.this).clearDiskCache();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class C20383 extends SimpleTarget<Drawable> {

        @Override
        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
            SlideShow_Video_Activity.this.ivPreview.setImageDrawable(resource);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show_1);
        this.application = MyApplication.getInstance();
        Intent data = getIntent();
        paths = data.getStringArrayListExtra("IMAGE_ARR");
        Log.d("bbbb...SSVA...", "imagesPath............................. = " + paths);
        Log.d("bbbb...SSVA...", "path..size.............................:: " + paths.size());
        getWindow().setFlags(1024, 1024);
        Intent intent = new Intent(getApplicationContext(), ImageCreatorService.class);
        intent.putExtra(ImageCreatorService.EXTRA_SELECTED_THEME, this.application.getCurrentTheme());
        startService(intent);
        init();
        setTheme();
        Log.d("bbbb...SSVA...", "..................onCreate................EXTRA_SELECTED_THEME.............." + this.application.getCurrentTheme());

    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.photovideomaker.pictovideditor.videocutermerger.activities.SlideShow_Video_Activity$1] */
    public void setTheme() {
        Log.d("bbbbb....SSVA....", "........((()(()()(()()()()((())()()()(.........setTheme...............");
        if (isFromSdCardAudio) {
            this.lockRunnable.play();
        } else {
            new Thread() { // from class: com.photovideomaker.pictovideditor.videocutermerger.activities.SlideShow_Video_Activity.1

                /* renamed from: com.photovideomaker.pictovideditor.videocutermerger.activities.SlideShow_Video_Activity$1$C09362 */
                /* loaded from: classes.dex */
                class C09362 implements Runnable {
                    C09362() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        SlideShow_Video_Activity.this.reinitMusic();
                        SlideShow_Video_Activity.this.lockRunnable.play();
                    }
                }

                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    THEMES themes = SlideShow_Video_Activity.this.application.selectedTheme;
                    Log.d("bbbbb....SSVA....", "........((()(()()(()()()()((())()()()(.........themes...............::::::" + themes);
                    try {
                        FileUtils.TEMP_DIRECTORY_AUDIO.mkdirs();
                        File tempFile = new File(FileUtils.TEMP_DIRECTORY_AUDIO, "temp.mp3");
                        if (tempFile.exists()) {
                            FileUtils.deleteFile(tempFile);
                        }
                        InputStream in = SlideShow_Video_Activity.this.getResources().openRawResource(themes.getThemeMusic());
                        FileOutputStream out = new FileOutputStream(tempFile);
                        byte[] buff = new byte[1024];
                        while (true) {
                            int read = in.read(buff);
                            if (read <= 0) {
                                break;
                            }
                            out.write(buff, 0, read);
                        }
                        MediaPlayer player = new MediaPlayer();
                        player.setDataSource(tempFile.getAbsolutePath());
                        player.setAudioStreamType(3);
                        player.prepare();
                        final MusicData musicData = new MusicData();
                        musicData.track_data = tempFile.getAbsolutePath();
                        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.photovideomaker.pictovideditor.videocutermerger.activities.SlideShow_Video_Activity.1.1
                            @Override // android.media.MediaPlayer.OnPreparedListener
                            public void onPrepared(MediaPlayer mp) {
                                musicData.track_duration = mp.getDuration();
                                mp.stop();
                            }
                        });
                        musicData.track_Title = "temp";
                        SlideShow_Video_Activity.this.application.setMusicData(musicData);
                        Log.d("bbbbb....SSVA....", "Music Data Is:-  musicData ::::: " + musicData);
                    } catch (Exception e) {
                    }
                    SlideShow_Video_Activity.this.runOnUiThread(new C09362());
                }
            }.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reinitMusic() {
        MusicData musicData = this.application.getMusicData();
        Log.d("bbbbb....SSVA....", ".11..Music Data Is:- " + this.application.getMusicData());
        Log.d("bbbbb....SSVA....", ".00..Music Data Is:- " + musicData.getTrack_data());
        if (musicData != null) {
            this.mPlayer = MediaPlayer.create(this, Uri.parse(musicData.track_data));
            Log.d("bbbbb....SSVA....", "...Music Data Is...  musicData.track_data.... :- " + musicData.track_data);
            if (this.mPlayer != null) {
                this.mPlayer.setLooping(true);
                try {
                    this.mPlayer.prepare();
                } catch (IOException e) {
                } catch (IllegalStateException e2) {
                }
            }
        }
    }

    public void reset() {
        MyApplication.isBreak = false;
        videoImages.clear();
        this.handler.removeCallbacks(this.lockRunnable);
        this.lockRunnable.stop();
        Glide.get(this).clearMemory();
        new C09418().start();
        FileUtils.deleteTempDir();
        this.glide = Glide.with((FragmentActivity) this);
        this.flLoader.setVisibility(View.VISIBLE);
        setTheme();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class LockRunnable implements Runnable {
        boolean isPause = false;

        LockRunnable() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public class C09441 implements Animation.AnimationListener {
            C09441() {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
                SlideShow_Video_Activity.this.ivPlayPause.setVisibility(View.VISIBLE);
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                SlideShow_Video_Activity.this.ivPlayPause.setVisibility(View.INVISIBLE);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public class C09452 implements Animation.AnimationListener {
            C09452() {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
                SlideShow_Video_Activity.this.ivPlayPause.setVisibility(View.VISIBLE);
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            SlideShow_Video_Activity.this.displayImage();
            if (!this.isPause) {
                Handler handler = SlideShow_Video_Activity.this.handler;
                LockRunnable lockRunnable = SlideShow_Video_Activity.this.lockRunnable;
                SlideShow_Video_Activity slideShow_Video_Activity = SlideShow_Video_Activity.this;
                handler.postDelayed(lockRunnable, Math.round(50.0f * SlideShow_Video_Activity.seconds));
            }
        }

        public boolean isPause() {
            return this.isPause;
        }

        public void play() {
            this.isPause = false;
            SlideShow_Video_Activity.this.playMusic();
            Handler handler = SlideShow_Video_Activity.this.handler;
            LockRunnable lockRunnable = SlideShow_Video_Activity.this.lockRunnable;
            SlideShow_Video_Activity slideShow_Video_Activity = SlideShow_Video_Activity.this;
            handler.postDelayed(lockRunnable, Math.round(50.0f * SlideShow_Video_Activity.seconds));
            Animation animation = new AlphaAnimation(1.0f, 0.0f);
            animation.setDuration(500L);
            animation.setFillAfter(true);
            animation.setAnimationListener(new C09441());
            SlideShow_Video_Activity.this.ivPlayPause.startAnimation(animation);
            if (SlideShow_Video_Activity.this.behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                SlideShow_Video_Activity.this.behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        }

        public void pause() {
            this.isPause = true;
            SlideShow_Video_Activity.this.pauseMusic();
            Animation animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration(500L);
            animation.setFillAfter(true);
            SlideShow_Video_Activity.this.ivPlayPause.startAnimation(animation);
            animation.setAnimationListener(new C09452());
        }

        public void stop() {
            pause();
            SlideShow_Video_Activity.f102i = 0;
            if (SlideShow_Video_Activity.this.mPlayer != null) {
                SlideShow_Video_Activity.this.mPlayer.stop();
            }
            SlideShow_Video_Activity.this.reinitMusic();
            SlideShow_Video_Activity.this.seekBar.setProgress(SlideShow_Video_Activity.f102i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playMusic() {
        if (this.flLoader.getVisibility() != View.VISIBLE && this.mPlayer != null && !this.mPlayer.isPlaying()) {
            this.mPlayer.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pauseMusic() {
        if (this.mPlayer != null && this.mPlayer.isPlaying()) {
            this.mPlayer.pause();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void displayImage() {
        Log.d("bbbbb....SSVA....", ".................displayImage.......00......... ::: f102i ::::::::  " + f102i);
        Log.d("bbbbb....SSVA....", ".................displayImage.......00........ ::: ..vd.size.. :::" + videoImages.size());
        try {
            if (f102i >= this.seekBar.getMax()) {
                f102i = 0;
                this.lockRunnable.stop();
            } else {
                if (f102i > 0 && this.flLoader.getVisibility() == View.VISIBLE) {
                    this.flLoader.setVisibility(View.GONE);
                    if (this.mPlayer != null && !this.mPlayer.isPlaying()) {
                        this.mPlayer.start();
                    }
                }
                Log.d("bbbbb....SSVA....", ".................displayImage.......00........ ::: vd.size :::" + videoImages.size());
                this.seekBar.setSecondaryProgress(videoImages.size());
                Log.d("bbbbb....SSVA....", ".................displayImage.......00........ :: getPro ::" + this.seekBar.getProgress());
                Log.d("bbbbb....SSVA....", ".................displayImage.......00........ :: getSec :: " + this.seekBar.getSecondaryProgress());
                if (this.seekBar.getProgress() < this.seekBar.getSecondaryProgress()) {
                    f102i %= videoImages.size();
                    Log.d("bbbbb....SSVA....", ".................displayImage......11...f102i...... ::: vd.size ::: " + videoImages.size());
                    Log.d("bbbbb....SSVA....", ".................displayImage......11...f102i......" + f102i);


                    this.glide.load(videoImages.get(f102i)).signature((Key) new MediaStoreSignature("image/*", System.currentTimeMillis(),
                                    0))
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(new C20383());


                    f102i++;
                    if (!this.isFromTouch) {
                        this.seekBar.setProgress(f102i);
                    }
                    Log.d("bbbbb....SSVA....", ".................displayImage......11...f102i...... @@@@@@  :::  " + f102i);
                    int j = (int) ((f102i / 30.0f) * seconds);
                    int mm = j / 60;
                    int ss = j % 60;
                    this.tvTime.setText(String.format("%02d:%02d", Integer.valueOf(mm), Integer.valueOf(ss)));
                    int total = (int) ((paths.size() - 1) * seconds);
                    this.tvEndTime.setText(String.format("%02d:%02d", Integer.valueOf(total / 60), Integer.valueOf(total % 60)));
                    Log.d("bbbbb....SSVA....", ".................tvTime..............." + String.format("%02d:%02d", Integer.valueOf(mm), Integer.valueOf(ss)));
                    Log.d("bbbbb....SSVA....", ".................endTime..............." + String.format("%02d:%02d", Integer.valueOf(total / 60), Integer.valueOf(total % 60)));
                }
            }
        } catch (Exception e) {
            Log.d("bbbbb....SSVA....", ".................displayImage......f102i........." + f102i);
            Log.d("bbbbb....SSVA....", ".................displayImage.......00........ ::: catch catch catch catch :::" + videoImages.size());
            this.glide = Glide.with((FragmentActivity) this);
        }
    }

    private void init() {
        this.inflater = LayoutInflater.from(this);
        seconds = this.application.getSecond();
        findViewById(R.id.video_clicker).setOnClickListener(this);
        findViewById(R.id.ibAddImages).setOnClickListener(this);
        findViewById(R.id.ibAddDuration).setOnClickListener(this);
        findViewById(R.id.ibAddMusic).setOnClickListener(this);
        this.flLoader = (LinearLayout) findViewById(R.id.flLoader);
        this.tv_next = (TextView) findViewById(R.id.tv_next);
        this.tv_next.setText("Save");
        this.tv_next.setOnClickListener(this);
        this.iv_back = (ImageView) findViewById(R.id.iv_back);
        this.iv_back.setOnClickListener(this);
        this.ivPreview = (ImageView) findViewById(R.id.previewImageView1);
        this.ivPlayPause = (ImageView) findViewById(R.id.ivPlayPause);
        this.seekBar = (SeekBar) findViewById(R.id.sbPlayTime);
        this.seekBar.setOnSeekBarChangeListener(this);
        this.tvEndTime = (TextView) findViewById(R.id.tvEndTime);
        this.tvTime = (TextView) findViewById(R.id.tvTime);
        this.glide = Glide.with((FragmentActivity) this);
        this.ivFrame = (ImageView) findViewById(R.id.ivFrame);
        this.rvAnimation = (RecyclerView) findViewById(R.id.rvThemes);
        this.rvDuration = (RecyclerView) findViewById(R.id.rvDuration);
        this.rvFrame = (RecyclerView) findViewById(R.id.rvFrame);
        this.animationAdapter = new Adapter_Animation(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager((Context) this, 1, RecyclerView.HORIZONTAL, false);
        this.rvAnimation.setLayoutManager(gridLayoutManager);
        this.rvAnimation.setItemAnimator(new DefaultItemAnimator());
        this.rvAnimation.setAdapter(this.animationAdapter);
        this.frameAdapter = new Adapter_Frame(this);
        GridLayoutManager gridLayoutManagerFrame = new GridLayoutManager((Context) this, 1, RecyclerView.HORIZONTAL, false);
        this.rvFrame.setLayoutManager(gridLayoutManagerFrame);
        this.rvFrame.setItemAnimator(new DefaultItemAnimator());
        this.rvFrame.setAdapter(this.frameAdapter);
        this.rvDuration.setHasFixedSize(true);
        this.rvDuration.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.rvDuration.setAdapter(new DurationAdapter());
        this.seekBar.setMax((paths.size() - 1) * 30);
        int total = (int) ((paths.size() - 1) * seconds);
        this.tvEndTime.setText(String.format("%02d:%02d", Integer.valueOf(total / 60), Integer.valueOf(total % 60)));
        this.seekBar.getThumb().setColorFilter(getResources().getColor(R.color.purple_200), PorterDuff.Mode.SRC_IN);
        this.glide.load(paths.get(0)).into(this.ivPreview);
        this.behavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        this.behavior.setBottomSheetCallback(new C16761());
        this.behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        this.lockRunnable.play();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        if (SongEditActivity.isFromSdcard) {
            isFromSdCardAudio = true;
            f102i = 0;
            reinitMusic();
            this.lockRunnable.play();
            SongEditActivity.isFromSdcard = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class C16761 extends BottomSheetBehavior.BottomSheetCallback {
        C16761() {
        }

        @Override // android.support.design.widget.BottomSheetBehavior.BottomSheetCallback
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == 3 && !SlideShow_Video_Activity.this.lockRunnable.isPause()) {
                SlideShow_Video_Activity.this.lockRunnable.pause();
            }
        }

        @Override // android.support.design.widget.BottomSheetBehavior.BottomSheetCallback
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    }

    /* loaded from: classes.dex */
    class C09407 implements Runnable {
        C09407() {
        }

        @Override // java.lang.Runnable
        public void run() {
            MyApplication.isBreak = false;
            SlideShow_Video_Activity.videoImages.clear();
            SlideShow_Video_Activity.min_pos = 0x7fffffff;
            Intent intent = new Intent(SlideShow_Video_Activity.this.getApplicationContext(), ImageCreatorService.class);
            intent.putExtra(ImageCreatorService.EXTRA_SELECTED_THEME, SlideShow_Video_Activity.this.application.getCurrentTheme());
            SlideShow_Video_Activity.this.startService(intent);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back /* 2131755199 */:
                onBackDialog();
                return;
            case R.id.tv_next /* 2131755201 */:
                this.lockRunnable.pause();
                openStoryNameDialog(this);
                return;
            case R.id.video_clicker /* 2131755299 */:
                if (this.lockRunnable.isPause()) {
                    this.lockRunnable.play();
                    return;
                } else {
                    this.lockRunnable.pause();
                    return;
                }
            case R.id.ibAddImages /* 2131755308 */:
                this.flLoader.setVisibility(View.GONE);
                MyApplication.isBreak = true;
                isEditModeEnable = true;
                lastData.addAll(paths);
                Intent intent = new Intent(this, Gallery_ImagePicker_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                intent.putExtra(Gallery_ImagePicker_Activity.EXTRA_FROM_PREVIEW, true);
                startActivityForResult(intent, 102);
                return;
            case R.id.ibAddMusic /* 2131755309 */:
                this.flLoader.setVisibility(View.GONE);
                this.lockRunnable.stop();
                startActivityForResult(new Intent(this, PredefineMusicActivtiy.class), 104);
                Log.e("bbbbb...SSVA...", "........ibAddMusic.........");
                return;
            case R.id.ibAddDuration /* 2131755310 */:
                this.behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                this.lockRunnable.play();
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("bbbbb...isNeedRestart", "........onActivityResult.........");
        isEditModeEnable = false;
        if (resultCode == -1) {
            switch (requestCode) {
                case 101:
                    Log.e("bbbbb...isNeedRestart", "........onActivityResult.....101....");
                    isFromSdCardAudio = true;
                    f102i = 0;
                    reinitMusic();
                    return;
                case 102:
                    Log.e("bbbbb...isNeedRestart", "........isNeedRestart true......Case 102....");
                    if (isNeedRestart()) {
                        Log.e("bbbbb...isNeedRestart", "........isNeedRestart true..........");
                        stopService(new Intent(getApplicationContext(), ImageCreatorService.class));
                        this.lockRunnable.stop();
                        this.seekBar.postDelayed(new C09407(), 1000L);
                        int total = (int) ((paths.size() - 1) * seconds);
                        Log.e("bbbbb...isNeedRestart", "........isNeedRestart true.......... paths.size()..000...  : " + paths.size());
                        Log.e("bbbbb...isNeedRestart", "........isNeedRestart true.......... : ...0000 ............: " + total);
                        this.seekBar.setMax((paths.size() - 1) * 30);
                        this.tvEndTime.setText(String.format("%02d:%02d", Integer.valueOf(total / 60), Integer.valueOf(total % 60)));
                        return;
                    }
                    if (ImageCreatorService.isImageComplate) {
                        Log.e("bbbbb...isNeedRestart", ".........Service complate.......");
                        MyApplication.isBreak = false;
                        videoImages.clear();
                        min_pos = 0x7fffffff;
                        Intent intent = new Intent(getApplicationContext(), ImageCreatorService.class);
                        intent.putExtra(ImageCreatorService.EXTRA_SELECTED_THEME, this.application.getCurrentTheme());
                        startService(intent);
                        f102i = 0;
                        this.seekBar.setProgress(0);
                        Log.e("bbbbb...isNeedRestart", "........isNeedRestart true.......... paths.size()  : " + paths.size());
                    }
                    this.seekBar.setMax((paths.size() - 1) * 30);
                    int total2 = (int) ((paths.size() - 1) * seconds);
                    Log.e("bbbbb...isNeedRestart", "........isNeedRestart true.......... : " + total2);
                    this.tvEndTime.setText(String.format("%02d:%02d", Integer.valueOf(total2 / 60), Integer.valueOf(total2 % 60)));
                    return;
                case 103:
                    this.lockRunnable.stop();
                    if (ImageCreatorService.isImageComplate || !MyApplication.isMyServiceRunning(getApplicationContext(), ImageCreatorService.class)) {
                        MyApplication.isBreak = false;
                        videoImages.clear();
                        min_pos = 0x7fffffff;
                        Intent intent2 = new Intent(getApplicationContext(), ImageCreatorService.class);
                        intent2.putExtra(ImageCreatorService.EXTRA_SELECTED_THEME, this.application.getCurrentTheme());
                        startService(intent2);
                    }
                    f102i = 0;
                    this.seekBar.setProgress(f102i);
                    int total3 = (int) ((paths.size() - 1) * seconds);
                    this.seekBar.setMax((paths.size() - 1) * 30);
                    this.tvEndTime.setText(String.format("%02d:%02d", Integer.valueOf(total3 / 60), Integer.valueOf(total3 % 60)));
                    return;
                case 104:
                    Log.d("bbbbb....SSVA....", "::::::::::::::::::::::::::::::  menu_done ::::::::::::::::::::::::");
                    isFromSdCardAudio = false;
                    f102i = 0;
                    reinitMusic();
                    this.lockRunnable.play();
                    Log.e("bbbbb...SSVA...", "........ibAddMusic.........");
                    return;
                default:
                    return;
            }
        }
    }

    private boolean isNeedRestart() {
        if (lastData.size() > paths.size()) {
            MyApplication.isBreak = true;
            Log.e("bbbbb.....isNeedRestart", "isNeedRestart size");
            return true;
        }
        for (int i = 0; i < lastData.size(); i++) {
            Log.e("bbbbb.....isNeedRestart", String.valueOf(lastData.get(i)) + "___ " + paths.get(i));
            if (!lastData.get(i).equals(paths.get(i))) {
                MyApplication.isBreak = true;
                return true;
            }
        }
        return false;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    @SuppressLint("ResourceType")
    public void openStoryNameDialog(final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(17170445);
        dialog.setContentView(R.layout.dialog_save_video);
        WindowManager.LayoutParams attributes = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        attributes.copyFrom(window.getAttributes());
        if (getScreenWidth() >= 720) {
            attributes.width = dpToPx(320);
        } else {
            attributes.width = -1;
            attributes.horizontalMargin = dpToPx(8);
        }
        attributes.height = -2;
        window.setAttributes(attributes);
        final EditText editText = (EditText) dialog.findViewById(R.id.edtStoryName);
        long currentTimeMillis = System.currentTimeMillis();
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(currentTimeMillis);
        editText.setText("VideoMaker_" + new SimpleDateFormat("MMddyyyy_HHmm", Locale.getDefault()).format(instance.getTime()) + ".mp4");
        editText.selectAll();
        editText.requestFocus();
        dialog.getWindow().setSoftInputMode(4);
        TextView customTextView2 = (TextView) dialog.findViewById(R.id.tvSave);
        ((TextView) dialog.findViewById(R.id.tvCancel)).setOnClickListener(new View.OnClickListener() { // from class: com.photovideomaker.pictovideditor.videocutermerger.activities.SlideShow_Video_Activity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        customTextView2.setOnClickListener(new View.OnClickListener() { // from class: com.photovideomaker.pictovideditor.videocutermerger.activities.SlideShow_Video_Activity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                int n = 1;
                String strVideoName = editText.getText().toString();
                if (strVideoName.equals("")) {
                    Toast.makeText(SlideShow_Video_Activity.this, (int) R.string.please_enter_story_name_, 0).show();
                    editText.setFocusable(true);
                    editText.setSelected(true);
                    n = 0;
                } else if (new File(new File(FileUtils.APP_DIRECTORY.getAbsolutePath()), String.valueOf(strVideoName) + ".mp4").exists()) {
                    Toast.makeText(SlideShow_Video_Activity.this, (int) R.string.video_name_already_exist_, 0).show();
                    editText.setFocusable(true);
                    editText.setSelected(true);
                    n = 0;
                }
                if (n != 0) {


                    dialog.getWindow().setSoftInputMode(2);
                    ((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    dialog.dismiss();
                    SlideShow_Video_Activity.this.handler.removeCallbacks(SlideShow_Video_Activity.this.lockRunnable);


//                    Intent intent = new Intent(SlideShow_Video_Activity.this, CreateVideoService.class);
                    if (!strVideoName.endsWith(".mp4")) {
                        strVideoName = String.valueOf(strVideoName) + ".mp4";
                    }
                    Log.d("bbbbb.......", "Video Name:- " + strVideoName);
//                    intent.putExtra("VideoName", strVideoName);
//                    SlideShow_Video_Activity.this.startService(intent);
                    SlideShow_Video_Activity.this.id = 101;
//                    SlideShow_Video_Activity.this.loadProgress();


                    createVideo(strVideoName);


                }
            }
        });
        dialog.show();
    }


    private void createVideo(String strVideoName) {
        String[] inputCode;
        Log.d("bbbbb...**LA...", ".....*********************************************************************.......createVideo.................");
        System.currentTimeMillis();
        float toatalSecond = (this.application.getSecond() * SlideShow_Video_Activity.paths.size()) - 1.0f;
        do {
        } while (!ImageCreatorService.isImageComplate);
        File r0 = new File(FileUtils.TEMP_DIRECTORY, "video.txt");
        r0.delete();

        File audioFile = new File(this.application.getMusicData().track_data);

        Log.d("bbbbb...**", "Audio File Is:- " + audioFile.getAbsolutePath());
        Log.d("bbbbb...**LA...", ".....******************.......createVideo......*********************...........");
        for (int i = 0; i < SlideShow_Video_Activity.videoImages.size(); i++) {
            appendVideoLog(String.format("file '%s'", SlideShow_Video_Activity.videoImages.get(i)));
            Log.d("bbbbb...**LA...", ".....******************........this.application.videoImages.get(i) ::: " + SlideShow_Video_Activity.videoImages.get(i));
        }

        File output = new File(FileUtils.MAKE_VIDEO_DIRECTORY, strVideoName);

        try {
            output.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


        final String videoPath = output.getAbsolutePath();
        Log.e("TAG", "videoPath " + videoPath);


        if (this.application.getMusicData() == null) {

            String[] strVideoWidthHeight = getResources().getStringArray(R.array.video_height_width);
            inputCode = new String[]{"-y",
                    "-r", String.valueOf(30.0f / this.application.getSecond()),
                    "-f", "concat",
                    "-i", r0.getAbsolutePath(),


//                    "-s", strVideoWidthHeight[0],

                /*    "-strict", "experimental",*/

                    "-r", "30",


                    "-preset", "ultrafast",

                    "-pix_fmt", "yuv420p",


                    "-aspect",
                    "9:16",
                    videoPath};


        } else if (this.application.getFrame() != 0) {
            if (!FileUtils.frameFile.exists()) {
                Log.e("TAG", ".....******************........else.... ::: " + MyApplication.VIDEO_WIDTH + "*" + MyApplication.VIDEO_HEIGHT);
                try {
                    Bitmap bm = BitmapFactory.decodeResource(getResources(), this.application.getFrame());
                    if (!(bm.getWidth() == MyApplication.VIDEO_WIDTH && bm.getHeight() == MyApplication.VIDEO_HEIGHT)) {
                        bm = ScalingUtilities.scaleCenterCrop(bm, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
                    }
                    FileOutputStream outStream = new FileOutputStream(FileUtils.frameFile);
                    bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                    outStream.flush();
                    outStream.close();
                    bm.recycle();
                    System.gc();
                } catch (Exception e) {
                }
            }
            Log.e("TAG", ".....******************........FileUtils.frameFile.getAbsolutePath().... ::: " + FileUtils.frameFile.getAbsolutePath());
            String[] strVideoWidthHeight = getResources().getStringArray(R.array.video_height_width);



            inputCode = new String[]{"-y",
                    "-r", String.valueOf(30.0f / this.application.getSecond()),
                    "-f", "concat",
                    "-safe", "0",
                    "-i", r0.getAbsolutePath(),
                    "-i", FileUtils.frameFile.getAbsolutePath(),
                    "-i", audioFile.getAbsolutePath(),
                    "-filter_complex", "overlay= 0:0",
//                    "-strict", "experimental",
//                    "-s", strVideoWidthHeight[0],

                    "-r", String.valueOf(30.0f / this.application.getSecond()),
                    "-t", String.valueOf(toatalSecond),

                    "-preset", "ultrafast",
                    "-pix_fmt", "yuv420p",
                    "-ac", "2",
                    "-aspect",
                    "9:16",
                    videoPath
            };


            Log.e("TGA", "...inputCode:-...frame... " + Arrays.toString(inputCode));
        } else {
            Log.d("bbbbb...**", "Music:-.............. " + this.application.getMusicData());
            String[] strVideoWidthHeight = getResources().getStringArray(R.array.video_height_width);

            int pos = EPreferences.getInstance(getApplicationContext()).getInt(EPreferences.PREF_KEY_VIDEO_QUALITY, 2);





            inputCode = new String[]{"-y",
                    "-r", String.valueOf(30.0f / this.application.getSecond()),
                    "-f", "concat",
                    "-safe", "0",
                    "-i", r0.getAbsolutePath(),
                    "-i", audioFile.getAbsolutePath(),

//                  "-s", strVideoWidthHeight[0],
                    "-r", "30",
                    "-t", String.valueOf(toatalSecond),

                    "-preset", "ultrafast",
                    "-pix_fmt", "yuv420p",
                    "-ac", "2",
                    "-aspect",
                    "9:16",
                    videoPath};


            Log.e("TAG", "...inputCode:-..11.. " + Arrays.toString(inputCode));
        }


        new AsyncTaskExample(inputCode, videoPath).execute();


    }


    private class AsyncTaskExample extends AsyncTask<String, String, String> {
        String[] command;
        String absolutePath;


        AsyncTaskExample(String[] command, final String absolutePath) {
            this.command = command;
            this.absolutePath = absolutePath;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("TAG", "onPreExecute");
            p = new ProgressDialog(SlideShow_Video_Activity.this);
            p.setMessage("Please wait...It is downloading");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();


        }

        @Override
        protected String doInBackground(String... strings) {



            long executionId = FFmpeg.executeAsync(command, new ExecuteCallback() {
                @Override
                public void apply(final long executionId, final int returnCode) {
                    if (returnCode == RETURN_CODE_SUCCESS) {



                        sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(new File(absolutePath))));
                        loadProgress(absolutePath);
                        Log.e("TAG", "Success");



//                    Toast.makeText(getApplicationContext(), "Sucess", Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(SlideShow_Video_Activity.this, VideoPreviewActivity.class);
//                    i.putExtra("DATA", absolutePath);
//                    startActivity(i);

                    } else if (returnCode == RETURN_CODE_CANCEL) {
                        Log.e("TAG", "Async command execution cancelled by user.");
                    } else {
                        Log.e("TAG", String.format("Async command execution failed with returnCode=%d.", returnCode));
                    }
                }
            });


            return "";



        }

        @Override
        protected void onPostExecute(String bitmap) {
            super.onPostExecute(bitmap);
            p.dismiss();
            Log.e("TAG", "onPostExecute");


        }
    }


    public static void appendVideoLog(String text) {
        if (!FileUtils.TEMP_DIRECTORY.exists()) {
            FileUtils.TEMP_DIRECTORY.mkdirs();
        }
        File logFile = new File(FileUtils.TEMP_DIRECTORY, "video.txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append((CharSequence) text);
            buf.newLine();
            buf.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }


    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint("WrongConstant")
    public void loadProgress(String  videoPath) {

        Intent intent = new Intent(this, Video_Play_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("KEY", "FromVideoAlbum");
        intent.putExtra("android.intent.extra.TEXT", videoPath);
        startActivity(intent);
        finish();
    }

    public void setFrame(int data) {
        Log.d("bbbbb....SSVA....", ".................setFrame...............");
        this.frame = data;
        if (data == -1) {
            this.ivFrame.setImageDrawable(null);
        } else {
            this.ivFrame.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), data), MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, false));
        }
        this.application.setFrame(data);
    }

    public int getFrame() {
        return this.application.getFrame();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class DurationAdapter extends RecyclerView.Adapter<DurationAdapter.ViewHolder> {

        /* loaded from: classes.dex */
        public class ViewHolder extends RecyclerView.ViewHolder {
            CircularTextView circulartextView;

            public ViewHolder(View view) {
                super(view);
                this.circulartextView = (CircularTextView) view.findViewById(R.id.tvCircluar);
            }
        }

        private DurationAdapter() {
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public int getItemCount() {
            return SlideShow_Video_Activity.this.duration.length;
        }

        public void onBindViewHolder(ViewHolder holder, int pos) {
            final float dur = SlideShow_Video_Activity.this.duration[pos].floatValue();
            Log.d("bbbbb....ISA....", "........*************....**.....dur..............." + dur);
            holder.circulartextView.setStrokeWidth(2);
            holder.circulartextView.setSolidColor("#000000");
            holder.circulartextView.setText(String.valueOf(SlideShow_Video_Activity.this.duration[pos]));
            if (dur == SlideShow_Video_Activity.seconds) {
                holder.circulartextView.setStrokeColor("#ffffff");
            } else {
                holder.circulartextView.setStrokeColor("#000000");
            }
            holder.circulartextView.setOnClickListener(new View.OnClickListener() { // from class: com.photovideomaker.pictovideditor.videocutermerger.activities.SlideShow_Video_Activity.DurationAdapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View arg0) {
                    float unused = SlideShow_Video_Activity.seconds = dur;
                    Log.d("bbbbb....ISA....", "........*************....**.....seconds..............." + SlideShow_Video_Activity.seconds);
                    SlideShow_Video_Activity.this.application.setSecond(SlideShow_Video_Activity.seconds);
                    DurationAdapter.this.notifyDataSetChanged();
                    SlideShow_Video_Activity.this.lockRunnable.play();
                }
            });
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
            return new ViewHolder(SlideShow_Video_Activity.this.inflater.inflate(R.layout.duration_list_item, parent, false));
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        Log.d("bbbbb.....", ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,, :::: " + this.go);
        if (this.behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            this.behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else if (this.go) {
            super.onBackPressed();
        } else {
            onBackDialog();
        }
    }

    private void onBackDialog() {
        new AlertDialog.Builder(this).setTitle(R.string.app_name).setMessage("Are you sure ? \nYour video is not prepared yet!").setPositiveButton("Go Back", new C09429()).setNegativeButton("Stay here", (DialogInterface.OnClickListener) null).create().show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class C09429 implements DialogInterface.OnClickListener {
        C09429() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
            SlideShow_Video_Activity.this.go = true;
            SlideShow_Video_Activity.videoImages.clear();
            SlideShow_Video_Activity.this.application.setFrame(0);
            MyApplication.isBreak = true;
            SlideShow_Video_Activity.this.handler.removeCallbacks(SlideShow_Video_Activity.this.lockRunnable);
            SlideShow_Video_Activity.this.lockRunnable.stop();
            SlideShow_Video_Activity.this.onBackPressed();
        }
    }
}
