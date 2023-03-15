package com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile;

import com.google.android.exoplayer2.extractor.ts.PsExtractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;




public class CheapMP3 extends CheapSoundFile {
    private static int[] BITRATES_MPEG1_L3 = {0, 32, 40, 48, 56, 64, 80, 96, 112, 128, 160, PsExtractor.AUDIO_STREAM, 224, 256, 320, 0};
    private static int[] BITRATES_MPEG2_L3 = {0, 8, 16, 24, 32, 40, 48, 56, 64, 80, 96, 112, 128,144, 160, 0};
    private static int[] SAMPLERATES_MPEG1_L3 = {44100, 48000, 32000, 0};
    private static int[] SAMPLERATES_MPEG2_L3 = {22050, 24000, 16000, 0};
    private int mAvgBitRate;
    private int mBitrateSum;
    private int mFileSize;
    private int[] mFrameGains;
    private int[] mFrameLens;
    private int[] mFrameOffsets;
    private int mGlobalChannels;
    private int mGlobalSampleRate;
    private int mMaxFrames;
    private int mMaxGain;
    private int mMinGain;
    private int mNumFrames;

    /* loaded from: classes.dex */
    static class C11611 implements CheapSoundFile.Factory {
        C11611() {
        }

        @Override // com.chettapps.videoeditor.videocutermerger .music.audiocutter.soundfile.CheapSoundFile.Factory
        public CheapSoundFile create() {
            return new CheapMP3();
        }

        @Override // com.chettapps.videoeditor.videocutermerger .music.audiocutter.soundfile.CheapSoundFile.Factory
        public String[] getSupportedExtensions() {
            return new String[]{"mp3"};
        }
    }

    public static CheapSoundFile.Factory getFactory() {
        return new C11611();
    }

    @Override // com.chettapps.videoeditor.videocutermerger .music.audiocutter.soundfile.CheapSoundFile
    public int getNumFrames() {
        return this.mNumFrames;
    }

    @Override // com.photovideomaker.pictovideditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile
    public int[] getFrameOffsets() {
        return this.mFrameOffsets;
    }

    @Override // com.photovideomaker.pictovideditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile
    public int getSamplesPerFrame() {
        return 1152;
    }

    @Override // com.photovideomaker.pictovideditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile
    public int[] getFrameLens() {
        return this.mFrameLens;
    }

    @Override // com.photovideomaker.pictovideditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile
    public int[] getFrameGains() {
        return this.mFrameGains;
    }

    @Override // com.photovideomaker.pictovideditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile
    public int getFileSizeBytes() {
        return this.mFileSize;
    }

    @Override // com.photovideomaker.pictovideditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile
    public int getAvgBitrateKbps() {
        return this.mAvgBitRate;
    }

    @Override // com.photovideomaker.pictovideditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile
    public int getSampleRate() {
        return this.mGlobalSampleRate;
    }

    @Override // com.photovideomaker.pictovideditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile
    public int getChannels() {
        return this.mGlobalChannels;
    }

    @Override // com.photovideomaker.pictovideditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile
    public String getFiletype() {
        return "MP3";
    }

    @Override // com.photovideomaker.pictovideditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile
    public int getSeekableFrameOffset(int frame) {
        if (frame <= 0) {
            return 0;
        }
        if (frame >= this.mNumFrames) {
            return this.mFileSize;
        }
        return this.mFrameOffsets[frame];
    }

    @Override // com.photovideomaker.pictovideditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile
    public void ReadFile(File inputFile) throws FileNotFoundException, IOException {
        int mpgVersion = 0;
        int bitRate;
        int sampleRate;
        int gain;
        super.ReadFile(inputFile);
        this.mNumFrames = 0;
        this.mMaxFrames = 64;
        this.mFrameOffsets = new int[this.mMaxFrames];
        this.mFrameLens = new int[this.mMaxFrames];
        this.mFrameGains = new int[this.mMaxFrames];
        this.mBitrateSum = 0;
        this.mMinGain = 255;
        this.mMaxGain = 0;
        this.mFileSize = (int) this.mInputFile.length();
        FileInputStream stream = new FileInputStream(this.mInputFile);
        int pos = 0;
        int offset = 0;
        byte[] buffer = new byte[12];
        while (pos < this.mFileSize - 12) {
            while (offset < 12) {
                offset += stream.read(buffer, offset, 12 - offset);
            }
            int bufferOffset = 0;
            while (bufferOffset < 12 && buffer[bufferOffset] != -1) {
                bufferOffset++;
            }
            if (this.mProgressListener != null) {
                boolean keepGoing = this.mProgressListener.reportProgress((pos * 1.0d) / this.mFileSize);
                if (!keepGoing) {
                    break;
                }
            }
            if (bufferOffset > 0) {
                for (int i = 0; i < 12 - bufferOffset; i++) {
                    buffer[i] = buffer[bufferOffset + i];
                }
                pos += bufferOffset;
                offset = 12 - bufferOffset;
            } else {
                if (buffer[1] == -6 || buffer[1] == -5) {
                    mpgVersion = 1;
                } else if (buffer[1] == -14 || buffer[1] == -13) {
                    mpgVersion = 2;
                } else {
                    for (int i2 = 0; i2 < 11; i2++) {
                        buffer[i2] = buffer[1 + i2];
                    }
                    pos++;
                    offset = 12 - 1;
                }
                if (mpgVersion == 1) {
                    bitRate = BITRATES_MPEG1_L3[(buffer[2] & 240) >> 4];
                    sampleRate = SAMPLERATES_MPEG1_L3[(buffer[2] & 12) >> 2];
                } else {
                    bitRate = BITRATES_MPEG2_L3[(buffer[2] & 240) >> 4];
                    sampleRate = SAMPLERATES_MPEG2_L3[(buffer[2] & 12) >> 2];
                }
                if (bitRate == 0 || sampleRate == 0) {
                    for (int i3 = 0; i3 < 10; i3++) {
                        buffer[i3] = buffer[2 + i3];
                    }
                    pos += 2;
                    offset = 12 - 2;
                } else {
                    this.mGlobalSampleRate = sampleRate;
                    int padding = (buffer[2] & 2) >> 1;
                    int frameLen = (((bitRate * 144) * 1000) / sampleRate) + padding;
                    if ((buffer[3] & 192) == 192) {
                        this.mGlobalChannels = 1;
                        if (mpgVersion == 1) {
                            gain = ((buffer[10] & 1) << 7) + ((buffer[11] & 254) >> 1);
                        } else {
                            gain = ((buffer[9] & 3) << 6) + ((buffer[10] & 252) >> 2);
                        }
                    } else {
                        this.mGlobalChannels = 2;
                        if (mpgVersion == 1) {
                            gain = ((buffer[9] & Byte.MAX_VALUE) << 1) + ((buffer[10] & 128) >> 7);
                        } else {
                            gain = 0;
                        }
                    }
                    this.mBitrateSum += bitRate;
                    this.mFrameOffsets[this.mNumFrames] = pos;
                    this.mFrameLens[this.mNumFrames] = frameLen;
                    this.mFrameGains[this.mNumFrames] = gain;
                    if (gain < this.mMinGain) {
                        this.mMinGain = gain;
                    }
                    if (gain > this.mMaxGain) {
                        this.mMaxGain = gain;
                    }
                    this.mNumFrames++;
                    if (this.mNumFrames == this.mMaxFrames) {
                        this.mAvgBitRate = this.mBitrateSum / this.mNumFrames;
                        int totalFramesGuess = ((this.mFileSize / this.mAvgBitRate) * sampleRate) / 144000;
                        int newMaxFrames = (totalFramesGuess * 11) / 10;
                        if (newMaxFrames < this.mMaxFrames * 2) {
                            newMaxFrames = this.mMaxFrames * 2;
                        }
                        int[] newOffsets = new int[newMaxFrames];
                        int[] newLens = new int[newMaxFrames];
                        int[] newGains = new int[newMaxFrames];
                        for (int i4 = 0; i4 < this.mNumFrames; i4++) {
                            newOffsets[i4] = this.mFrameOffsets[i4];
                            newLens[i4] = this.mFrameLens[i4];
                            newGains[i4] = this.mFrameGains[i4];
                        }
                        this.mFrameOffsets = newOffsets;
                        this.mFrameLens = newLens;
                        this.mFrameGains = newGains;
                        this.mMaxFrames = newMaxFrames;
                    }
                    stream.skip(frameLen - 12);
                    pos += frameLen;
                    offset = 0;
                }
            }
        }
        if (this.mNumFrames > 0) {
            this.mAvgBitRate = this.mBitrateSum / this.mNumFrames;
        } else {
            this.mAvgBitRate = 0;
        }
    }

    @Override // com.photovideomaker.pictovideditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile
    public void WriteFile(File outputFile, int startFrame, int numFrames) throws IOException {
        outputFile.createNewFile();
        FileInputStream in = new FileInputStream(this.mInputFile);
        FileOutputStream out = new FileOutputStream(outputFile);
        int maxFrameLen = 0;
        for (int i = 0; i < numFrames; i++) {
            if (this.mFrameLens[startFrame + i] > maxFrameLen) {
                maxFrameLen = this.mFrameLens[startFrame + i];
            }
        }
        byte[] buffer = new byte[maxFrameLen];
        int pos = 0;
        for (int i2 = 0; i2 < numFrames; i2++) {
            int skip = this.mFrameOffsets[startFrame + i2] - pos;
            int len = this.mFrameLens[startFrame + i2];
            if (skip > 0) {
                in.skip(skip);
                pos += skip;
            }
            in.read(buffer, 0, len);
            out.write(buffer, 0, len);
            pos += len;
        }
        in.close();
        out.close();
    }
}
