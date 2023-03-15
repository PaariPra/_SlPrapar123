package com.chettapps.videoeditor.videocutermerger.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


/* loaded from: classes.dex */
public class FileUtils2 {
    @SuppressLint({"NewApi"})
    public static String getPath(Context context, Uri uri) {
        boolean isKitKat;
        if (Build.VERSION.SDK_INT >= 19) {
            isKitKat = true;
        } else {
            isKitKat = false;
        }
        if (!isKitKat || !DocumentsContract.isDocumentUri(context, uri)) {


            if ("content".equalsIgnoreCase(uri.getScheme())) {
                if (isGooglePhotosUri(uri)) {
                    return uri.getLastPathSegment();
                }
                return getDataColumn(context, uri, null, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            } else {
                return null;
            }





        } else if (isExternalStorageDocument(uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            String[] split = docId.split(":");
            if ("primary".equalsIgnoreCase(split[0])) {
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            }
            return null;
        } else if (isDownloadsDocument(uri)) {
            String id = DocumentsContract.getDocumentId(uri);
            Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id).longValue());
            return getDataColumn(context, contentUri, null, null);
        } else if (!isMediaDocument(uri)) {
            return null;
        } else {
            String docId2 = DocumentsContract.getDocumentId(uri);
            String[] split2 = docId2.split(":");
            String type = split2[0];
            Uri contentUri2 = null;
            if ("image".equals(type)) {
                contentUri2 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
                contentUri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
                contentUri2 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }
            String[] selectionArgs = {split2[1]};
            return getDataColumn(context, contentUri2, "_id=?", selectionArgs);
        }
    }

    private static String getDataColumn(@NonNull Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String string;
        String[] projection = {"_data"};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor == null || !cursor.moveToFirst()) {
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
            int column_index = cursor.getColumnIndexOrThrow("_data");
             string = cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return  string;
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private static boolean isExternalStorageDocument(@NonNull Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(@NonNull Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(@NonNull Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
