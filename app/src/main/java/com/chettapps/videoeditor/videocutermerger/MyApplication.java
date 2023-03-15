package com.chettapps.videoeditor.videocutermerger;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;

import android.text.TextUtils;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.chettapps.videoeditor.R;
import com.chettapps.videoeditor.videocutermerger.activities.SlideShow_Video_Activity;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;


/* loaded from: classes.dex */
public class MyApplication extends MultiDexApplication {
    static SharedPreferences.Editor editor;
    private static MyApplication instance;
    static SharedPreferences sharedpreferences;
    private ArrayList<String> allFolder;
    private MusicData musicData;
    private OnProgressReceiver onProgressReceiver;


    public static int VIDEO_HEIGHT = 480;
    public static int VIDEO_WIDTH = 720;




    public static boolean isBreak = false;
    public static String autostart_app_name = "";
    int frame = 0;
    int filter = 0;
    public int min_pos = 2147483647;
    public int posForAddMusicDialog = 0;
    private float second = 2.0f;
    public THEMES selectedTheme = THEMES.Shine;
    final String MY_PREFS_NAME = "appnames";


    public void setFrame(int data) {
        this.frame = data;
    }

    public int getFrame() {
        return this.frame;
    }

    public float getSecond() {
        Log.d("bbbbb....MyApp....", "...........###......getSecond...............");
        return this.second;
    }

    public void setSecond(float second) {
        this.second = second;
        Log.d("bbbbb....MyApp....", "...........###......setSecond...............");
    }

    public static MyApplication getInstance() {
        return instance;
    }

    @Override // android.app.Application
    public void onCreate() {
        Log.d("bbbbb....MyApp....", "...........###......onCreate...............");
        super.onCreate();



        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        instance = this;
        init();
        sharedpreferences = getSharedPreferences("appnames", 0);
        editor = sharedpreferences.edit();
    }

    public static void setcheck(boolean check) {
        editor.putBoolean("check", check).commit();
    }

    public static boolean getcheck() {
        return sharedpreferences.getBoolean("check", false);
    }

    private void init() {
        Log.d("bbbbb....MyApp....", "...........###......init...............");


            setVideoHeightWidth();

    }


    public Uri getUriFromPath(String path) {
        return Uri.fromFile(new File(path));
    }

    public String getBucketNameFromURI(Uri uri) {
        String string = null;
        if (uri.toString().startsWith("/")) {
            return uri.toString();
        }
        Cursor query = getContentResolver().query(uri, new String[]{"bucket_display_name"}, null, null, null);
        if (query.moveToFirst()) {
            string = query.getString(query.getColumnIndexOrThrow("_data"));
        }
        query.close();
        return string;
    }

    public void clearAllSelection() {
        Log.d("bbbbb....MyApp....", "...........###......clearAllSelection...............");
        SlideShow_Video_Activity.videoImages.clear();
        System.gc();
    }

    public void setCurrentTheme(String currentTheme) {
        Log.d("bbbbb....MyApp....", "...........###......setCurrentTheme..............." + currentTheme);
        SharedPreferences.Editor editor2 = getSharedPreferences("theme", 0).edit();
        editor2.putString("current_theme", currentTheme);
        editor2.commit();
    }

    public String getCurrentTheme() {
        Log.d("bbbbb....MyApp....", "...........###......getCurrentTheme..............." + getSharedPreferences("theme", 0).getString("current_theme", THEMES.Shine.toString()));
        return getSharedPreferences("theme", 0).getString("current_theme", THEMES.Shine.toString());
    }

    public void setOnProgressReceiver(OnProgressReceiver onProgressReceiver) {
        Log.d("bbbbb....MyApp....", "...........###......setOnProgressReceiver...............");
        this.onProgressReceiver = onProgressReceiver;
    }

    public OnProgressReceiver getOnProgressReceiver() {
        Log.d("bbbbb....MyApp....", "...........###......OnProgressReceiver...............");
        return this.onProgressReceiver;
    }

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        Log.d("bbbbb....MyApp....", "...........###......isMyServiceRunning...............");
        for (ActivityManager.RunningServiceInfo service : ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).
                getRunningServices(0x7fffffff)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void setVideoHeightWidth() {
        Log.d("bbbbb....MyApp....", "...........###......setVideoHeightWidth...............");
        String strTemp = getResources().getStringArray(R.array.video_height_width)[EPreferences.getInstance(getApplicationContext()).getInt(EPreferences.PREF_KEY_VIDEO_QUALITY, 2)];
        Log.d("bbbbb..", "MyApplication VideoQuality value is:- " + strTemp);
        VIDEO_WIDTH = Integer.parseInt(strTemp.split(Pattern.quote("*"))[0]);
        VIDEO_HEIGHT = Integer.parseInt(strTemp.split(Pattern.quote("*"))[1]);
    }

    public void setMusicData(MusicData musicData) {
        SlideShow_Video_Activity.isFromSdCardAudio = false;
        this.musicData = musicData;
        Log.d("bbbbb....MyApp....", "...........###......setMusicData..............." + musicData);
    }

    public MusicData getMusicData() {
        Log.d("bbbbb....MyApp....", "...........###......getMusicData..............." + this.musicData);
        return this.musicData;
    }

    public ArrayList<MusicData> getMusicFiles() {
        Log.d("bbbbb....MyApp....", "...........###......getMusicFiles...............");
        ArrayList<MusicData> mMusicDatas = new ArrayList<>();
        Cursor mCursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{"_id","title", "_data", "_display_name", "duration"}, "is_music != 0", null, "title ASC");
        int trackId = mCursor.getColumnIndex("_id");
        int trackTitle = mCursor.getColumnIndex("title");
        int trackDisplayName = mCursor.getColumnIndex("_display_name");
        int trackData = mCursor.getColumnIndex("_data");
        int trackDuration = mCursor.getColumnIndex("duration");
        while (mCursor.moveToNext()) {
            String path = mCursor.getString(trackData);
            if (isAudioFile(path)) {
                MusicData musicData = new MusicData();
                musicData.track_Id = mCursor.getLong(trackId);
                musicData.track_Title = mCursor.getString(trackTitle);
                musicData.track_data = path;
                musicData.track_duration = mCursor.getLong(trackDuration);
                musicData.track_displayName = mCursor.getString(trackDisplayName);
                mMusicDatas.add(musicData);
            }
        }
        return mMusicDatas;
    }

    private boolean isAudioFile(String path) {
        Log.d("bbbbb....MyApp....", "...........###......isAudioFile..............." + path);
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        return path.endsWith(".mp3");
    }
}
