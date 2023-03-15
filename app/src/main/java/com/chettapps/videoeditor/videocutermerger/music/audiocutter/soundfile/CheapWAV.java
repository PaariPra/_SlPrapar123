package com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/* loaded from: classes.dex */
public class CheapWAV extends CheapSoundFile {
    private int mChannels;
    private int mFileSize;
    private int mFrameBytes;
    private int[] mFrameGains;
    private int[] mFrameLens;
    private int[] mFrameOffsets;
    private int mNumFrames;
    private int mOffset;
    private int mSampleRate;

    /* loaded from: classes.dex */
    static class C11621 implements Factory {
        C11621() {
        }

        @Override // com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile.Factory
        public CheapSoundFile create() {
            return new CheapWAV();
        }

        @Override // com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile.Factory
        public String[] getSupportedExtensions() {
            return new String[]{"wav"};
        }
    }

    public static Factory getFactory() {
        return new C11621();
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile
    public int getNumFrames() {
        return this.mNumFrames;
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile
    public int getSamplesPerFrame() {
        return this.mSampleRate / 50;
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile
    public int[] getFrameOffsets() {
        return this.mFrameOffsets;
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile
    public int[] getFrameLens() {
        return this.mFrameLens;
    }

    @Override // com.chettapps.videoeditor.videocutermerger .music.audiocutter.soundfile.CheapSoundFile
    public int[] getFrameGains() {
        return this.mFrameGains;
    }

    @Override // com.chettapps.videoeditor.videocutermerger .music.audiocutter.soundfile.CheapSoundFile
    public int getFileSizeBytes() {
        return this.mFileSize;
    }

    @Override // com.chettapps.videoeditor.videocutermerger .music.audiocutter.soundfile.CheapSoundFile
    public int getAvgBitrateKbps() {
        return ((this.mSampleRate * this.mChannels) * 2) / 1024;
    }

    @Override // com.chettapps.videoeditor.videocutermerger .music.audiocutter.soundfile.CheapSoundFile
    public int getSampleRate() {
        return this.mSampleRate;
    }

    @Override // com.chettapps.videoeditor.videocutermerger .music.audiocutter.soundfile.CheapSoundFile
    public int getChannels() {
        return this.mChannels;
    }

    @Override // com.chettapps.videoeditor.videocutermerger .music.audiocutter.soundfile.CheapSoundFile
    public String getFiletype() {
        return "WAV";
    }

    @Override // com.chettapps.videoeditor.videocutermerger .music.audiocutter.soundfile.CheapSoundFile
    public void ReadFile(File inputFile) throws FileNotFoundException, IOException {
        super.ReadFile(inputFile);
        this.mFileSize = (int) this.mInputFile.length();
        if (this.mFileSize < 128) {
            throw new IOException("File too small to parse");
        }
        FileInputStream stream = new FileInputStream(this.mInputFile);
        byte[] header = new byte[12];
        stream.read(header, 0, 12);
        this.mOffset += 12;
        if (header[0] == 82 && header[1] == 73 && header[2] == 70 && header[3] == 70 && header[8] == 87 && header[9] == 65 && header[10] == 86 && header[11] == 69) {
            this.mChannels = 0;
            this.mSampleRate = 0;
            while (this.mOffset + 8 <= this.mFileSize) {
                byte[] chunkHeader = new byte[8];
                stream.read(chunkHeader, 0, 8);
                this.mOffset += 8;
                int chunkLen = ((chunkHeader[7] & 255) << 24) | ((chunkHeader[6] & 255) << 16) | ((chunkHeader[5] & 255) << 8) | (chunkHeader[4] & 255);
                if (chunkHeader[0] != 102 || chunkHeader[1] != 109 || chunkHeader[2] != 116 || chunkHeader[3] != 32) {
                    if (chunkHeader[0] == 100 && chunkHeader[1] == 97 && chunkHeader[2] == 116 && chunkHeader[3] == 97) {
                        if (this.mChannels != 0 && this.mSampleRate != 0) {
                            this.mFrameBytes = ((this.mSampleRate * this.mChannels) / 50) * 2;
                            this.mNumFrames = ((this.mFrameBytes - 1) + chunkLen) / this.mFrameBytes;
                            this.mFrameOffsets = new int[this.mNumFrames];
                            this.mFrameLens = new int[this.mNumFrames];
                            this.mFrameGains = new int[this.mNumFrames];
                            byte[] oneFrame = new byte[this.mFrameBytes];
                            int i = 0;
                            int frameIndex = 0;
                            while (i < chunkLen) {
                                int oneFrameBytes = this.mFrameBytes;
                                if (i + oneFrameBytes > chunkLen) {
                                    i = chunkLen - oneFrameBytes;
                                }
                                stream.read(oneFrame, 0, oneFrameBytes);
                                int maxGain = 0;
                                int j = 1;
                                while (j < oneFrameBytes) {
                                    int val = Math.abs((int) oneFrame[j]);
                                    if (val > maxGain) {
                                        maxGain = val;
                                    }
                                    j += this.mChannels * 4;
                                }
                                this.mFrameOffsets[frameIndex] = this.mOffset;
                                this.mFrameLens[frameIndex] = oneFrameBytes;
                                this.mFrameGains[frameIndex] = maxGain;
                                frameIndex++;
                                this.mOffset += oneFrameBytes;
                                i += oneFrameBytes;
                                if (this.mProgressListener != null && !this.mProgressListener.reportProgress((i * 1.0d) / chunkLen)) {
                                    break;
                                }
                            }
                        } else {
                            throw new IOException("Bad WAV file: data chunk before fmt chunk");
                        }
                    }
                    stream.skip(chunkLen);
                    this.mOffset += chunkLen;
                } else if (chunkLen < 16 || chunkLen > 1024) {
                    throw new IOException("WAV file has bad fmt chunk");
                } else {
                    byte[] fmt = new byte[chunkLen];
                    stream.read(fmt, 0, chunkLen);
                    this.mOffset += chunkLen;
                    int format = ((fmt[1] & 255) << 8) | (fmt[0] & 255);
                    this.mChannels = ((fmt[3] & 255) << 8) | (fmt[2] & 255);
                    this.mSampleRate = ((fmt[7] & 255) << 24) | ((fmt[6] & 255) << 16) | ((fmt[5] & 255) << 8) | (fmt[4] & 255);
                    if (format != 1) {
                        throw new IOException("Unsupported WAV file encoding");
                    }
                }
            }
            return;
        }
        throw new IOException("Not a WAV file");
    }

    @Override // com.chettapps.videoeditor.videocutermerger .music.audiocutter.soundfile.CheapSoundFile
    public void WriteFile(File outputFile, int startFrame, int numFrames) throws IOException {
        outputFile.createNewFile();
        FileInputStream in = new FileInputStream(this.mInputFile);
        FileOutputStream out = new FileOutputStream(outputFile);
        long totalAudioLen = 0;
        for (int i = 0; i < numFrames; i++) {
            totalAudioLen += this.mFrameLens[startFrame + i];
        }
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = this.mSampleRate;
        long byteRate = this.mSampleRate * 2 * this.mChannels;
        out.write(new byte[]{82, 73, 70, 70, (byte) (255 & totalDataLen), (byte) ((totalDataLen >> 8) & 255), (byte) ((totalDataLen >> 16) & 255), (byte) ((totalDataLen >> 24) & 255), 87, 65, 86, 69, 102, 109, 116, 32, 16, 0, 0, 0, 1, 0, (byte) this.mChannels, 0, (byte) (255 & longSampleRate), (byte) ((longSampleRate >> 8) & 255), (byte) ((longSampleRate >> 16) & 255), (byte) ((longSampleRate >> 24) & 255), (byte) (255 & byteRate), (byte) ((byteRate >> 8) & 255), (byte) ((byteRate >> 16) & 255), (byte) ((byteRate >> 24) & 255), (byte) (this.mChannels * 2), 0, 16, 0, 100, 97, 116, 97, (byte) (255 & totalAudioLen), (byte) ((totalAudioLen >> 8) & 255), (byte) ((totalAudioLen >> 16) & 255), (byte) ((totalAudioLen >> 24) & 255)}, 0, 44);
        byte[] buffer = new byte[this.mFrameBytes];
        int pos = 0;
        for (int i2 = 0; i2 < numFrames; i2++) {
            int skip = this.mFrameOffsets[startFrame + i2] - pos;
            int len = this.mFrameLens[startFrame + i2];
            if (skip >= 0) {
                if (skip > 0) {
                    in.skip(skip);
                    pos += skip;
                }
                in.read(buffer, 0, len);
                out.write(buffer, 0, len);
                pos += len;
            }
        }
        in.close();
        out.close();
    }
}
