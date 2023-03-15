package com.chettapps.videoeditor.videocutermerger.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.chettapps.videoeditor.R;


import com.chettapps.videoeditor.videocutermerger.EPreferences;
import com.chettapps.videoeditor.videocutermerger.MyApplication;
import com.chettapps.videoeditor.videocutermerger.adapters.Adapter_SwapEdit_RecyclerList;

import com.chettapps.videoeditor.videocutermerger.helper.OnStartDragListener;
import com.chettapps.videoeditor.videocutermerger.helper.SimpleItemTouchHelperCallback;
import com.chettapps.videoeditor.videocutermerger.imageeditor.EditImageActivity;
import com.chettapps.videoeditor.videocutermerger.objects.Image;
import com.chettapps.videoeditor.videocutermerger.utils.Animation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class Swap_Edit_Activity extends AppCompatActivity implements View.OnClickListener, OnStartDragListener, Adapter_SwapEdit_RecyclerList.OnClickImageEdit {
    public static ArrayList<Image> imageList;
    private Adapter_SwapEdit_RecyclerList adapter;
    private ImageView ivBack;
    private ImageView ivNext;
    private ItemTouchHelper mItemTouchHelper;
    private ArrayList<String> paths;
    private int position;
    private RecyclerView recyclerView;
    private TextView tvNext;
    private TextView tvtitle;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_swap_and_edit);
        this.ivBack = (ImageView) findViewById(R.id.iv_back);
        this.ivNext = (ImageView) findViewById(R.id.iv_next);
        this.tvNext = (TextView) findViewById(R.id.tv_next);
        this.tvtitle = (TextView) findViewById(R.id.titleappbar);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("IMAGE");
        imageList = bundle.getParcelableArrayList("IMAGE");
        this.recyclerView = (RecyclerView) findViewById(R.id.rv_image);
        this.recyclerView.hasFixedSize();
        this.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        this.adapter = new Adapter_SwapEdit_RecyclerList(this, this, imageList, this);
        this.recyclerView.setAdapter(this.adapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(this.adapter);
        this.mItemTouchHelper = new ItemTouchHelper(callback);
        this.mItemTouchHelper.attachToRecyclerView(this.recyclerView);
        this.ivNext.setOnClickListener(this);
        this.ivBack.setOnClickListener(this);
        this.tvNext.setOnClickListener(this);
        this.tvtitle.setOnClickListener(this);


    }

    @Override // com.photovideomaker.pictovideditor.videocutermerger.helper.OnStartDragListener
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        this.mItemTouchHelper.startDrag(viewHolder);
        imageList = this.adapter.getmItems();
    }

    @Override // com.photovideomaker.pictovideditor.videocutermerger.adapters.Adapter_SwapEdit_RecyclerList.OnClickImageEdit
    public void onClickImageEdit(int position) {
        imageList = this.adapter.getmItems();
        Uri imageUri = Uri.fromFile(new File(imageList.get(position).getPath()));
        Log.d("bbbb....SAndE......", "...img..onclick....." + imageUri);
        Intent data = new Intent(this, EditImageActivity.class);
        data.putExtra("KEY_DATA_RESULT", imageUri.toString());
        data.putExtra("id", position);
        Log.d("bbbbb....", "..swap....send...****.....imgpaths : " + imageUri.toString());
        startActivity(data);
        finish();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back /* 2131755199 */:
            case R.id.titleappbar /* 2131755200 */:
                finish();
                Animation.previewAnimation(this);
                return;
            case R.id.tv_next /* 2131755201 */:
            case R.id.iv_next /* 2131755202 */:
                this.paths = new ArrayList<>();
                for (int i = 0; i < imageList.size(); i++) {
                    this.paths.add(imageList.get(i).getPath());
                }
                if (this.paths.size() != 0) {
                    changeSettingsWithId();
                    return;
                } else {
                    Toast.makeText(this, getResources().getString(R.string.nullimg), Toast.LENGTH_SHORT).show();
                    return;
                }
            default:
                return;
        }
    }

    public void changeSettingsWithId() {
        String[] strVideoResolution = getResources().getStringArray(R.array.video_resolution);
        EPreferences.getInstance(getApplicationContext()).getInt(EPreferences.PREF_KEY_VIDEO_QUALITY, 1);
        videoSettings(strVideoResolution, "Video Quality", EPreferences.PREF_KEY_VIDEO_QUALITY, EPreferences.getInstance(getApplicationContext()).getInt(EPreferences.PREF_KEY_VIDEO_QUALITY, 1));
    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint("ResourceType")
    public void videoSettings(final String[] items, final String title, final String keyPref, final int selectItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setCancelable(true);
        builder.setSingleChoiceItems(items, selectItem, new DialogInterface.OnClickListener() { // from class: com.photovideomaker.pictovideditor.videocutermerger.activities.Swap_Edit_Activity.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    Swap_Edit_Activity.this.warnnigAlertDialog(items, title, keyPref, selectItem, item);
                }
                EPreferences.getInstance(Swap_Edit_Activity.this).putInt(keyPref, item);
                Swap_Edit_Activity.this.setDefaultVideoQuality();
                Swap_Edit_Activity.this.setVideoHeightWidth();
            }
        });
        builder.setNegativeButton(17039360, new C09555());
        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() { // from class: com.photovideomaker.pictovideditor.videocutermerger.activities.Swap_Edit_Activity.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent data = new Intent(Swap_Edit_Activity.this, SlideShow_Video_Activity.class);
                data.putExtra("IMAGE_ARR", Swap_Edit_Activity.this.paths);
                Log.d("bbbb.....SAndE...", "...send...*********************......paths....." + Swap_Edit_Activity.this.paths);
                Swap_Edit_Activity.this.startActivity(data);
                Swap_Edit_Activity.this.finish();
                Animation.nextAnimation(Swap_Edit_Activity.this);
            }
        });
        builder.create().show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void warnnigAlertDialog(String[] items, String title, final String keyPref, int selectItem, final int clickedItem) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Warnning!");
        alertDialogBuilder.setMessage("You selected video quality " + items[clickedItem] + ",It may take more time to create.").setCancelable(false).setPositiveButton("Proceed", new DialogInterface.OnClickListener() { // from class: com.photovideomaker.pictovideditor.videocutermerger.activities.Swap_Edit_Activity.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int n) {
                EPreferences.getInstance(Swap_Edit_Activity.this).putInt(keyPref, clickedItem);
                Swap_Edit_Activity.this.setDefaultVideoQuality();
                Swap_Edit_Activity.this.setVideoHeightWidth();
                dialogInterface.dismiss();
            }
        }).setNegativeButton("Select Another", new C09577());
        alertDialogBuilder.create().show();
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
            Log.d("bbbb..TAG", "Setting VideoQuality value is  C09577");
            String[] strVideoResolution = Swap_Edit_Activity.this.getResources().getStringArray(R.array.video_resolution);
            EPreferences.getInstance(Swap_Edit_Activity.this.getApplicationContext()).getInt(EPreferences.PREF_KEY_VIDEO_QUALITY, 1);
            Swap_Edit_Activity.this.videoSettings(strVideoResolution, "Video Quality", EPreferences.PREF_KEY_VIDEO_QUALITY, EPreferences.getInstance(Swap_Edit_Activity.this.getApplicationContext()).getInt(EPreferences.PREF_KEY_VIDEO_QUALITY, 1));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDefaultVideoQuality() {
        Log.d("bbbb..TAG", "Setting VideoQuality value is ....setDefaultVideoQuality....:- ");
        getResources().getStringArray(R.array.video_height_width);
        EPreferences.getInstance(getApplicationContext()).getInt(EPreferences.PREF_KEY_VIDEO_QUALITY, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setVideoHeightWidth() {
        String strTemp = getResources().getStringArray(R.array.video_height_width)[EPreferences.getInstance(getApplicationContext()).getInt(EPreferences.PREF_KEY_VIDEO_QUALITY, 2)];
        Log.d("bbbb..TAG", "Setting VideoQuality value is ...**..setVideoHeightWidth..**.....:- " + strTemp);
        MyApplication.VIDEO_WIDTH = Integer.parseInt(strTemp.split(Pattern.quote("*"))[0]);
        MyApplication.VIDEO_HEIGHT = Integer.parseInt(strTemp.split(Pattern.quote("*"))[1]);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        Animation.previewAnimation(this);
    }




}
