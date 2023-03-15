package com.chettapps.videoeditor.videocutermerger.activities;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chettapps.videoeditor.R;
import com.chettapps.videoeditor.videocutermerger.MusicData;
import com.chettapps.videoeditor.videocutermerger.MyApplication;
import com.chettapps.videoeditor.videocutermerger.adapters.Adapter_SongList;
import com.chettapps.videoeditor.videocutermerger.utils.FileUtils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;


public class PredefineMusicActivtiy extends AppCompatActivity {
    private MyApplication application;
    Button btnFromStorage;
    GridView gvSongList;
    MediaPlayer mediaPlayer;
    String path;
    Adapter_SongList songListAdapter;
    TextView tv_Done;
    private final int REQUEST_PICK_AUDIO = 101;
    final int[] resID = {R.raw._1, R.raw._2, R.raw._3, R.raw._4, R.raw._5};
    ArrayList<String> songName = new ArrayList<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class C09321 implements View.OnClickListener {
        C09321() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (PredefineMusicActivtiy.this.application.getMusicFiles().size() > 0) {
                PredefineMusicActivtiy.this.startActivityForResult(new Intent(PredefineMusicActivtiy.this, SongEditActivity.class), 101);
                PredefineMusicActivtiy.this.finish();
                PredefineMusicActivtiy.this.stopPlaying(PredefineMusicActivtiy.this.mediaPlayer);
                return;
            }
            Toast.makeText(PredefineMusicActivtiy.this.getApplicationContext(), "No Music found in device\nPlease add music in sdCard", 1).show();
        }
    }

    /* loaded from: classes.dex */
    public class ChangeMusic extends Thread {
        public ChangeMusic() {
        }

        /* loaded from: classes.dex */
        class C09341 implements Runnable {
            C09341() {
            }

            @Override // java.lang.Runnable
            public void run() {
                PredefineMusicActivtiy.this.setMusic();
                PredefineMusicActivtiy.this.setResult(-1, new Intent(PredefineMusicActivtiy.this, SlideShow_Video_Activity.class));
                PredefineMusicActivtiy.this.finish();
            }
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            PredefineMusicActivtiy.this.runOnUiThread(new C09341());
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityApi14, android.app.Activity, android.view.LayoutInflater.Factory2
    public View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    @Override // android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityApi14, android.app.Activity, android.view.LayoutInflater.Factory
    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predefine_music_activtiy);
        bindView();
        init();
        addListener();
        this.songListAdapter = new Adapter_SongList(this, this.songName);
        this.gvSongList.setAdapter((ListAdapter) this.songListAdapter);
    }

    private void init() {
        this.application = MyApplication.getInstance();
        this.songName.add("Music 1");
        this.songName.add("Music 2");
        this.songName.add("Music 3");
        this.songName.add("Music 4");
        this.songName.add("Music 5");
        Log.e("bbbbb....PMA....", "ARRAY SIZE : " + SlideShow_Video_Activity.paths.size());
    }

    private void bindView() {
        this.gvSongList = (GridView) findViewById(R.id.gvSongList);
        this.btnFromStorage = (Button) findViewById(R.id.btnFromStorage);
        this.btnFromStorage = (Button) findViewById(R.id.btnFromStorage);
        this.tv_Done = (TextView) findViewById(R.id.tv_done);
        this.gvSongList.setVerticalScrollBarEnabled(false);
        this.tv_Done.setOnClickListener(new View.OnClickListener() { // from class: com.chettapps.videoeditor.videocutermerger.activities.PredefineMusicActivtiy.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                PredefineMusicActivtiy.this.stopPlaying(PredefineMusicActivtiy.this.mediaPlayer);
                new ChangeMusic().start();
            }
        });
    }

    private void addListener() {
        this.btnFromStorage.setOnClickListener(new C09321());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopPlaying(MediaPlayer mp) {
        if (mp != null) {
            mp.stop();
            mp.release();
        }
    }

    public void setMusic(int pos) {
        this.path = "android.resource://" + getPackageName() + "/" + this.resID[pos];
        this.application.posForAddMusicDialog = pos;
        stopPlaying(this.mediaPlayer);
        this.mediaPlayer = MediaPlayer.create(this, this.resID[pos]);
        this.mediaPlayer.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setMusic() {
        try {
            FileUtils.TEMP_DIRECTORY_AUDIO.mkdirs();
            File tempFile = new File(FileUtils.TEMP_DIRECTORY_AUDIO, "temp.mp3");
            if (tempFile.exists()) {
                FileUtils.deleteFile(tempFile);
            }
            InputStream in = getResources().openRawResource(this.resID[this.application.posForAddMusicDialog]);
            FileOutputStream out = new FileOutputStream(tempFile);
            byte[] buff = new byte[1024];
            while (true) {
                int read = in.read(buff);
                if (read <= 0) {
                    MediaPlayer player = new MediaPlayer();
                    player.setDataSource(tempFile.getAbsolutePath());
                    player.setAudioStreamType(3);
                    player.prepare();
                    final MusicData musicData = new MusicData();
                    musicData.track_data = tempFile.getAbsolutePath();
                    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.chettapps.videoeditor.videocutermerger.activities.PredefineMusicActivtiy.2
                        @Override // android.media.MediaPlayer.OnPreparedListener
                        public void onPrepared(MediaPlayer mp) {
                            musicData.track_duration = mp.getDuration();
                            mp.stop();
                        }
                    });
                    musicData.track_Title = "temp";
                    this.application.setMusicData(musicData);
                    return;
                }
                out.write(buff, 0, read);
            }
        } catch (Exception e) {
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        stopPlaying(this.mediaPlayer);
        super.onBackPressed();
    }
}
