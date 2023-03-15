package com.chettapps.videoeditor.videocutermerger.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chettapps.videoeditor.R;
import com.chettapps.videoeditor.videocutermerger.adapters.Adapter_Image;
import com.chettapps.videoeditor.videocutermerger.objects.Image;

import java.util.ArrayList;

/* loaded from: classes.dex */
public class Fragment_Image extends Fragment {
    private Adapter_Image adapterImage;
    private ArrayList<Image> arrImage = new ArrayList<>();
    private Context mContext;
    private Adapter_Image.OnClickImage onClickImage;
    private RecyclerView recycleView;

    public void setOnClickImage(Adapter_Image.OnClickImage onClickImage) {
        this.onClickImage = onClickImage;
    }

    @Override // android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity().getBaseContext();
    }

    public void setImagesAlbum(ArrayList<Image> arrImage) {
        this.arrImage = arrImage;
    }

    @Override // android.support.v4.app.Fragment
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        this.recycleView = (RecyclerView) rootView.findViewById(R.id.rv_fr_image);
        this.recycleView.setLayoutManager(new GridLayoutManager(this.mContext, 3));
        this.adapterImage = new Adapter_Image(this.arrImage, this.mContext, this.onClickImage);
        this.recycleView.setAdapter(this.adapterImage);
    }

    public void notifiData(ArrayList<Image> arrImage) {
        this.arrImage = arrImage;
        this.adapterImage.notifyDataSetChanged();
    }
}
