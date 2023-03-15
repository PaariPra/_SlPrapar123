package com.chettapps.videoeditor.videocutermerger.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.chettapps.videoeditor.R;
import com.chettapps.videoeditor.videocutermerger.adapters.MyListAdapter;
import com.chettapps.videoeditor.videocutermerger.adapters.clickinterface;
import com.gowtham.library.utils.CompressOption;
import com.gowtham.library.utils.LogMessage;
import com.gowtham.library.utils.TrimVideo;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class GetAllVIdeoActivty extends AppCompatActivity {
    private ArrayList<ModelVideo> videosList = new ArrayList<>();

    private RecyclerView rv_allvideo;
    private MyListAdapter myListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_video_activty);

        initviews();
        loadVideos();
        initdata();
    }

    private void initviews() {

        rv_allvideo = findViewById(R.id.rv_allvideo);
    }

    ActivityResultLauncher<Intent> videoTrimResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK &&
                        result.getData() != null) {
                    Uri uri = Uri.parse(TrimVideo.getTrimmedVideoPath(result.getData()));


                    Intent intent = new Intent(this, Video_Play_Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("KEY", "FromVideoAlbum");
                    intent.putExtra("android.intent.extra.TEXT", TrimVideo.getTrimmedVideoPath(result.getData()));
                    startActivity(intent);




                } else
                    LogMessage.v("videoTrimResultLauncher data is null");
            });


    private void initdata() {

        myListAdapter= new MyListAdapter(videosList, GetAllVIdeoActivty.this, new clickinterface() {
            @Override
            public void onclick(String data) {
                TrimVideo.activity( data)
                        .setCompressOption(new CompressOption())
                        .start(GetAllVIdeoActivty.this, videoTrimResultLauncher);
            }

            @Override
            public void onclick2(ArrayList<ModelVideo> data) {

            }
        },0);
        rv_allvideo.setLayoutManager(new LinearLayoutManager(GetAllVIdeoActivty.this));
        rv_allvideo.setAdapter(myListAdapter);


    }

    private void loadVideos() {
        String[] projection = {MediaStore.Video.Media._ID, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DURATION};
        String sortOrder = MediaStore.Video.Media.DATE_ADDED + " DESC";
        Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection,
                null,
                null,
                null);

        if (cursor != null) {
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColumn);
                String title = cursor.getString(titleColumn);
                int duration = cursor.getInt(durationColumn);

                Uri data = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);


                sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE",data));









                videosList.add(new ModelVideo(id, data, title, duration));


            }
        }


        Log.e("TAG", "loadVideos: " + videosList.size());


    }

}