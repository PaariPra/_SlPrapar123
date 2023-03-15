package com.chettapps.videoeditor.videocutermerger;


import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import com.chettapps.videoeditor.R;
import com.google.android.exoplayer2.util.MimeTypes;
import com.chettapps.videoeditor.videocutermerger.activities.SlideShow_Video_Activity;
import com.chettapps.videoeditor.videocutermerger.activities.Video_Play_Activity;
import com.chettapps.videoeditor.videocutermerger.utils.FileUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CreateVideoService extends IntentService {
    public static final int NOTIFICATION_ID = 1001;
    MyApplication application;
    private File audioFile;
    private File audioIp;
    int last;
    private String strVideoName;
    String timeRe;
    private float toatalSecond;

    public CreateVideoService() {
        this(CreateVideoService.class.getName());
    }

    public CreateVideoService(String name) {
        super(name);
        this.timeRe = "\\btime=\\b\\d\\d:\\d\\d:\\d\\d.\\d\\d";
        this.last = 0;
    }

    @Override // android.app.IntentService
    protected void onHandleIntent(Intent intent) {
        this.application = MyApplication.getInstance();
        this.strVideoName = intent.getStringExtra("VideoName");
        createVideo();
    }

    private void createVideo() {
        String[] inputCode;
        Log.d("bbbbb...**LA...", ".....*********************************************************************.......createVideo.................");
        System.currentTimeMillis();
        this.toatalSecond = (this.application.getSecond() * SlideShow_Video_Activity.paths.size()) - 1.0f;
        do {
        } while (!ImageCreatorService.isImageComplate);
        File r0 = new File(FileUtils.TEMP_DIRECTORY, "video.txt");
        r0.delete();
        this.audioFile = new File(this.application.getMusicData().track_data);
        Log.d("bbbbb...**", "Audio File Is:- " + this.audioFile.getAbsolutePath());
        Log.d("bbbbb...**LA...", ".....******************.......createVideo......*********************...........");
        for (int i = 0; i < SlideShow_Video_Activity.videoImages.size(); i++) {
            appendVideoLog(String.format("file '%s'", SlideShow_Video_Activity.videoImages.get(i)));
            Log.d("bbbbb...**LA...", ".....******************........this.application.videoImages.get(i) ::: " + SlideShow_Video_Activity.videoImages.get(i));
        }
        final String videoPath = new File(FileUtils.MAKE_VIDEO_DIRECTORY, this.strVideoName).getAbsolutePath();
        Log.d("bbbbb...**LA...", "videoPath Is:- ::: " + videoPath);
        Log.d("bbbbb...**", "Music Data Path Is:- " + this.application.getMusicData().track_data);
        Log.d("bbbbb...**", "Music :- " + this.application.getMusicData());
        if (this.application.getMusicData() == null) {
            Log.d("bbbbb...**LA...", ".....******************........if.... ::: ");


            inputCode = new String[]{FileUtils.getFFmpeg(this),
                    "-r", String.valueOf(30.0f / this.application.getSecond()),
                    "-f", "concat",
                    "-i", r0.getAbsolutePath(), "-r", "30", "-c:v", "libx264", "-preset", "ultrafast", "-pix_fmt", "yuv420p", videoPath};



        } else if (this.application.getFrame() != 0) {
            if (!FileUtils.frameFile.exists()) {
                Log.d("bbbbb...**LA...", ".....******************........else.... ::: " + MyApplication.VIDEO_WIDTH + "*" + MyApplication.VIDEO_HEIGHT);
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
            Log.e("bbbbb...**LA...", ".....******************........FileUtils.frameFile.getAbsolutePath().... ::: " + FileUtils.frameFile.getAbsolutePath());
            inputCode = new String[]{FileUtils.getFFmpeg(this),
                    "-r", String.valueOf(30.0f / this.application.getSecond()),
                    "-f", "concat",
                    "-safe", "0",
                    "-i", r0.getAbsolutePath(),
                    "-i", FileUtils.frameFile.getAbsolutePath(), "-i", this.audioFile.getAbsolutePath(),
                    "-filter_complex", "overlay= 0:0",

                    "-strict", "experimental",


                    "-r", String.valueOf(30.0f / this.application.getSecond()), "-t", String.valueOf(this.toatalSecond), "-c:v", "libx264", "-preset", "ultrafast", "-pix_fmt", "yuv420p", "-ac", "2", videoPath};
            Log.d("bbbbb...**", "...inputCode:-...frame... " + Arrays.toString(inputCode));
        } else {
            Log.d("bbbbb...**", "Music:-.............. " + this.application.getMusicData());



            String[] strVideoWidthHeight = getResources().getStringArray(R.array.video_height_width);
            int pos = EPreferences.getInstance(getApplicationContext()).getInt(EPreferences.PREF_KEY_VIDEO_QUALITY, 2);
            Log.d("bbbbb...**", "pos:-.... " + pos);
            Log.d("bbbbb...**", "Music:-.... " + this.audioFile.getAbsolutePath());


            inputCode = new String[]{FileUtils.getFFmpeg(this), "-r", String.valueOf(30.0f / this.application.getSecond()),
                    "-f", "concat", "-safe", "0", "-i", r0.getAbsolutePath(), "-i", this.audioFile.getAbsolutePath(),
                    "-strict", "experimental",

                    "-s", strVideoWidthHeight[pos],
                    "-r", "30", "-t", String.valueOf(this.toatalSecond), "-c:v", "libx264", "-preset", "ultrafast", "-pix_fmt", "yuv420p", "-ac", "2", videoPath};


            Log.d("bbbbb...**", "...inputCode:-..11.. " + Arrays.toString(inputCode));
        }
        System.gc();
        Process process = null;
   /*     try {
            process = Runtime.getRuntime().exec(inputCode);
            while (!Util.isProcessCompleted(process)) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String line = bufferedReader.readLine();
                if (line != null) {
                    Log.e("process", line);
                    int durationToprogtess = ((int) ((75.0f * durationToprogtess(line)) / 100.0f)) + 25;
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        } finally {
            Util.destroyProcess(process);
        }

*/



        try {
            long fileSize = new File(videoPath).length();
            String artist = String.valueOf(getResources().getText(R.string.artist_name));
            ContentValues values = new ContentValues();
            values.put("_data", videoPath);
            values.put("_size", Long.valueOf(fileSize));
            values.put("mime_type", MimeTypes.VIDEO_MP4);
            values.put("artist", artist);
            values.put("duration", Float.valueOf(this.toatalSecond * 1000.0f));
            getContentResolver().insert(MediaStore.Audio.Media.getContentUriForPath(videoPath), values);
        } catch (Exception e3) {
            Log.e("bbbbb...CreateVSer", "Err msg  " + e3.getMessage());
        }
        try {
            sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(new File(videoPath))));
        } catch (Exception e32) {
            e32.printStackTrace();
        }
        this.application.clearAllSelection();




        buildNotification(videoPath);
        Log.e("bbbbb...CreateVSer", ".....str ::::: " + videoPath);
        new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.photovideomaker.pictovideditor.videocutermerger.CreateVideoService.1
            @Override // java.lang.Runnable
            public void run() {
                OnProgressReceiver receiver = CreateVideoService.this.application.getOnProgressReceiver();
                Log.e("bbbbb...CreateVSer", ".......receiver... ::::: " + receiver);
                if (receiver != null) {
                    receiver.onVideoProgressFrameUpdate(100.0f);
                    receiver.onProgressFinish(videoPath);
                }
            }
        });
        FileUtils.deleteTempDir();
        this.application.setFrame(-1);




        stopSelf();
    }

    private void buildNotification(String videoPath) {
        Log.e("bbbbb...CreateVSer", ".......buildNotification.11111111111111111................... ::::: " + videoPath);
        Intent notificationIntent = new Intent(this, Video_Play_Activity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationIntent.putExtra("android.intent.extra.TEXT", videoPath);
        notificationIntent.putExtra("KEY", "Notification");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        Resources res = getResources();
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentIntent(contentIntent).setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_launcher_background)).
                setWhen(System.currentTimeMillis()).setAutoCancel(true).setContentTitle(getResources().getString(R.string.app_name)).setContentText(getString(R.string.video_created)).setDefaults(1).setAutoCancel(true);
        Notification n = builder.build();
        n.defaults |= -1;
    }

    private int durationToprogtess(String input) {
        int progress = 0;
        Matcher matcher = Pattern.compile(this.timeRe).matcher(input);
        int HOUR = 60 * 60;
        if (TextUtils.isEmpty(input) || !input.contains("time=")) {
            return this.last;
        }
        while (matcher.find()) {
            String time = matcher.group();
            String[] splitTime = time.substring(time.lastIndexOf(61) + 1).split(":");
            float hour = (Float.valueOf(splitTime[0]).floatValue() * HOUR) + (Float.valueOf(splitTime[1]).floatValue() * 60) + Float.valueOf(splitTime[2]).floatValue();
            progress = (int) ((100.0f * hour) / this.toatalSecond);
            updateInMili(hour);
        }
        this.last = progress;
        return progress;
    }

    private void updateInMili(float time) {
        final double progress = (time * 100.0d) / this.toatalSecond;
        new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.photovideomaker.pictovideditor.videocutermerger.CreateVideoService.2
            @Override // java.lang.Runnable
            public void run() {
                OnProgressReceiver receiver = CreateVideoService.this.application.getOnProgressReceiver();
                if (receiver != null) {
                    receiver.onVideoProgressFrameUpdate((float) progress);
                }
            }
        });
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



}
