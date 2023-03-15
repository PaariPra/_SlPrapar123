package com.chettapps.videoeditor.videocutermerger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;

import com.chettapps.videoeditor.R;
import com.chettapps.videoeditor.videocutermerger.activities.MainActivity;


public class splace extends AppCompatActivity {
    public static int check_main = 1;
    private static int SPLASH_TIME_OUT = PathInterpolatorCompat.MAX_NUM_POINTS;


    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_main_activity);

        new Handler().postDelayed(new Runnable() {



            @Override
            public void run() {
                Intent i = new Intent(splace.this, MainActivity.class);
                splace.this.startActivity(i);
                splace.this.finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
