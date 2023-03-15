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
import android.util.Log;



import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class FileUtils {
    public static File getFileFromUri(final Context context, final Uri uri) throws Exception {

        String path = null;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) { // TODO: 2015. 11. 17. KITKAT

                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        path = Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes

                } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    path = getDataColumn(context, contentUri, null, null);
                } else if (isMediaDocument(uri)) { // MediaProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };
                    path = getDataColumn(context, contentUri, selection, selectionArgs);
                }  // MediaStore (and general)
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                path = getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                path = uri.getPath();
            }
            return new File(path);
        } else {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            return new File(cursor.getString(cursor.getColumnIndex("_data")));
        }
    }

    // Get the value of the data column for this Uri. This is useful for
    // MediaStore Uris, and other file-based ContentProviders.
    // @param context       The context.
    // @param uri           The Uri to query.
    // @param selection     (Optional) Filter used in the query.
    // @param selectionArgs (Optional) Selection arguments used in the query.
    // @return The value of the _data column, which is typically a file path.

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = MediaStore.Images.Media.DATA;
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    // @param uri The Uri to check.
    // @return Whether the Uri authority is ExternalStorageProvide
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    // @param uri The Uri to check.
    // @return Whether the Uri authority is DownloadsProvider.
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    // @param uri The Uri to check.
    // @return Whether the Uri authority is MediaProvider.
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    private static final int DEFAULT_BUFFER_SIZE = 4096;
    private static final int EOF = -1;
    static final String ffmpegFileName = "ffmpeg";
    public static final String hiddenDirectoryName = ".MyGalaryLock/";
    public static final String hiddenDirectoryNameImage = "Image/";
    public static final String hiddenDirectoryNameThumbImage = ".thumb/Image/";
    public static final String hiddenDirectoryNameThumbVideo = ".thumb/Video/";
    public static final String hiddenDirectoryNameVideo = "Video/";
    private static File[] mStorageList;
    public static File mSdCard = new File(Environment.getExternalStorageDirectory().getAbsolutePath());


    public static File APP_DIRECTORY = new File(mSdCard, "Pic2Video");
    public static final File TEMP_DIRECTORY = new File(APP_DIRECTORY, "temp");


    public static final File TEMP_DIRECTORY_AUDIO = new File(APP_DIRECTORY, ".temp_audio");
    public static final File TEMP_IMG_DIRECTORY = new File(APP_DIRECTORY, ".temp_image");
    public static final File TEMP_VID_DIRECTORY = new File(TEMP_DIRECTORY, ".temp_vid");
    public static final File EDIT_IMG_DIRECTORY = new File(APP_DIRECTORY, "EditImage");
    public static final File FRAME_IMG_DIRECTORY = new File(APP_DIRECTORY, ".Frame");
    public static final File frameFile = new File(FRAME_IMG_DIRECTORY, ".frame.png");
    public static String hiddenDirectoryNameThumb = ".MyGalaryLock/.thumbnail/";
    public static long mDeleteFileCount = 0;
    public static File mDownloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    public static final String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
    public static String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
    public static String unlockDirectoryNameImage = "GalaryLock/Image/";
    public static String unlockDirectoryNameVideo = "GalaryLock/Video/";
    public static final File CUT_VIDEO_DIRECTORY = new File(APP_DIRECTORY, "CutVideo");
    public static final File JOIN_VIDEO_DIRECTORY = new File(APP_DIRECTORY, "JoinVideo");
    public static final File MAKE_VIDEO_DIRECTORY = new File(APP_DIRECTORY, "MakeVideo");

    static {
        if (!TEMP_IMG_DIRECTORY.exists()) {
            TEMP_IMG_DIRECTORY.mkdirs();
        }
        if (!TEMP_DIRECTORY.exists()) {
            TEMP_DIRECTORY.mkdirs();
        }
        if (!TEMP_VID_DIRECTORY.exists()) {
            TEMP_VID_DIRECTORY.mkdirs();
        }
        if (!EDIT_IMG_DIRECTORY.exists()) {
            EDIT_IMG_DIRECTORY.mkdirs();
        }
        if (!FRAME_IMG_DIRECTORY.exists()) {
            FRAME_IMG_DIRECTORY.mkdirs();
        }
        if (!CUT_VIDEO_DIRECTORY.exists()) {
            CUT_VIDEO_DIRECTORY.mkdirs();
        }
        if (!JOIN_VIDEO_DIRECTORY.exists()) {
            JOIN_VIDEO_DIRECTORY.mkdirs();
        }
        if (!MAKE_VIDEO_DIRECTORY.exists()) {
            MAKE_VIDEO_DIRECTORY.mkdirs();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class C10701 extends Thread {
        private final File val$child;

        C10701(File file) {
            this.val$child = file;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            FileUtils.deleteFile(this.val$child);
        }
    }

    public static File getImageDirectory(String theme) {
        File imageDir = new File(TEMP_DIRECTORY, theme);
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }
        return imageDir;
    }

    public static File getImageDirectory(String theme, int iNo) {
        File imageDir = new File(getImageDirectory(theme), String.format("IMG_%03d", Integer.valueOf(iNo)));
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }
        return imageDir;
    }

    public static boolean deleteThemeDir(String theme) {
        return deleteFile(getImageDirectory(theme));
    }

    public FileUtils() {
        mDeleteFileCount = 0L;
    }

    private static File[] getStorge() {
        List<File> storage = new ArrayList<>();
        if (rawExternalStorage != null) {
            storage.add(new File(rawExternalStorage));
        } else if (mSdCard != null) {
            storage.add(mSdCard);
        }
        if (rawSecondaryStoragesStr != null) {
            storage.add(new File(rawSecondaryStoragesStr));
        }
        mStorageList = new File[storage.size()];
        for (int i = 0; i < storage.size(); i++) {
            mStorageList[i] = storage.get(i);
        }
        return mStorageList;
    }

    public static File[] getStorages() {
        return mStorageList != null ? mStorageList : getStorge();
    }

    public static File getHiddenAppDirectory(File sdCard) {
        File file = new File(sdCard, hiddenDirectoryName);
        if (file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static void deleteTempDir() {
        File[] listFiles;
        for (File child : TEMP_DIRECTORY.listFiles()) {
            new C10701(child).start();
        }
    }

    public static boolean deleteFile(File mFile) {
        boolean idDelete = false;
        if (mFile == null) {
            return false;
        }
        if (mFile.exists()) {
            if (mFile.isDirectory()) {
                File[] children = mFile.listFiles();
                if (children != null && children.length > 0) {
                    for (File child : children) {
                        mDeleteFileCount += child.length();
                        deleteFile(child);
                    }
                }
                mDeleteFileCount += mFile.length();
                idDelete = mFile.delete();
            } else {
                mDeleteFileCount += mFile.length();
                idDelete = mFile.delete();
            }
        }
        return idDelete;
    }

    @SuppressLint({"DefaultLocale"})
    public static String getDuration(long duration) {
        if (duration < 1000) {
            return String.format("%02d:%02d", 0, 0);
        }
        long n = duration / 1000;
        long n2 = n / 3600;
        long n4 = n - ((3600 * n2) + (60 * ((n - (3600 * n2)) / 60)));
        if (n2 == 0) {
            return String.format("%02d:%02d", 0L, Long.valueOf(n4));
        }
        return String.format("%02d:%02d:%02d", Long.valueOf(n2), 0L, Long.valueOf(n4));
    }

/*
    */
/* JADX INFO: Access modifiers changed from: package-private *//*

    public static boolean copyBinaryFromAssetsToData(Context context, String fileNameFromAssets, String outputFileName) {
        boolean z = false;
        File filesDirectory = getFilesDirectory(context);
        try {
            InputStream is = context.getAssets().open(fileNameFromAssets);
            OutputStream os = new FileOutputStream(new File(filesDirectory, outputFileName));
            byte[] buffer = new byte[4096];
            while (true) {
                int n = is.read(buffer);
                if (-1 == n) {
                    Util.close(os);
                    Util.close(is);
                    z = true;
                    return true;
                }
                os.write(buffer, 0, n);
            }
        } catch (IOException e) {
            Log.e("bbbbb......", "bbbbb........issue in coping binary from assets to data. ", e);
            return z;
        }
    }
*/

    static File getFilesDirectory(Context context) {
        return context.getFilesDir();
    }

    public static String getFFmpeg(Context context) {
        return String.valueOf(getFilesDirectory(context).getAbsolutePath()) + File.separator + ffmpegFileName;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getFFmpeg(Context context, Map<String, String> environmentVars) {
        String ffmpegCommand = "";
        if (environmentVars != null) {
            for (Map.Entry<String, String> var : environmentVars.entrySet()) {
                ffmpegCommand = String.valueOf(ffmpegCommand) + var.getKey() + "=" + var.getValue() + " ";
            }
        }
        return String.valueOf(ffmpegCommand) + getFFmpeg(context);
    }

//    /* JADX INFO: Access modifiers changed from: package-private */
//    public static String SHA1(String file) {
//        try {
//            InputStream is2 = new BufferedInputStream(new FileInputStream(file));
//            String SHA1 = SHA1(is2);
//            Util.close(is2);
//            return SHA1;
//        } catch (IOException e3) {
//            Log.e("bbbbb......", "bbbbb........", e3);
//            Util.close((InputStream) null);
//            return null;
//        }
//    }

//    static String SHA1(InputStream is) {
//        try {
//            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
//            byte[] buffer = new byte[4096];
//            while (true) {
//                int read = is.read(buffer);
//                if (read == -1) {
//                    Formatter formatter = new Formatter();
//                    int length = messageDigest.digest().length;
//                    String str = formatter.toString();
//                    return str;
//                }
//                messageDigest.update(buffer, 0, read);
//            }
//        } catch (IOException e2) {
//            Log.e("bbbbb......", "" + e2);
//            return null;
//        } catch (NoSuchAlgorithmException e) {
//            Log.e("bbbbb......", " " + e);
//            return null;
//        } finally {
//            Util.close(is);
//        }
//    }

    public static void appendLog(File parent, String text) {
        if (!parent.exists()) {
            try {
                parent.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(parent, true));
            buf.append((CharSequence) text);
            buf.newLine();
            buf.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
}
