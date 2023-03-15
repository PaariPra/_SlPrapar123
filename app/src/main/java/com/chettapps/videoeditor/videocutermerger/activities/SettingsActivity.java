package com.chettapps.videoeditor.videocutermerger.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;


import androidx.appcompat.app.AppCompatActivity;

import com.chettapps.videoeditor.R;
import com.chettapps.videoeditor.videocutermerger .EPreferences;
import com.chettapps.videoeditor.videocutermerger .MyApplication;


import java.util.List;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class SettingsActivity extends AppCompatActivity {
    EPreferences ePref;
    ImageView iv_back;
    View.OnClickListener rlClickListener;
    RelativeLayout rlVideoQauality;
    private Toolbar toolbar;
    TextView tvResolution;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class C09533 implements View.OnClickListener {
        C09533() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            SettingsActivity.this.changeSettingsWithId(v);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class C09555 implements DialogInterface.OnClickListener {
        C09555() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int id) {
            dialog.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class C09577 implements DialogInterface.OnClickListener {
        C09577() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int n) {
            dialogInterface.dismiss();
            String[] strVideoResolution = SettingsActivity.this.getResources().getStringArray(R.array.video_resolution);
            EPreferences.getInstance(SettingsActivity.this.getApplicationContext()).getInt(EPreferences.PREF_KEY_VIDEO_QUALITY, 1);
            SettingsActivity.this.videoSettings(strVideoResolution, "Video Quality", EPreferences.PREF_KEY_VIDEO_QUALITY, EPreferences.getInstance(SettingsActivity.this.getApplicationContext()).getInt(EPreferences.PREF_KEY_VIDEO_QUALITY, 1));
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        bindView();
        init();
        addListener();


    }

    private void bindView() {
        this.rlVideoQauality = (RelativeLayout) findViewById(R.id.rlVideoQauality);
        this.tvResolution = (TextView) findViewById(R.id.tvResolution);
        this.iv_back = (ImageView) findViewById(R.id.iv_back);
        this.iv_back.setOnClickListener(new View.OnClickListener() { // from class: com.chettapps.videoeditor.videocutermerger .activities.SettingsActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SettingsActivity.this.onBackPressed();
            }
        });
    }

    public static Typeface getTypefaceRegular(Activity mContext) {
        return Typeface.createFromAsset(mContext.getAssets(), "seguiemj.ttf");
    }

    private void init() {
        ((TextView) findViewById(R.id.tvGeneral)).setTypeface(getTypefaceRegular(this));
        ((TextView) findViewById(R.id.tvVideoQuality)).setTypeface(getTypefaceRegular(this));
        this.tvResolution.setTypeface(getTypefaceRegular(this));
        this.ePref = EPreferences.getInstance(this);
        setDefaultVideoQuality();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDefaultVideoQuality() {
        this.tvResolution.setText(getResources().getStringArray(R.array.video_resolution)[EPreferences.getInstance(getApplicationContext()).getInt(EPreferences.PREF_KEY_VIDEO_QUALITY, 1)]);
    }

    private void addListener() {
        this.rlClickListener = new C09533();
        this.rlVideoQauality.setOnClickListener(this.rlClickListener);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        getWindow().clearFlags(128);
        super.onBackPressed();
    }

    public void changeSettingsWithId(View v) {
        if (v.getId() == R.id.rlVideoQauality) {
            String[] strVideoResolution = getResources().getStringArray(R.array.video_resolution);
            EPreferences.getInstance(getApplicationContext()).getInt(EPreferences.PREF_KEY_VIDEO_QUALITY, 1);
            videoSettings(strVideoResolution, "Video Quality", EPreferences.PREF_KEY_VIDEO_QUALITY, EPreferences.getInstance(getApplicationContext()).getInt(EPreferences.PREF_KEY_VIDEO_QUALITY, 1));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int arg1, Intent arg2) {
        super.onActivityResult(requestCode, arg1, arg2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint("ResourceType")
    public void videoSettings(final String[] items, final String title, final String keyPref, final int selectItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setCancelable(true);
        builder.setSingleChoiceItems(items, selectItem, new DialogInterface.OnClickListener() { // from class: com.chettapps.videoeditor.videocutermerger .activities.SettingsActivity.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    SettingsActivity.this.warnnigAlertDialog(items, title, keyPref, selectItem, item);
                }
                EPreferences.getInstance(SettingsActivity.this).putInt(keyPref, item);
                SettingsActivity.this.setDefaultVideoQuality();
                SettingsActivity.this.setVideoHeightWidth();
            }
        });
        builder.setNegativeButton(17039360, new C09555());
        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() { // from class: com.photovideomaker.pictovideditor.videocutermerger.activities.SettingsActivity.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void warnnigAlertDialog(String[] items, String title, final String keyPref, int selectItem, final int clickedItem) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Warnning!");
        alertDialogBuilder.setMessage("You selected video quality " + items[clickedItem] + ",It may take more time to create.").setCancelable(false).setPositiveButton("Proceed", new DialogInterface.OnClickListener() { // from class: com.photovideomaker.pictovideditor.videocutermerger.activities.SettingsActivity.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int n) {
                EPreferences.getInstance(SettingsActivity.this).putInt(keyPref, clickedItem);
                SettingsActivity.this.setDefaultVideoQuality();
                SettingsActivity.this.setVideoHeightWidth();
                dialogInterface.dismiss();
            }
        }).setNegativeButton("Select Another", new C09577());
        alertDialogBuilder.create().show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setVideoHeightWidth() {
        String strTemp = getResources().getStringArray(R.array.video_height_width)[EPreferences.getInstance(getApplicationContext()).getInt(EPreferences.PREF_KEY_VIDEO_QUALITY, 2)];
        Log.d("TAG", "Setting VideoQuality value is:- " + strTemp);
        MyApplication.VIDEO_WIDTH = Integer.parseInt(strTemp.split(Pattern.quote("*"))[0]);
        MyApplication.VIDEO_HEIGHT = Integer.parseInt(strTemp.split(Pattern.quote("*"))[1]);
    }



}
