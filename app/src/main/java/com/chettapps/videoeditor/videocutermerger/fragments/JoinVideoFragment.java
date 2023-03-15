package com.chettapps.videoeditor.videocutermerger.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chettapps.videoeditor.R;
import com.chettapps.videoeditor.videocutermerger.activities.Video_Play_Activity;
import com.chettapps.videoeditor.videocutermerger.objects.VideoData;
import com.chettapps.videoeditor.videocutermerger.utils.FileUtils;


import java.io.File;
import java.util.ArrayList;
import java.util.Locale;


/* loaded from: classes.dex */
public class JoinVideoFragment extends Fragment
{
    private ArrayList<VideoData> mVideoDatas;
    private VideoJoinAdapter mVideoJoinAdapter;
    private RecyclerView rvVideojoin;

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video_join, container, false);
        this.rvVideojoin = (RecyclerView) rootView.findViewById(R.id.rvVideojoin);
        init();
        setUpRecyclerView();
        return rootView;
    }

    private void init() {
        getVideoList();
    }

    private void setUpRecyclerView()
    {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        this.mVideoJoinAdapter = new VideoJoinAdapter(getContext(), this.mVideoDatas);
        this.rvVideojoin.setLayoutManager(mLayoutManager);
        this.rvVideojoin.setItemAnimator(new DefaultItemAnimator());
        this.rvVideojoin.setAdapter(this.mVideoJoinAdapter);


    }

    private void getVideoList() {
        this.mVideoDatas = new ArrayList<>();
        String[] projection = {"_data", "_id", "bucket_display_name", "duration", "title", "datetaken", "_display_name"};
        Cursor cur = getActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, "_data like '%" +
                FileUtils.JOIN_VIDEO_DIRECTORY.getAbsolutePath() + "%'", null, "datetaken DESC");
        if (cur.moveToFirst()) {
            int bucketColumn = cur.getColumnIndex("duration");
            int data = cur.getColumnIndex("_data");
            int name = cur.getColumnIndex("title");
            int dateTaken = cur.getColumnIndex("datetaken");
            do {
                VideoData videoData = new VideoData();
                videoData.videoDuration = cur.getLong(bucketColumn);
                videoData.videoFullPath = cur.getString(data);
                videoData.videoName = cur.getString(name);
                videoData.dateTaken = cur.getLong(dateTaken);
                if (new File(videoData.videoFullPath).exists()) {
                    this.mVideoDatas.add(videoData);
                }
            } while (cur.moveToNext());
        }
    }

    /* loaded from: classes.dex */
    public class VideoJoinAdapter extends RecyclerView.Adapter<VideoJoinAdapter.Holder> {
        private Context mContext;
        public ArrayList<VideoData> mVideoDatas;

        /* loaded from: classes.dex */
        public class Holder extends RecyclerView.ViewHolder {
            ImageButton ibtnPlay;
            private ImageView ivDelete;
            private ImageView ivPreview;
            private ImageView ivShare;
            private TextView tvFileName;
            TextView txtDuration;

            public Holder(View v) {
                super(v);
                this.ivPreview = (ImageView) v.findViewById(R.id.list_item_video_thumb);
                this.tvFileName = (TextView) v.findViewById(R.id.list_item_video_title);
                this.ivShare = (ImageView) v.findViewById(R.id.ivShare);
                this.ivDelete = (ImageView) v.findViewById(R.id.ivDelete);
                this.ibtnPlay = (ImageButton) v.findViewById(R.id.timeline_play);
                this.txtDuration = (TextView) v.findViewById(R.id.duration);
            }
        }

        public VideoJoinAdapter(Context context, ArrayList<VideoData> mVideoDatas1) {
            this.mVideoDatas = mVideoDatas1;
            this.mContext = context;
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.mVideoDatas.size();
        }

        public void onBindViewHolder(Holder holder, @SuppressLint("RecyclerView") final int pos) {
            final String pathVideo = String.valueOf(new File(this.mVideoDatas.get(pos).videoFullPath));
            Glide.with(this.mContext).load(this.mVideoDatas.get(pos).videoFullPath).into(holder.ivPreview);
            MediaMetadataRetriever mRetriever = new MediaMetadataRetriever();
            mRetriever.setDataSource(pathVideo);
            final Bitmap frame = mRetriever.getFrameAtTime();
            long timeMillis = 0;
            try {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(pathVideo);
                timeMillis = Long.parseLong(retriever.extractMetadata(9));
            } catch (Exception ex) {
                ex.printStackTrace();
                MediaPlayer mp = MediaPlayer.create(this.mContext, Uri.fromFile(new File(pathVideo)));
                if (mp != null) {
                    timeMillis = mp.getDuration();
                    mp.release();
                }
            }
            long duration = timeMillis / 1000;
            long hours = duration / 3600;
            long minutes = (duration - (3600 * hours)) / 60;
            long seconds = duration - ((3600 * hours) + (60 * minutes));
            holder.txtDuration.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d", Long.valueOf(hours), Long.valueOf(minutes), Long.valueOf(seconds)));
            holder.tvFileName.setText(String.valueOf(this.mVideoDatas.get(pos).videoName) + ".mp4");
            holder.ivPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(VideoJoinAdapter.this.mContext, Video_Play_Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("android.intent.extra.TEXT", VideoJoinAdapter.this.mVideoDatas.get(pos).videoFullPath);
                    intent.putExtra(VideoJoinAdapter.this.mContext.getResources().getString(R.string.video_position_key), pos);
                    intent.putExtra("FromVideoAlbumkey", true);
                    VideoJoinAdapter.this.mContext.startActivity(intent);
                }
            });

            holder.ibtnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    frame.getWidth();
                    frame.getHeight();
                    Intent intent = new Intent(VideoJoinAdapter.this.mContext, Video_Play_Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("android.intent.extra.TEXT", VideoJoinAdapter.this.mVideoDatas.get(pos).videoFullPath);
                    intent.putExtra(VideoJoinAdapter.this.mContext.getResources().getString(R.string.video_position_key), pos);
                    intent.putExtra("FromVideoAlbumkey", true);
                    VideoJoinAdapter.this.mContext.startActivity(intent);
                }
            });
            holder.ivShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    VideoJoinAdapter.this.shareVideoViaIntent(VideoJoinAdapter.this.mContext, pathVideo, true);
                }
            });
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {

                @Override // android.view.View.OnClickListener
                public void onClick(View arg0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(VideoJoinAdapter.this.mContext);
                    builder.setTitle(R.string.delete_video_);
                    builder.setMessage(R.string.are_you_sure_to_delete_ + VideoJoinAdapter.this.mVideoDatas.get(pos).videoName + ".mp4 ?");
                    final int i = pos;
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() { // from class: com.chettapps.videoeditor.videocutermerger.fragments.JoinVideoFragment.VideoJoinAdapter.4.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            FileUtils.deleteFile(new File(VideoJoinAdapter.this.mVideoDatas.remove(i).videoFullPath));
                            VideoJoinAdapter.this.notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("Cancel", (DialogInterface.OnClickListener) null);
                    builder.show();
                }
            });
        }

        public void shareVideoViaIntent(Context context, String pathVideo, boolean isChooser) {
            Intent shareIntent;
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("video/*");
            intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(pathVideo)));
            if (isChooser) {
                shareIntent = Intent.createChooser(intent, context.getString(R.string.text_share_via));
            } else {
                shareIntent = intent;
            }
            context.startActivity(shareIntent);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int arg1) {
            return new Holder(LayoutInflater.from(this.mContext).inflate(R.layout.list_item_published_video, parent, false));
        }
    }
}
