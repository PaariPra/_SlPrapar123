package com.chettapps.videoeditor.videocutermerger.music;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.Random;

/* loaded from: classes.dex */
public class SeekTest {
    public static final String PREF_SEEK_TEST_DATE = "seek_test_date";
    public static final String PREF_SEEK_TEST_RESULT = "seek_test_result";
    private static byte[] SILENCE_MP3_FRAME;
    public static long after;
    public static long before;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class C09501 implements MediaPlayer.OnCompletionListener {
        C09501() {
        }

        @Override // android.media.MediaPlayer.OnCompletionListener
        public synchronized void onCompletion(MediaPlayer arg0) {
            Log.d("Ringdroid", "Got callback");
            SeekTest.after = System.currentTimeMillis();
        }
    }

    public static boolean CanSeekAccurately(SharedPreferences prefs) {
        Log.d("Ringdroid", "Running CanSeekAccurately");
        boolean result = prefs.getBoolean(PREF_SEEK_TEST_RESULT, false);
        long testDate = prefs.getLong(PREF_SEEK_TEST_DATE, 0L);
        long now = new Date().getTime();
        if (now - testDate < 604800000) {
            Log.d("Ringdroid", "Fast MP3 seek result cached: " + result);
            return result;
        }
        String filename = "/sdcard/silence" + new Random().nextLong() + ".mp3";
        File file = new File(filename);
        boolean ok = false;
        try {
            new RandomAccessFile(file, "r");
        } catch (Exception e) {
            ok = true;
        }
        if (ok) {
            Log.d("Ringdroid", "Writing " + filename);
            try {
                file.createNewFile();
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    for (int i = 0; i < 80; i++) {
                        fileOutputStream.write(SILENCE_MP3_FRAME, 0, SILENCE_MP3_FRAME.length);
                    }
                    Log.d("Ringdroid", "File written, starting to play");
                    MediaPlayer player = new MediaPlayer();
                    player.setAudioStreamType(3);
                    player.setDataSource(new FileInputStream(filename).getFD(), SILENCE_MP3_FRAME.length * 70, SILENCE_MP3_FRAME.length * 10);
                    Log.d("Ringdroid", "Preparing");
                    player.prepare();
                    before = 0L;
                    after = 0L;
                    player.setOnCompletionListener(new C09501());
                    Log.d("Ringdroid", "Starting");
                    player.start();
                    for (int i2 = 0; i2 < 200 && before == 0; i2++) {
                        try {
                            if (player.getCurrentPosition() > 0) {
                                Log.d("Ringdroid", "Started playing after " + (i2 * 10) + " ms");
                                before = System.currentTimeMillis();
                            }
                            Thread.sleep(10L);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            Log.d("Ringdroid", "Couldn't play: " + e2.toString());
                            Log.d("Ringdroid", "Fast MP3 seek disabled by default");
                            try {
                                file.delete();
                            } catch (Exception e3) {
                            }
                            SharedPreferences.Editor prefsEditor = prefs.edit();
                            prefsEditor.putLong(PREF_SEEK_TEST_DATE, now);
                            prefsEditor.putBoolean(PREF_SEEK_TEST_RESULT, result);
                            prefsEditor.commit();
                            return false;
                        }
                    }
                    if (before == 0) {
                        Log.d("Ringdroid", "Never started playing.");
                        Log.d("Ringdroid", "Fast MP3 seek disabled by default");
                        try {
                            file.delete();
                        } catch (Exception e4) {
                        }
                        SharedPreferences.Editor prefsEditor2 = prefs.edit();
                        prefsEditor2.putLong(PREF_SEEK_TEST_DATE, now);
                        prefsEditor2.putBoolean(PREF_SEEK_TEST_RESULT, result);
                        prefsEditor2.commit();
                        return false;
                    }
                    Log.d("Ringdroid", "Sleeping");
                    for (int i3 = 0; i3 < 300 && after == 0; i3++) {
                        Log.d("Ringdroid", "Pos: " + player.getCurrentPosition());
                        Thread.sleep(10L);
                    }
                    Log.d("Ringdroid", "Result: " + before + ", " + after);
                    if (after <= before || after >= before + 2000) {
                        Log.d("Ringdroid", "Fast MP3 seek disabled");
                    } else {
                        Log.d("Ringdroid", "Fast MP3 seek enabled: " + (after > before ? after - before : -1L));
                        result = true;
                    }
                    SharedPreferences.Editor prefsEditor3 = prefs.edit();
                    prefsEditor3.putLong(PREF_SEEK_TEST_DATE, now);
                    prefsEditor3.putBoolean(PREF_SEEK_TEST_RESULT, result);
                    prefsEditor3.commit();
                    try {
                        file.delete();
                    } catch (Exception e5) {
                    }
                    return result;
                } catch (Exception e6) {
                    Log.d("Ringdroid", "Couldn't write temp silence file");
                    try {
                        file.delete();
                    } catch (Exception e7) {
                    }
                    return false;
                }
            } catch (Exception e8) {
                Log.d("Ringdroid", "Couldn't output for writing");
                return false;
            }
        } else {
            Log.d("Ringdroid", "Couldn't find temporary filename");
            return false;
        }
    }

    static {
        byte[] bArr = new byte[104];
        bArr[0] = -1;
        bArr[1] = -5;
        bArr[2] = 16;
        bArr[3] = -60;
        bArr[5] = 3;
        bArr[6] = -127;
        bArr[7] = -12;
        bArr[8] = 1;
        bArr[9] = 38;
        bArr[10] = 96;
        bArr[12] = 64;
        bArr[13] = 32;
        bArr[14] = 89;
        bArr[15] = Byte.MIN_VALUE;
        bArr[16] = 35;
        bArr[17] = 72;
        bArr[19] = 9;
        bArr[20] = 116;
        bArr[22] = 1;
        bArr[23] = 18;
        bArr[24] = 3;
        bArr[25] = -1;
        bArr[26] = -1;
        bArr[27] = -1;
        bArr[28] = -1;
        bArr[29] = -2;
        bArr[30] = -97;
        bArr[31] = 99;
        bArr[32] = -65;
        bArr[33] = -47;
        bArr[34] = 122;
        bArr[35] = 63;
        bArr[36] = 93;
        bArr[37] = 1;
        bArr[38] = -1;
        bArr[39] = -1;
        bArr[40] = -1;
        bArr[41] = -1;
        bArr[42] = -2;
        bArr[43] = -115;
        bArr[44] = -83;
        bArr[45] = 108;
        bArr[46] = 49;
        bArr[47] = 66;
        bArr[48] = -61;
        bArr[49] = 2;
        bArr[50] = -57;
        bArr[51] = 12;
        bArr[52] = 9;
        bArr[53] = -122;
        bArr[54] = -125;
        bArr[55] = -88;
        bArr[56] = 122;
        bArr[57] = 58;
        bArr[58] = 104;
        bArr[59] = 76;
        bArr[60] = 65;
        bArr[61] = 77;
        bArr[62] = 69;
        bArr[63] = 51;
        bArr[64] = 46;
        bArr[65] = 57;
        bArr[66] = 56;
        bArr[67] = 46;
        bArr[68] = 50;
        SILENCE_MP3_FRAME = bArr;
    }
}
