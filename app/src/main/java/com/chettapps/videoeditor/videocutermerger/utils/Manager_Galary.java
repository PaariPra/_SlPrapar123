package com.chettapps.videoeditor.videocutermerger.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;

import com.chettapps.videoeditor.videocutermerger.objects.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/* loaded from: classes.dex */
public class Manager_Galary {
    private static final String TAG = "ManagerGalary";
    private Context mContext;
    private ArrayList<String> imagePaths = new ArrayList<>();
    private ArrayList<Image> arrImage = new ArrayList<>();
    private int count = 0;

    public Manager_Galary(Context context) {
        this.mContext = context;
        queryGalary();
    }

    public ArrayList<String> getImagePaths() {
        return this.imagePaths;
    }

    public ArrayList<Image> getArrImage() {
        return this.arrImage;
    }

    public void setArrImage(ArrayList<Image> arrImage) {
        this.arrImage = arrImage;
    }

    public void queryGalary() {
        this.imagePaths.clear();
        String[] projection = {"_id", "bucket_display_name", "datetaken", "_data", "_size", "width", "date_added", "height"};
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cur = this.mContext.getContentResolver().query(images, projection, null, null, null);
        if (cur.moveToFirst()) {
            int bucketColumn = cur.getColumnIndex("bucket_display_name");
            int dateColumn = cur.getColumnIndex("datetaken");
            int pathColum = cur.getColumnIndex("_data");
            int sizeColum = cur.getColumnIndex("_size");
            int widthColum = cur.getColumnIndex("width");
            int heightColum = cur.getColumnIndex("height");
            do {
                String bucket = cur.getString(bucketColumn);
                String date = cur.getString(dateColumn);
                String width = cur.getString(widthColum);
                String height = cur.getString(heightColum);
                String path = cur.getString(pathColum);
                String size = cur.getString(sizeColum);
                if (size != null) {
                    int sizeImage = Integer.parseInt(size);
                    int sizeImage2 = sizeImage / 1024;
                    String mydate = convertDate(date);
                    this.count++;
                    if (new File(path).exists()) {
                        this.imagePaths.add(path);
                        this.arrImage.add(new Image(mydate, bucket, path, width, height, sizeImage2 + ""));
                    }
                } else {
                    return;
                }
            } while (cur.moveToNext());
            Log.d("bbbb........", "size image : " + this.arrImage.size());
        }
    }

    private String convertDate(String myDate) {
        try {
            long millisecond = Long.parseLong(myDate);
            String dateString = DateFormat.format("dd/MM/yyyy", new Date(millisecond)).toString();
            return dateString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
