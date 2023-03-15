package com.chettapps.videoeditor.videocutermerger.utils;

import android.app.Activity;

import com.chettapps.videoeditor.R;


/* loaded from: classes.dex */
public class Animation {
    public static void nextAnimation(Activity activity) {
        activity.overridePendingTransition(R.anim.anim_right, R.anim.anim_left);
    }

    public static void previewAnimation(Activity activity) {
        activity.overridePendingTransition(R.anim.enter_anim_rtl, R.anim.exit_anim_rtl);
    }
}
