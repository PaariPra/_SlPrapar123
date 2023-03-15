package com.chettapps.videoeditor.videocutermerger.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chettapps.videoeditor.R;
import com.chettapps.videoeditor.videocutermerger.objects.Image;


import java.io.File;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class Adapter_Image extends RecyclerView.Adapter<Adapter_Image.ItemImage> {
    private ArrayList<Image> imageList;
    private Context mContext;
    private OnClickImage onClickImage;

    /* loaded from: classes.dex */
    public interface OnClickImage {
        void onClickImage(int i);
    }

    public Adapter_Image(ArrayList<Image> imageList, Context mContext, OnClickImage onClickImage) {
        this.imageList = imageList;
        this.mContext = mContext;
        this.onClickImage = onClickImage;
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public ItemImage onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(this.mContext).inflate(R.layout.item_image_card, parent, false);
        return new ItemImage(itemView);
    }

    public void onBindViewHolder(ItemImage holder, final int position) {
        Glide.with(this.mContext).load(new File(this.imageList.get(position).getPath())).into(holder.thumbnail);
        holder.thumbnail.setOnClickListener(new View.OnClickListener() { // from class: com.photovideomaker.pictovideditor.videocutermerger.adapters.Adapter_Image.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Adapter_Image.this.onClickImage.onClickImage(position);
            }
        });
        if (this.imageList.get(position).isClicked()) {
            holder.isChecked.setVisibility(0);
        } else {
            holder.isChecked.setVisibility(8);
        }
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.imageList.size();
    }

    /* loaded from: classes.dex */
    public class ItemImage extends RecyclerView.ViewHolder {
        ImageView isChecked;
        ImageView thumbnail;

        public ItemImage(View itemView) {
            super(itemView);
            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.isChecked = (ImageView) itemView.findViewById(R.id.iv_checked);
        }
    }
}
