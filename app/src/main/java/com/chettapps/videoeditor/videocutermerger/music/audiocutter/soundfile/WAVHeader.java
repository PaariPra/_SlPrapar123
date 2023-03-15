package com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile;


public class WAVHeader
{
    private int mChannels;
    private byte[] mHeader = null;
    private int mNumBytesPerSample;
    private int mNumSamples;
    private int mSampleRate;

    public WAVHeader(int sampleRate, int numChannels, int numSamples) {
        this.mNumBytesPerSample = this.mChannels * 2;
        this.mSampleRate = sampleRate;
        this.mChannels = numChannels;
        this.mNumSamples = numSamples;
        setHeader();
    }


    public byte[] getWAVHeader() {
        return this.mHeader;
    }

    public static byte[] getWAVHeader(int sampleRate, int numChannels, int numSamples) {
        return new WAVHeader(sampleRate, numChannels, numSamples).mHeader;
    }



    public String toString() {
        byte[] bArr;
        boolean break_line;
        boolean insert_space;
        String str = "";
        if (this.mHeader == null) {
            return str;
        }
        int count = 0;
        for (byte b : this.mHeader) {
            if (count <= 0 || count % 32 != 0) {
                break_line = false;
            } else {
                break_line = true;
            }
            if (count <= 0 || count % 4 != 0 || break_line) {
                insert_space = false;
            } else {
                insert_space = true;
            }
            if (break_line) {
                str = String.valueOf(str) + '\n';
            }
            if (insert_space) {
                str = String.valueOf(str) + ' ';
            }
            str = String.valueOf(str) + String.format("%02X", Byte.valueOf(b));
            count++;
        }
        return str;
    }

    private void setHeader() {
        byte[] header = new byte[46];
        System.arraycopy(new byte[]{82, 73, 70, 70}, 0, header, 0, 4);
        int size = (this.mNumSamples * this.mNumBytesPerSample) + 36;
        int i2 = 4 + 1;
        header[4] = (byte) (size & 255);
        int i = i2 + 1;
        header[i2] = (byte) ((size >> 8) & 255);
        int i22 = i + 1;
        header[i] = (byte) ((size >> 16) & 255);
        int i3 = i22 + 1;
        header[i22] = (byte) ((size >> 24) & 255);
        System.arraycopy(new byte[]{87, 65, 86, 69}, 0, header, i3, 4);
        int i4 = i3 + 4;
        System.arraycopy(new byte[]{102, 109, 116, 32}, 0, header, i4, 4);
        int i5 = i4 + 4;
        byte[] obj = new byte[4];
        obj[0] = 16;
        System.arraycopy(obj, 0, header, i5, 4);
        int i6 = i5 + 4;
        byte[] obj2 = new byte[2];
        obj2[0] = 1;
        System.arraycopy(obj2, 0, header, i6, 2);
        int i7 = i6 + 2;
        int i23 = i7 + 1;
        header[i7] = (byte) (this.mChannels & 255);
        int i8 = i23 + 1;
        header[i23] = (byte) ((this.mChannels >> 8) & 255);
        int i24 = i8 + 1;
        header[i8] = (byte) (this.mSampleRate & 255);
        int i9 = i24 + 1;
        header[i24] = (byte) ((this.mSampleRate >> 8) & 255);
        int i25 = i9 + 1;
        header[i9] = (byte) ((this.mSampleRate >> 16) & 255);
        int i10 = i25 + 1;
        header[i25] = (byte) ((this.mSampleRate >> 24) & 255);
        int byteRate = this.mSampleRate * this.mNumBytesPerSample;
        int i26 = i10 + 1;
        header[i10] = (byte) (byteRate & 255);
        int i11 = i26 + 1;
        header[i26] = (byte) ((byteRate >> 8) & 255);
        int i27 = i11 + 1;
        header[i11] = (byte) ((byteRate >> 16) & 255);
        int i12 = i27 + 1;
        header[i27] = (byte) ((byteRate >> 24) & 255);
        int i28 = i12 + 1;
        header[i12] = (byte) (this.mNumBytesPerSample & 255);
        int i13 = i28 + 1;
        header[i28] = (byte) ((this.mNumBytesPerSample >> 8) & 255);
        byte[] obj3 = new byte[2];
        obj3[0] = 16;
        System.arraycopy(obj3, 0, header, i13, 2);
        int i14 = i13 + 2;
        System.arraycopy(new byte[]{100, 97, 116, 97}, 0, header, i14, 4);
        int i15 = i14 + 4;
        int size2 = this.mNumSamples * this.mNumBytesPerSample;
        int i29 = i15 + 1;
        header[i15] = (byte) (size2 & 255);
        int i16 = i29 + 1;
        header[i29] = (byte) ((size2 >> 8) & 255);
        int i210 = i16 + 1;
        header[i16] = (byte) ((size2 >> 16) & 255);
        int i17 = i210 + 1;
        header[i210] = (byte) ((size2 >> 24) & 255);
        this.mHeader = header;
    }


}
