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
import com.chettapps.videoeditor.videocutermerger .adapters.Adapter_Album;
import com.chettapps.videoeditor.videocutermerger .objects.Album;
import com.chettapps.videoeditor.videocutermerger .objects.Image;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class Fragment_Album extends Fragment {
    private Adapter_Album adapterAlbum;
    private Context mContext;
    private Adapter_Album.OnClickAlbum onClickAlbum;
    private RecyclerView recycleView;
    private ArrayList<Album> arrAlbum = new ArrayList<>();
    private ArrayList<Image> arrImage = new ArrayList<>();
    private ArrayList<String> arrBucketAlbum = new ArrayList<>();

    public void setOnClickAlbum(Adapter_Album.OnClickAlbum onClickAlbum) {
        this.onClickAlbum = onClickAlbum;
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
        View rootView = inflater.inflate(R.layout.fragment_album, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        this.arrAlbum.clear();
        for (int i = 0; i < this.arrImage.size(); i++) {
            getBucket(this.arrImage.get(i).getBucket(), this.arrBucketAlbum);
        }
        for (int i2 = 0; i2 < this.arrBucketAlbum.size(); i2++) {
            Album album = new Album(this.arrBucketAlbum.get(i2), this.arrImage.get(0).getPath(), new ArrayList());
            for (int j = 0; j < this.arrImage.size(); j++) {
                if (this.arrBucketAlbum.get(i2).equals(this.arrImage.get(j).getBucket())) {
                    album.addImage(this.arrImage.get(j));
                }
            }
            this.arrAlbum.add(album);
        }
        this.recycleView = (RecyclerView) rootView.findViewById(R.id.rv_fr_album);
        this.recycleView.setLayoutManager(new GridLayoutManager(this.mContext, 2));
        this.adapterAlbum = new Adapter_Album(this.mContext, this.arrAlbum, this.onClickAlbum);
        this.recycleView.setAdapter(this.adapterAlbum);
    }

    public void getBucket(String bucket, List<String> arrBucket) {
        boolean isAdd = false;
        for (int i = 0; i < arrBucket.size(); i++) {
            if (bucket.equals(arrBucket.get(i))) {
                isAdd = true;
            }
        }
        if (!isAdd) {
            this.arrBucketAlbum.add(bucket);
        }
    }

    public List<Album> getArrAlbum() {
        return this.arrAlbum;
    }
}
