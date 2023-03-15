package com.chettapps.videoeditor.videocutermerger.music.audiocutter.soundfile;

import android.annotation.SuppressLint;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;

import com.google.android.exoplayer2.util.MimeTypes;

import org.apache.commons.io.IOUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.Arrays;

/* loaded from: classes.dex */
public class SoundFile {
    private int mAvgBitRate;
    private int mChannels;
    private ByteBuffer mDecodedBytes;
    private ShortBuffer mDecodedSamples;
    private int mFileSize;
    private String mFileType;
    private int[] mFrameGains;
    private int[] mFrameLens;
    private int[] mFrameOffsets;
    private int mNumFrames;
    private int mNumSamples;
    private int mSampleRate;
    private File mInputFile = null;
    private ProgressListener mProgressListener = null;

    /* loaded from: classes.dex */
    public interface ProgressListener {
        boolean reportProgress(double d);
    }

    /* loaded from: classes.dex */
    public class InvalidInputException extends Exception {
        private static final long serialVersionUID = -2505698991597837165L;

        public InvalidInputException(String message) {
            super(message);
        }
    }

    public static String[] getSupportedExtensions() {
        return new String[]{"mp3", "wav", "3gpp", "3gp", "amr", "aac", "m4a", "ogg"};
    }

    public static boolean isFilenameSupported(String filename) {
        String[] extensions = getSupportedExtensions();
        for (String str : extensions) {
            if (filename.endsWith("." + str)) {
                return true;
            }
        }
        return false;
    }

    public static SoundFile create(String fileName, ProgressListener progressListener) throws FileNotFoundException, IOException, InvalidInputException {
        File f = new File(fileName);
        if (f.exists()) {
            String[] components = f.getName().toLowerCase().split("\\.");
            if (components.length < 2 || !Arrays.asList(getSupportedExtensions()).contains(components[components.length - 1])) {
                return null;
            }
            SoundFile soundFile = new SoundFile();
            soundFile.setProgressListener(progressListener);
            soundFile.ReadFile(f);
            return soundFile;
        }
        throw new FileNotFoundException(fileName);
    }

    public static SoundFile record(ProgressListener progressListener) {
        if (progressListener == null) {
            return null;
        }
        SoundFile soundFile = new SoundFile();
        soundFile.setProgressListener(progressListener);
        soundFile.RecordAudio();
        return soundFile;
    }

    public String getFiletype() {
        return this.mFileType;
    }

    public int getFileSizeBytes() {
        return this.mFileSize;
    }

    public int getAvgBitrateKbps() {
        return this.mAvgBitRate;
    }

    public int getSampleRate() {
        return this.mSampleRate;
    }

    public int getChannels() {
        return this.mChannels;
    }

    public int getNumSamples() {
        return this.mNumSamples;
    }

    public int getNumFrames() {
        return this.mNumFrames;
    }

    public int getSamplesPerFrame() {
        return 1024;
    }

    public int[] getFrameGains() {
        return this.mFrameGains;
    }

    public ShortBuffer getSamples() {
        if (this.mDecodedSamples == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT < 24 || Build.VERSION.SDK_INT > 25) {
            return this.mDecodedSamples.asReadOnlyBuffer();
        }
        return this.mDecodedSamples;
    }

    private SoundFile() {
    }

    private void setProgressListener(ProgressListener progressListener) {
        this.mProgressListener = progressListener;
    }

    private void ReadFile(File inputFile) throws FileNotFoundException, IOException, InvalidInputException {
        MediaExtractor extractor = new MediaExtractor();
        MediaFormat format = null;
        this.mInputFile = inputFile;
        String[] components = this.mInputFile.getPath().split("\\.");
        this.mFileType = components[components.length - 1];
        this.mFileSize = (int) this.mInputFile.length();
        extractor.setDataSource(this.mInputFile.getPath());
        int numTracks = extractor.getTrackCount();
        int i = 0;
        while (true) {
            if (i >= numTracks) {
                break;
            }
            format = extractor.getTrackFormat(i);
            if (format.getString("mime").startsWith("audio/")) {
                extractor.selectTrack(i);
                break;
            }
            i++;
        }
        if (i == numTracks) {
            throw new InvalidInputException("No audio track found in " + this.mInputFile);
        }
        this.mChannels = format.getInteger("channel-count");
        this.mSampleRate = format.getInteger("sample-rate");
        int expectedNumSamples = (int) (((((float) format.getLong("durationUs")) / 1000000.0f) * this.mSampleRate) + 0.5f);
        MediaCodec codec = MediaCodec.createDecoderByType(format.getString("mime"));
        codec.configure(format, (Surface) null, (MediaCrypto) null, 0);
        codec.start();
        int decodedSamplesSize = 0;
        byte[] decodedSamples = null;
        ByteBuffer[] inputBuffers = codec.getInputBuffers();
        ByteBuffer[] outputBuffers = codec.getOutputBuffers();
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        int tot_size_read = 0;
        boolean done_reading = false;
        this.mDecodedBytes = ByteBuffer.allocate(1048576);
        Boolean firstSampleData = true;
        do {
            int inputBufferIndex = codec.dequeueInputBuffer(100L);
            if (!done_reading && inputBufferIndex >= 0) {
                int sample_size = extractor.readSampleData(inputBuffers[inputBufferIndex], 0);
                if (firstSampleData.booleanValue() && format.getString("mime").equals(MimeTypes.AUDIO_AAC) && sample_size == 2) {
                    extractor.advance();
                    tot_size_read += sample_size;
                    Boolean.valueOf(false);
                }
                if (sample_size < 0) {
                    codec.queueInputBuffer(inputBufferIndex, 0, 0, -1L, 4);
                    done_reading = true;
                } else {
                    codec.queueInputBuffer(inputBufferIndex, 0, sample_size, extractor.getSampleTime(), 0);
                    extractor.advance();
                    tot_size_read += sample_size;
                    if (this.mProgressListener != null && !this.mProgressListener.reportProgress(tot_size_read / this.mFileSize)) {
                        extractor.release();
                        codec.stop();
                        codec.release();
                        return;
                    }
                }
                firstSampleData = false;
            }
            int outputBufferIndex = codec.dequeueOutputBuffer(info, 100L);
            if (outputBufferIndex < 0 || info.size <= 0) {
                if (outputBufferIndex == -3) {
                    outputBuffers = codec.getOutputBuffers();
                }
                if ((info.flags & 4) == 0) {
                    break;
                }
            } else {
                if (decodedSamplesSize < info.size) {
                    decodedSamplesSize = info.size;
                    decodedSamples = new byte[decodedSamplesSize];
                }
                outputBuffers[outputBufferIndex].get(decodedSamples, 0, info.size);
                outputBuffers[outputBufferIndex].clear();
                if (this.mDecodedBytes.remaining() < info.size) {
                    int position = this.mDecodedBytes.position();
                    int newSize = (int) (position * ((1.0d * this.mFileSize) / tot_size_read) * 1.2d);
                    if (newSize - position < info.size + 5242880) {
                        newSize = info.size + position + 5242880;
                    }
                    ByteBuffer newDecodedBytes = null;
                    int retry = 10;
                    while (retry > 0) {
                        try {
                            newDecodedBytes = ByteBuffer.allocate(newSize);
                            break;
                        } catch (OutOfMemoryError e) {
                            retry--;
                        }
                    }
                    if (retry == 0) {
                        break;
                    }
                    this.mDecodedBytes.rewind();
                    newDecodedBytes.put(this.mDecodedBytes);
                    this.mDecodedBytes = newDecodedBytes;
                    this.mDecodedBytes.position(position);
                }
                this.mDecodedBytes.put(decodedSamples, 0, info.size);
                codec.releaseOutputBuffer(outputBufferIndex, false);
                if ((info.flags & 4) == 0) {
                    break;
                }
            }
        } while (this.mDecodedBytes.position() / (this.mChannels * 2) < expectedNumSamples);
        this.mNumSamples = this.mDecodedBytes.position() / (this.mChannels * 2);
        this.mDecodedBytes.rewind();
        this.mDecodedBytes.order(ByteOrder.LITTLE_ENDIAN);
        this.mDecodedSamples = this.mDecodedBytes.asShortBuffer();
        this.mAvgBitRate = (int) (((this.mFileSize * 8) * (this.mSampleRate / this.mNumSamples)) / 1000.0f);
        extractor.release();
        codec.stop();
        codec.release();
        this.mNumFrames = this.mNumSamples / getSamplesPerFrame();
        if (this.mNumSamples % getSamplesPerFrame() != 0) {
            this.mNumFrames++;
        }
        this.mFrameGains = new int[this.mNumFrames];
        this.mFrameLens = new int[this.mNumFrames];
        this.mFrameOffsets = new int[this.mNumFrames];
        int frameLens = (int) (((this.mAvgBitRate * 1000) / 8) * (getSamplesPerFrame() / this.mSampleRate));
        for (int i2 = 0; i2 < this.mNumFrames; i2++) {
            int gain = -1;
            for (int j = 0; j < getSamplesPerFrame(); j++) {
                int value = 0;
                for (int k = 0; k < this.mChannels; k++) {
                    if (this.mDecodedSamples.remaining() > 0) {
                        value += Math.abs((int) this.mDecodedSamples.get());
                    }
                }
                int value2 = value / this.mChannels;
                if (gain < value2) {
                    gain = value2;
                }
            }
            this.mFrameGains[i2] = (int) Math.sqrt(gain);
            this.mFrameLens[i2] = frameLens;
            this.mFrameOffsets[i2] = (int) (((this.mAvgBitRate * 1000) / 8) * i2 * (getSamplesPerFrame() / this.mSampleRate));
        }
        this.mDecodedSamples.rewind();
    }

    private void RecordAudio() {
        int value;
        if (this.mProgressListener != null) {
            this.mInputFile = null;
            this.mFileType = "raw";
            this.mFileSize = 0;
            this.mSampleRate = 44100;
            this.mChannels = 1;
            short[] buffer = new short[1024];
            int minBufferSize = AudioRecord.getMinBufferSize(this.mSampleRate, 16, 2);
            if (minBufferSize < this.mSampleRate * 2) {
                minBufferSize = this.mSampleRate * 2;
            }
            @SuppressLint("MissingPermission") AudioRecord audioRecord = new AudioRecord(0, this.mSampleRate, 16, 2, minBufferSize);
            this.mDecodedBytes = ByteBuffer.allocate(this.mSampleRate * 20 * 2);
            this.mDecodedBytes.order(ByteOrder.LITTLE_ENDIAN);
            this.mDecodedSamples = this.mDecodedBytes.asShortBuffer();
            audioRecord.startRecording();
            do {
                if (this.mDecodedSamples.remaining() < 1024) {
                    try {
                        ByteBuffer newDecodedBytes = ByteBuffer.allocate(this.mDecodedBytes.capacity() + (this.mSampleRate * 10 * 2));
                        int position = this.mDecodedSamples.position();
                        this.mDecodedBytes.rewind();
                        newDecodedBytes.put(this.mDecodedBytes);
                        this.mDecodedBytes = newDecodedBytes;
                        this.mDecodedBytes.order(ByteOrder.LITTLE_ENDIAN);
                        this.mDecodedBytes.rewind();
                        this.mDecodedSamples = this.mDecodedBytes.asShortBuffer();
                        this.mDecodedSamples.position(position);
                    } catch (OutOfMemoryError e) {
                    }
                }
                audioRecord.read(buffer, 0, buffer.length);
                this.mDecodedSamples.put(buffer);
            } while (this.mProgressListener.reportProgress(this.mDecodedSamples.position() / this.mSampleRate));
            audioRecord.stop();
            audioRecord.release();
            this.mNumSamples = this.mDecodedSamples.position();
            this.mDecodedSamples.rewind();
            this.mDecodedBytes.rewind();
            this.mAvgBitRate = (this.mSampleRate * 16) / 1000;
            this.mNumFrames = this.mNumSamples / getSamplesPerFrame();
            if (this.mNumSamples % getSamplesPerFrame() != 0) {
                this.mNumFrames++;
            }
            this.mFrameGains = new int[this.mNumFrames];
            this.mFrameLens = null;
            this.mFrameOffsets = null;
            for (int i = 0; i < this.mNumFrames; i++) {
                int gain = -1;
                for (int j = 0; j < getSamplesPerFrame(); j++) {
                    if (this.mDecodedSamples.remaining() > 0) {
                        value = Math.abs((int) this.mDecodedSamples.get());
                    } else {
                        value = 0;
                    }
                    if (gain < value) {
                        gain = value;
                    }
                }
                this.mFrameGains[i] = (int) Math.sqrt(gain);
            }
            this.mDecodedSamples.rewind();
        }
    }

    public void WriteFile(File outputFile, int startFrame, int numFrames) throws IOException {
        WriteFile(outputFile, (startFrame * getSamplesPerFrame()) / this.mSampleRate, ((startFrame + numFrames) * getSamplesPerFrame()) / this.mSampleRate);
    }

    public void WriteFile(File outputFile, float startTime, float endTime) throws IOException {
        int numChannels;
        long presentation_time2;
        int startOffset = ((int) (this.mSampleRate * startTime)) * 2 * this.mChannels;
        int numSamples = (int) ((endTime - startTime) * this.mSampleRate);
        if (this.mChannels == 1) {
            numChannels = 2;
        } else {
            numChannels = this.mChannels;
        }
        int bitrate = 64000 * numChannels;
        MediaCodec codec = MediaCodec.createEncoderByType(MimeTypes.AUDIO_AAC);
        MediaFormat format = MediaFormat.createAudioFormat(MimeTypes.AUDIO_AAC, this.mSampleRate, numChannels);
        format.setInteger("bitrate", bitrate);
        codec.configure(format, (Surface) null, (MediaCrypto) null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        codec.start();
        int estimatedEncodedSize = (int) ((endTime - startTime) * (bitrate / 8) * 1.1d);
        ByteBuffer encodedBytes = ByteBuffer.allocate(estimatedEncodedSize);
        ByteBuffer[] inputBuffers = codec.getInputBuffers();
        ByteBuffer[] outputBuffers = codec.getOutputBuffers();
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        boolean done_reading = false;
        byte[] buffer = new byte[numChannels * 1024 * 2];
        this.mDecodedBytes.position(startOffset);
        int numSamples2 = numSamples + 2048;
        int tot_num_frames = (numSamples2 / 1024) + 1;
        if (numSamples2 % 1024 != 0) {
            tot_num_frames++;
        }
        int[] frame_sizes = new int[tot_num_frames];
        int num_out_frames = 0;
        int num_frames = 0;
        int num_samples_left = numSamples2;
        int encodedSamplesSize = 0;
        byte[] encodedSamples = null;
        long presentation_time = 0;
        while (true) {
            int inputBufferIndex = codec.dequeueInputBuffer(100L);
            if (done_reading || inputBufferIndex < 0) {
                presentation_time2 = presentation_time;
            } else if (num_samples_left <= 0) {
                codec.queueInputBuffer(inputBufferIndex, 0, 0, -1L, 4);
                done_reading = true;
                presentation_time2 = presentation_time;
            } else {
                inputBuffers[inputBufferIndex].clear();
                if (buffer.length > inputBuffers[inputBufferIndex].remaining()) {
                    continue;
                } else {
                    int bufferSize = this.mChannels == 1 ? buffer.length / 2 : buffer.length;
                    if (this.mDecodedBytes.remaining() < bufferSize) {
                        for (int i = this.mDecodedBytes.remaining(); i < bufferSize; i++) {
                            buffer[i] = 0;
                        }
                        this.mDecodedBytes.get(buffer, 0, this.mDecodedBytes.remaining());
                    } else {
                        this.mDecodedBytes.get(buffer, 0, bufferSize);
                    }
                    if (this.mChannels == 1) {
                        for (int i2 = bufferSize - 1; i2 >= 1; i2 -= 2) {
                            buffer[(i2 * 2) + 1] = buffer[i2];
                            buffer[i2 * 2] = buffer[i2 - 1];
                            buffer[(i2 * 2) - 1] = buffer[(i2 * 2) + 1];
                            buffer[(i2 * 2) - 2] = buffer[i2 * 2];
                        }
                    }
                    num_samples_left -= 1024;
                    inputBuffers[inputBufferIndex].put(buffer);
                    int num_frames2 = num_frames + 1;
                    presentation_time2 = (long) (((num_frames * 1024) * 1000000.0d) / this.mSampleRate);
                    codec.queueInputBuffer(inputBufferIndex, 0, buffer.length, presentation_time2, 0);
                    num_frames = num_frames2;
                }
            }
            int outputBufferIndex = codec.dequeueOutputBuffer(info, 100L);
            if (outputBufferIndex >= 0 && info.size > 0 && info.presentationTimeUs >= 0) {
                if (num_out_frames < frame_sizes.length) {
                    int num_out_frames2 = num_out_frames + 1;
                    frame_sizes[num_out_frames] = info.size;
                    num_out_frames = num_out_frames2;
                }
                if (encodedSamplesSize < info.size) {
                    encodedSamplesSize = info.size;
                    encodedSamples = new byte[encodedSamplesSize];
                }
                outputBuffers[outputBufferIndex].get(encodedSamples, 0, info.size);
                outputBuffers[outputBufferIndex].clear();
                codec.releaseOutputBuffer(outputBufferIndex, false);
                if (encodedBytes.remaining() < info.size) {
                    estimatedEncodedSize = (int) (estimatedEncodedSize * 1.2d);
                    ByteBuffer newEncodedBytes = ByteBuffer.allocate(estimatedEncodedSize);
                    int position = encodedBytes.position();
                    encodedBytes.rewind();
                    newEncodedBytes.put(encodedBytes);
                    encodedBytes = newEncodedBytes;
                    encodedBytes.position(position);
                }
                encodedBytes.put(encodedSamples, 0, info.size);
            } else if (outputBufferIndex == -3) {
                outputBuffers = codec.getOutputBuffers();
            }
            if ((info.flags & 4) != 0) {
                break;
            }
            presentation_time = presentation_time2;
        }
        int encoded_size = encodedBytes.position();
        encodedBytes.rewind();
        codec.stop();
        codec.release();
        byte[] buffer2 = new byte[4096];
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            fileOutputStream.write(MP4Header.getMP4Header(this.mSampleRate, numChannels, frame_sizes, bitrate));
            while (encoded_size - encodedBytes.position() > buffer2.length) {
                encodedBytes.get(buffer2);
                fileOutputStream.write(buffer2);
            }
            int remaining = encoded_size - encodedBytes.position();
            if (remaining > 0) {
                encodedBytes.get(buffer2, 0, remaining);
                fileOutputStream.write(buffer2, 0, remaining);
            }
            fileOutputStream.close();
        } catch (IOException e) {
            Log.e("Ringdroid", "Failed to create the .m4a file.");
            Log.e("Ringdroid", getStackTrace(e));
        }
    }

    private void swapLeftRightChannels(byte[] buffer) {
        byte[] left = new byte[2];
        byte[] right = new byte[2];
        if (buffer.length % 4 == 0) {
            for (int offset = 0; offset < buffer.length; offset += 4) {
                left[0] = buffer[offset];
                left[1] = buffer[offset + 1];
                right[0] = buffer[offset + 2];
                right[1] = buffer[offset + 3];
                buffer[offset] = right[0];
                buffer[offset + 1] = right[1];
                buffer[offset + 2] = left[0];
                buffer[offset + 3] = left[1];
            }
        }
    }

    public void WriteWAVFile(File outputFile, int startFrame, int numFrames) throws IOException {
        WriteWAVFile(outputFile, (startFrame * getSamplesPerFrame()) / this.mSampleRate, ((startFrame + numFrames) * getSamplesPerFrame()) / this.mSampleRate);
    }

    public void WriteWAVFile(File outputFile, float startTime, float endTime) throws IOException {
        int startOffset = ((int) (this.mSampleRate * startTime)) * 2 * this.mChannels;
        int numSamples = (int) ((endTime - startTime) * this.mSampleRate);
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        outputStream.write(WAVHeader.getWAVHeader(this.mSampleRate, this.mChannels, numSamples));
        byte[] buffer = new byte[this.mChannels * 1024 * 2];
        this.mDecodedBytes.position(startOffset);
        int numBytesLeft = this.mChannels * numSamples * 2;
        while (numBytesLeft >= buffer.length) {
            if (this.mDecodedBytes.remaining() < buffer.length) {
                for (int i = this.mDecodedBytes.remaining(); i < buffer.length; i++) {
                    buffer[i] = 0;
                }
                this.mDecodedBytes.get(buffer, 0, this.mDecodedBytes.remaining());
            } else {
                this.mDecodedBytes.get(buffer);
            }
            if (this.mChannels == 2) {
                swapLeftRightChannels(buffer);
            }
            outputStream.write(buffer);
            numBytesLeft -= buffer.length;
        }
        if (numBytesLeft > 0) {
            if (this.mDecodedBytes.remaining() < numBytesLeft) {
                for (int i2 = this.mDecodedBytes.remaining(); i2 < numBytesLeft; i2++) {
                    buffer[i2] = 0;
                }
                this.mDecodedBytes.get(buffer, 0, this.mDecodedBytes.remaining());
            } else {
                this.mDecodedBytes.get(buffer, 0, numBytesLeft);
            }
            if (this.mChannels == 2) {
                swapLeftRightChannels(buffer);
            }
            outputStream.write(buffer, 0, numBytesLeft);
        }
        outputStream.close();
    }

    private void DumpSamples(String fileName) {
        String externalRootDir = Environment.getExternalStorageDirectory().getPath();
        if (!externalRootDir.endsWith("/")) {
            externalRootDir = String.valueOf(externalRootDir) + "/";
        }
        String parentDir = String.valueOf(externalRootDir) + "media/audio/debug/";
        File parentDirFile = new File(parentDir);
        parentDirFile.mkdirs();
        if (!parentDirFile.isDirectory()) {
            parentDir = externalRootDir;
        }
        if (fileName == null || fileName.isEmpty()) {
            fileName = "samples.tsv";
        }
        File outFile = new File(String.valueOf(parentDir) + fileName);
        BufferedWriter writer = null;
        this.mDecodedSamples.rewind();
        try {
            BufferedWriter writer2 = new BufferedWriter(new FileWriter(outFile));
            int sampleIndex = 0;
            while (sampleIndex < this.mNumSamples) {
                try {
                    String row = Float.toString(sampleIndex / this.mSampleRate);
                    for (int channelIndex = 0; channelIndex < this.mChannels; channelIndex++) {
                        row = String.valueOf(row) + "\t" + ((int) this.mDecodedSamples.get());
                    }
                    writer2.write(String.valueOf(row) + IOUtils.LINE_SEPARATOR_UNIX);
                    sampleIndex++;
                } catch (IOException e) {
                    writer = writer2;
                }
            }
            writer = writer2;
        } catch (IOException e3) {
            Log.w("Ringdroid", "Failed to create the sample TSV file.");
            Log.w("Ringdroid", getStackTrace(e3));
            try {
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            this.mDecodedSamples.rewind();
        }
        try {
            writer.close();
        } catch (Exception e4) {
            Log.w("Ringdroid", "Failed to close sample TSV file.");
            Log.w("Ringdroid", getStackTrace(e4));
        }
        this.mDecodedSamples.rewind();
    }

    private void DumpSamples() {
        DumpSamples(null);
    }

    private String getStackTrace(Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }
}
