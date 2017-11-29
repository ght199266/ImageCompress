package com.lly.library;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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

public class QuickCompress implements Handler.Callback {
    /**
     * 压缩完成
     */
    private static final int MESSAGE_COMPRESS_SUCCESS = 1;
    /**
     * 压缩失败
     */
    private static final int MESSAGE_COMPRESS_FAUL = 2;

    private CompressCallback mCompressCallback;

    private int mMaxWidth;
    private int mMaxHeight;
    private int mQuality;
    private String outputPath;
    private Bitmap.CompressFormat format;

    private ExecutorService mSingleThreadExecutor;
    private Handler mHandler;


    public QuickCompress(Builder builder) {
        this.mMaxHeight = builder.maxHeight;
        this.mMaxWidth = builder.maxWidth;
        this.mQuality = builder.quality;
        this.outputPath = builder.outputPath;
        this.format = builder.format;
        mSingleThreadExecutor = Executors.newSingleThreadExecutor();
        mHandler = new Handler(Looper.getMainLooper(), this);
    }

    /**
     * 压缩图片
     *
     * @param file     文件路径
     * @param callback 接口回调
     */
    public void execute(final File file, final CompressCallback callback) {
        this.mCompressCallback = callback;
        mSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    File outPutFile = CompressOptions.compress(file, mMaxWidth, mMaxHeight, format, mQuality, outputPath);
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_COMPRESS_SUCCESS, outPutFile));
                } catch (IOException e) {
                    e.printStackTrace();
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_COMPRESS_FAUL, e.toString()));
                }
            }
        });
    }

    public File load(final File file) throws IOException {
        return CompressOptions.compress(file, mMaxWidth, mMaxHeight, format, mQuality, outputPath);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MESSAGE_COMPRESS_SUCCESS:
                mCompressCallback.onComplete((File) msg.obj);
                break;
            case MESSAGE_COMPRESS_FAUL:
                mCompressCallback.onFail((String) msg.obj);
                break;
            default:
                break;
        }
        return false;
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
