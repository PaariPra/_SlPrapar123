package com.chettapps.videoeditor.videocutermerger.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.chettapps.videoeditor.R;
import com.chettapps.videoeditor.videocutermerger.MyApplication;

import com.chettapps.videoeditor.videocutermerger.fragments.CutVideoFragment;
import com.chettapps.videoeditor.videocutermerger.fragments.JoinVideoFragment;
import com.chettapps.videoeditor.videocutermerger.fragments.MakeVideoFragment;

import com.google.android.material.tabs.TabLayout;
import com.chettapps.videoeditor.videocutermerger.utils.FileUtils;


public class MyCreationActivity extends AppCompatActivity {
    public static final String EXTRA_FROM_VIDEO = "EXTRA_FROM_VIDEO";
    private boolean isFromVideo = false;
    TabLayout tabLayout;
    ViewPager viewPager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_creation);
        try {
            if (Build.VERSION.SDK_INT >= 19) {
                Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                Uri contentUri = Uri.fromFile(FileUtils.APP_DIRECTORY);
                mediaScanIntent.setData(contentUri);
                sendBroadcast(mediaScanIntent);
            } else {
                sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(FileUtils.APP_DIRECTORY)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.isFromVideo = getIntent().hasExtra(EXTRA_FROM_VIDEO);
        this.tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        this.viewPager = (ViewPager) findViewById(R.id.viewPager);
        this.tabLayout.addTab(this.tabLayout.newTab().setText("Make Video"));
        this.tabLayout.addTab(this.tabLayout.newTab().setText("Cut Video"));
        this.tabLayout.addTab(this.tabLayout.newTab().setText("Join Video"));
        MyAdapter adapter = new MyAdapter(this, getSupportFragmentManager(), this.tabLayout.getTabCount());
        this.viewPager.setAdapter(adapter);
        this.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.tabLayout));
        int score = getIntent().getIntExtra("pos", 0);
        Log.d("bbbbb...*****....", "............RCV..........score ::: " + score);
        this.viewPager.setCurrentItem(score);
        this.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                MyCreationActivity.this.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyApplication.getInstance().setFrame(0);
        if (this.isFromVideo) {
            Intent intent2 = new Intent(this, MainActivity.class);
            intent2.setFlags(268468224);
            startActivity(intent2);
            finish();
        }
    }


    public class MyAdapter extends FragmentPagerAdapter {
        private Context myContext;
        int totalTabs;

        public MyAdapter(Context context, FragmentManager fm, int totalTabs) {
            super(fm);
            this.myContext = context;
            this.totalTabs = totalTabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MakeVideoFragment();
                case 1:
                    CutVideoFragment Cut = new CutVideoFragment();
                    return Cut;
                case 2:
                    JoinVideoFragment Join = new JoinVideoFragment();
                    return Join;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return this.totalTabs;
        }
    }





}
