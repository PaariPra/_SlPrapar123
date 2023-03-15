package com.chettapps.videoeditor.videocutermerger.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chettapps.videoeditor.R;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


import org.jetbrains.annotations.NotNull;

import java.io.File;


/* loaded from: classes.dex */
public class Video_Play_Activity extends AppCompatActivity implements View.OnClickListener {
    Boolean FromVideoAlbum;
    private ImageView iv_back;
    LinearLayout lvcre;
    LinearLayout lvhome;
    LinearLayout lvsh;
    TextView titleappbar;

    private int videoact;

    private Uri frifile;
    private String pathImage;
    private File file;
    private Dialog dialog;
    private ConstraintLayout cl_1, cl_2;
    private SimpleExoPlayer player;
    private ProgressBar progressBar;


    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play2);
        this.iv_back = (ImageView) findViewById(R.id.iv_back);
        this.titleappbar = (TextView) findViewById(R.id.titleappbar);
        this.lvsh = (LinearLayout) findViewById(R.id.lvsh);
        this.lvhome = (LinearLayout) findViewById(R.id.lvhome);
        this.lvcre = (LinearLayout) findViewById(R.id.lvcre);



        this.pathImage = getIntent().getStringExtra("android.intent.extra.TEXT");
        this.videoact = getIntent().getIntExtra("videoact", 0);


        this.FromVideoAlbum = Boolean.valueOf(getIntent().getExtras().getBoolean("FromVideoAlbumkey"));



        File f = new File(this.pathImage);
        this.titleappbar.setText(f.getName());



        PlayerView playerView = findViewById(R.id.player_view);
        progressBar = findViewById(R.id.progressBar_video_play);
        progressBar.setVisibility(View.VISIBLE);

        DefaultTrackSelector trackSelector = new DefaultTrackSelector(Video_Play_Activity.this);
        player = new SimpleExoPlayer.Builder(Video_Play_Activity.this)
                .setTrackSelector(trackSelector)
                .build();
        playerView.setPlayer(player);

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(Video_Play_Activity.this,
                Util.getUserAgent(Video_Play_Activity.this, getResources().getString(R.string.app_name)));
        // This is the MediaSource representing the media to be played.
        assert pathImage != null;


        MediaItem mediaItem = MediaItem.fromUri(Uri.fromFile(new File(pathImage)));
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mediaItem);



        // Prepare the player with the source.
        player.setMediaSource(mediaSource);
        player.prepare();
        player.setPlayWhenReady(true);



        player.addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean playWhenReady) {
                if (playWhenReady) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPlayerError(@NotNull PlaybackException error) {
                Log.d("show_error", error.toString());
                progressBar.setVisibility(View.GONE);

            }
        });






        if (this.FromVideoAlbum.booleanValue()) {
            this.lvcre.setVisibility(View.GONE);
            this.lvhome.setVisibility(View.GONE);
        }
        this.lvsh.setOnClickListener(this);
        this.lvhome.setOnClickListener(this);
        this.lvcre.setOnClickListener(this);
        this.iv_back.setOnClickListener(this);


    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();

    }

    @Override // android.support.v7.app.AppCompatActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {

            Log.d("bbbbb...............", ".....onBackPressed........FromVideoAlbum.........." + this.FromVideoAlbum);
            Intent intent = new Intent(this, MyCreationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(MyCreationActivity.EXTRA_FROM_VIDEO, true);
            intent.putExtra("pos", this.videoact);
            startActivity(intent);
            finish();

    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back /* 2131755199 */:
                onBackPressed();
                return;
            case R.id.lvsh /* 2131755319 */:
                shareVideoViaIntent(this, this.pathImage, true);
                return;
            case R.id.lvhome /* 2131755320 */:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return;
            case R.id.lvcre /* 2131755321 */:
                startActivity(new Intent(this, MyCreationActivity.class).putExtra(MyCreationActivity.EXTRA_FROM_VIDEO, true));
                finish();
                return;
            default:
                return;
        }
    }

    public static void shareVideoViaIntent(Context context, String pathVideo, boolean isChooser) {
        Intent shareIntent;
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("video*/");
        intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(pathVideo)));
        if (isChooser) {
            shareIntent = Intent.createChooser(intent, context.getString(R.string.text_share_via));
        } else {
            shareIntent = intent;
        }
        context.startActivity(shareIntent);
    }






}
