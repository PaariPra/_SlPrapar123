package com.chettapps.videoeditor.videocutermerger.activities;

import android.net.Uri;

public class ModelVideo {
    long id;
    Uri data;
    String title;
    int duration_formatted;;

    public ModelVideo(long id, Uri data, String title, int duration_formatted) {
        this.id = id;
        this.data = data;
        this.title = title;
        this.duration_formatted = duration_formatted;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Uri getData() {
        return data;
    }

    public void setData(Uri data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration_formatted() {
        return duration_formatted;
    }

    public void setDuration_formatted(int duration_formatted) {
        this.duration_formatted = duration_formatted;
    }
}
