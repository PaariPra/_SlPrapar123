package com.chettapps.videoeditor.videocutermerger.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chettapps.videoeditor.R;
import com.chettapps.videoeditor.videocutermerger.MyApplication;
import com.chettapps.videoeditor.videocutermerger.utils.Animation;
import com.chettapps.videoeditor.videocutermerger.utils.Utils;
import com.chettapps.videoeditor.videocutermerger.utils.FileUtils2;

/* loaded from: classes.dex */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int ACTION_SELECT_VIDEO = 1;
    private static final String[] PERMISSION = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};

    private static final String VIDEO_PATH_KEY = "video-file-path";
    ImageView banner;
    private LinearLayout btCreateVideo;
    private LinearLayout btMyVideo;
    private LinearLayout btSetting;
    private LinearLayout bt_cut;
    private LinearLayout bt_join;
    private LinearLayout bt_edivi;
    Dialog dialogFirst;
    final int mediaSize = 550;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_main);
        this.btCreateVideo = (LinearLayout) findViewById(R.id.bt_new_video);
        this.btMyVideo = (LinearLayout) findViewById(R.id.bt_my_video);
        this.btSetting = (LinearLayout) findViewById(R.id.bt_setting);
        this.bt_cut = (LinearLayout) findViewById(R.id.bt_cut);
        this.bt_join = (LinearLayout) findViewById(R.id.bt_join);
        this.bt_edivi = (LinearLayout) findViewById(R.id.bt_edivi);
        turnPermiss();


    }

    public void init() {
        this.btCreateVideo.setOnClickListener(this);
        this.bt_cut.setOnClickListener(this);
        this.bt_join.setOnClickListener(this);
        this.bt_edivi.setOnClickListener(this);
        this.btMyVideo.setOnClickListener(new View.OnClickListener() { // from class: com.photovideomaker.pictovideditor.videocutermerger.activities.MainActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < 23) {
                    MainActivity.this.startActivity(new Intent(MainActivity.this, MyCreationActivity.class));
                } else if (Utils.checkPermission(MainActivity.PERMISSION, MainActivity.this) == 0) {
                    MainActivity.this.startActivity(new Intent(MainActivity.this, MyCreationActivity.class));
                } else {
                    MainActivity.this.requestPermissions(MainActivity.PERMISSION, 1);
                }
            }
        });
        this.btSetting.setOnClickListener(new View.OnClickListener() { // from class: com.photovideomaker.pictovideditor.videocutermerger.activities.MainActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < 23) {
                    MainActivity.this.startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                } else if (Utils.checkPermission(MainActivity.PERMISSION, MainActivity.this) == 0) {
                    MainActivity.this.startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                } else {
                    MainActivity.this.requestPermissions(MainActivity.PERMISSION, 1);
                }
            }
        });
    }

    public void turnPermiss() {
        if (Build.VERSION.SDK_INT < 23) {
            init();
        } else if (Utils.checkPermission(PERMISSION, this) == 0) {
            init();
        } else {
            requestPermissions(PERMISSION, 1);
        }
    }

    @Override
    // android.support.v4.app.FragmentActivity, android.app.Activity, android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0) {
            init();
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_new_video /* 2131755251 */:
            case R.id.iv_creat /* 2131755252 */:
                if (Build.VERSION.SDK_INT < 23) {
                    MyApplication.isBreak = false;
                    MyApplication.getInstance().setMusicData(null);
                    startActivity(new Intent(this, Gallery_ImagePicker_Activity.class));
                    Animation.nextAnimation(this);
                    return;
                } else if (Utils.checkPermission(PERMISSION, this) == 0) {
                    MyApplication.isBreak = false;
                    MyApplication.getInstance().setMusicData(null);
                    startActivity(new Intent(this, Gallery_ImagePicker_Activity.class));
                    Animation.nextAnimation(this);
                    return;
                } else {
                    requestPermissions(PERMISSION, 1);
                    return;
                }
            case R.id.customTextView /* 2131755253 */:
            case R.id.iv_cut /* 2131755255 */:
            case R.id.cutTextView /* 2131755256 */:
            default:
                return;


            case R.id.bt_cut /* 2131755254 */:
                if (Build.VERSION.SDK_INT < 23) {

                    startActivity(new Intent(MainActivity.this, GetAllVIdeoActivty.class));
                    return;
                } else if (Utils.checkPermission(PERMISSION, this) == 0) {
                    startActivity(new Intent(MainActivity.this, GetAllVIdeoActivty.class)
                            .putExtra("type", "cut"));


                    return;
                } else {
                    requestPermissions(PERMISSION, 1);
                    return;
                }



            case R.id.bt_edivi /* 2131755254 */:
                if (Build.VERSION.SDK_INT < 23) {

                    startActivity(new Intent(MainActivity.this, GetAllVIdeoActivty.class));
                    return;
                } else if (Utils.checkPermission(PERMISSION, this) == 0) {
                    startActivity(new Intent(MainActivity.this, GetAllVIdeoActivty.class)
                            .putExtra("type", "edit"));


                    return;
                } else {
                    requestPermissions(PERMISSION, 1);
                    return;
                }




            case R.id.bt_join /* 2131755257 */:
                if (Build.VERSION.SDK_INT < 23) {
                    startActivity(new Intent(this, AllJoinVideoe.class));
                    return;
                } else if (Utils.checkPermission(PERMISSION, this) == 0) {
                    startActivity(new Intent(this, AllJoinVideoe.class));
                    return;
                } else {
                    requestPermissions(PERMISSION, 1);
                    return;

                }
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == -1 && data != null) {
            Uri selectedUri = data.getData();
            if (selectedUri != null) {
//                startTrimActivity(selectedUri);
            } else {
                Toast.makeText(this, (int) R.string.toast_cannot_retrieve_selected_video, 0).show();
            }
        }
    }
//
//    private void startTrimActivity(@NonNull Uri uri) {
//        String path = FileUtils2.getPath(this, uri);
//
//
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        retriever.setDataSource(path);
//
//        String time = retriever.extractMetadata(9);
//        int timeInmillisec = Integer.parseInt(time);
//        Log.d("bbbb....", "...Send....timeInmillisec......." + timeInmillisec);
//
//        Intent intent = new Intent(this, CutActivity.class);
//        intent.putExtra("video_path", FileUtils2.getPath(this, uri));
//
//
//
//
//
//        startActivity(intent);
//    }


    public void appexit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit this app");
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() { // from class: com.photovideomaker.pictovideditor.videocutermerger.activities.MainActivity.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.super.onBackPressed();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) null);
        builder.show();
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        if (isNetworkAvailable()) {
            this.dialogFirst.show();
        } else {
            appexit();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
