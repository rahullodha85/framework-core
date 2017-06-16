package com.hbcd.utility.screenrecorder;

import org.monte.media.Format;
import org.monte.media.Registry;
import org.monte.screenrecorder.ScreenRecorder;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SpecializedScreenRecorder extends ScreenRecorder {

    private String name;

    public SpecializedScreenRecorder(GraphicsConfiguration cfg,
                                     Rectangle captureArea, Format fileFormat, Format screenFormat,
                                     Format mouseFormat, Format audioFormat, File movieFolder,
                                     String Name) throws IOException, AWTException {
        super(cfg, captureArea, fileFormat, screenFormat, mouseFormat,
                audioFormat, movieFolder);
        this.name = Name;
    }

    @Override
    protected File createMovieFile(Format fileFormat) throws IOException {
        if (!movieFolder.exists()) {
            movieFolder.mkdirs();
        } else if (!movieFolder.isDirectory()) {
            throw new IOException(String.format("\"%s\" is not a directory.", movieFolder));
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd-HH.mm.ss");
        Date date = new Date();
        return new File(movieFolder, String.format("%s-%s.%s", name, dateFormat.format(new Date()), Registry.getInstance().getExtension(fileFormat)));
    }
}
