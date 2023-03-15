package com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;



/* loaded from: classes.dex */
public class CheapAAC extends CheapSoundFile {
    public static final int kFTYP = 1718909296;
    public static final int kMDAT = 1835295092;
    public static final int kMP4A = 1836069985;
    public static final int kSTCO = 1937007471;
    public static final int kSTSC = 1937011555;
    private HashMap<Integer, Atom> mAtomMap;
    private int mBitrate;
    private int mChannels;
    private int mFileSize;
    private int[] mFrameGains;
    private int[] mFrameLens;
    private int[] mFrameOffsets;
    private int mMaxGain;
    private int mMdatLength;
    private int mMdatOffset;
    private int mMinGain;
    private int mNumFrames;
    private int mOffset;
    private int mSampleRate;
    private int mSamplesPerFrame;
    public static final int kDINF = 1684631142;
    public static final int kHDLR = 1751411826;
    public static final int kMDHD = 1835296868;
    public static final int kMDIA = 1835297121;
    public static final int kMINF = 1835626086;
    public static final int kMOOV = 1836019574;
    public static final int kMVHD = 1836476516;
    public static final int kSMHD = 1936549988;
    public static final int kSTBL = 1937007212;
    public static final int kSTSD = 1937011556;
    public static final int kSTSZ = 1937011578;
    public static final int kSTTS = 1937011827;
    public static final int kTKHD = 1953196132;
    public static final int kTRAK = 1953653099;
    public static final int[] kRequiredAtoms = {kDINF, kHDLR, kMDHD, kMDIA, kMINF, kMOOV, kMVHD, kSMHD, kSTBL, kSTSD, kSTSZ, kSTTS, kTKHD, kTRAK};
    public static final int[] kSaveDataAtoms = {kDINF, kHDLR, kMDHD, kMVHD, kSMHD, kTKHD, kSTSD};

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class Atom {
        public byte[] data;
        public int len;
        public int start;

        Atom() {
        }
    }

    /* loaded from: classes.dex */
    static class C11591 implements Factory {
        C11591() {
        }

        @Override // com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile.Factory
        public CheapSoundFile create() {
            return new CheapAAC();
        }

        @Override // com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile.Factory
        public String[] getSupportedExtensions() {
            return new String[]{"aac", "m4a"};
        }
    }

    public static Factory getFactory() {
        return new C11591();
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile
    public int getNumFrames() {
        return this.mNumFrames;
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile
    public int getSamplesPerFrame() {
        return this.mSamplesPerFrame;
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile
    public int[] getFrameOffsets() {
        return this.mFrameOffsets;
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile
    public int[] getFrameLens() {
        return this.mFrameLens;
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile
    public int[] getFrameGains() {
        return this.mFrameGains;
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile
    public int getFileSizeBytes() {
        return this.mFileSize;
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile
    public int getAvgBitrateKbps() {
        return this.mFileSize / (this.mNumFrames * this.mSamplesPerFrame);
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile
    public int getSampleRate() {
        return this.mSampleRate;
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile
    public int getChannels() {
        return this.mChannels;
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile
    public String getFiletype() {
        return "AAC";
    }

    public String atomToString(int atomType) {
        return String.valueOf(String.valueOf(String.valueOf("" + ((char) ((atomType >> 24) & 255))) + ((char) ((atomType >> 16) & 255))) + ((char) ((atomType >> 8) & 255))) + ((char) (atomType & 255));
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile
    public void ReadFile(File inputFile) throws FileNotFoundException, IOException {
        super.ReadFile(inputFile);
        this.mChannels = 0;
        this.mSampleRate = 0;
        this.mBitrate = 0;
        this.mSamplesPerFrame = 0;
        this.mNumFrames = 0;
        this.mMinGain = 255;
        this.mMaxGain = 0;
        this.mOffset = 0;
        this.mMdatOffset = -1;
        this.mMdatLength = -1;
        this.mAtomMap = new HashMap<>();
        this.mFileSize = (int) this.mInputFile.length();
        if (this.mFileSize < 128) {
            throw new IOException("File too small to parse");
        }
        byte[] header = new byte[8];
        new FileInputStream(this.mInputFile).read(header, 0, 8);
        if (header[0] == 0 && header[4] == 102 && header[5] == 116 && header[6] == 121 && header[7] == 112) {
            parseMp4(new FileInputStream(this.mInputFile), this.mFileSize);
            if (this.mMdatOffset <= 0 || this.mMdatLength <= 0) {
                throw new IOException("Didn't find mdat");
            }
            FileInputStream stream = new FileInputStream(this.mInputFile);
            stream.skip(this.mMdatOffset);
            this.mOffset = this.mMdatOffset;
            parseMdat(stream, this.mMdatLength);
            boolean bad = false;
            int[] iArr = kRequiredAtoms;
            for (int requiredAtomType : iArr) {
                if (!this.mAtomMap.containsKey(Integer.valueOf(requiredAtomType))) {
                    System.out.println("Missing atom: " + atomToString(requiredAtomType));
                    bad = true;
                }
            }
            if (bad) {
                throw new IOException("Could not parse MP4 file");
            }
            return;
        }
        throw new IOException("Unknown file format");
    }

    private void parseMp4(InputStream stream, int maxLen) throws IOException {
        int[] iArr;
        while (maxLen > 8) {
            int initialOffset = this.mOffset;
            byte[] atomHeader = new byte[8];
            stream.read(atomHeader, 0, 8);
            int atomLen = ((atomHeader[0] & 255) << 24) | ((atomHeader[1] & 255) << 16) | ((atomHeader[2] & 255) << 8) | (atomHeader[3] & 255);
            if (atomLen > maxLen) {
                atomLen = maxLen;
            }
            int atomType = ((atomHeader[4] & 255) << 24) | ((atomHeader[5] & 255) << 16) | ((atomHeader[6] & 255) << 8) | (atomHeader[7] & 255);
            Atom atom = new Atom();
            atom.start = this.mOffset;
            atom.len = atomLen;
            this.mAtomMap.put(Integer.valueOf(atomType), atom);
            this.mOffset += 8;
            if (atomType == 1836019574 || atomType == 1953653099 || atomType == 1835297121 || atomType == 1835626086 || atomType == 1937007212) {
                parseMp4(stream, atomLen);
            } else if (atomType == 1937011578) {
                parseStsz(stream, atomLen - 8);
            } else if (atomType == 1937011827) {
                parseStts(stream, atomLen - 8);
            } else if (atomType == 1835295092) {
                this.mMdatOffset = this.mOffset;
                this.mMdatLength = atomLen - 8;
            } else {
                for (int savedAtomType : kSaveDataAtoms) {
                    if (savedAtomType == atomType) {
                        byte[] data = new byte[atomLen - 8];
                        stream.read(data, 0, atomLen - 8);
                        this.mOffset += atomLen - 8;
                        this.mAtomMap.get(Integer.valueOf(atomType)).data = data;
                    }
                }
            }
            if (atomType == 1937011556) {
                parseMp4aFromStsd();
            }
            maxLen -= atomLen;
            int skipLen = atomLen - (this.mOffset - initialOffset);
            if (skipLen < 0) {
                throw new IOException("Went over by " + (-skipLen) + " bytes");
            }
            stream.skip(skipLen);
            this.mOffset += skipLen;
        }
    }

    void parseStts(InputStream stream, int maxLen) throws IOException {
        byte[] sttsData = new byte[16];
        stream.read(sttsData, 0, 16);
        this.mOffset += 16;
        this.mSamplesPerFrame = ((sttsData[12] & 255) << 24) | ((sttsData[13] & 255) << 16) | ((sttsData[14] & 255) << 8) | (sttsData[15] & 255);
    }

    void parseStsz(InputStream stream, int maxLen) throws IOException {
        byte[] stszHeader = new byte[12];
        stream.read(stszHeader, 0, 12);
        this.mOffset += 12;
        this.mNumFrames = ((stszHeader[8] & 255) << 24) | ((stszHeader[9] & 255) << 16) | ((stszHeader[10] & 255) << 8) | (stszHeader[11] & 255);
        this.mFrameOffsets = new int[this.mNumFrames];
        this.mFrameLens = new int[this.mNumFrames];
        this.mFrameGains = new int[this.mNumFrames];
        byte[] frameLenBytes = new byte[this.mNumFrames * 4];
        stream.read(frameLenBytes, 0, this.mNumFrames * 4);
        this.mOffset += this.mNumFrames * 4;
        for (int i = 0; i < this.mNumFrames; i++) {
            this.mFrameLens[i] = ((frameLenBytes[(i * 4) + 0] & 255) << 24) | ((frameLenBytes[(i * 4) + 1] & 255) << 16) | ((frameLenBytes[(i * 4) + 2] & 255) << 8) | (frameLenBytes[(i * 4) + 3] & 255);
        }
    }

    void parseMp4aFromStsd() {
        byte[] stsdData = this.mAtomMap.get(Integer.valueOf((int) kSTSD)).data;
        this.mChannels = ((stsdData[32] & 255) << 8) | (stsdData[33] & 255);
        this.mSampleRate = ((stsdData[40] & 255) << 8) | (stsdData[41] & 255);
    }

    void parseMdat(InputStream stream, int maxLen) throws IOException {
        int initialOffset = this.mOffset;
        for (int i = 0; i < this.mNumFrames; i++) {
            this.mFrameOffsets[i] = this.mOffset;
            if ((this.mOffset - initialOffset) + this.mFrameLens[i] > maxLen - 8) {
                this.mFrameGains[i] = 0;
            } else {
                readFrameAndComputeGain(stream, i);
            }
            if (this.mFrameGains[i] < this.mMinGain) {
                this.mMinGain = this.mFrameGains[i];
            }
            if (this.mFrameGains[i] > this.mMaxGain) {
                this.mMaxGain = this.mFrameGains[i];
            }
            if (this.mProgressListener != null && !this.mProgressListener.reportProgress((this.mOffset * 1.0d) / this.mFileSize)) {
                return;
            }
        }
    }

    void readFrameAndComputeGain(InputStream stream, int frameIndex) throws IOException {
        int maxSfb;
        int scaleFactorGrouping;
        int maskPresent;
        int startBit;
        if (this.mFrameLens[frameIndex] < 4) {
            this.mFrameGains[frameIndex] = 0;
            stream.skip(this.mFrameLens[frameIndex]);
            return;
        }
        int initialOffset = this.mOffset;
        byte[] data = new byte[4];
        stream.read(data, 0, 4);
        this.mOffset += 4;
        switch ((data[0] & 224) >> 5) {
            case 0:
                this.mFrameGains[frameIndex] = ((data[0] & 1) << 7) | ((data[1] & 254) >> 1);
                break;
            case 1:
                int i = (data[1] & 16) >> 4;
                if (((data[1] & 96) >> 5) == 2) {
                    maxSfb = data[1] & 15;
                    scaleFactorGrouping = (data[2] & 254) >> 1;
                    maskPresent = ((data[2] & 1) << 1) | ((data[3] & 128) >> 7);
                    startBit = 25;
                } else {
                    maxSfb = ((data[1] & 15) << 2) | ((data[2] & 192) >> 6);
                    scaleFactorGrouping = -1;
                    maskPresent = (data[2] & 24) >> 3;
                    startBit = 21;
                }
                if (maskPresent == 1) {
                    int sfgZeroBitCount = 0;
                    for (int b = 0; b < 7; b++) {
                        if (((1 << b) & scaleFactorGrouping) == 0) {
                            sfgZeroBitCount++;
                        }
                    }
                    startBit += (sfgZeroBitCount + 1) * maxSfb;
                }
                int bytesNeeded = ((startBit + 7) / 8) + 1;
                byte[] data2 = new byte[bytesNeeded];
                data2[0] = data[0];
                data2[1] = data[1];
                data2[2] = data[2];
                data2[3] = data[3];
                stream.read(data2, 4, bytesNeeded - 4);
                this.mOffset += bytesNeeded - 4;
                int firstChannelGain = 0;
                for (int b2 = 0; b2 < 8; b2++) {
                    int b1 = 7 - ((b2 + startBit) % 8);
                    firstChannelGain += (((1 << b1) & data2[(b2 + startBit) / 8]) >> b1) << (7 - b2);
                }
                this.mFrameGains[frameIndex] = firstChannelGain;
                break;
            default:
                if (frameIndex <= 0) {
                    this.mFrameGains[frameIndex] = 0;
                    break;
                } else {
                    this.mFrameGains[frameIndex] = this.mFrameGains[frameIndex - 1];
                    break;
                }
        }
        int skip = this.mFrameLens[frameIndex] - (this.mOffset - initialOffset);
        stream.skip(skip);
        this.mOffset += skip;
    }

    public void StartAtom(FileOutputStream out, int atomType) throws IOException {
        int atomLen = this.mAtomMap.get(Integer.valueOf(atomType)).len;
        byte[] atomHeader = {(byte) ((atomLen >> 24) & 255), (byte) ((atomLen >> 16) & 255), (byte) ((atomLen >> 8) & 255), (byte) (atomLen & 255), (byte) ((atomType >> 24) & 255), (byte) ((atomType >> 16) & 255), (byte) ((atomType >> 8) & 255), (byte) (atomType & 255)};
        out.write(atomHeader, 0, 8);
    }

    public void WriteAtom(FileOutputStream out, int atomType) throws IOException {
        Atom atom = this.mAtomMap.get(Integer.valueOf(atomType));
        StartAtom(out, atomType);
        out.write(atom.data, 0, atom.len - 8);
    }

    public void SetAtomData(int atomType, byte[] data) {
        Atom atom = this.mAtomMap.get(Integer.valueOf(atomType));
        if (atom == null) {
            atom = new Atom();
            this.mAtomMap.put(Integer.valueOf(atomType), atom);
        }
        atom.len = data.length + 8;
        atom.data = data;
    }

    @Override // com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile.CheapSoundFile
    public void WriteFile(File outputFile, int startFrame, int numFrames) throws IOException {
        outputFile.createNewFile();
        FileInputStream in = new FileInputStream(this.mInputFile);
        FileOutputStream out = new FileOutputStream(outputFile);
        byte[] r13 = new byte[24];
        SetAtomData(kFTYP, r13);
        byte[] r132 = new byte[16];
        SetAtomData(kSTTS, r132);
        byte[] r133 = new byte[20];
        r133[7] = 1;
        r133[11] = 1;
        r133[12] = (byte) ((numFrames >> 24) & 255);
        r133[13] = (byte) ((numFrames >> 16) & 255);
        r133[14] = (byte) ((numFrames >> 8) & 255);
        r133[15] = (byte) (numFrames & 255);
        r133[19] = 1;
        SetAtomData(kSTSC, r133);
        byte[] stszData = new byte[(numFrames * 4) + 12];
        stszData[8] = (byte) ((numFrames >> 24) & 255);
        stszData[9] = (byte) ((numFrames >> 16) & 255);
        stszData[10] = (byte) ((numFrames >> 8) & 255);
        stszData[11] = (byte) (numFrames & 255);
        for (int i = 0; i < numFrames; i++) {
            stszData[(i * 4) + 12] = (byte) ((this.mFrameLens[startFrame + i] >> 24) & 255);
            stszData[(i * 4) + 13] = (byte) ((this.mFrameLens[startFrame + i] >> 16) & 255);
            stszData[(i * 4) + 14] = (byte) ((this.mFrameLens[startFrame + i] >> 8) & 255);
            stszData[(i * 4) + 15] = (byte) (this.mFrameLens[startFrame + i] & 255);
        }
        SetAtomData(kSTSZ, stszData);
        int mdatOffset = (numFrames * 4) + 144 + this.mAtomMap.get(Integer.valueOf((int) kSTSD)).len + this.mAtomMap.get(Integer.valueOf((int) kSTSC)).len + this.mAtomMap.get(Integer.valueOf((int) kMVHD)).len + this.mAtomMap.get(Integer.valueOf((int) kTKHD)).len + this.mAtomMap.get(Integer.valueOf((int) kMDHD)).len + this.mAtomMap.get(Integer.valueOf((int) kHDLR)).len + this.mAtomMap.get(Integer.valueOf((int) kSMHD)).len + this.mAtomMap.get(Integer.valueOf((int) kDINF)).len;
        byte[] r134 = new byte[12];
        r134[7] = 1;
        r134[8] = (byte) ((mdatOffset >> 24) & 255);
        r134[9] = (byte) ((mdatOffset >> 16) & 255);
        r134[10] = (byte) ((mdatOffset >> 8) & 255);
        r134[11] = (byte) (mdatOffset & 255);
        SetAtomData(kSTCO, r134);
        this.mAtomMap.get(Integer.valueOf((int) kSTBL)).len = this.mAtomMap.get(Integer.valueOf((int) kSTSZ)).len + this.mAtomMap.get(Integer.valueOf((int) kSTSD)).len + 8 + this.mAtomMap.get(Integer.valueOf((int) kSTTS)).len + this.mAtomMap.get(Integer.valueOf((int) kSTSC)).len + this.mAtomMap.get(Integer.valueOf((int) kSTCO)).len;
        this.mAtomMap.get(Integer.valueOf((int) kMINF)).len = this.mAtomMap.get(Integer.valueOf((int) kSMHD)).len + this.mAtomMap.get(Integer.valueOf((int) kDINF)).len + 8 + this.mAtomMap.get(Integer.valueOf((int) kSTBL)).len;
        this.mAtomMap.get(Integer.valueOf((int) kMDIA)).len = this.mAtomMap.get(Integer.valueOf((int) kHDLR)).len + this.mAtomMap.get(Integer.valueOf((int) kMDHD)).len + 8 + this.mAtomMap.get(Integer.valueOf((int) kMINF)).len;
        this.mAtomMap.get(Integer.valueOf((int) kTRAK)).len = this.mAtomMap.get(Integer.valueOf((int) kTKHD)).len + 8 + this.mAtomMap.get(Integer.valueOf((int) kMDIA)).len;
        this.mAtomMap.get(Integer.valueOf((int) kMOOV)).len = this.mAtomMap.get(Integer.valueOf((int) kMVHD)).len + 8 + this.mAtomMap.get(Integer.valueOf((int) kTRAK)).len;
        int mdatLen = 8;
        for (int i2 = 0; i2 < numFrames; i2++) {
            mdatLen += this.mFrameLens[startFrame + i2];
        }
        this.mAtomMap.get(Integer.valueOf((int) kMDAT)).len = mdatLen;
        WriteAtom(out, kFTYP);
        StartAtom(out, kMOOV);
        WriteAtom(out, kMVHD);
        StartAtom(out, kTRAK);
        WriteAtom(out, kTKHD);
        StartAtom(out, kMDIA);
        WriteAtom(out, kMDHD);
        WriteAtom(out, kHDLR);
        StartAtom(out, kMINF);
        WriteAtom(out, kDINF);
        WriteAtom(out, kSMHD);
        StartAtom(out, kSTBL);
        WriteAtom(out, kSTSD);
        WriteAtom(out, kSTTS);
        WriteAtom(out, kSTSC);
        WriteAtom(out, kSTSZ);
        WriteAtom(out, kSTCO);
        StartAtom(out, kMDAT);
        int maxFrameLen = 0;
        for (int i3 = 0; i3 < numFrames; i3++) {
            if (this.mFrameLens[startFrame + i3] > maxFrameLen) {
                maxFrameLen = this.mFrameLens[startFrame + i3];
            }
        }
        byte[] buffer = new byte[maxFrameLen];
        int pos = 0;
        for (int i4 = 0; i4 < numFrames; i4++) {
            int skip = this.mFrameOffsets[startFrame + i4] - pos;
            int len = this.mFrameLens[startFrame + i4];
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
