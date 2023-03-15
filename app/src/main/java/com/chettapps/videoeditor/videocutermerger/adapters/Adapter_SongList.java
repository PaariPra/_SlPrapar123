package com.chettapps.videoeditor.videocutermerger.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.chettapps.videoeditor.R;
import com.chettapps.videoeditor.videocutermerger.activities.PredefineMusicActivtiy;

import java.util.ArrayList;

/* loaded from: classes.dex */
public class Adapter_SongList extends BaseAdapter {
    Context context;
    int row_index = 0;
    ArrayList<String> songName;

    public Adapter_SongList(Context cnx, ArrayList<String> name) {
        this.context = cnx;
        this.songName = name;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.songName.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int arg0) {
        return this.songName.get(arg0);
    }

    @Override // android.widget.Adapter
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override // android.widget.Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.row_musiclist, parent, false);
        }
        TextView tvMusicName = (TextView) convertView.findViewById(R.id.tvMusicName);
        ImageView ivMusic = (ImageView) convertView.findViewById(R.id.ivImageSong);
        LinearLayout llMusic = (LinearLayout) convertView.findViewById(R.id.llMusic);
        if (this.row_index == position) {
            ivMusic.setBackgroundResource(R.drawable.border_white);
            tvMusicName.setTextColor(this.context.getResources().getColor(R.color.blue));
        } else {
            ivMusic.setBackgroundResource(R.drawable.border_black);
            tvMusicName.setTextColor(this.context.getResources().getColor(R.color.black));
        }
        tvMusicName.setText(this.songName.get(position));
        llMusic.setOnClickListener(new View.OnClickListener() { // from class: com.chettapps.videoeditor.videocutermerger.adapters.Adapter_SongList.1
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                Adapter_SongList.this.row_index = position;
                Adapter_SongList.this.notifyDataSetChanged();
                ((PredefineMusicActivtiy) Adapter_SongList.this.context).setMusic(position);
            }
        });
        return convertView;
    }
}
