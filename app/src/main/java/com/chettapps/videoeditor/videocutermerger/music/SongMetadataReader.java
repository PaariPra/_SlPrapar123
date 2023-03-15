package com.chettapps.videoeditor.videocutermerger.music;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.HashMap;
import java.util.Iterator;



/* loaded from: classes.dex */
public class SongMetadataReader {
    public Activity mActivity;
    public int mDuration;
    public String mFilename;
    public String mTitle;
    public Uri GENRES_URI = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;
    public String mAlbum = "";
    public String mArtist = "";
    public String mGenre = "";
    public int mYear = -1;

    public SongMetadataReader(Activity activity, String filename) {
        this.mActivity = null;
        this.mFilename = "";
        this.mTitle = "";
        this.mActivity = activity;
        this.mFilename = filename;
        this.mTitle = getBasename(filename);
        try {
            ReadMetadata();
        } catch (Exception e) {
        }
    }

    private void ReadMetadata() {
        HashMap<String, String> genreIdMap = new HashMap<>();
        Cursor c = this.mActivity.getContentResolver().query(this.GENRES_URI, new String[]{"_id", "name"}, null, null, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            genreIdMap.put(c.getString(0), c.getString(1));
            c.moveToNext();
        }
        this.mGenre = "";
        Iterator<String> it = genreIdMap.keySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            String genreId = it.next();
            if (this.mActivity.getContentResolver().query(makeGenreUri(genreId), new String[]{"_data"}, "_data LIKE \"" + this.mFilename + "\"", null, null).getCount() != 0) {
                this.mGenre = genreIdMap.get(genreId);
                break;
            }
        }
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(this.mFilename);
        Cursor c2 = this.mActivity.getContentResolver().query(uri, new String[]{"_id","title", "artist", "album", "year", "duration", "_data"}, "_data LIKE \"" + this.mFilename + "\"", null, null);
        if (c2.getCount() == 0) {
            this.mTitle = getBasename(this.mFilename);
            this.mArtist = "";
            this.mAlbum = "";
            this.mYear = -1;
            return;
        }
        c2.moveToFirst();
        this.mTitle = getStringFromColumn(c2, "title");
        if (this.mTitle == null || this.mTitle.length() == 0) {
            this.mTitle = getBasename(this.mFilename);
        }
        this.mArtist = getStringFromColumn(c2, "artist");
        this.mAlbum = getStringFromColumn(c2, "album");
        this.mYear = getIntegerFromColumn(c2, "year");
        this.mDuration = getIntegerFromColumn(c2, "duration");
    }

    private Uri makeGenreUri(String genreId) {
        return Uri.parse(this.GENRES_URI.toString() + "/" + genreId + "/members");
    }

    private String getStringFromColumn(Cursor c, String columnName) {
        String value = c.getString(c.getColumnIndexOrThrow(columnName));
        if (value == null || value.length() <= 0) {
            return null;
        }
        return value;
    }

    private int getIntegerFromColumn(Cursor c, String columnName) {
        Integer value = Integer.valueOf(c.getInt(c.getColumnIndexOrThrow(columnName)));
        if (value != null) {
            return value.intValue();
        }
        return -1;
    }

    private String getBasename(String filename) {
        return filename.substring(filename.lastIndexOf(47) + 1, filename.lastIndexOf(46));
    }
}
