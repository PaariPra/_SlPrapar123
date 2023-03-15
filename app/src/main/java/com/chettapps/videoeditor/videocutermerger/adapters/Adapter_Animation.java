package com.chettapps.videoeditor.videocutermerger.adapters;

import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.chettapps.videoeditor.R;
import com.chettapps.videoeditor.videocutermerger .ImageCreatorService;
import com.chettapps.videoeditor.videocutermerger .MyApplication;
import com.chettapps.videoeditor.videocutermerger .THEMES;
import com.chettapps.videoeditor.videocutermerger .activities.SlideShow_Video_Activity;
import com.chettapps.videoeditor.videocutermerger.utils.FileUtils;


import java.util.ArrayList;
import java.util.Arrays;

/* loaded from: classes.dex */
public class Adapter_Animation extends RecyclerView.Adapter<Adapter_Animation.Holder> {
    private LayoutInflater inflater;
    private SlideShow_Video_Activity slideShow_video_activity;
    private MyApplication application = MyApplication.getInstance();



    private int[] drawable = {
            R.drawable.t1,
            R.drawable.t2, R.drawable.t3, R.drawable.t4,
            R.drawable.t5, R.drawable.t6, R.drawable.t7,
            R.drawable.t8, R.drawable.t9, R.drawable.t10,
            R.drawable.t11, R.drawable.t12, R.drawable.t13,
            R.drawable.t14, R.drawable.t15, R.drawable.t16,
            R.drawable.t17, R.drawable.t18,
            R.drawable.t19,

            R.drawable.t19

    };



    private ArrayList<THEMES> list = new ArrayList<>(Arrays.asList(THEMES.values()));







    /* loaded from: classes.dex */
    public class Holder extends RecyclerView.ViewHolder {
        private View clickableView;
        private ImageView ivThumb;

        public Holder(View v) {
            super(v);
            this.ivThumb = (ImageView) v.findViewById(R.id.ivThumb);
            this.clickableView = v.findViewById(R.id.clickableView);
        }
    }

    public Adapter_Animation(SlideShow_Video_Activity SlideShow_Video_Activity) {
        this.slideShow_video_activity = SlideShow_Video_Activity;
        this.inflater = LayoutInflater.from(SlideShow_Video_Activity);
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public Holder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
        Log.d("bbbbb....MovieAdp....", ".................onCreateViewHolder...............");
        return new Holder(this.inflater.inflate(R.layout.items_list_animation, paramViewGroup, false));
    }

    public void onBindViewHolder(Holder holder, final int pos) {
        THEMES themes = this.list.get(pos);
        Glide.with(this.application).load(Integer.valueOf(this.drawable[pos])).into(holder.ivThumb);
        if (themes == this.application.selectedTheme) {
            Log.d("bbbbb....MovieAdp....", "...........******....*..if..............." + this.application.selectedTheme);
            Log.d("bbbbb....MovieAdp....", "...........******....*..if............... :: " + themes);
            holder.ivThumb.setBackgroundResource(R.drawable.selectimages_box);
        } else {
            Log.d("bbbbb....MovieAdp....", "...........******....*..else...............");
            holder.ivThumb.setBackgroundResource(0);
        }
        holder.clickableView.setOnClickListener(new View.OnClickListener() { // from class: com.chettapps.videoeditor.videocutermerger .adapters.Adapter_Animation.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Log.d("bbbbb....MovieAdp....", "...........******......clickableView...............");
                Log.d("bbbbb....MovieAdp....", "...........******......Adapter_Animation.this.list.get(pos)..............." + Adapter_Animation.this.list.get(pos));
                Log.d("bbbbb....MovieAdp....", "...........******......list.get(pos)..............." + Adapter_Animation.this.list.get(pos));



                if (Adapter_Animation.this.list.get(pos) != Adapter_Animation.this.application.selectedTheme) {


                    Adapter_Animation.this.deleteThemeDir(Adapter_Animation.this.application.selectedTheme.toString());
                    SlideShow_Video_Activity.videoImages.clear();


                    Adapter_Animation.this.application.selectedTheme = (THEMES) Adapter_Animation.this.list.get(pos);


                    Adapter_Animation.this.application.setCurrentTheme(Adapter_Animation.this.application.selectedTheme.toString());
                    Adapter_Animation.this.slideShow_video_activity.reset();
                    Intent intent = new Intent(Adapter_Animation.this.application, ImageCreatorService.class);
                    intent.putExtra(ImageCreatorService.EXTRA_SELECTED_THEME, Adapter_Animation.this.application.getCurrentTheme());
                    Adapter_Animation.this.application.startService(intent);
                    Log.d("bbbbb....MovieAdp....", "...........******......ImageCreatorService.EXTRA_SELECTED_THEME..............selected_theme");
                    Log.d("bbbbb....MovieAdp....", "...........******......clickableView..............." + Adapter_Animation.this.application.getCurrentTheme());
                    Adapter_Animation.this.notifyDataSetChanged();



                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r0v0, types: [com.chettapps.videoeditor.videocutermerger .adapters.Adapter_Animation$2] */
    public void deleteThemeDir(final String dir) {
        new Thread() { // from class: com.chettapps.videoeditor.videocutermerger .adapters.Adapter_Animation.2
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                FileUtils.deleteThemeDir(dir);
            }
        }.start();
    }

    public ArrayList<THEMES> getList() {
        return this.list;
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.list.size();
    }
}
