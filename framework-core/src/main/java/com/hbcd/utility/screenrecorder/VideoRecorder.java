package com.hbcd.utility.screenrecorder;

import org.monte.media.Format;
import org.monte.media.FormatKeys.MediaType;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

import java.awt.*;
import java.io.File;

import static org.monte.media.AudioFormatKeys.EncodingKey;
import static org.monte.media.AudioFormatKeys.FrameRateKey;
import static org.monte.media.AudioFormatKeys.KeyFrameIntervalKey;
import static org.monte.media.AudioFormatKeys.MIME_AVI;
import static org.monte.media.AudioFormatKeys.MediaTypeKey;
import static org.monte.media.AudioFormatKeys.MimeTypeKey;
import static org.monte.media.VideoFormatKeys.*;

public class VideoRecorder {
    private ScreenRecorder screenRecorder;
    private String location;
    private boolean _isRecording = true;
    private String fileName;

    public VideoRecorder(String location) {
        this.location = location;
    }

    public VideoRecorder(String location, boolean _isR) {
        this.location = location;
        _isRecording = _isR;
    }

    public void startRecording(String ScenarioName) throws Exception {

        if (!_isRecording) return;  //Don't do anything when not configure as not recording

        File file = new File(location);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        Rectangle captureSize = new Rectangle(0, 0, width, height);

        GraphicsConfiguration gc = GraphicsEnvironment
                .getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration();

        this.screenRecorder = new SpecializedScreenRecorder(
                gc,
                captureSize,
                new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey,
                        ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                        CompressorNameKey,
                        ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24,
                        FrameRateKey, Rational.valueOf(15), QualityKey, 1.0f,
                        KeyFrameIntervalKey, 15 * 60), new Format(MediaTypeKey,
                MediaType.VIDEO, EncodingKey, "black", FrameRateKey,
                Rational.valueOf(15)), null, file, ScenarioName);
        this.screenRecorder.start();
        fileName = this.screenRecorder.getCreatedMovieFiles().get(0).getName();
    }

    public void stopRecording() throws Exception {
        if (!_isRecording) return;   //Don't do anything when not configure as not recording

        this.screenRecorder.stop();

    }

    public String getRecordedFileName() {
        return fileName;
    }
}
