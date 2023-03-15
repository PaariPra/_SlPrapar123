package com.chettapps.videoeditor.videocutermerger.activities;

import android.content.Intent;
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


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chettapps.videoeditor.R;
import com.chettapps.videoeditor.videocutermerger.MyApplication;
import com.chettapps.videoeditor.videocutermerger.adapters.Adapter_Album;
import com.chettapps.videoeditor.videocutermerger.adapters.Adapter_Gallery_PickedImage;
import com.chettapps.videoeditor.videocutermerger.adapters.Adapter_Image;

import com.chettapps.videoeditor.videocutermerger.fragments.Fragment_Album;
import com.chettapps.videoeditor.videocutermerger.fragments.Fragment_Image;
import com.chettapps.videoeditor.videocutermerger.objects.Image;
import com.chettapps.videoeditor.videocutermerger.utils.Animation;
import com.chettapps.videoeditor.videocutermerger.utils.Manager_Galary;

import java.util.ArrayList;
import java.util.List;


public class Gallery_ImagePicker_Activity extends AppCompatActivity
        implements Adapter_Album.OnClickAlbum, View.OnClickListener,
        Adapter_Image.OnClickImage, Adapter_Gallery_PickedImage.OnClickCancel
{

    public static final String EXTRA_FROM_PREVIEW = "extra_from_preview";
    private Adapter_Gallery_PickedImage adapterGalleryPickedImage;
    MyApplication application;
    private ArrayList<Image> arrImagesAlbum;
    private ImageView btClear;
    private Fragment_Album fragmentAlbum;
    private Fragment_Image imageFragment;
    private ArrayList<String> imagePaths;
    private ArrayList<Image> imagesAlBum;
    private ArrayList<Image> imagesPicked;
    private ImageView ivBack;
    private ImageView ivNext;
    private Manager_Galary managerGalary;
    private RecyclerView rvPickedImage;
    private TextView tvNext;
    private TextView tvSelected;
    private TextView tvTitle;
    public boolean isFromPreview = false;
    int limitImageMax = 30;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_image_picker);
        this.isFromPreview = getIntent().hasExtra(EXTRA_FROM_PREVIEW);
        Log.d("bbbbb....ISA....", ".................OnCreate........isFromPreview......." + this.isFromPreview);
        initUI();


    }

    private void initUI() {
        this.tvSelected = (TextView) findViewById(R.id.tv_count_selected_img);
        this.tvNext = (TextView) findViewById(R.id.tv_next);
        this.tvTitle = (TextView) findViewById(R.id.titleappbar);
        this.ivBack = (ImageView) findViewById(R.id.iv_back);
        this.ivNext = (ImageView) findViewById(R.id.iv_next);
        this.ivBack.setOnClickListener(this);
        this.ivNext.setOnClickListener(this);
        this.tvNext.setOnClickListener(this);
        this.tvTitle.setOnClickListener(this);
        this.btClear = (ImageView) findViewById(R.id.btn_clear);
        this.btClear.setOnClickListener(this);
        this.rvPickedImage = (RecyclerView) findViewById(R.id.rv_picked_image);
        this.rvPickedImage.setLayoutManager(new LinearLayoutManager(this, 0, false));
        this.imagesPicked = new ArrayList<>();
        this.adapterGalleryPickedImage = new Adapter_Gallery_PickedImage(this.imagesPicked, this, this);
        this.adapterGalleryPickedImage.setHasStableIds(true);
        this.rvPickedImage.setAdapter(this.adapterGalleryPickedImage);
        this.managerGalary = new Manager_Galary(this);
        this.arrImagesAlbum = this.managerGalary.getArrImage();
        this.imagePaths = this.managerGalary.getImagePaths();
        this.fragmentAlbum = new Fragment_Album();
        this.fragmentAlbum.setOnClickAlbum(this);
        this.fragmentAlbum.setImagesAlbum(this.arrImagesAlbum);
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.frame_image_picker, this.fragmentAlbum);
        t.commit();
        if (this.isFromPreview) {
            this.btClear.setVisibility(8);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back /* 2131755199 */:
            case R.id.titleappbar /* 2131755200 */:
                try {
                    if (!this.imageFragment.isVisible() || this.imageFragment == null) {
                        finish();
                        Animation.previewAnimation(this);
                    } else {
                        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                        t.replace(R.id.frame_image_picker, this.fragmentAlbum);
                        t.commit();
                    }
                    return;
                } catch (Exception e) {
                    finish();
                    Animation.previewAnimation(this);
                    e.printStackTrace();
                    return;
                }
            case R.id.tv_next /* 2131755201 */:
            case R.id.iv_next /* 2131755202 */:
                if (this.isFromPreview) {
                    Log.d("bbbb.....GIMA...", "...tv_next....isFromPreview..... 000 ::: " + this.isFromPreview);
                    setResult(-1);
                    for (int i = 0; i < this.imagesPicked.size(); i++) {
                        SlideShow_Video_Activity.paths.add(String.valueOf(this.imagesPicked.get(i).getPath()));
                        Log.d("bbbb...SSVA...", "imagesPath............imagesPicked.get(i).getPath());................. = " + this.imagesPicked.get(i).getPath());
                    }
                    Log.d("bbbb...SSVA...", "imagesPath............................. = " + SlideShow_Video_Activity.paths);
                    Log.d("bbbb.....GIMA...", "...tv_next....isFromPreview..... 111 ::: " + SlideShow_Video_Activity.paths.size());
                    finish();
                    break;
                } else {
                    Intent intent = new Intent(this, Swap_Edit_Activity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("IMAGE", this.imagesPicked);
                    Log.d("bbbb.....GIMA...", "...IPA..imagesPicked....." + this.imagesPicked);
                    intent.putExtra("IMAGE", bundle);
                    startActivity(intent);
                    finish();
                    Animation.nextAnimation(this);
                    return;
                }
            case R.id.btn_clear /* 2131755239 */:
                break;
            default:
                return;
        }
        for (int i2 = 0; i2 < this.imagesPicked.size(); i2++) {
            this.arrImagesAlbum.get(this.arrImagesAlbum.indexOf(this.imagesPicked.get(i2))).setClicked(false);
        }
        this.imageFragment.notifiData(this.imagesAlBum);
        this.imagesPicked.clear();
        this.adapterGalleryPickedImage.notifyDataSetChanged();
        this.tvSelected.setText(getString(R.string.selected_0_image_s));
    }

    @Override // com.chettapps.videoeditor.videocutermerger.adapters.Adapter_Image.OnClickImage
    public void onClickImage(int position) {
        Log.d("bbbb....GIMA......", "...................................................................................... : " + this.imagesPicked.toString());
        Log.d("bbbb....GIMA......", "..........................................imagesPicked.size()......................... : " + this.imagesPicked.size());
        if (this.imagesPicked.size() < this.limitImageMax) {
            this.imagesPicked.add(this.imagesAlBum.get(position));
            this.imagesAlBum.get(position).setClicked(true);
            this.imageFragment.notifiData(this.imagesAlBum);
            this.adapterGalleryPickedImage.notifyDataSetChanged();
            this.tvSelected.setText(getString(R.string.selected) + " " + this.imagesPicked.size() + " " + getString(R.string.image));
            this.rvPickedImage.smoothScrollToPosition(this.imagesPicked.size() - 1);
            Log.d("bbbb....GIMA......", this.imagesPicked.toString());
            return;
        }
        Toast.makeText(this, "Choose image Limit is 30", 0).show();
    }

    @Override // com.chettapps.videoeditor.videocutermerger.adapters.Adapter_Album.OnClickAlbum
    public void onClickAlbum(int position) {
        ((TextView) findViewById(R.id.titleappbar)).setText(getString(R.string.pick_image));
        this.imagesAlBum = this.fragmentAlbum.getArrAlbum().get(position).getArrImage();
        this.imageFragment = new Fragment_Image();
        this.imageFragment.setImagesAlbum(this.imagesAlBum);
        this.imageFragment.setOnClickImage(this);
        Log.d("bbbb...GIMA.....", "onClick" + position);
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.frame_image_picker, this.imageFragment);
        t.commit();
    }

    @Override // com.chettapps.videoeditor.videocutermerger.adapters.Adapter_Gallery_PickedImage.OnClickCancel
    public void onClickCancel(int position) {
        this.arrImagesAlbum.get(this.arrImagesAlbum.indexOf(this.imagesPicked.get(position))).setClicked(false);
        this.imageFragment.notifiData(this.imagesAlBum);
        this.imagesPicked.remove(this.imagesPicked.get(position));
        this.adapterGalleryPickedImage.notifyDataSetChanged();
        this.tvSelected.setText(getString(R.string.selected) + " " + this.imagesPicked.size() + " " + getString(R.string.image));
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        try {
            if (!this.imageFragment.isVisible() || this.imageFragment == null) {
                finish();
                Animation.previewAnimation(this);
            } else {
                ((TextView) findViewById(R.id.titleappbar)).setText(getString(R.string.pick_album));
                FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                t.replace(R.id.frame_image_picker, this.fragmentAlbum);
                t.commit();
            }
            if (this.isFromPreview) {
                setResult(-1);
                finish();
            }
        } catch (NullPointerException e) {
            finish();
            Animation.previewAnimation(this);
        }
    }


}
