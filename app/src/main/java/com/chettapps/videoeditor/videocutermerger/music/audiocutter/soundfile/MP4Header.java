package com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile;

import com.coremedia.iso.boxes.DataEntryUrlBox;
import com.coremedia.iso.boxes.DataInformationBox;
import com.coremedia.iso.boxes.DataReferenceBox;
import com.coremedia.iso.boxes.FileTypeBox;
import com.coremedia.iso.boxes.HandlerBox;
import com.coremedia.iso.boxes.MediaBox;
import com.coremedia.iso.boxes.MediaHeaderBox;
import com.coremedia.iso.boxes.MediaInformationBox;
import com.coremedia.iso.boxes.MovieBox;
import com.coremedia.iso.boxes.MovieHeaderBox;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.SampleSizeBox;
import com.coremedia.iso.boxes.SampleTableBox;
import com.coremedia.iso.boxes.SampleToChunkBox;
import com.coremedia.iso.boxes.SoundMediaHeaderBox;
import com.coremedia.iso.boxes.StaticChunkOffsetBox;
import com.coremedia.iso.boxes.TimeToSampleBox;
import com.coremedia.iso.boxes.TrackBox;
import com.coremedia.iso.boxes.TrackHeaderBox;
import com.coremedia.iso.boxes.mdat.MediaDataBox;
import com.coremedia.iso.boxes.sampleentry.AudioSampleEntry;
import com.googlecode.mp4parser.boxes.mp4.ESDescriptorBox;



/* loaded from: classes.dex */
public class MP4Header {
    private byte[] ESDescriptor_top;
    private int mBitrate;
    private int mChannels;
    private byte[] mDurationMS;
    private int[] mFrameSize;
    private byte[] mHeader;
    private int mMaxFrameSize;
    private byte[] mNumSamples;
    private int mSampleRate;
    private byte[] mTime;
    private int mTotSize;

    public MP4Header(int sampleRate, int numChannels, int[] frame_size, int bitrate) {
        if (frame_size != null && frame_size.length >= 2 && frame_size[0] == 2) {
            this.mSampleRate = sampleRate;
            this.mChannels = numChannels;
            this.mFrameSize = frame_size;
            this.mBitrate = bitrate;
            this.mMaxFrameSize = this.mFrameSize[0];
            this.mTotSize = this.mFrameSize[0];
            for (int i = 1; i < this.mFrameSize.length; i++) {
                if (this.mMaxFrameSize < this.mFrameSize[i]) {
                    this.mMaxFrameSize = this.mFrameSize[i];
                }
                this.mTotSize += this.mFrameSize[i];
            }
            long time = (System.currentTimeMillis() / 1000) + 2082758400;
            this.mTime = new byte[4];
            this.mTime[0] = (byte) ((time >> 24) & 255);
            this.mTime[1] = (byte) ((time >> 16) & 255);
            this.mTime[2] = (byte) ((time >> 8) & 255);
            this.mTime[3] = (byte) (255 & time);
            int numSamples = (frame_size.length - 1) * 1024;
            int durationMS = (numSamples * 1000) / this.mSampleRate;
            durationMS = (numSamples * 1000) % this.mSampleRate > 0 ? durationMS + 1 : durationMS;
            this.mNumSamples = new byte[]{(byte) ((numSamples >> 26) & 255), (byte) ((numSamples >> 16) & 255), (byte) ((numSamples >> 8) & 255), (byte) (numSamples & 255)};
            this.mDurationMS = new byte[]{(byte) ((durationMS >> 26) & 255), (byte) ((durationMS >> 16) & 255), (byte) ((durationMS >> 8) & 255), (byte) (durationMS & 255)};
            setHeader();
        }
    }

    public byte[] getMP4Header() {
        return this.mHeader;
    }

    public static byte[] getMP4Header(int sampleRate, int numChannels, int[] frame_size, int bitrate) {
        return new MP4Header(sampleRate, numChannels, frame_size, bitrate).mHeader;
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
        Atom[] atomArr;
        Atom a_ftyp = getFTYPAtom();
        Atom a_moov = getMOOVAtom();
        Atom a_mdat = new Atom(MediaDataBox.TYPE);
        Atom a_stco = a_moov.getChild("trak.mdia.minf.stbl.stco");
        if (a_stco == null) {
            this.mHeader = null;
            return;
        }
        byte[] data = a_stco.getData();
        int chunk_offset = a_ftyp.getSize() + a_moov.getSize() + a_mdat.getSize();
        int length = data.length - 4;
        int i = length + 1;
        data[length] = (byte) ((chunk_offset >> 24) & 255);
        int length2 = i + 1;
        data[i] = (byte) ((chunk_offset >> 16) & 255);
        int i2 = length2 + 1;
        data[length2] = (byte) ((chunk_offset >> 8) & 255);
        int length3 = i2 + 1;
        data[i2] = (byte) (chunk_offset & 255);
        byte[] header = new byte[chunk_offset];
        int length4 = 0;
        for (Atom atom : new Atom[]{a_ftyp, a_moov, a_mdat}) {
            byte[] atom_bytes = atom.getBytes();
            System.arraycopy(atom_bytes, 0, header, length4, atom_bytes.length);
            length4 += atom_bytes.length;
        }
        int size = this.mTotSize + 8;
        int length5 = length4 - 8;
        int i3 = length5 + 1;
        header[length5] = (byte) ((size >> 24) & 255);
        int length6 = i3 + 1;
        header[i3] = (byte) ((size >> 16) & 255);
        int i4 = length6 + 1;
        header[length6] = (byte) ((size >> 8) & 255);
        int length7 = i4 + 1;
        header[i4] = (byte) (size & 255);
        this.mHeader = header;
    }

    private Atom getFTYPAtom() {
        Atom atom = new Atom(FileTypeBox.TYPE);
        byte[] bArr = new byte[20];
        bArr[0] = 77;
        bArr[1] = 52;
        bArr[2] = 65;
        bArr[3] = 32;
        bArr[8] = 77;
        bArr[9] = 52;
        bArr[10] = 65;
        bArr[11] = 32;
        bArr[12] = 109;
        bArr[13] = 112;
        bArr[14] = 52;
        bArr[15] = 50;
        bArr[16] = 105;
        bArr[17] = 115;
        bArr[18] = 111;
        bArr[19] = 109;
        atom.setData(bArr);
        return atom;
    }

    private Atom getMOOVAtom() {
        Atom atom = new Atom(MovieBox.TYPE);
        atom.addChild(getMVHDAtom());
        atom.addChild(getTRAKAtom());
        return atom;
    }

    private Atom getMVHDAtom() {
        Atom atom = new Atom(MovieHeaderBox.TYPE, (byte) 0, 0);
        byte[] bArr = new byte[96];
        bArr[0] = this.mTime[0];
        bArr[1] = this.mTime[1];
        bArr[2] = this.mTime[2];
        bArr[3] = this.mTime[3];
        bArr[4] = this.mTime[0];
        bArr[5] = this.mTime[1];
        bArr[6] = this.mTime[2];
        bArr[7] = this.mTime[3];
        bArr[10] = 3;
        bArr[11] = -24;
        bArr[12] = this.mDurationMS[0];
        bArr[13] = this.mDurationMS[1];
        bArr[14] = this.mDurationMS[2];
        bArr[15] = this.mDurationMS[3];
        bArr[17] = 1;
        bArr[20] = 1;
        bArr[33] = 1;
        bArr[49] = 1;
        bArr[64] = 64;
        bArr[95] = 2;
        atom.setData(bArr);
        return atom;
    }

    private Atom getTRAKAtom() {
        Atom atom = new Atom(TrackBox.TYPE);
        atom.addChild(getTKHDAtom());
        atom.addChild(getMDIAAtom());
        return atom;
    }

    private Atom getTKHDAtom() {
        Atom atom = new Atom(TrackHeaderBox.TYPE, (byte) 0, 7);
        byte[] bArr = new byte[80];
        bArr[0] = this.mTime[0];
        bArr[1] = this.mTime[1];
        bArr[2] = this.mTime[2];
        bArr[3] = this.mTime[3];
        bArr[4] = this.mTime[0];
        bArr[5] = this.mTime[1];
        bArr[6] = this.mTime[2];
        bArr[7] = this.mTime[3];
        bArr[11] = 1;
        bArr[16] = this.mDurationMS[0];
        bArr[17] = this.mDurationMS[1];
        bArr[18] = this.mDurationMS[2];
        bArr[19] = this.mDurationMS[3];
        bArr[32] = 1;
        bArr[37] = 1;
        bArr[53] = 1;
        bArr[68] = 64;
        atom.setData(bArr);
        return atom;
    }

    private Atom getMDIAAtom() {
        Atom atom = new Atom(MediaBox.TYPE);
        atom.addChild(getMDHDAtom());
        atom.addChild(getHDLRAtom());
        atom.addChild(getMINFAtom());
        return atom;
    }

    private Atom getMDHDAtom() {
        Atom atom = new Atom(MediaHeaderBox.TYPE, (byte) 0, 0);
        byte[] bArr = new byte[20];
        bArr[0] = this.mTime[0];
        bArr[1] = this.mTime[1];
        bArr[2] = this.mTime[2];
        bArr[3] = this.mTime[3];
        bArr[4] = this.mTime[0];
        bArr[5] = this.mTime[1];
        bArr[6] = this.mTime[2];
        bArr[7] = this.mTime[3];
        bArr[8] = (byte) (this.mSampleRate >> 24);
        bArr[9] = (byte) (this.mSampleRate >> 16);
        bArr[10] = (byte) (this.mSampleRate >> 8);
        bArr[11] = (byte) this.mSampleRate;
        bArr[12] = this.mNumSamples[0];
        bArr[13] = this.mNumSamples[1];
        bArr[14] = this.mNumSamples[2];
        bArr[15] = this.mNumSamples[3];
        atom.setData(bArr);
        return atom;
    }

    private Atom getHDLRAtom() {
        Atom atom = new Atom(HandlerBox.TYPE, (byte) 0, 0);
        byte[] bArr = new byte[32];
        bArr[4] = 115;
        bArr[5] = 111;
        bArr[6] = 117;
        bArr[7] = 110;
        bArr[20] = 83;
        bArr[21] = 111;
        bArr[22] = 117;
        bArr[23] = 110;
        bArr[24] = 100;
        bArr[25] = 72;
        bArr[26] = 97;
        bArr[27] = 110;
        bArr[28] = 100;
        bArr[29] = 108;
        bArr[30] = 101;
        atom.setData(bArr);
        return atom;
    }

    private Atom getMINFAtom() {
        Atom atom = new Atom(MediaInformationBox.TYPE);
        atom.addChild(getSMHDAtom());
        atom.addChild(getDINFAtom());
        atom.addChild(getSTBLAtom());
        return atom;
    }

    private Atom getSMHDAtom() {
        Atom atom = new Atom(SoundMediaHeaderBox.TYPE, (byte) 0, 0);
        atom.setData(new byte[4]);
        return atom;
    }

    private Atom getDINFAtom() {
        Atom atom = new Atom(DataInformationBox.TYPE);
        atom.addChild(getDREFAtom());
        return atom;
    }

    private Atom getDREFAtom() {
        Atom atom = new Atom(DataReferenceBox.TYPE, (byte) 0, 0);
        byte[] url = getURLAtom().getBytes();
        byte[] data = new byte[url.length + 4];
        data[3] = 1;
        System.arraycopy(url, 0, data, 4, url.length);
        atom.setData(data);
        return atom;
    }

    private Atom getURLAtom() {
        return new Atom(DataEntryUrlBox.TYPE, (byte) 0, 1);
    }

    private Atom getSTBLAtom() {
        Atom atom = new Atom(SampleTableBox.TYPE);
        atom.addChild(getSTSDAtom());
        atom.addChild(getSTTSAtom());
        atom.addChild(getSTSCAtom());
        atom.addChild(getSTSZAtom());
        atom.addChild(getSTCOAtom());
        return atom;
    }

    private Atom getSTSDAtom() {
        Atom atom = new Atom(SampleDescriptionBox.TYPE, (byte) 0, 0);
        byte[] mp4a = getMP4AAtom().getBytes();
        byte[] data = new byte[mp4a.length + 4];
        data[3] = 1;
        System.arraycopy(mp4a, 0, data, 4, mp4a.length);
        atom.setData(data);
        return atom;
    }

    private Atom getMP4AAtom() {
        Atom atom = new Atom(AudioSampleEntry.TYPE3);
        byte[] ase = new byte[28];
        ase[7] = 1;
        ase[16] = (byte) (this.mChannels >> 8);
        ase[17] = (byte) this.mChannels;
        ase[19] = 16;
        ase[24] = (byte) (this.mSampleRate >> 8);
        ase[25] = (byte) this.mSampleRate;
        byte[] esds = getESDSAtom().getBytes();
        byte[] data = new byte[ase.length + esds.length];
        System.arraycopy(ase, 0, data, 0, ase.length);
        System.arraycopy(esds, 0, data, ase.length, esds.length);
        atom.setData(data);
        return atom;
    }

    private Atom getESDSAtom() {
        Atom atom = new Atom(ESDescriptorBox.TYPE, (byte) 0, 0);
        atom.setData(getESDescriptor());
        return atom;
    }

    private byte[] getESDescriptor() {
        int[] samplingFrequencies = {96000, 88200, 64000, 48000, 44100, 32000, 24000, 22050, 16000, 12000, 11025, 8000, 7350};
        this.ESDescriptor_top = new byte[5];
        byte[] decConfigDescr_top = {4, 17, 64, 21};
        byte[] audioSpecificConfig = new byte[4];
        audioSpecificConfig[0] = 5;
        audioSpecificConfig[1] = 2;
        audioSpecificConfig[2] = 16;
        byte[] slConfigDescr = {6, 1, 2};
        int bufferSize = 768;
        while (bufferSize < this.mMaxFrameSize * 2) {
            bufferSize += 256;
        }
        byte[] decConfigDescr = new byte[decConfigDescr_top[1] + 2];
        System.arraycopy(decConfigDescr_top, 0, decConfigDescr, 0, decConfigDescr_top.length);
        int length = decConfigDescr_top.length;
        int i = length + 1;
        decConfigDescr[length] = (byte) ((bufferSize >> 16) & 255);
        int length2 = i + 1;
        decConfigDescr[i] = (byte) ((bufferSize >> 8) & 255);
        int i2 = length2 + 1;
        decConfigDescr[length2] = (byte) (bufferSize & 255);
        int length3 = i2 + 1;
        decConfigDescr[i2] = (byte) ((this.mBitrate >> 24) & 255);
        int i3 = length3 + 1;
        decConfigDescr[length3] = (byte) ((this.mBitrate >> 16) & 255);
        int length4 = i3 + 1;
        decConfigDescr[i3] = (byte) ((this.mBitrate >> 8) & 255);
        int i4 = length4 + 1;
        decConfigDescr[length4] = (byte) (this.mBitrate & 255);
        int length5 = i4 + 1;
        decConfigDescr[i4] = (byte) ((this.mBitrate >> 24) & 255);
        int i5 = length5 + 1;
        decConfigDescr[length5] = (byte) ((this.mBitrate >> 16) & 255);
        int length6 = i5 + 1;
        decConfigDescr[i5] = (byte) ((this.mBitrate >> 8) & 255);
        int i6 = length6 + 1;
        decConfigDescr[length6] = (byte) (this.mBitrate & 255);
        int index = 0;
        while (index < samplingFrequencies.length && samplingFrequencies[index] != this.mSampleRate) {
            index++;
        }
        if (index == samplingFrequencies.length) {
            index = 4;
        }
        audioSpecificConfig[2] = (byte) (audioSpecificConfig[2] | ((byte) ((index >> 1) & 7)));
        audioSpecificConfig[3] = (byte) (audioSpecificConfig[3] | ((byte) (((index & 1) << 7) | ((this.mChannels & 15) << 3))));
        System.arraycopy(audioSpecificConfig, 0, decConfigDescr, i6, audioSpecificConfig.length);
        byte[] ESDescriptor = new byte[this.ESDescriptor_top[1] + 2];
        System.arraycopy(this.ESDescriptor_top, 0, ESDescriptor, 0, this.ESDescriptor_top.length);
        int length7 = this.ESDescriptor_top.length;
        System.arraycopy(decConfigDescr, 0, ESDescriptor, length7, decConfigDescr.length);
        System.arraycopy(slConfigDescr, 0, ESDescriptor, decConfigDescr.length + length7, slConfigDescr.length);
        return ESDescriptor;
    }

    private Atom getSTTSAtom() {
        Atom atom = new Atom(TimeToSampleBox.TYPE, (byte) 0, 0);
        int numAudioFrames = this.mFrameSize.length - 1;
        byte[] bArr = new byte[20];
        bArr[3] = 2;
        bArr[7] = 1;
        bArr[12] = (byte) ((numAudioFrames >> 24) & 255);
        bArr[13] = (byte) ((numAudioFrames >> 16) & 255);
        bArr[14] = (byte) ((numAudioFrames >> 8) & 255);
        bArr[15] = (byte) (numAudioFrames & 255);
        bArr[18] = 4;
        atom.setData(bArr);
        return atom;
    }


    private Atom getSTSCAtom() {
        Atom atom = new Atom(SampleToChunkBox.TYPE, (byte) 0, 0);
        int numFrames = this.mFrameSize.length;
        byte[] bArr = new byte[16];
        bArr[3] = 1;
        bArr[7] = 1;
        bArr[8] = (byte) ((numFrames >> 24) & 255);
        bArr[9] = (byte) ((numFrames >> 16) & 255);
        bArr[10] = (byte) ((numFrames >> 8) & 255);
        bArr[11] = (byte) (numFrames & 255);
        bArr[15] = 1;
        atom.setData(bArr);
        return atom;
    }


    private Atom getSTSZAtom() {
        Atom atom = new Atom(SampleSizeBox.TYPE, (byte) 0, 0);
        int numFrames = this.mFrameSize.length;
        byte[] data = new byte[(numFrames * 4) + 8];
        data[0] = 0;
        int offset2 = 1 + 1;
        data[1] = 0;
        int offset = offset2 + 1;
        data[offset2] = 0;
        int offset22 = offset + 1;
        data[offset] = 0;
        int offset3 = offset22 + 1;
        data[offset22] = (byte) ((numFrames >> 24) & 255);
        int offset23 = offset3 + 1;
        data[offset3] = (byte) ((numFrames >> 16) & 255);
        int offset4 = offset23 + 1;
        data[offset23] = (byte) ((numFrames >> 8) & 255);
        data[offset4] = (byte) (numFrames & 255);
        int[] iArr = this.mFrameSize;
        int offset5 = offset4 + 1;
        for (int size : iArr) {
            int offset24 = offset5 + 1;
            data[offset5] = (byte) ((size >> 24) & 255);
            int offset6 = offset24 + 1;
            data[offset24] = (byte) ((size >> 16) & 255);
            int offset25 = offset6 + 1;
            data[offset6] = (byte) ((size >> 8) & 255);
            offset5 = offset25 + 1;
            data[offset25] = (byte) (size & 255);
        }
        atom.setData(data);
        return atom;
    }

    private Atom getSTCOAtom() {
        Atom atom = new Atom(StaticChunkOffsetBox.TYPE, (byte) 0, 0);
        byte[] bArr = new byte[8];
        bArr[3] = 1;
        atom.setData(bArr);
        return atom;
    }
}
