package com.chettapps.videoeditor.videocutermerger.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chettapps.videoeditor.R;
import com.chettapps.videoeditor.videocutermerger.activities.Video_Play_Activity;
import com.chettapps.videoeditor.videocutermerger.utils.FileUtils;


import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/* loaded from: classes.dex */
public class CutVideoFragment extends Fragment {
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;

    @Override // android.support.v4.app.Fragment
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootview = inflater.inflate(R.layout.fragment_video_cut, container, false);
        this.recyclerView = (RecyclerView) rootview.findViewById(R.id.rvVideocut);
        this.layoutManager = new GridLayoutManager(getActivity(), 2);
        this.recyclerView.setLayoutManager(this.layoutManager);
        getListFiles(new File(FileUtils.CUT_VIDEO_DIRECTORY.getAbsolutePath()));
        this.recyclerView.setAdapter(new SaveVideoAdapter(getListFiles(new File(FileUtils.CUT_VIDEO_DIRECTORY.getAbsolutePath())), getActivity()));
        return rootview;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayList<File> getListFiles(File file) {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();
        if (files != null) {
            System.out.println("Descending order.");
            Log.d("cccccccc", "Descending order.");
            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            for (File file2 : files) {
                if ((file2.getName().endsWith(".mp4") || file2.getName().endsWith(".gif")) && !arrayList.contains(file2)) {
                    arrayList.add(file2);
                }
            }
        }
        return arrayList;
    }

    /* loaded from: classes.dex */
    public class SaveVideoAdapter extends RecyclerView.Adapter<SaveVideoAdapter.MyViewHolder> {
        Activity activity;
        private ArrayList<File> filesList;
        Bitmap frame;

        /* loaded from: classes.dex */
        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageButton ibtnPlay;
            private ImageView ivDelete;
            private ImageView ivPreview;
            private ImageView ivShare;
            private TextView tvFileName;
            TextView txtDuration;

            public MyViewHolder(View v) {
                super(v);
                this.ivPreview = (ImageView) v.findViewById(R.id.list_item_video_thumb);
                this.tvFileName = (TextView) v.findViewById(R.id.list_item_video_title);
                this.ivShare = (ImageView) v.findViewById(R.id.ivShare);
                this.ivDelete = (ImageView) v.findViewById(R.id.ivDelete);
                this.ibtnPlay = (ImageButton) v.findViewById(R.id.timeline_play);
                this.txtDuration = (TextView) v.findViewById(R.id.duration);
            }
        }

        public SaveVideoAdapter(ArrayList<File> arrayList, Activity activity) {
            this.filesList = arrayList;
            this.activity = activity;
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_published_video, viewGroup, false));
        }

        public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
            final File file = this.filesList.get(i);
            String v_name = file.getName();
            myViewHolder.tvFileName.setText(v_name);
            long timeMillis = 0;
            try {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(String.valueOf(file));
                timeMillis = Long.parseLong(retriever.extractMetadata(9));
                this.frame = retriever.getFrameAtTime();
            } catch (Exception ex) {
                ex.printStackTrace();
                MediaPlayer mp = MediaPlayer.create(this.activity, Uri.fromFile(new File(String.valueOf(file))));
                if (mp != null) {
                    timeMillis = mp.getDuration();
                    mp.release();
                }
            }
            long duration = timeMillis / 1000;
            long hours = duration / 3600;
            long minutes = (duration - (3600 * hours)) / 60;
            long seconds = duration - ((3600 * hours) + (60 * minutes));
            myViewHolder.txtDuration.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d", Long.valueOf(hours), Long.valueOf(minutes), Long.valueOf(seconds)));
            Glide.with(this.activity).load(Uri.fromFile(new File(file.getAbsolutePath()))).into(myViewHolder.ivPreview);
            myViewHolder.ivPreview.setOnClickListener(new View.OnClickListener() { // from class: com.chettapps.videoeditor.videocutermerger.fragments.CutVideoFragment.SaveVideoAdapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    Intent intent1 = new Intent(view.getContext(), Video_Play_Activity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("android.intent.extra.TEXT", file.getAbsolutePath());
                    bundle.putString("TITLE", file.getName());
                    bundle.putBoolean("FromVideoAlbumkey", true);
                    intent1.putExtras(bundle);
                    view.getContext().startActivity(intent1);
                }
            });
            myViewHolder.ibtnPlay.setOnClickListener(new View.OnClickListener() { // from class: com.chettapps.videoeditor.videocutermerger.fragments.CutVideoFragment.SaveVideoAdapter.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    int width = SaveVideoAdapter.this.frame.getWidth();
                    int height = SaveVideoAdapter.this.frame.getHeight();
                    Log.d("bbbb...video.....", "........width :::::::::::: " + width);
                    Log.d("bbbb...video.....", "........height ::::::::::: " + height);
                    Intent intent1 = new Intent(view.getContext(), Video_Play_Activity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("android.intent.extra.TEXT", file.getAbsolutePath());
                    bundle.putString("TITLE", file.getName());
                    bundle.putBoolean("FromVideoAlbumkey", true);
                    intent1.putExtras(bundle);
                    view.getContext().startActivity(intent1);
                }
            });
            myViewHolder.ivShare.setOnClickListener(new View.OnClickListener() { // from class: com.chettapps.videoeditor.videocutermerger.fragments.CutVideoFragment.SaveVideoAdapter.3
                @Override // android.view.View.OnClickListener
                public void onClick(View arg0) {
                    SaveVideoAdapter.this.shareVideoViaIntent(SaveVideoAdapter.this.activity, String.valueOf(file), true);
                }
            });
            myViewHolder.ivDelete.setOnClickListener(new View.OnClickListener() { // from class: com.chettapps.videoeditor.videocutermerger.fragments.CutVideoFragment.SaveVideoAdapter.4
                @Override // android.view.View.OnClickListener
                public void onClick(View arg0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SaveVideoAdapter.this.activity);
                    builder.setTitle(R.string.delete_video_);
                    builder.setMessage(R.string.are_you_sure_to_delete_);
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() { // from class: com.chettapps.videoeditor.videocutermerger.fragments.CutVideoFragment.SaveVideoAdapter.4.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            FileUtils.deleteFile(file);
                            CutVideoFragment.this.recyclerView.setAdapter(new SaveVideoAdapter(CutVideoFragment.this.getListFiles(new File(FileUtils.CUT_VIDEO_DIRECTORY.getAbsolutePath())), CutVideoFragment.this.getActivity()));
                        }
                    });
                    builder.setNegativeButton("Cancel", (DialogInterface.OnClickListener) null);
                    builder.show();
                }
            });
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.filesList.size();
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
    }
}
