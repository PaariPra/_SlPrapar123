package com.chettapps.videoeditor.videocutermerger.activities;

import static android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;

import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.bumptech.glide.Glide;
import com.chettapps.videoeditor.R;
import com.chettapps.videoeditor.databinding.ActivityPreviewVideoBinding;
import com.chettapps.videoeditor.videocutermerger.imageeditor.DimensionData;
import com.chettapps.videoeditor.videocutermerger.imageeditor.PropertiesBSFragment;
import com.gowtham.library.utils.TrimVideo;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.SaveSettings;
import ja.burhanrashid52.photoeditor.TextStyleBuilder;
import ja.burhanrashid52.photoeditor.ViewType;

public class PreviewVideoActivity extends AppCompatActivity implements OnPhotoEditorListener, PropertiesBSFragment.Properties,
        View.OnClickListener,
        StickerBSFragment.StickerListener {
    public static DimensionData getScaledDimension(DimensionData imgSize, DimensionData
            boundary) {

        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        double new_width = original_width;
        double new_height = original_height;

        Log.d("video_width >> ", original_width + " video_height >> " + original_height);
        Log.d("display_width >> ", bound_width + " display_height >> " + bound_height);

        /*if (original_width > original_height) {

        } else {

        }*/


        //scale width to fit
        new_width = bound_width;
        //scale height to maintain aspect ratio
        new_height = (new_width * original_height) / original_width;

//        new_height = ((double)original_width / (double) bound_width) * bound_height;


        // first check if we need to scale width
      /*  if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }*/
        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }


        return new DimensionData((int) new_width, (int) new_height);
    }



    private ActivityPreviewVideoBinding binding;
    private static final String TAG = PreviewVideoActivity.class.getSimpleName();
    private static final int CAMERA_REQUEST = 52;
    private static final int PICK_REQUEST = 53;
    private PhotoEditor mPhotoEditor;
    private String globalVideoUrl = "";
    private PropertiesBSFragment propertiesBSFragment;
    private StickerBSFragment mStickerBSFragment;
    private MediaPlayer mediaPlayer;
    private String videoPath = "";
    private String imagePath = "";
    private ArrayList<String> exeCmd;

    private String[] newCommand;
    private ProgressDialog progressDialog;

    private int originalDisplayWidth;
    private int originalDisplayHeight;
    private int newCanvasWidth, newCanvasHeight;
    private int DRAW_CANVASW = 0;
    private int DRAW_CANVASH = 0;

    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.start();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_preview_video);
        initViews();
//        Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
//        Glide.with(this).load(getIntent().getStringExtra("DATA")).into(binding.ivImage.getSource());
        Glide.with(this).load(R.drawable.trans).centerCrop().into(binding.ivImage.getSource());

        videoPath = getIntent().getStringExtra("DATA");
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoPath);
        String metaRotation = retriever.extractMetadata(METADATA_KEY_VIDEO_ROTATION);
        int rotation = metaRotation == null ? 0 : Integer.parseInt(metaRotation);
        if (rotation == 90 || rotation == 270) {
            DRAW_CANVASH = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            DRAW_CANVASW = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        } else {
            DRAW_CANVASW = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            DRAW_CANVASH = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        }
        setCanvasAspectRatio();

        binding.videoSurface.getLayoutParams().width = newCanvasWidth;
        binding.videoSurface.getLayoutParams().height = newCanvasHeight;

        binding.ivImage.getLayoutParams().width = newCanvasWidth;
        binding.ivImage.getLayoutParams().height = newCanvasHeight;

        Log.d(">>", "width>> " + newCanvasWidth + "height>> " + newCanvasHeight + " rotation >> " + rotation);
    }

    private void initViews() {

        progressDialog = new ProgressDialog(this);
        mStickerBSFragment = new StickerBSFragment();
        mStickerBSFragment.setStickerListener(this);
        propertiesBSFragment = new PropertiesBSFragment();
        propertiesBSFragment.setPropertiesChangeListener(this);
        mPhotoEditor = new PhotoEditor.Builder(this, binding.ivImage)
                .setPinchTextScalable(true) // set flag to make text scalable when pinch
                .setDeleteView(binding.imgDelete)
                //.setDefaultTextTypeface(mTextRobotoTf)
                //.setDefaultEmojiTypeface(mEmojiTypeFace)
                .build(); // build photo editor sdk

        mPhotoEditor.setOnPhotoEditorListener(this);

        binding.imgClose.setOnClickListener(this);
        binding.imgDone.setOnClickListener(this);
        binding.imgDraw.setOnClickListener(this);
        binding.imgText.setOnClickListener(this);
        binding.imgUndo.setOnClickListener(this);
        binding.imgSticker.setOnClickListener(this);

        binding.videoSurface.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
//                activityHomeBinding.videoSurface.getLayoutParams().height=640;
//                activityHomeBinding.videoSurface.getLayoutParams().width=720;
                Surface surface = new Surface(surfaceTexture);

                try {
                    mediaPlayer = new MediaPlayer();
//                    mediaPlayer.setDataSource("http://daily3gp.com/vids/747.3gp");

                    Log.d("VideoPath>>", videoPath);
                    mediaPlayer.setDataSource(videoPath);
                    mediaPlayer.setSurface(surface);
                    mediaPlayer.prepare();
                    mediaPlayer.setOnCompletionListener(onCompletionListener);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.start();
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

            }
        });

        exeCmd = new ArrayList<>();


    }

    public void executeCommand(String[] command, final String absolutePath) {


            long executionId = FFmpeg.executeAsync(command, new ExecuteCallback() {
                        @Override
                        public void apply(final long executionId, final int returnCode) {
                            if (returnCode == RETURN_CODE_SUCCESS) {


                      /*          Toast.makeText(getApplicationContext(), "Sucess", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(PreviewVideoActivity.this, VideoPreviewActivity.class);
                                i.putExtra("DATA", absolutePath);
                                startActivity(i);*/



                                Intent intent = new Intent(PreviewVideoActivity.this, Video_Play_Activity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("KEY", "FromVideoAlbum");
                                intent.putExtra("android.intent.extra.TEXT", absolutePath);
                                startActivity(intent);




                            } else if (returnCode == RETURN_CODE_CANCEL) {
                                Log.e("TAG", "Async command execution cancelled by user.");
                            } else {
                                Log.e("TAG", String.format("Async command execution failed with returnCode=%d.", returnCode));
                            }
                        }
            });




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.imgClose:
                onBackPressed();
                break;


            case R.id.imgDone:
                saveImage();
                break;


            case R.id.imgDraw:
                setDrawingMode();
                break;
            case R.id.imgText:
                TextEditorDialogFragment textEditorDialogFragment = TextEditorDialogFragment.show(this, 0);
                textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor()

                {

                    @Override
                    public void onDone(String inputText, int colorCode, int position) {



                        final TextStyleBuilder styleBuilder = new TextStyleBuilder();
                        styleBuilder.withTextColor(colorCode);
                        Typeface typeface = ResourcesCompat.getFont(PreviewVideoActivity.this,
                                TextEditorDialogFragment.getDefaultFontIds(PreviewVideoActivity.this).get(position));
                        styleBuilder.withTextFont(typeface);
                        mPhotoEditor.addText(typeface,inputText, colorCode);


                    }
                });
                break;
            case R.id.imgUndo:
                Log.d("canvas>>", mPhotoEditor.undo() + "");
                mPhotoEditor.undo();
                break;
            case R.id.imgSticker:
                mStickerBSFragment.show(getSupportFragmentManager(), mStickerBSFragment.getTag());
                break;

        }
    }

    private void setCanvasAspectRatio() {

        originalDisplayHeight = getDisplayHeight();
        originalDisplayWidth = getDisplayWidth();

        DimensionData displayDiamenion = getScaledDimension(new DimensionData((int) DRAW_CANVASW, (int) DRAW_CANVASH),
                        new DimensionData(originalDisplayWidth, originalDisplayHeight));
        newCanvasWidth = displayDiamenion.width;
        newCanvasHeight = displayDiamenion.height;

    }




    private void setDrawingMode() {
        if (mPhotoEditor.getBrushDrawableMode()) {
            mPhotoEditor.setBrushDrawingMode(false);
            binding.imgDraw.setBackgroundColor(ContextCompat.getColor(this, R.color.black_trasp));
        } else {
            mPhotoEditor.setBrushDrawingMode(true);
            binding.imgDraw.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200));
            propertiesBSFragment.show(getSupportFragmentManager(), propertiesBSFragment.getTag());
        }
    }

    @SuppressLint("MissingPermission")
    private void saveImage() {

        File file = new File(Environment.getExternalStorageDirectory()
                + File.separator + ""
                + System.currentTimeMillis() + ".png");
        try {
            file.createNewFile();

            SaveSettings saveSettings = new SaveSettings.Builder()
                    .setClearViewsEnabled(true)
                    .setTransparencyEnabled(false)
                    .build();

            mPhotoEditor.saveAsFile(file.getAbsolutePath(), saveSettings, new PhotoEditor.OnSaveListener() {
                @Override
                public void onSuccess(@NonNull String imagePath) {
                    PreviewVideoActivity.this.imagePath = imagePath;
                    Log.d("imagePath>>", imagePath);
                    Log.d("imagePath2>>", Uri.fromFile(new File(imagePath)).toString());
                    binding.ivImage.getSource().setImageURI(Uri.fromFile(new File(imagePath)));
                    Toast.makeText(PreviewVideoActivity.this, "Saved successfully...", Toast.LENGTH_SHORT).show();
                    applayWaterMark();
                }

                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(PreviewVideoActivity.this, "Saving Failed...", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    private void applayWaterMark() {

//        imagePath = generatePath(Uri.fromFile(new File(imagePath)),PreviewVideoActivity.this);

//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        retriever.setDataSource(videoPath);
//        int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
//        int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        /*if (width > height) {
            int tempWidth = width;
            width = height;
            height = tempWidth;
        }*/

//        Log.d(">>", "width>> " + width + "height>> " + height);
//        retriever.release();

        File output = new File(Environment.getExternalStorageDirectory()
                + File.separator + ""
                + System.currentTimeMillis() + ".mp4");


        try {
            output.createNewFile();

            exeCmd.add("-y");
            exeCmd.add("-i");
            exeCmd.add(videoPath);
            exeCmd.add("-i");
            exeCmd.add(imagePath);
            exeCmd.add("-filter_complex");


            exeCmd.add("[1:v]scale=" + DRAW_CANVASW + ":" + DRAW_CANVASH + "[ovrl];[0:v][ovrl]overlay=x=0:y=0");
            exeCmd.add("-s");
            exeCmd.add("1280x720");




            exeCmd.add("-preset");
            exeCmd.add("ultrafast");
            exeCmd.add(output.getAbsolutePath());


            newCommand = new String[exeCmd.size()];
            for (int j = 0; j < exeCmd.size(); j++) {
                newCommand[j] = exeCmd.get(j);
            }


            for (int k = 0; k < newCommand.length; k++) {
                Log.d("CMD==>>", newCommand[k] + "");
            }

//            newCommand = new String[]{"-i", videoPath, "-i", imagePath, "-preset", "ultrafast", "-filter_complex",
//            "[1:v]scale=2*trunc(" + (width / 2) + "):2*trunc(" + (height/ 2) + ") [ovrl], [0:v][ovrl]overlay=0:0" , output.getAbsolutePath()};





            executeCommand(newCommand, output.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onStickerClick(Bitmap bitmap) {
        mPhotoEditor.setBrushDrawingMode(false);
        binding.imgDraw.setBackgroundColor(ContextCompat.getColor(this, R.color.black_trasp));
        mPhotoEditor.addImage(bitmap);
    }


    public String generatePath(Uri uri, Context context) {
        String filePath = null;
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat) {
            filePath = generateFromKitkat(uri, context);
        }

        if (filePath != null) {
            return filePath;
        }

        Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.MediaColumns.DATA}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        return filePath == null ? uri.getPath() : filePath;
    }

    @TargetApi(19)
    private String generateFromKitkat(Uri uri, Context context) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            String wholeID = DocumentsContract.getDocumentId(uri);

            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Video.Media.DATA};
            String sel = MediaStore.Video.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().
                    query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{id}, null);


            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }

            cursor.close();
        }
        return filePath;
    }

    private int getDisplayWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    private int getDisplayHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {
        Log.d(TAG, "onAddViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {
        Log.d(TAG, "onRemoveViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStartViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStopViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @Override
    public void onColorChanged(int colorCode) {
        mPhotoEditor.setBrushColor(colorCode);
    }

    @Override
    public void onOpacityChanged(int opacity) {

    }

    @Override
    public void onBrushSizeChanged(int brushSize) {

    }

    @Override
    public void onEditTextChangeListener(@Nullable View rootView, @Nullable String text, int colorCode) {

    }

    @Override
    public void onTouchSourceImage(@Nullable MotionEvent event) {

    }


//
//    @Override
//    public void onEditTextChangeListener(final View rootView, String text, int colorCode, final int position) {
//        TextEditorDialogFragment textEditorDialogFragment =
//                TextEditorDialogFragment.show(this, text, colorCode, position);
//        textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
//            @Override
//            public void onDone(String inputText, int colorCode, int position) {
//                final TextStyleBuilder styleBuilder = new TextStyleBuilder();
//                styleBuilder.withTextColor(colorCode);
//                Typeface typeface = ResourcesCompat.getFont(PreviewVideoActivity.this, TextEditorDialogFragment.getDefaultFontIds(PreviewVideoActivity.this).get(position));
//                styleBuilder.withTextFont(typeface);
//                mPhotoEditor.editText(rootView, inputText, styleBuilder, position);
//            }
//        });
//    }
//    @Override
//    public void onEditTextChangeListener(@Nullable View rootView, @Nullable String text, int colorCode) {
//
//
//
//
//    }
//
//    @Override
//    public void onTouchSourceImage(@Nullable MotionEvent event) {
//
//    }
}
