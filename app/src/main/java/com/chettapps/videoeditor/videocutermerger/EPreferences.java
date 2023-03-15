package com.chettapps.videoeditor.videocutermerger;

import android.content.Context;
import android.content.SharedPreferences;

/* loaded from: classes.dex */
public class EPreferences {
    public static final String DURATION_VALUE = "duration_value";
    public static final String IS_TITLE = "false";
    public static final String KEY_FIRST_RUN = "First_Run";
    private static final int MODE_PRIVATE = 0;
    public static final String PREF_AUDIO_PATH = "pref_audio_path";
    public static final String PREF_CB_MUSIC_FILE_POSITION = "pref_cb_music_file_position";
    public static final String PREF_KEY_ANIMATION_INDEX = "pref_key_animation_index";
    public static final String PREF_KEY_FILTER_INDEX = "pref_key_filter_index";
    public static final String PREF_KEY_IS_FIRST_TIME = "is_first_time";
    public static final String PREF_KEY_NOTIFICATION_FLESH = "notification_flesh";
    public static final String PREF_KEY_NOTIFICATION_RING = "notification_ring";
    public static final String PREF_KEY_NOTIFICATION_VIBRATE = "notification_vibrate";
    public static final String PREF_KEY_SAVEING_CAM_IMG = "pref_key_saveing_cam_img";
    public static final String PREF_KEY_VIDEONAME = "videoname";
    public static final String PREF_KEY_VIDEO_FRAME_PATH = "pref_key_video_frame_path";
    public static final String PREF_KEY_VIDEO_QUALITY = "pref_key_video_quality";
    public static final String PREF_KEY_WANT_DAILY_ALERT = "pref_key_daily_alert";
    private static final String PREF_NAME = "slideshow_pref";
    public static final String TITLE_INDEX = "title_index";
    public static final String TITLE_STRING = "title_string";
    private SharedPreferences m_csPref;

    public static EPreferences getInstance(Context context) {
        return new EPreferences(context, PREF_NAME, 0);
    }

    private EPreferences(Context context, String pref_name, int mode) {
        this.m_csPref = context.getSharedPreferences(pref_name, mode);
    }

    public String getString(String key, String defaultValue) {
        return this.m_csPref.getString(key, defaultValue);
    }

    public void setString(String key, String value) {
        SharedPreferences.Editor edit = this.m_csPref.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public int getInt(String key, int defaultValue) {
        return this.m_csPref.getInt(key, defaultValue);
    }

    public void clear(String key) {
        this.m_csPref.edit().remove(key).commit();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return this.m_csPref.getBoolean(key, defaultValue);
    }

    public int putBoolean(String key, boolean defaultValue) {
        SharedPreferences.Editor edit = this.m_csPref.edit();
        edit.putBoolean(key, defaultValue);
        edit.commit();
        return 0;
    }

    public int putInt(String key, int defaultValue) {
        SharedPreferences.Editor edit = this.m_csPref.edit();
        edit.putInt(key, defaultValue);
        edit.commit();
        return 0;
    }

    public int putString(String key, String defaultValue) {
        SharedPreferences.Editor edit = this.m_csPref.edit();
        edit.putString(key, defaultValue);
        edit.commit();
        return 0;
    }
}
