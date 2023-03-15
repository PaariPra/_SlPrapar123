package com.chettapps.videoeditor.videocutermerger.utility;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;


import com.chettapps.videoeditor.videocutermerger.utils.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/* loaded from: classes.dex */
public class Utility {
    public static ArrayList<String> reverseVideoList(ArrayList<String> file_names) {
        ArrayList<String> new_file_names = new ArrayList<>();
        int count = file_names.size() - 1;
        for (int i = 0; i < file_names.size(); i++) {
            new_file_names.add(file_names.get(count));
            count--;
        }
        return new_file_names;
    }

    public static void clearUpLogFiles() {
        String f_1 = FileUtils.APP_DIRECTORY.getAbsolutePath() + "ffmpeglicense.lic";
        String f_2 = FileUtils.APP_DIRECTORY.getAbsolutePath() + "vk.log";
        deleteFile(f_1);
        deleteFile(f_2);
    }

    public static boolean deleteFile(String name) {
        File f = new File(name);
        if (!f.exists()) {
            return false;
        }
        f.delete();
        return true;
    }

    public static Uri updateMediaDB(Context context, String fileName) {
        ContentValues cv = new ContentValues();
        File file = new File(fileName);
        long current = System.currentTimeMillis();
        long modDate = file.lastModified();
        cv.put("title", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date(current)));
        cv.put("_data", file.getAbsolutePath());
        cv.put("date_added", Integer.valueOf((int) (current / 1000)));
        cv.put("date_modified", Integer.valueOf((int) (modDate / 1000)));
        cv.put("duration", (Long) 1L);
        cv.put("mime_type", "video/*");
        Uri mVideoRecordUri = context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, cv);
        if (mVideoRecordUri == null) {
            return null;
        }
        context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", mVideoRecordUri));
        return mVideoRecordUri;
    }
}
