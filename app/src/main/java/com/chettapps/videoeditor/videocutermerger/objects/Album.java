package com.chettapps.videoeditor.videocutermerger.objects;

import java.util.ArrayList;

/* loaded from: classes.dex */
public class Album {
    private ArrayList<Image> arrImage;
    private String bucket;
    private String pathFirstImage;

    public Album(String bucket, String pathFirstImage, ArrayList<Image> arrImage) {
        this.bucket = bucket;
        this.pathFirstImage = pathFirstImage;
        this.arrImage = arrImage;
    }

    public void addImage(Image image) {
        this.arrImage.add(image);
    }

    public String getBucket() {
        return this.bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getPathFirstImage() {
        return this.pathFirstImage;
    }

    public void setPathFirstImage(String pathFirstImage) {
        this.pathFirstImage = pathFirstImage;
    }

    public ArrayList<Image> getArrImage() {
        return this.arrImage;
    }

    public void setArrImage(ArrayList<Image> arrImage) {
        this.arrImage = arrImage;
    }
}
