package com.chettapps.videoeditor.videocutermerger.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.chettapps.videoeditor.R;
import com.chettapps.videoeditor.videocutermerger.MyApplication;
import com.chettapps.videoeditor.videocutermerger.objects.Album;

import java.io.File;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class Adapter_Album extends RecyclerView.Adapter<Adapter_Album.ItemAlbum> {
    private ArrayList<Album> albumList;
    MyApplication application;
    private ArrayList<String> folderId;
    private Context mContext;
    private OnClickAlbum onClickAlbum;

    /* loaded from: classes.dex */
    public interface OnClickAlbum {
        void onClickAlbum(int i);
    }

    public Adapter_Album(Context mContext, ArrayList<Album> albumList, OnClickAlbum onClickAlbum) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.onClickAlbum = onClickAlbum;
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public ItemAlbum onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_card, parent, false);
        return new ItemAlbum(itemView);
    }

    public void onBindViewHolder(ItemAlbum holder, final int position) {
        holder.title.setText(this.albumList.get(position).getBucket());
        holder.size.setText(this.albumList.get(position).getArrImage().size() + "");
        Glide.with(this.mContext).load(new File(this.albumList.get(position).getArrImage().get(0).getPath())).into(holder.thumbnail);
        holder.thumbnail.setOnClickListener(new View.OnClickListener() { // from class: com.chettapps.videoeditor.videocutermerger.adapters.Adapter_Album.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Adapter_Album.this.onClickAlbum.onClickAlbum(position);
            }
        });
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.albumList.size();
    }

    /* loaded from: classes.dex */
    public class ItemAlbum extends RecyclerView.ViewHolder {
        TextView size;
        ImageView thumbnail;
        TextView title;

        public ItemAlbum(View itemView) {
            super(itemView);
            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.size = (TextView) itemView.findViewById(R.id.count);
        }
    }
}
