package com.lly.library;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.lly.library.call.CompressCallback;
import com.lly.library.compress.CompressOptions;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * QuickCompress[v 1.0.0]
 * classes:com.lly.imagecompress.QuickCompress
 *
 * @author lileiyi
 * @date 2017/11/27
 * @time 15:06
 * @description
 */

public class QuickCompress {

    private int mMaxWidth;
    private int mMaxHeight;
    private int mQuality;
    private String outputPath;
    private Bitmap.CompressFormat format;

    private ExecutorService mSingleThreadExecutor;

    public QuickCompress(Builder builder) {
        this.mMaxHeight = builder.maxHeight;
        this.mMaxWidth = builder.maxWidth;
        this.mQuality = builder.quality;
        this.outputPath = builder.outputPath;
        this.format = builder.format;
        mSingleThreadExecutor = Executors.newSingleThreadExecutor();
    }

    public void execute(final File file, final CompressCallback callback) {
        mSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    File outPutFile = CompressOptions.compress(file, mMaxWidth, mMaxHeight, format, mQuality, outputPath);
                    callback.onComplete(outPutFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.onFail(e.toString());
                }
            }
        });
    }

    public File load(final File file) {
        File file1 = null;
        try {
            file1 = CompressOptions.compress(file, mMaxWidth, mMaxHeight, format, mQuality, outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file1;
    }


    public static final class Builder {
        int maxWidth;
        int maxHeight;
        int quality;
        String outputPath;
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;

        public Builder() {
            quality = 80;
            maxWidth = 768;
            maxHeight = 1024;
        }

        public Builder maxWidth(int width) {
            if (width < 0) throw new IllegalArgumentException("width < 0");
            this.maxWidth = width;
            return this;
        }

        public Builder maxHeight(int height) {
            if (height < 0) throw new IllegalArgumentException("height < 0");
            this.maxHeight = height;
            return this;
        }

        public Builder quality(int quality) {
            if (quality < 0 || quality > 100)
                throw new IllegalArgumentException("quality maybe 0 - 100");
            this.quality = quality;
            return this;
        }

        public Builder outputPath(String outPutPath) {
            if (TextUtils.isEmpty(outPutPath))
                throw new NullPointerException("outPutPath is null ");
            this.outputPath = outPutPath;
            return this;
        }

        public Builder compressFormat(Bitmap.CompressFormat format) {
            this.format = format;
            return this;
        }

        public QuickCompress build() {
            return new QuickCompress(this);
        }
    }
}
