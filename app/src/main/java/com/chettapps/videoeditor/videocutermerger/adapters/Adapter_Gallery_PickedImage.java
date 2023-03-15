package com.chettapps.videoeditor.videocutermerger.adapters;

import android.annotation.SuppressLint;
import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.recyclerview.widget.RecyclerView;

import com.chettapps.videoeditor.R;
import com.chettapps.videoeditor.videocutermerger.MyApplication;
import com.chettapps.videoeditor.videocutermerger.objects.Image;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class Adapter_Gallery_PickedImage extends RecyclerView.Adapter<Adapter_Gallery_PickedImage.ItemPickedImage> {
    MyApplication application;
    private File image;
    private ArrayList<Image> imageList;
    private Context mContext;
    private OnClickCancel onClickCancel;

    /* loaded from: classes.dex */
    public interface OnClickCancel {
        void onClickCancel(int i);
    }

    public Adapter_Gallery_PickedImage(ArrayList<Image> imageList, Context mContext, OnClickCancel onClickCancel) {
        this.imageList = imageList;
        this.mContext = mContext;
        this.onClickCancel = onClickCancel;
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public ItemPickedImage onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(this.mContext).inflate(R.layout.item_picked_image_card, parent, false);
        return new ItemPickedImage(itemView);
    }

    public void onBindViewHolder(ItemPickedImage holder, @SuppressLint("RecyclerView") final int position) {
        this.image = new File(this.imageList.get(position).getPath());
        Picasso.get().load(this.image).resize(200, 200).into(holder.thumbnail);
        Log.d("bbbb.....GIMA...", "...Adapter..image....." + this.image);
        holder.ivCancel.setOnClickListener(new View.OnClickListener() { // from class: com.chettapps.videoeditor.videocutermerger.adapters.Adapter_Gallery_PickedImage.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Adapter_Gallery_PickedImage.this.onClickCancel.onClickCancel(position);
            }
        });
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public long getItemId(int position) {
        return position;
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.imageList.size();
    }

    /* loaded from: classes.dex */
    public class ItemPickedImage extends RecyclerView.ViewHolder {
        ImageView ivCancel;
        ImageView thumbnail;

        public ItemPickedImage(View itemView) {
            super(itemView);
            this.ivCancel = (ImageView) itemView.findViewById(R.id.iv_cancel);
            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        }
    }
}
