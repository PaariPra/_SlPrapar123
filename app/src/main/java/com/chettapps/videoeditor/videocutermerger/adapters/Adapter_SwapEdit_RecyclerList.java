package com.chettapps.videoeditor.videocutermerger.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.chettapps.videoeditor.R;
import com.chettapps.videoeditor.videocutermerger.helper.ItemTouchHelperAdapter;
import com.chettapps.videoeditor.videocutermerger.helper.ItemTouchHelperViewHolder;
import com.chettapps.videoeditor.videocutermerger.helper.OnStartDragListener;
import com.chettapps.videoeditor.videocutermerger.objects.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/* loaded from: classes.dex */
public class Adapter_SwapEdit_RecyclerList extends RecyclerView.Adapter<Adapter_SwapEdit_RecyclerList.ItemViewHolder> implements ItemTouchHelperAdapter {
    private Context mContext;
    private final OnStartDragListener mDragStartListener;
    private ArrayList<Image> mItems;
    private OnClickImageEdit onClickImageEdit;

    /* loaded from: classes.dex */
    public interface OnClickImageEdit {
        void onClickImageEdit(int i);
    }

    public ArrayList<Image> getmItems() {
        return this.mItems;
    }

    public Adapter_SwapEdit_RecyclerList(Context context, OnStartDragListener dragStartListener, ArrayList<Image> images, OnClickImageEdit onClickImageEdit) {
        this.mDragStartListener = dragStartListener;
        this.mItems = images;
        this.mContext = context;
        this.onClickImageEdit = onClickImageEdit;
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_edit_swap, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        Glide.with(this.mContext).load(new File(this.mItems.get(position).getPath())).into(holder.thumbnail);
        holder.thumbnail.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.photovideomaker.pictovideditor.videocutermerger.adapters.Adapter_SwapEdit_RecyclerList.1
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View v) {
                Adapter_SwapEdit_RecyclerList.this.mDragStartListener.onStartDrag(holder);
                return false;
            }
        });
        holder.thumbnail.setOnClickListener(new View.OnClickListener() { // from class: com.photovideomaker.pictovideditor.videocutermerger.adapters.Adapter_SwapEdit_RecyclerList.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Adapter_SwapEdit_RecyclerList.this.onClickImageEdit.onClickImageEdit(position);
            }
        });
        holder.ivCancel.setOnClickListener(new View.OnClickListener() { // from class: com.photovideomaker.pictovideditor.videocutermerger.adapters.Adapter_SwapEdit_RecyclerList.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Adapter_SwapEdit_RecyclerList.this.mItems.remove(position);
                Adapter_SwapEdit_RecyclerList.this.notifyDataSetChanged();
            }
        });
    }

    @Override // com.photovideomaker.pictovideditor.videocutermerger.helper.ItemTouchHelperAdapter
    public void onItemDismiss(int position) {
        this.mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override // com.photovideomaker.pictovideditor.videocutermerger.helper.ItemTouchHelperAdapter
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(this.mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mItems.size();
    }

    /* loaded from: classes.dex */
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        public ImageView ivCancel;
        public ImageView thumbnail;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.ivCancel = (ImageView) itemView.findViewById(R.id.iv_cancel);
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.helper.ItemTouchHelperViewHolder
        public void onItemSelected() {
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.helper.ItemTouchHelperViewHolder
        public void onItemClear() {
        }
    }
}
