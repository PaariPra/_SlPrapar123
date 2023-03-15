package com.chettapps.videoeditor.videocutermerger.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.chettapps.videoeditor.R;
import com.chettapps.videoeditor.videocutermerger.MyApplication;
import com.chettapps.videoeditor.videocutermerger.activities.SlideShow_Video_Activity;
import com.chettapps.videoeditor.videocutermerger.utils.FileUtils;


import java.io.FileOutputStream;

/* loaded from: classes.dex */
public class Adapter_Frame extends RecyclerView.Adapter<Adapter_Frame.Holder> {
    SlideShow_Video_Activity activity;
    private LayoutInflater inflater;
    private int[] drawable = {R.drawable.ic_frame_none, R.drawable.f1, R.drawable.f2, R.drawable.f3, R.drawable.f4,
            R.drawable.f5, R.drawable.f6, R.drawable.f7, R.drawable.f8, R.drawable.f9, R.drawable.f10,
            R.drawable.filter1, R.drawable.filter2, R.drawable.filter3, R.drawable.filter4,
            R.drawable.filter5, R.drawable.filter6, R.drawable.filter7, R.drawable.filter8,
            R.drawable.filter9, R.drawable.filter10, R.drawable.filter11, R.drawable.filter12,
            R.drawable.filter13, R.drawable.filter14, R.drawable.filter15, R.drawable.filter16,
            R.drawable.filter17, R.drawable.filter18, R.drawable.filter19, R.drawable.filter20,
            R.drawable.filter21, R.drawable.filter22, R.drawable.filter23, R.drawable.filter24,
            R.drawable.filter25, R.drawable.filter26, R.drawable.filter27, R.drawable.filter28,
            R.drawable.filter29, R.drawable.filter30, R.drawable.filter31, R.drawable.filter32,
            R.drawable.filter33, R.drawable.filter34, R.drawable.filter35, R.drawable.filter36,
            R.drawable.filter37, R.drawable.filter38, R.drawable.filter39, R.drawable.filter40,
            R.drawable.filter41, R.drawable.filter42, R.drawable.filter43, R.drawable.filter44,
            R.drawable.filter45, R.drawable.filter46, R.drawable.filter47, R.drawable.filter48,
            R.drawable.filter49, R.drawable.filter50, R.drawable.filter51, R.drawable.filter52,
            R.drawable.filter53, R.drawable.filter54, R.drawable.filter55, R.drawable.filter56,
            R.drawable.filter57, R.drawable.filter58, R.drawable.filter59, R.drawable.filter60,
            R.drawable.filter61, R.drawable.filter62, R.drawable.filter63, R.drawable.filter64,
            R.drawable.filter65, R.drawable.filter66, R.drawable.filter67, R.drawable.filter68,
            R.drawable.filter69, R.drawable.filter70, R.drawable.filter71, R.drawable.filter72,
            R.drawable.filter73, R.drawable.filter74, R.drawable.filter75, R.drawable.filter76,
            R.drawable.filter77, R.drawable.filter78, R.drawable.filter79, R.drawable.filter80,
            R.drawable.filter81, R.drawable.filter82, R.drawable.filter83, R.drawable.filter84,
            R.drawable.filter85, R.drawable.filter86, R.drawable.filter87, R.drawable.filter88,
            R.drawable.filter89, R.drawable.filter90, R.drawable.filter91, R.drawable.filter92,
            R.drawable.filter93, R.drawable.filter94, R.drawable.filter95, R.drawable.filter96,
            R.drawable.filter97, R.drawable.filter98, R.drawable.filter99, R.drawable.filter100,
            R.drawable.filter101};
    int lastPos = 0;
    private MyApplication application = MyApplication.getInstance();

    /* loaded from: classes.dex */
    public class Holder extends RecyclerView.ViewHolder {
        private View clickableView;
        private ImageView ivThumb;
        private View mainView;

        public Holder(View v) {
            super(v);
            this.ivThumb = (ImageView) v.findViewById(R.id.ivThumb);
            this.clickableView = v.findViewById(R.id.clickableView);
            this.mainView = v;
        }
    }

    public Adapter_Frame(SlideShow_Video_Activity activity) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity);
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.drawable.length;
    }

    public int getItem(int pos) {
        return this.drawable[pos];
    }

    public void onBindViewHolder(Holder holder, final int pos) {
        final int themes;
        if (pos == 0) {
            themes = R.drawable.ic_trans;
        } else {
            themes = getItem(pos);
        }
        holder.ivThumb.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(application).load(Integer.valueOf(this.drawable[pos])).into(holder.ivThumb);
        if (this.activity.getFrame() == 0 && pos == 0) {
            holder.ivThumb.setBackgroundResource(R.drawable.selectimages_box);
        } else if (themes == this.activity.getFrame()) {
            holder.ivThumb.setBackgroundResource(R.drawable.selectimages_box);
        } else {
            holder.ivThumb.setBackgroundResource(0);
        }
        holder.clickableView.setOnClickListener(new View.OnClickListener() { // from class: com.chettapps.videoeditor.videocutermerger.adapters.Adapter_Frame.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (themes != Adapter_Frame.this.activity.getFrame()) {
                    Adapter_Frame.this.activity.setFrame(themes);
                    if (themes != -1) {
                        Adapter_Frame.this.notifyItemChanged(Adapter_Frame.this.lastPos);
                        Adapter_Frame.this.notifyItemChanged(pos);
                        Adapter_Frame.this.lastPos = pos;
                        FileUtils.deleteFile(FileUtils.frameFile);
                        try {
                            Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(Adapter_Frame.this.activity.getResources(), themes), MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, false);
                            FileOutputStream outStream = new FileOutputStream(FileUtils.frameFile);
                            bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                            outStream.flush();
                            outStream.close();
                            bm.recycle();
                            System.gc();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        });
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public Holder onCreateViewHolder(ViewGroup parent, int pos) {
        return new Holder(this.inflater.inflate(R.layout.items_list_frame, parent, false));
    }
}
