package com.chettapps.videoeditor.videocutermerger.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chettapps.videoeditor.R;
import com.chettapps.videoeditor.videocutermerger.activities.ModelVideo;
import com.chettapps.videoeditor.videocutermerger.utils.FileUtils2;
import com.gowtham.library.utils.CompressOption;
import com.gowtham.library.utils.LogMessage;
import com.gowtham.library.utils.TrimVideo;


import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>
{
    private ArrayList<ModelVideo> listdata;



    private ArrayList<ModelVideo> allselcdetfsta= new ArrayList<>();



    private Activity activity;
    private File file = null;
    private clickinterface clickinterface;
    private int type;


    public MyListAdapter(ArrayList<ModelVideo> listdata, Activity activity, clickinterface clickinterface, int type) {
        this.listdata = listdata;
        this.activity = activity;
        this.clickinterface = clickinterface;
        this.type=type;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.video_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
         ModelVideo myListData = listdata.get(position);
        getRealPathFromURI(activity, myListData.getData());
        file = new File(myListData.getData().toString());
        String data = FileUtils2.getPath(activity, myListData.getData());
        file = new File(data);

        try {
            activity.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file)));
        } catch (Exception e32) {
            e32.printStackTrace();
        }



        if(type==0)
        {
            holder.iv_chck.setVisibility(View.GONE);
        }
        else
        {
            holder.iv_chck.setVisibility(View.VISIBLE);
        }


        if(allselcdetfsta.contains(myListData))
        {
            holder.iv_chck.setImageResource(R.drawable.ic_baseline_check_circle_24);
        }

        else
        {
            holder.iv_chck.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
        }



        holder.tv_name.setText(myListData.getTitle());
        Glide.with(activity).load(myListData.getData()).into(holder.imageView);

        int duration = myListData.getDuration_formatted();


        String duration_formatted;
        int sec = (duration / 1000) % 60;
        int min = (duration / (1000 * 60)) % 60;
        int hrs = duration / (1000 * 60 * 60);

        if (hrs == 0) {
            duration_formatted = String.valueOf(min).concat(":".concat(String.format(Locale.UK, "%02d", sec)));
        } else {
            duration_formatted = String.valueOf(hrs).concat(":".concat(String.format(Locale.UK, "%02d", min).concat(":".concat(String.format(Locale.UK, "%02d", sec)))));
        }

        holder.tv_duration.setText(duration_formatted);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {


                if(type==0)
                {


                    ModelVideo myListData = listdata.get(position);
                    clickinterface.onclick(String.valueOf(myListData.getData()));




                }


                else
                {



                    if(allselcdetfsta.contains(myListData))
                    {
                        allselcdetfsta.remove(myListData);
                        holder.iv_chck.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
                    }


                    else
                    {
                        allselcdetfsta.add(myListData);
                        holder.iv_chck.setImageResource(R.drawable.ic_baseline_check_circle_24);
                    }
                    clickinterface.onclick2(allselcdetfsta);






                }
            }
        });


    }


    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView, iv_chck;
        public TextView tv_duration, tv_name;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.iv_chck = (ImageView) itemView.findViewById(R.id.iv_chck);
            this.tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            this.tv_duration = (TextView) itemView.findViewById(R.id.tv_duration);

        }


    }
}  