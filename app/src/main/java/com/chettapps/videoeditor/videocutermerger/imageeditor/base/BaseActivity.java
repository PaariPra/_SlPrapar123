package com.chettapps.videoeditor.videocutermerger.imageeditor.base;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;


public class BaseActivity extends AppCompatActivity {
    public static final int READ_WRITE_STORAGE = 52;
    private ProgressDialog mProgressDialog;

    public boolean requestPermission(String permission) {
        boolean isGranted;
        if (ContextCompat.checkSelfPermission(this, permission) == 0) {
            isGranted = true;
        } else {
            isGranted = false;
        }
        if (!isGranted) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 52);
        }
        return isGranted;
    }

    public void isPermissionGranted(boolean isGranted, String permission) {
    }

    public void makeFullScreen() {
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity, android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 52:
                isPermissionGranted(grantResults[0] == 0, permissions[0]);
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void showLoading(@NonNull String message) {
        this.mProgressDialog = new ProgressDialog(this);
        this.mProgressDialog.setMessage(message);
        this.mProgressDialog.setProgressStyle(0);
        this.mProgressDialog.setCancelable(false);
        this.mProgressDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void hideLoading() {
        if (this.mProgressDialog != null) {
            this.mProgressDialog.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void showSnackbar(@NonNull String message) {
        View view = findViewById(16908290);
        if (view != null) {
            Snackbar.make(view, message, -1).show();
        } else {
            Toast.makeText(this, message, 0).show();
        }
    }
}
