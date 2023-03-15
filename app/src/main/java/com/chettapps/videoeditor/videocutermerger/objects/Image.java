package com.chettapps.videoeditor.videocutermerger.objects;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class Image implements Parcelable {
    public static final Creator<Image> CREATOR = new Creator<Image>() { // from class: com.chettapps.videoeditor.videocutermerger.objects.Image.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
    private String bucket;
    private String date;
    private String height;
    private Bitmap imgEdited;
    private boolean isClicked;
    private String path;
    private String size;
    private String width;

    public boolean isClicked() {
        return this.isClicked;
    }

    public void setClicked(boolean clicked) {
        this.isClicked = clicked;
    }

    public Image(String date, String bucket, String path, String width, String height, String size) {
        this.date = date;
        this.bucket = bucket;
        this.path = path;
        this.width = width;
        this.height = height;
        this.size = size;
    }

    public Image() {
    }

    protected Image(Parcel in) {
        this.date = in.readString();
        this.bucket = in.readString();
        this.path = in.readString();
    }

    public Bitmap getImgEdited() {
        return this.imgEdited;
    }

    public void setImgEdited(Bitmap imgEdited) {
        this.imgEdited = imgEdited;
    }

    public String getWidth() {
        return this.width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return this.height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBucket() {
        return this.bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeString(this.bucket);
        dest.writeString(this.path);
    }
}
