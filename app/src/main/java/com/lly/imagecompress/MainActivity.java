package com.lly.imagecompress;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.lly.imagecompress.call.CompressCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.os.Environment.getExternalStorageDirectory;

public class MainActivity extends AppCompatActivity {

    ImageView image;
    ImageView image2;

    private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        image = (ImageView) findViewById(R.id.image);
        image2 = (ImageView) findViewById(R.id.image2);


    }

    public void comress(View view) {

        String path = getExternalStorageDirectory().getAbsolutePath() + "/aa.jpg";

        File file1 = getExternalStorageDirectory();
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(file1.getAbsolutePath() + File.separator + fileName);

        QuickCompress.Builder builder = new QuickCompress.Builder()
                .maxHeight(1024)
                .maxWidth(768)
                .quality(80)
                .compressFormat(Bitmap.CompressFormat.JPEG)
                .outputPath(file.getAbsolutePath());

        QuickCompress quickCompress = builder.build();
        quickCompress.execute(new File(path), new CompressCallback() {
            @Override
            public void onComplete(File Images) {
                Log.v("test", "Images:=" + Images.getAbsolutePath());
            }

            @Override
            public void onFail(String info) {
                Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
            }

        });
    }


    public int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {     //API 19
            return bitmap.getAllocationByteCount();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        } else {
            return bitmap.getRowBytes() * bitmap.getHeight(); //earlier version
        }
    }

    public void saveImageToGallery(Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "zhangxunPics");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 1, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
