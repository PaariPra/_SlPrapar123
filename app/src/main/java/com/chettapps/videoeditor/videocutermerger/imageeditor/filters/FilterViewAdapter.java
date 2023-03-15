package com.chettapps.videoeditor.videocutermerger.imageeditor.filters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.chettapps.videoeditor.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import ja.burhanrashid52.photoeditor.PhotoFilter;

/* loaded from: classes.dex */
public class FilterViewAdapter extends RecyclerView.Adapter<FilterViewAdapter.ViewHolder> {
    private FilterListener mFilterListener;
    private List<Pair<String, PhotoFilter>> mPairList = new ArrayList();

    public FilterViewAdapter(FilterListener filterListener) {
        this.mFilterListener = filterListener;
        setupFilters();
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_filter_view, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pair<String, PhotoFilter> filterPair = this.mPairList.get(position);
        Bitmap fromAsset = getBitmapFromAsset(holder.itemView.getContext(), (String) filterPair.first);
        holder.mImageFilterView.setImageBitmap(fromAsset);
        holder.mTxtFilterName.setText(((PhotoFilter) filterPair.second).name().replace("_", " "));
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mPairList.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageFilterView;
        TextView mTxtFilterName;

        ViewHolder(View itemView) {
            super(itemView);
            this.mImageFilterView = (ImageView) itemView.findViewById(R.id.imgFilterView);
            this.mTxtFilterName = (TextView) itemView.findViewById(R.id.txtFilterName);
            itemView.setOnClickListener(new View.OnClickListener() { // from class: com.chettapps.videoeditor.videocutermerger.imageeditor.filters.FilterViewAdapter.ViewHolder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    FilterViewAdapter.this.mFilterListener.onFilterSelected((PhotoFilter) ((Pair) FilterViewAdapter.this.mPairList.get(ViewHolder.this.getLayoutPosition())).second);
                }
            });
        }
    }

    private Bitmap getBitmapFromAsset(Context context, String strName) {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream istr = assetManager.open(strName);
            return BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setupFilters() {
        this.mPairList.add(new Pair<>("filters/original.jpg", PhotoFilter.NONE));
        this.mPairList.add(new Pair<>("filters/auto_fix.png", PhotoFilter.AUTO_FIX));
        this.mPairList.add(new Pair<>("filters/brightness.png", PhotoFilter.BRIGHTNESS));
        this.mPairList.add(new Pair<>("filters/contrast.png", PhotoFilter.CONTRAST));
        this.mPairList.add(new Pair<>("filters/documentary.png", PhotoFilter.DOCUMENTARY));
        this.mPairList.add(new Pair<>("filters/dual_tone.png", PhotoFilter.DUE_TONE));
        this.mPairList.add(new Pair<>("filters/fill_light.png", PhotoFilter.FILL_LIGHT));
        this.mPairList.add(new Pair<>("filters/fish_eye.png", PhotoFilter.FISH_EYE));
        this.mPairList.add(new Pair<>("filters/grain.png", PhotoFilter.GRAIN));
        this.mPairList.add(new Pair<>("filters/gray_scale.png", PhotoFilter.GRAY_SCALE));
        this.mPairList.add(new Pair<>("filters/lomish.png", PhotoFilter.LOMISH));
        this.mPairList.add(new Pair<>("filters/negative.png", PhotoFilter.NEGATIVE));
        this.mPairList.add(new Pair<>("filters/posterize.png", PhotoFilter.POSTERIZE));
        this.mPairList.add(new Pair<>("filters/saturate.png", PhotoFilter.SATURATE));
        this.mPairList.add(new Pair<>("filters/sepia.png", PhotoFilter.SEPIA));
        this.mPairList.add(new Pair<>("filters/sharpen.png", PhotoFilter.SHARPEN));
        this.mPairList.add(new Pair<>("filters/temprature.png", PhotoFilter.TEMPERATURE));
        this.mPairList.add(new Pair<>("filters/tint.png", PhotoFilter.TINT));
        this.mPairList.add(new Pair<>("filters/vignette.png", PhotoFilter.VIGNETTE));
        this.mPairList.add(new Pair<>("filters/cross_process.png", PhotoFilter.CROSS_PROCESS));
        this.mPairList.add(new Pair<>("filters/b_n_w.png", PhotoFilter.BLACK_WHITE));
        this.mPairList.add(new Pair<>("filters/flip_horizental.png", PhotoFilter.FLIP_HORIZONTAL));
        this.mPairList.add(new Pair<>("filters/flip_vertical.png", PhotoFilter.FLIP_VERTICAL));
        this.mPairList.add(new Pair<>("filters/rotate.png", PhotoFilter.ROTATE));
    }
}
