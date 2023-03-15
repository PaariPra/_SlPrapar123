package com.chettapps.videoeditor.videocutermerger.activities;


import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.chettapps.videoeditor.R;
import com.chettapps.videoeditor.videocutermerger.EPreferences;
import com.chettapps.videoeditor.videocutermerger.adapters.MyListAdapter;
import com.chettapps.videoeditor.videocutermerger.adapters.clickinterface;
import com.chettapps.videoeditor.videocutermerger.utility.Utility;
import com.chettapps.videoeditor.videocutermerger.utils.FileUtils2;


import com.chettapps.videoeditor.videocutermerger.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;

public class AllJoinVideoe extends AppCompatActivity {
    private ArrayList<ModelVideo> videosList = new ArrayList<>();

    private RecyclerView rv_allvideo;
    private MyListAdapter myListAdapter;

    private ConstraintLayout cl_allmarge;
    private ArrayList<ModelVideo> video_list = new ArrayList<>();
    String output_video_file = "";
    public String MEDIA_FILE_PATH = null;
    MergeVideo mergeVideo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_join_videoe);

        initviews();
        loadVideos();
        initdata();


    }

    private void initviews() {

        rv_allvideo = findViewById(R.id.rv_allvideo);
        cl_allmarge = findViewById(R.id.cl_allmarge);


        cl_allmarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveMp4Dialog();


            }
        });


    }


    private void initdata() {

        myListAdapter = new MyListAdapter(videosList, AllJoinVideoe.this, new clickinterface() {
            @Override
            public void onclick(String data) {

            }

            @Override
            public void onclick2(ArrayList<ModelVideo> data) {
                video_list = data;

                if (data.size() > 0) {

                    cl_allmarge.setVisibility(View.VISIBLE);


                } else {
                    cl_allmarge.setVisibility(View.GONE);


                }


            }
        }, 1);

        rv_allvideo.setLayoutManager(new LinearLayoutManager(AllJoinVideoe.this));
        rv_allvideo.setAdapter(myListAdapter);


    }

    public void saveMp4Dialog() {
        final Dialog d = new Dialog(this);
        d.requestWindowFeature(1);
        d.setContentView(R.layout.dialog_save);
        d.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());
        lp.width = -1;
        lp.height = -2;
        d.show();
        d.getWindow().setAttributes(lp);
        d.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return false;
            }
        });


        ((TextView) d.findViewById(R.id.clip_details)).setText(String.valueOf(this.video_list.size()) + " mp4 files will be merged.");


        ImageView save = (ImageView) d.findViewById(R.id.save);
        ((ImageView) d.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setVisibility(View.INVISIBLE);
                Handler handler = new Handler();
                final Dialog dialog = d;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    }
                }, 100L);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setVisibility(View.INVISIBLE);
                Handler handler = new Handler();
                final Dialog dialog = d;


                handler.postDelayed(new Runnable() {
                    @Override
                    @SuppressLint({"InflateParams"})
                    public void run() {
                        v.setVisibility(View.VISIBLE);
                        String FILE_NAME = ((TextView) dialog.findViewById(R.id.name)).getText().toString();
                        if (FILE_NAME.length() == 0) {
                            show("Input A Video Name!");
                        } else if (FILE_NAME.contains("/") || FILE_NAME.contains("\"")) {
                            show("Invalid name! Cannot contain '/' or '' characters.");
                        } else {


                            output_video_file = getFileName(FILE_NAME, ".mp4");

                            MEDIA_FILE_PATH = output_video_file;
                            RequestPermission();
                            dialog.dismiss();


                        }
                    }
                }, 100L);


            }
        });
    }


    public void previewDialog(String video_path) {
        Intent i = new Intent(this, Video_Play_Activity.class);
        i.putExtra("android.intent.extra.TEXT", video_path);
        i.putExtra("title", "");
        i.putExtra("videoact", 2);
        startActivity(i);
        finish();
    }


    public ArrayList<ModelVideo> reverseVideoList(ArrayList<ModelVideo> file_names) {
        ArrayList<ModelVideo> new_file_names = new ArrayList<>();
        int count = file_names.size() - 1;
        for (int i = 0; i < file_names.size(); i++) {
            new_file_names.add(file_names.get(count));
            count--;
        }
        return new_file_names;
    }


    public class MergeVideo extends AsyncTask<Void, Void, Boolean> {

        String output_video_file;
        private ProgressDialog progressDialog;

        MergeVideo(AllJoinVideoe x0, String x1) {
            this(x1);
        }



        private MergeVideo(String output_video_file)
        {

            this.output_video_file = "";
            this.output_video_file = output_video_file;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            this.progressDialog = new ProgressDialog(AllJoinVideoe.this);
            this.progressDialog.setProgressStyle(1);
            this.progressDialog.setMessage("Merging video Please wait...");
            this.progressDialog.setMax(100);
            this.progressDialog.setProgress(0);
            this.progressDialog.setCancelable(false);
            this.progressDialog.setButton(-1, Html.fromHtml("Stop!"), new DialogInterface.OnClickListener()
            {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    mergeVideo.cancel(true);
                    dialog.dismiss();


                }
            });
            this.progressDialog.show();
        }


        public Boolean doInBackground(Void... params) {
            String LOG_PATH = FileUtils.JOIN_VIDEO_DIRECTORY.getAbsolutePath() + "/vk.log";
            File f = new File(LOG_PATH);
            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return Boolean.valueOf(mergeVideo());
        }


        public void onPostExecute(Boolean result) {


        }

        private boolean mergeVideo() {
            try {


                String output = FileUtils.JOIN_VIDEO_DIRECTORY.getAbsolutePath() + "/" + this.output_video_file;
                int pos = EPreferences.getInstance(AllJoinVideoe.this.getApplicationContext()).getInt(EPreferences.PREF_KEY_VIDEO_QUALITY, 2);
                ArrayList<String> command_builder = new ArrayList<>();


                command_builder.add("-y");


                ArrayList<ModelVideo> reverse_video_list = reverseVideoList(video_list);


                for (int i = 0; i < reverse_video_list.size(); i++) {
                    command_builder.add("-i");

                    String data = FileUtils2.getPath(AllJoinVideoe.this, reverse_video_list.get(i).getData());
                    command_builder.add(data);

                }
                int n = video_list.size();


                command_builder.add("-filter_complex");

                command_builder.add("[0:v]scale=iw*min(1920/iw\\,1080/ih):ih*min(1920/iw\\,1080/ih), " +
                        "pad=1920:1080:(1920-iw*min(1920/iw\\,1080/ih))/2:(1080-ih*min(1920/iw\\,1080/ih))/2,setsar=1:1[v0];[1:v]" +
                        " scale=iw*min(1920/iw\\,1080/ih):ih*min(1920/iw\\,1080/ih), " +
                        "pad=1920:1080:(1920-iw*min(1920/iw\\,1080/ih))/2:(1080-ih*min(1920/iw\\,1080/ih))/2," +
                        "setsar=1:1[v1];[v0][0:a][v1][1:a] concat=n=" + n + ":v=1:a=1");




                /*   command_builder.add("[0:v]scale=1280x720,setsar=1:1[v0];[1:v]scale=1280x720,setsar=1:1[v1];[v0][0:a][v1][1:a] concat=n=" + n + ":v=1:a=1");
                 */


/*

                command_builder.add("-c");
                command_builder.add("copy");
*/


//
//                command_builder.add("-vcodec");
//                command_builder.add("mpeg4");


                command_builder.add(output);


                long executionId = FFmpeg.executeAsync((String[]) command_builder.toArray(new String[command_builder.size()]), new ExecuteCallback() {
                    @Override
                    public void apply(final long executionId, final int returnCode) {
                        if (returnCode == RETURN_CODE_SUCCESS) {

                            Utility.updateMediaDB(getApplicationContext(), output);
                            progressDialog.dismiss();
                            AllJoinVideoe.this.previewDialog(FileUtils.JOIN_VIDEO_DIRECTORY.getAbsolutePath() + "/" + output_video_file);
                            Utility.clearUpLogFiles();






                        } else if (returnCode == RETURN_CODE_CANCEL) {
                            Log.e("TAG", "Async command execution cancelled by user.");
                        } else {
                            Log.e("TAG", String.format("Async command execution failed with returnCode=%d.", returnCode));
                        }
                    }
                });


                return true;
            } catch (Exception e2) {
                e2.printStackTrace();
                return false;
            }
        }
    }


    @SuppressLint({"NewApi"})
    public void RequestPermission() {
        if (Build.VERSION.SDK_INT < 23) {
            saveMP4();
        } else if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            saveMP4();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {

        } else {
            String[] strArr = {"android.permission.WRITE_EXTERNAL_STORAGE"};
            requestPermissions(strArr, 19);
        }
    }


    public void saveMP4() {

        this.mergeVideo = (MergeVideo) new MergeVideo(this, this.output_video_file).execute(new Void[0]);


    }


    public String getFileName(String user_choice, String extension) {
        Log.d("bbbbb..Video join", "######## user_choice....... ::" + user_choice);
        String file_name = user_choice;
        int count = 1;
        while (new File(FileUtils.JOIN_VIDEO_DIRECTORY.getAbsolutePath() + file_name + extension).exists()) {
            file_name = String.valueOf(user_choice) + "(" + count + ")";
            Log.d("bbbbb..Video join", "######## file_name....... ::" + file_name);
            count++;
        }
        return String.valueOf(file_name) + extension;
    }


    protected void show(String message) {
        new AlertDialog.Builder(this).setMessage(message).setNegativeButton("Okay", new DialogInterface.OnClickListener() {

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
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
                sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", data));
                videosList.add(new ModelVideo(id, data, title, duration));


            }
        }


        Log.e("TAG", "loadVideos: " + videosList.size());


    }

}