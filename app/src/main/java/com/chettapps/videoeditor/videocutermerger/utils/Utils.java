package com.chettapps.videoeditor.videocutermerger.utils;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.core.content.ContextCompat;


/* loaded from: classes.dex */
public class Utils {
    public static int checkPermission(String[] permissions, Context context) {
        int permissionCheck = 0;
        for (String permission : permissions) {
            permissionCheck += ContextCompat.checkSelfPermission(context, permission);
        }
        return permissionCheck;
    }

    @SuppressLint("WrongConstant")
    public static void rateApp(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent("android.intent.action.VIEW", uri);
        goToMarket.addFlags(1208483840);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }
}
