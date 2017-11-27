package com.lly.imagecompress.compress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * CompressOptions[v 1.0.0]
 * classes:com.lly.imagecompress.compress.CompressOptions
 *
 * @author lileiyi
 * @date 2017/11/27
 * @time 16:24
 * @description
 */

public class CompressOptions {


    public static File compress(File file, int reqWidth, int reqHeight, Bitmap.CompressFormat format, int quality, String outputPath) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        //压缩图片大小
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        Bitmap scaledBitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        //图片旋转的情况
        ExifInterface exif;
        exif = new ExifInterface(file.getAbsolutePath());
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
        Matrix matrix = new Matrix();
        if (orientation == 6) {
            matrix.postRotate(90);
        } else if (orientation == 3) {
            matrix.postRotate(180);
        } else if (orientation == 8) {
            matrix.postRotate(270);
        }
        scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        FileOutputStream fileOutputStream = null;
        //压缩质量
        try {
            fileOutputStream = new FileOutputStream(outputPath);
            scaledBitmap.compress(format, quality, fileOutputStream);
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            if (!scaledBitmap.isRecycled()) {
                scaledBitmap.recycle();
                scaledBitmap = null;
            }
        }
        return new File(outputPath);
    }


    /**
     * 计算图片缩放比例
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
