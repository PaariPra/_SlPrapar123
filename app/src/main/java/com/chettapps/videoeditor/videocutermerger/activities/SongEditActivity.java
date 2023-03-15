package com.chettapps.videoeditor.videocutermerger.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;



import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;




import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.chettapps.videoeditor.R;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.util.MimeTypes;

import com.chettapps.videoeditor.videocutermerger.MusicData;
import com.chettapps.videoeditor.videocutermerger.MyApplication;
import com.chettapps.videoeditor.videocutermerger.music.MarkerView;
import com.chettapps.videoeditor.videocutermerger.music.SeekTest;
import com.chettapps.videoeditor.videocutermerger.music.SongMetadataReader;
import com.chettapps.videoeditor.videocutermerger.music.WaveformView;
import com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile;
import com.chettapps.videoeditor.videocutermerger.utils.FileUtils;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;



/* loaded from: classes.dex */
public class SongEditActivity extends AppCompatActivity implements MarkerView.MarkerListener, WaveformView.WaveformListener {
    public static final String EDIT = "com.ringdroid.action.EDIT";
    public static boolean isFromSdcard = false;
    private MusicAdapter mAdapter;
    private AlertDialog mAlertDialog;
    private String mArtist;
    boolean mCanSeekAccurately;
    private float mDensity;
    private MarkerView mEndMarker;
    private int mEndPos;
    private TextView mEndText;
    private boolean mEndVisible;
    private String mExtension;
    private ImageButton mFfwdButton;
    private File mFile;
    private boolean mFinishActivity;
    private int mFlingVelocity;
    private Handler mHandler;
    private TextView mInfo;
    private String mInfoContent;
    private boolean mIsPlaying;
    private boolean mKeyDown;
    private int mLastDisplayedEndPos;
    private int mLastDisplayedStartPos;
    private Thread mLoadSoundFileThread;
    private boolean mLoadingKeepGoing;
    private long mLoadingLastUpdateTime;
    private int mMarkerBottomOffset;
    private int mMarkerLeftInset;
    private int mMarkerRightInset;
    private int mMarkerTopOffset;
    private int mMaxPos;
    private ArrayList<MusicData> mMusicDatas;
    private RecyclerView mMusicList;
    private int mOffset;
    private int mOffsetGoal;
    private ImageButton mPlayButton;
    private int mPlayEndMsec;
    private int mPlayStartMsec;
    private MediaPlayer mPlayer;
    private ProgressDialog mProgressDialog;
    private Thread mRecordAudioThread;
    private String mRecordingFilename;
    private Uri mRecordingUri;
    private ImageButton mRewindButton;
    private Thread mSaveSoundFileThread;
    private CheapSoundFile mSoundFile;
    private MarkerView mStartMarker;
    private int mStartPos;
    private TextView mStartText;
    private boolean mStartVisible;
    private String mTitle;
    private boolean mTouchDragging;
    private int mTouchInitialEndPos;
    private int mTouchInitialOffset;
    private int mTouchInitialStartPos;
    private float mTouchStart;
    private long mWaveformTouchStartMsec;
    private WaveformView mWaveformView;
    private int mWidth;
    private MusicData selectedMusicData;
    private Toolbar toolbar;
    private AppCompatTextView tvDone;
    private boolean isFromItemClick = false;
    private View.OnClickListener mFfwdListener = new C09714();
    private String mFilename = "record";
    private View.OnClickListener mPlayListener = new C09692();
    private View.OnClickListener mRewindListener = new C09703();
    private TextWatcher mTextWatcher = new C09725();
    private Runnable mTimerRunnable = new C09681();

    /* loaded from: classes.dex */
    class C09681 implements Runnable {
        C09681() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (SongEditActivity.this.mStartPos != SongEditActivity.this.mLastDisplayedStartPos && !SongEditActivity.this.mStartText.hasFocus()) {
                SongEditActivity.this.mStartText.setText(SongEditActivity.this.formatTime(SongEditActivity.this.mStartPos));
                SongEditActivity.this.mLastDisplayedStartPos = SongEditActivity.this.mStartPos;
            }
            if (SongEditActivity.this.mEndPos != SongEditActivity.this.mLastDisplayedEndPos && !SongEditActivity.this.mEndText.hasFocus()) {
                SongEditActivity.this.mEndText.setText(SongEditActivity.this.formatTime(SongEditActivity.this.mEndPos));
                SongEditActivity.this.mLastDisplayedEndPos = SongEditActivity.this.mEndPos;
            }
            SongEditActivity.this.mHandler.postDelayed(SongEditActivity.this.mTimerRunnable, 100L);
        }
    }

    /* loaded from: classes.dex */
    class C09692 implements View.OnClickListener {
        C09692() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View sender) {
            SongEditActivity.this.onPlay(SongEditActivity.this.mStartPos);
        }
    }

    /* loaded from: classes.dex */
    class C09703 implements View.OnClickListener {
        C09703() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View sender) {
            if (SongEditActivity.this.mIsPlaying) {
                int newPos = SongEditActivity.this.mPlayer.getCurrentPosition() - 5000;
                if (newPos < SongEditActivity.this.mPlayStartMsec) {
                    newPos = SongEditActivity.this.mPlayStartMsec;
                }
                SongEditActivity.this.mPlayer.seekTo(newPos);
                return;
            }
            SongEditActivity.this.mStartMarker.requestFocus();
            SongEditActivity.this.markerFocus(SongEditActivity.this.mStartMarker);
        }
    }

    /* loaded from: classes.dex */
    class C09714 implements View.OnClickListener {
        C09714() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View sender) {
            if (SongEditActivity.this.mIsPlaying) {
                int newPos = SongEditActivity.this.mPlayer.getCurrentPosition() + DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS;
                if (newPos > SongEditActivity.this.mPlayEndMsec) {
                    newPos = SongEditActivity.this.mPlayEndMsec;
                }
                SongEditActivity.this.mPlayer.seekTo(newPos);
                return;
            }
            SongEditActivity.this.mEndMarker.requestFocus();
            SongEditActivity.this.markerFocus(SongEditActivity.this.mEndMarker);
        }
    }

    /* loaded from: classes.dex */
    class C09725 implements TextWatcher {
        C09725() {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable s) {
            if (SongEditActivity.this.mStartText.hasFocus()) {
                try {
                    SongEditActivity.this.mStartPos = SongEditActivity.this.mWaveformView.secondsToPixels(Double.parseDouble(SongEditActivity.this.mStartText.getText().toString()));
                    SongEditActivity.this.updateDisplay();
                } catch (NumberFormatException e) {
                }
            }
            if (SongEditActivity.this.mEndText.hasFocus()) {
                try {
                    SongEditActivity.this.mEndPos = SongEditActivity.this.mWaveformView.secondsToPixels(Double.parseDouble(SongEditActivity.this.mEndText.getText().toString()));
                    SongEditActivity.this.updateDisplay();
                } catch (NumberFormatException e2) {
                }
            }
        }
    }

    /* loaded from: classes.dex */
    class C09747 implements View.OnClickListener {
        C09747() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            SongEditActivity.this.onSave();
            MyApplication.getInstance().setMusicData(SongEditActivity.this.selectedMusicData);
            SongEditActivity.isFromSdcard = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class C09758 implements Runnable {
        C09758() {
        }

        @Override // java.lang.Runnable
        public void run() {
            SongEditActivity.this.updateDisplay();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class C09769 implements DialogInterface.OnCancelListener {
        C09769() {
        }

        @Override // android.content.DialogInterface.OnCancelListener
        public void onCancel(DialogInterface dialog) {
            SongEditActivity.this.mLoadingKeepGoing = false;
        }
    }

    /* loaded from: classes.dex */
    public class LoadMusics extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;

        public LoadMusics() {
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            super.onPreExecute();
            this.pDialog = new ProgressDialog(SongEditActivity.this);
            this.pDialog.setTitle("Please wait");
            this.pDialog.setMessage("Loading music...");
            this.pDialog.show();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public Void doInBackground(Void... paramVarArgs) {
            SongEditActivity.this.mMusicDatas = MyApplication.getInstance().getMusicFiles();
            if (SongEditActivity.this.mMusicDatas.size() > 0) {
                SongEditActivity.this.selectedMusicData = (MusicData) SongEditActivity.this.mMusicDatas.get(0);
                SongEditActivity.this.mFilename = SongEditActivity.this.selectedMusicData.getTrack_data();
                return null;
            }
            SongEditActivity.this.mFilename = "record";
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void onPostExecute(Void result) {
            super.onPostExecute( result);
            this.pDialog.dismiss();
            if (!SongEditActivity.this.mFilename.equals("record")) {
                SongEditActivity.this.setUpRecyclerView();
                SongEditActivity.this.loadFromFile();
                SongEditActivity.this.supportInvalidateOptionsMenu();
            } else if (SongEditActivity.this.mMusicDatas.size() > 0) {
                Toast.makeText(SongEditActivity.this.getApplicationContext(), "No Music found in device\nPlease add music in sdCard", Toast.LENGTH_LONG).show();
            }
        }
    }

    /* loaded from: classes.dex */
    public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.Holder> {
        RadioButton mButton;
        private ArrayList<MusicData> musicDatas;
        SparseBooleanArray booleanArray = new SparseBooleanArray();
        int mSelectedChoice = 0;

        /* loaded from: classes.dex */
        public class Holder extends RecyclerView.ViewHolder {
            public CheckBox radioMusicName;

            public Holder(View v) {
                super(v);
                this.radioMusicName = (CheckBox) v.findViewById(R.id.radioMusicName);
            }
        }

        public MusicAdapter(ArrayList<MusicData> mMusicDatas) {
            this.musicDatas = mMusicDatas;
            this.booleanArray.put(0, true);
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public Holder onCreateViewHolder(ViewGroup parent, int paramInt) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.music_list_items, parent, false));
        }

        public void onBindViewHolder(Holder holder, @SuppressLint("RecyclerView") final int pos) {
            holder.radioMusicName.setText(this.musicDatas.get(pos).track_displayName);
            holder.radioMusicName.setChecked(this.booleanArray.get(pos, false));
            holder.radioMusicName.setOnClickListener(new View.OnClickListener() { // from class: com.chettapps.videoeditor.videocutermerger.activities.SongEditActivity.MusicAdapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View arg0) {
                    MusicAdapter.this.booleanArray.clear();
                    MusicAdapter.this.booleanArray.put(pos, true);
                    SongEditActivity.this.onPlay(-1);
                    MusicAdapter.this.playMusic(pos);
                    SongEditActivity.this.isFromItemClick = true;
                    MusicAdapter.this.notifyDataSetChanged();
                }
            });
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.musicDatas.size();
        }

        public void playMusic(int pos) {
            if (this.mSelectedChoice != pos) {
                SongEditActivity.this.selectedMusicData = (MusicData) SongEditActivity.this.mMusicDatas.get(pos);
                SongEditActivity.this.mFilename = SongEditActivity.this.selectedMusicData.getTrack_data();
                SongEditActivity.this.loadFromFile();
            }
            this.mSelectedChoice = pos;
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityApi14, android.app.Activity, android.view.LayoutInflater.Factory2
    public View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    @Override // android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityApi14, android.app.Activity, android.view.LayoutInflater.Factory
    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    public void onCreate(Bundle icicle) {
        Log.v("bbbbb........Ringdroid", "EditActivity OnCreate");
        super.onCreate(icicle);
        this.mPlayer = null;
        this.mIsPlaying = false;
        this.mAlertDialog = null;
        this.mProgressDialog = null;
        this.mLoadSoundFileThread = null;
        this.mRecordAudioThread = null;
        this.mSaveSoundFileThread = null;
        this.mRecordingFilename = null;
        this.mRecordingUri = null;
        this.mKeyDown = false;
        this.mHandler = new Handler();
        loadGui();
        init();
        this.mHandler.postDelayed(this.mTimerRunnable, 100L);
    }

    private void closeThread(Thread thread) {
        if (thread != null && thread.isAlive()) {
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        Log.v("bbbbb........Ringdroid", "EditActivity OnDestroy");
        this.mLoadingKeepGoing = false;
        closeThread(this.mLoadSoundFileThread);
        closeThread(this.mRecordAudioThread);
        closeThread(this.mSaveSoundFileThread);
        this.mLoadSoundFileThread = null;
        this.mRecordAudioThread = null;
        this.mSaveSoundFileThread = null;
        if (this.mProgressDialog != null) {
            this.mProgressDialog.dismiss();
            this.mProgressDialog = null;
        }
        if (this.mAlertDialog != null) {
            this.mAlertDialog.dismiss();
            this.mAlertDialog = null;
        }
        if (this.mPlayer != null) {
            if (this.mPlayer.isPlaying()) {
                this.mPlayer.stop();
            }
            this.mPlayer.release();
            this.mPlayer = null;
        }
        if (this.mRecordingFilename != null) {
            try {
                if (!new File(this.mRecordingFilename).delete()) {
                    showFinalAlert(new Exception(), R.string.delete_tmp_error);
                }
                getContentResolver().delete(this.mRecordingUri, null, null);
            } catch (Exception e) {
                showFinalAlert(e, R.string.delete_tmp_error);
            }
        }
        super.onDestroy();
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        Log.v("bbbbb........Ringdroid", "EditActivity onConfigurationChanged");
        final int saveZoomLevel = this.mWaveformView.getZoomLevel();
        super.onConfigurationChanged(newConfig);
        loadGui();
        this.mHandler.postDelayed(new Runnable() { // from class: com.chettapps.videoeditor.videocutermerger.activities.SongEditActivity.1
            @Override // java.lang.Runnable
            public void run() {
                SongEditActivity.this.mStartMarker.requestFocus();
                SongEditActivity.this.markerFocus(SongEditActivity.this.mStartMarker);
                SongEditActivity.this.mWaveformView.setZoomLevel(saveZoomLevel);
                SongEditActivity.this.mWaveformView.recomputeHeights(SongEditActivity.this.mDensity);
                SongEditActivity.this.updateDisplay();
            }
        }, 500L);
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("bbbbb........Log", "=>" + this.mFilename);
        if (TextUtils.isEmpty(this.mFilename) || !this.mFilename.equals("record")) {
            Log.d("bbbbb........Log", "Inside If");
            getMenuInflater().inflate(R.menu.menu_selection, menu);
            menu.removeItem(R.id.menu_clear);
            this.tvDone = (AppCompatTextView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_done));
            this.tvDone.setText(getString(R.string.done));
            this.tvDone.setTextSize(2, 20.0f);
            this.tvDone.setGravity(17);
            this.tvDone.setPadding(0, 0, (int) getResources().getDimension(R.dimen.dp_4), 0);
            this.tvDone.setTextColor(ContextCompat.getColor(this, R.color.white));
            this.tvDone.setOnClickListener(new C09747());
            return true;
        }
        Log.d("bbbbb........Log", "Inside else");
        menu.clear();
        return true;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override // android.support.v7.app.AppCompatActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 62) {
            return super.onKeyDown(keyCode, event);
        }
        onPlay(this.mStartPos);
        return true;
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.WaveformView.WaveformListener
    public void waveformDraw() {
        this.mWidth = this.mWaveformView.getMeasuredWidth();
        if (this.mOffsetGoal != this.mOffset && !this.mKeyDown) {
            updateDisplay();
        } else if (this.mIsPlaying) {
            updateDisplay();
        } else if (this.mFlingVelocity != 0) {
            updateDisplay();
        }
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.WaveformView.WaveformListener
    public void waveformTouchStart(float x) {
        this.mTouchDragging = true;
        this.mTouchStart = x;
        this.mTouchInitialOffset = this.mOffset;
        this.mFlingVelocity = 0;
        this.mWaveformTouchStartMsec = getCurrentTime();
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.WaveformView.WaveformListener
    public void waveformTouchMove(float x) {
        this.mOffset = trap((int) (this.mTouchInitialOffset + (this.mTouchStart - x)));
        updateDisplay();
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.WaveformView.WaveformListener
    public void waveformTouchEnd() {
        this.mTouchDragging = false;
        this.mOffsetGoal = this.mOffset;
        if (getCurrentTime() - this.mWaveformTouchStartMsec < 300) {
            if (this.mIsPlaying) {
                int seekMsec = this.mWaveformView.pixelsToMillisecs((int) (this.mTouchStart + this.mOffset));
                if (seekMsec < this.mPlayStartMsec || seekMsec >= this.mPlayEndMsec) {
                    handlePause();
                } else {
                    this.mPlayer.seekTo(seekMsec);
                }
            } else {
                onPlay((int) (this.mTouchStart + this.mOffset));
            }
        }
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.WaveformView.WaveformListener
    public void waveformFling(float vx) {
        this.mTouchDragging = false;
        this.mOffsetGoal = this.mOffset;
        this.mFlingVelocity = (int) (-vx);
        updateDisplay();
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.WaveformView.WaveformListener
    public void waveformZoomIn() {
        this.mWaveformView.zoomIn();
        this.mStartPos = this.mWaveformView.getStart();
        this.mEndPos = this.mWaveformView.getEnd();
        this.mMaxPos = this.mWaveformView.maxPos();
        this.mOffset = this.mWaveformView.getOffset();
        this.mOffsetGoal = this.mOffset;
        updateDisplay();
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.WaveformView.WaveformListener
    public void waveformZoomOut() {
        this.mWaveformView.zoomOut();
        this.mStartPos = this.mWaveformView.getStart();
        this.mEndPos = this.mWaveformView.getEnd();
        this.mMaxPos = this.mWaveformView.maxPos();
        this.mOffset = this.mWaveformView.getOffset();
        this.mOffsetGoal = this.mOffset;
        updateDisplay();
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.MarkerView.MarkerListener
    public void markerDraw() {
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.MarkerView.MarkerListener
    public void markerTouchStart(MarkerView marker, float x) {
        this.mTouchDragging = true;
        this.mTouchStart = x;
        this.mTouchInitialStartPos = this.mStartPos;
        this.mTouchInitialEndPos = this.mEndPos;
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.MarkerView.MarkerListener
    public void markerTouchMove(MarkerView marker, float x) {
        float delta = x - this.mTouchStart;
        if (marker == this.mStartMarker) {
            this.mStartPos = trap((int) (this.mTouchInitialStartPos + delta));
            this.mEndPos = trap((int) (this.mTouchInitialEndPos + delta));
        } else {
            this.mEndPos = trap((int) (this.mTouchInitialEndPos + delta));
            if (this.mEndPos < this.mStartPos) {
                this.mEndPos = this.mStartPos;
            }
        }
        updateDisplay();
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.MarkerView.MarkerListener
    public void markerTouchEnd(MarkerView marker) {
        this.mTouchDragging = false;
        if (marker == this.mStartMarker) {
            setOffsetGoalStart();
        } else {
            setOffsetGoalEnd();
        }
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.MarkerView.MarkerListener
    public void markerLeft(MarkerView marker, int velocity) {
        this.mKeyDown = true;
        if (marker == this.mStartMarker) {
            int saveStart = this.mStartPos;
            this.mStartPos = trap(this.mStartPos - velocity);
            this.mEndPos = trap(this.mEndPos - (saveStart - this.mStartPos));
            setOffsetGoalStart();
        }
        if (marker == this.mEndMarker) {
            if (this.mEndPos == this.mStartPos) {
                this.mStartPos = trap(this.mStartPos - velocity);
                this.mEndPos = this.mStartPos;
            } else {
                this.mEndPos = trap(this.mEndPos - velocity);
            }
            setOffsetGoalEnd();
        }
        updateDisplay();
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.MarkerView.MarkerListener
    public void markerRight(MarkerView marker, int velocity) {
        this.mKeyDown = true;
        if (marker == this.mStartMarker) {
            int saveStart = this.mStartPos;
            this.mStartPos += velocity;
            if (this.mStartPos > this.mMaxPos) {
                this.mStartPos = this.mMaxPos;
            }
            this.mEndPos += this.mStartPos - saveStart;
            if (this.mEndPos > this.mMaxPos) {
                this.mEndPos = this.mMaxPos;
            }
            setOffsetGoalStart();
        }
        if (marker == this.mEndMarker) {
            this.mEndPos += velocity;
            if (this.mEndPos > this.mMaxPos) {
                this.mEndPos = this.mMaxPos;
            }
            setOffsetGoalEnd();
        }
        updateDisplay();
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.MarkerView.MarkerListener
    public void markerEnter(MarkerView marker) {
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.MarkerView.MarkerListener
    public void markerKeyUp() {
        this.mKeyDown = false;
        updateDisplay();
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.MarkerView.MarkerListener
    public void markerFocus(MarkerView marker) {
        this.mKeyDown = false;
        if (marker == this.mStartMarker) {
            setOffsetGoalStartNoUpdate();
        } else {
            setOffsetGoalEndNoUpdate();
        }
        this.mHandler.postDelayed(new C09758(), 100L);
    }

    private void bindView() {
        this.mMusicList = (RecyclerView) findViewById(R.id.rvMusicList);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void init() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new LoadMusics().execute(new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setUpRecyclerView() {
        this.mAdapter = new MusicAdapter(this.mMusicDatas);
        this.mMusicList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        this.mMusicList.setItemAnimator(new DefaultItemAnimator());
        this.mMusicList.setAdapter(this.mAdapter);
    }

    private void loadGui() {
        setContentView(R.layout.activity_add_music);
        bindView();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        this.mDensity = metrics.density;
        this.mMarkerLeftInset = (int) (46.0f * this.mDensity);
        this.mMarkerRightInset = (int) (48.0f * this.mDensity);
        this.mMarkerTopOffset = (int) (this.mDensity * 10.0f);
        this.mMarkerBottomOffset = (int) (this.mDensity * 10.0f);
        this.mStartText = (TextView) findViewById(R.id.starttext);
        this.mStartText.addTextChangedListener(this.mTextWatcher);
        this.mEndText = (TextView) findViewById(R.id.endtext);
        this.mEndText.addTextChangedListener(this.mTextWatcher);
        this.mPlayButton = (ImageButton) findViewById(R.id.play);
        this.mPlayButton.setOnClickListener(this.mPlayListener);
        this.mRewindButton = (ImageButton) findViewById(R.id.rew);
        this.mRewindButton.setOnClickListener(this.mRewindListener);
        this.mFfwdButton = (ImageButton) findViewById(R.id.ffwd);
        this.mFfwdButton.setOnClickListener(this.mFfwdListener);
        enableDisableButtons();
        this.mWaveformView = (WaveformView) findViewById(R.id.waveform);
        this.mWaveformView.setListener(this);
        this.mMaxPos = 0;
        this.mLastDisplayedStartPos = -1;
        this.mLastDisplayedEndPos = -1;
        if (this.mSoundFile != null && !this.mWaveformView.hasSoundFile()) {
            this.mWaveformView.setSoundFile(this.mSoundFile);
            this.mWaveformView.recomputeHeights(this.mDensity);
            this.mMaxPos = this.mWaveformView.maxPos();
        }
        this.mStartMarker = (MarkerView) findViewById(R.id.startmarker);
        this.mStartMarker.setListener(this);
        this.mStartMarker.setAlpha(1.0f);
        this.mStartMarker.setFocusable(true);
        this.mStartMarker.setFocusableInTouchMode(true);
        this.mStartVisible = true;
        this.mEndMarker = (MarkerView) findViewById(R.id.endmarker);
        this.mEndMarker.setListener(this);
        this.mEndMarker.setAlpha(1.0f);
        this.mEndMarker.setFocusable(true);
        this.mEndMarker.setFocusableInTouchMode(true);
        this.mEndVisible = true;
        updateDisplay();
    }

    private String getExtensionFromFilename(String filename) {
        return filename.substring(filename.lastIndexOf(46), filename.length());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFatalError(CharSequence errorInternalName, CharSequence errorString, Exception exception) {
        Log.i("bbbbb........Ringdroid", "handleFatalError");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r3v13, types: [com.chettapps.videoeditor.videocutermerger.activities.SongEditActivity$3] */
    public void loadFromFile() {
        this.mFile = new File(this.mFilename);
        this.mExtension = getExtensionFromFilename(this.mFilename);
        SongMetadataReader metadataReader = new SongMetadataReader(this, this.mFilename);
        this.mTitle = metadataReader.mTitle;
        this.mArtist = metadataReader.mArtist;
        String titleLabel = this.mTitle;
        if (this.mArtist != null && this.mArtist.length() > 0) {
            titleLabel = String.valueOf(titleLabel) + " - " + this.mArtist;
        }
        setTitle(titleLabel);
        this.mLoadingLastUpdateTime = getCurrentTime();
        this.mLoadingKeepGoing = true;
        this.mFinishActivity = false;
        this.mProgressDialog = new ProgressDialog(this);
        this.mProgressDialog.setProgressStyle(1);
        this.mProgressDialog.setTitle(R.string.progress_dialog_loading);
        this.mProgressDialog.setCancelable(true);
        this.mProgressDialog.setOnCancelListener(new C09769());
        this.mProgressDialog.show();
        final CheapSoundFile.ProgressListener listener = new CheapSoundFile.ProgressListener() { // from class: com.chettapps.videoeditor.videocutermerger.activities.SongEditActivity.2
            @Override // com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile.ProgressListener
            public boolean reportProgress(double fractionComplete) {
                long now = SongEditActivity.this.getCurrentTime();
                if (now - SongEditActivity.this.mLoadingLastUpdateTime > 100) {
                    SongEditActivity.this.mProgressDialog.setProgress((int) (SongEditActivity.this.mProgressDialog.getMax() * fractionComplete));
                    SongEditActivity.this.mLoadingLastUpdateTime = now;
                }
                return SongEditActivity.this.mLoadingKeepGoing;
            }
        };
        this.mCanSeekAccurately = false;
        new Thread() { // from class: com.chettapps.videoeditor.videocutermerger .activities.SongEditActivity.3
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                SongEditActivity.this.mCanSeekAccurately = SeekTest.CanSeekAccurately(SongEditActivity.this.getPreferences(0));
                System.out.println("Seek test done, creating media player.");
                try {
                    MediaPlayer player = new MediaPlayer();
                    player.setDataSource(SongEditActivity.this.mFile.getAbsolutePath());
                    player.setAudioStreamType(3);
                    player.prepare();
                    SongEditActivity.this.mPlayer = player;
                } catch (IOException e) {
                    SongEditActivity.this.mHandler.post(new Runnable() { // from class: com.chettapps.videoeditor.videocutermerger .activities.SongEditActivity.3.1
                        @Override // java.lang.Runnable
                        public void run() {
                            SongEditActivity.this.handleFatalError("ReadError", SongEditActivity.this.getResources().getText(R.string.read_error), e);
                        }
                    });
                }
            }
        }.start();
        this.mLoadSoundFileThread = new Thread() { // from class: com.photovideomaker.pictovideditor.videocutermerger.activities.SongEditActivity.4

            /* renamed from: com.photovideomaker.pictovideditor.videocutermerger.activities.SongEditActivity$4$C09632 */
            /* loaded from: classes.dex */
            class C09632 implements Runnable {
                C09632() {
                }

                @Override // java.lang.Runnable
                public void run() {
                }
            }

            /* renamed from: com.photovideomaker.pictovideditor.videocutermerger.activities.SongEditActivity$4$C09654 */
            /* loaded from: classes.dex */
            class C09654 implements Runnable {
                C09654() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    SongEditActivity.this.finishOpeningSoundFile();
                }
            }

            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                final String err;
                try {
                    SongEditActivity.this.mSoundFile = CheapSoundFile.create(SongEditActivity.this.mFile.getAbsolutePath(), listener);
                    if (SongEditActivity.this.mSoundFile == null) {
                        SongEditActivity.this.mProgressDialog.dismiss();
                        String[] components = SongEditActivity.this.mFile.getName().toLowerCase().split("\\.");
                        if (components.length < 2) {
                            err = SongEditActivity.this.getResources().getString(R.string.no_extension_error);
                        } else {
                            err = String.valueOf(SongEditActivity.this.getResources().getString(R.string.bad_extension_error)) + " " + components[components.length - 1];
                        }
                        SongEditActivity.this.mHandler.post(new Runnable() { // from class: com.photovideomaker.pictovideditor.videocutermerger.activities.SongEditActivity.4.1
                            @Override // java.lang.Runnable
                            public void run() {
                                SongEditActivity.this.showFinalAlert(new Exception(), err);
                            }
                        });
                        return;
                    }
                    SongEditActivity.this.mProgressDialog.dismiss();
                    if (SongEditActivity.this.mLoadingKeepGoing) {
                        SongEditActivity.this.mHandler.post(new C09654());
                    } else if (SongEditActivity.this.mFinishActivity) {
                        SongEditActivity.this.finish();
                    }
                } catch (Exception e) {
                    SongEditActivity.this.mProgressDialog.dismiss();
                    e.printStackTrace();
                    SongEditActivity.this.mInfoContent = e.toString();
                    SongEditActivity.this.runOnUiThread(new C09632());
                    SongEditActivity.this.mHandler.post(new Runnable() { // from class: com.photovideomaker.pictovideditor.videocutermerger.activities.SongEditActivity.4.2
                        @Override // java.lang.Runnable
                        public void run() {
                            SongEditActivity.this.showFinalAlert(e, SongEditActivity.this.getResources().getText(R.string.read_error));
                        }
                    });
                }
            }
        };
        this.mLoadSoundFileThread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishOpeningSoundFile() {
        this.mWaveformView.setSoundFile(this.mSoundFile);
        this.mWaveformView.recomputeHeights(this.mDensity);
        this.mMaxPos = this.mWaveformView.maxPos();
        this.mLastDisplayedStartPos = -1;
        this.mLastDisplayedEndPos = -1;
        this.mTouchDragging = false;
        this.mOffset = 0;
        this.mOffsetGoal = 0;
        this.mFlingVelocity = 0;
        resetPositions();
        if (this.mEndPos > this.mMaxPos) {
            this.mEndPos = this.mMaxPos;
        }
        updateDisplay();
        if (this.isFromItemClick) {
            onPlay(this.mStartPos);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updateDisplay() {
        int offsetDelta;
        if (this.mIsPlaying) {
            int now = this.mPlayer.getCurrentPosition();
            int frames = this.mWaveformView.millisecsToPixels(now);
            this.mWaveformView.setPlayback(frames);
            setOffsetGoalNoUpdate(frames - (this.mWidth / 2));
            if (now >= this.mPlayEndMsec) {
                handlePause();
            }
        }
        if (!this.mTouchDragging) {
            if (this.mFlingVelocity != 0) {
                int offsetDelta2 = this.mFlingVelocity / 30;
                if (this.mFlingVelocity > 80) {
                    this.mFlingVelocity -= 80;
                } else if (this.mFlingVelocity < -80) {
                    this.mFlingVelocity += 80;
                } else {
                    this.mFlingVelocity = 0;
                }
                this.mOffset += offsetDelta2;
                if (this.mOffset + (this.mWidth / 2) > this.mMaxPos) {
                    this.mOffset = this.mMaxPos - (this.mWidth / 2);
                    this.mFlingVelocity = 0;
                }
                if (this.mOffset < 0) {
                    this.mOffset = 0;
                    this.mFlingVelocity = 0;
                }
                this.mOffsetGoal = this.mOffset;
            } else {
                int offsetDelta3 = this.mOffsetGoal - this.mOffset;
                if (offsetDelta3 > 10) {
                    offsetDelta = offsetDelta3 / 10;
                } else if (offsetDelta3 > 0) {
                    offsetDelta = 1;
                } else if (offsetDelta3 < -10) {
                    offsetDelta = offsetDelta3 / 10;
                } else if (offsetDelta3 < 0) {
                    offsetDelta = -1;
                } else {
                    offsetDelta = 0;
                }
                this.mOffset += offsetDelta;
            }
        }
        this.mWaveformView.setParameters(this.mStartPos, this.mEndPos, this.mOffset);
        this.mWaveformView.invalidate();
        this.mStartMarker.setContentDescription(((Object) getResources().getText(R.string.start_marker)) + " " + formatTime(this.mStartPos));
        this.mEndMarker.setContentDescription(((Object) getResources().getText(R.string.end_marker)) + " " + formatTime(this.mEndPos));
        int startX = (this.mStartPos - this.mOffset) - this.mMarkerLeftInset;
        if (this.mStartMarker.getWidth() + startX < 0) {
            if (this.mStartVisible) {
                this.mStartMarker.setAlpha(0.0f);
                this.mStartVisible = false;
            }
            startX = 0;
        } else if (!this.mStartVisible) {
            this.mHandler.postDelayed(new Runnable() { // from class: com.photovideomaker.pictovideditor.videocutermerger.activities.SongEditActivity.5
                @Override // java.lang.Runnable
                public void run() {
                    SongEditActivity.this.mStartVisible = true;
                    SongEditActivity.this.mStartMarker.setAlpha(1.0f);
                }
            }, 0L);
        }
        int endX = ((this.mEndPos - this.mOffset) - this.mEndMarker.getWidth()) + this.mMarkerRightInset;
        if (this.mEndMarker.getWidth() + endX < 0) {
            if (this.mEndVisible) {
                this.mEndMarker.setAlpha(0.0f);
                this.mEndVisible = false;
            }
            endX = 0;
        } else if (!this.mEndVisible) {
            this.mHandler.postDelayed(new Runnable() { // from class: com.photovideomaker.pictovideditor.videocutermerger.activities.SongEditActivity.6
                @Override // java.lang.Runnable
                public void run() {
                    SongEditActivity.this.mEndVisible = true;
                    SongEditActivity.this.mEndMarker.setAlpha(1.0f);
                }
            }, 0L);
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2, -2);
        params.setMargins(startX, this.mMarkerTopOffset, -this.mStartMarker.getWidth(), -this.mStartMarker.getHeight());
        this.mStartMarker.setLayoutParams(params);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(-2, -2);
        params2.setMargins(endX, (this.mWaveformView.getMeasuredHeight() - this.mEndMarker.getHeight()) - this.mMarkerBottomOffset, -this.mStartMarker.getWidth(), -this.mStartMarker.getHeight());
        this.mEndMarker.setLayoutParams(params2);
    }

    @SuppressLint("ResourceType")
    private void enableDisableButtons() {
        if (this.mIsPlaying) {
            this.mPlayButton.setImageResource(17301539);
            this.mPlayButton.setContentDescription(getResources().getText(R.string.stop));
            return;
        }
        this.mPlayButton.setImageResource(17301540);
        this.mPlayButton.setContentDescription(getResources().getText(R.string.play));
    }

    private void resetPositions() {
        this.mStartPos = this.mWaveformView.secondsToPixels(0.0d);
        this.mEndPos = this.mWaveformView.secondsToPixels(15.0d);
    }

    private int trap(int pos) {
        if (pos < 0) {
            return 0;
        }
        if (pos > this.mMaxPos) {
            return this.mMaxPos;
        }
        return pos;
    }

    private void setOffsetGoalStart() {
        setOffsetGoal(this.mStartPos - (this.mWidth / 2));
    }

    private void setOffsetGoalStartNoUpdate() {
        setOffsetGoalNoUpdate(this.mStartPos - (this.mWidth / 2));
    }

    private void setOffsetGoalEnd() {
        setOffsetGoal(this.mEndPos - (this.mWidth / 2));
    }

    private void setOffsetGoalEndNoUpdate() {
        setOffsetGoalNoUpdate(this.mEndPos - (this.mWidth / 2));
    }

    private void setOffsetGoal(int offset) {
        setOffsetGoalNoUpdate(offset);
        updateDisplay();
    }

    private void setOffsetGoalNoUpdate(int offset) {
        if (!this.mTouchDragging) {
            this.mOffsetGoal = offset;
            if (this.mOffsetGoal + (this.mWidth / 2) > this.mMaxPos) {
                this.mOffsetGoal = this.mMaxPos - (this.mWidth / 2);
            }
            if (this.mOffsetGoal < 0) {
                this.mOffsetGoal = 0;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String formatTime(int pixels) {
        return (this.mWaveformView == null || !this.mWaveformView.isInitialized()) ? "" : formatDecimal(this.mWaveformView.pixelsToSeconds(pixels));
    }

    private String formatDecimal(double x) {
        int xWhole = (int) x;
        int xFrac = (int) ((100.0d * (x - xWhole)) + 0.5d);
        if (xFrac >= 100) {
            xWhole++;
            xFrac -= 100;
            if (xFrac < 10) {
                xFrac *= 10;
            }
        }
        return xFrac < 10 ? String.valueOf(xWhole) + ".0" + xFrac : String.valueOf(xWhole) + "." + xFrac;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handlePause() {
        if (this.mPlayer != null && this.mPlayer.isPlaying()) {
            this.mPlayer.pause();
        }
        this.mWaveformView.setPlayback(-1);
        this.mIsPlaying = false;
        enableDisableButtons();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void onPlay(int startPosition) {
        if (this.mIsPlaying) {
            handlePause();
        } else if (this.mPlayer != null) {
            try {
                this.mPlayStartMsec = this.mWaveformView.pixelsToMillisecs(startPosition);
                if (startPosition < this.mStartPos) {
                    this.mPlayEndMsec = this.mWaveformView.pixelsToMillisecs(this.mStartPos);
                } else if (startPosition > this.mEndPos) {
                    this.mPlayEndMsec = this.mWaveformView.pixelsToMillisecs(this.mMaxPos);
                } else {
                    this.mPlayEndMsec = this.mWaveformView.pixelsToMillisecs(this.mEndPos);
                }
                this.mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.photovideomaker.pictovideditor.videocutermerger.activities.SongEditActivity.7
                    @Override // android.media.MediaPlayer.OnCompletionListener
                    public void onCompletion(MediaPlayer mp) {
                        SongEditActivity.this.handlePause();
                    }
                });
                this.mIsPlaying = true;
                this.mPlayer.seekTo(this.mPlayStartMsec);
                this.mPlayer.start();
                updateDisplay();
                enableDisableButtons();
            } catch (Exception e) {
                showFinalAlert(e, R.string.play_error);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showFinalAlert(Exception e, CharSequence message) {
        CharSequence title;
        if (e != null) {
            Log.e("bbbbb........Ringdroid", "Error: " + ((Object) message));
            Log.e("bbbbb........Ringdroid", getStackTrace(e));
            title = getResources().getText(R.string.alert_title_failure);
            setResult(0, new Intent());
        } else {
            Log.v("bbbbb........Ringdroid", "Success: " + ((Object) message));
            title = getResources().getText(R.string.alert_title_success);
        }
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton(R.string.alert_ok_button, new DialogInterface.OnClickListener() { // from class: com.photovideomaker.pictovideditor.videocutermerger.activities.SongEditActivity.8
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) {
                SongEditActivity.this.finish();
            }
        }).setCancelable(false).show();
    }

    private void showFinalAlert(Exception e, int messageResourceId) {
        showFinalAlert(e, getResources().getText(messageResourceId));
    }

    private String makeRingtoneFilename(CharSequence title, String extension) {
        FileUtils.TEMP_DIRECTORY_AUDIO.mkdirs();
        File tempFile = new File(FileUtils.TEMP_DIRECTORY_AUDIO, ((Object) title) + extension);
        if (tempFile.exists()) {
            FileUtils.deleteFile(tempFile);
        }
        return tempFile.getAbsolutePath();
    }

    /* JADX WARN: Type inference failed for: r0v13, types: [com.photovideomaker.pictovideditor.videocutermerger.activities.SongEditActivity$9] */
    private void saveRingtone(final CharSequence title) {
        final String outPath = makeRingtoneFilename(title, this.mExtension);
        if (outPath == null) {
            showFinalAlert(new Exception(), R.string.no_unique_filename);
            return;
        }
        double startTime = this.mWaveformView.pixelsToSeconds(this.mStartPos);
        double endTime = this.mWaveformView.pixelsToSeconds(this.mEndPos);
        final int startFrame = this.mWaveformView.secondsToFrames(startTime);
        final int endFrame = this.mWaveformView.secondsToFrames(endTime);
        final int duration = (int) ((endTime - startTime) + 0.5d);
        this.mProgressDialog = new ProgressDialog(this);
        this.mProgressDialog.setProgressStyle(0);
        this.mProgressDialog.setMessage(getString(R.string.please_wait_));
        this.mProgressDialog.setIndeterminate(true);
        this.mProgressDialog.setCancelable(false);
        this.mProgressDialog.show();
        new Thread() {


            class C16781 implements CheapSoundFile.ProgressListener {
                C16781() {
                }

                @Override
                public boolean reportProgress(double frac) {
                    return true;
                }
            }

            @Override
            public void run() {
                final CharSequence errorMessage;
                final File outFile = new File(outPath);
                try {
                    SongEditActivity.this.mSoundFile.WriteFile(outFile, startFrame, endFrame - startFrame);
                    CheapSoundFile.create(outPath, new C16781());
                    SongEditActivity.this.mProgressDialog.dismiss();
                    final String str = outPath;
                    final int i = duration;
                    SongEditActivity.this.mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            SongEditActivity.this.afterSavingRingtone(title, str, outFile, i);
                        }
                    });
                } catch (Exception e) {
                    final Exception e2 = e;
                    SongEditActivity.this.mProgressDialog.dismiss();
                    if (e2.getMessage().equals("No space left on device")) {
                        errorMessage = SongEditActivity.this.getResources().getText(R.string.no_space_error);
                    } else {
                        errorMessage = SongEditActivity.this.getResources().getText(R.string.write_error);
                    }
                    SongEditActivity.this.mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            SongEditActivity.this.handleFatalError("WriteError", errorMessage, e2);
                        }
                    });
                }
            }
        }.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void afterSavingRingtone(CharSequence title, String outPath, File outFile, int duration) {
        if (outFile.length() <= 512) {
            outFile.delete();
            new AlertDialog.Builder(this).setTitle(R.string.alert_title_failure).setMessage(R.string.too_small_error).setPositiveButton(R.string.alert_ok_button, (DialogInterface.OnClickListener) null).setCancelable(false).show();
            return;
        }
        long fileSize = outFile.length();
        String artist = String.valueOf(getResources().getText(R.string.artist_name));
        ContentValues values = new ContentValues();
        values.put("_data", outPath);
        values.put("title", title.toString());
        values.put("_size", Long.valueOf(fileSize));
        values.put("mime_type", MimeTypes.AUDIO_MPEG);
        values.put("artist", artist);
        values.put("duration", Integer.valueOf(duration));
        values.put("is_music", (Boolean) true);
        Log.e("bbbbb........audio", "duaration is " + duration);
        setResult(-1, new Intent().setData(getContentResolver().insert(MediaStore.Audio.Media.getContentUriForPath(outPath), values)));
        this.selectedMusicData.track_data = outPath;
        this.selectedMusicData.track_duration = duration * 1000;
        MyApplication.getInstance().setMusicData(this.selectedMusicData);
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSave() {
        if (this.mIsPlaying) {
            handlePause();
        }
        saveRingtone("temp");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getCurrentTime() {
        return System.nanoTime() / C.MICROS_PER_SECOND;
    }

    private String getStackTrace(Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }
}
