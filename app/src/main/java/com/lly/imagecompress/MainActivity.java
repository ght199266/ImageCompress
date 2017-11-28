package com.lly.imagecompress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.lly.imagecompress.call.CompressCallback;

import java.io.File;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static android.os.Environment.getExternalStorageDirectory;

public class MainActivity extends AppCompatActivity {

    ImageView image;
    ImageView image2;

    private String path = getExternalStorageDirectory().getAbsolutePath() + "/aa.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.image);
        image2 = (ImageView) findViewById(R.id.image2);


    }

    public void comress(View view) {
        //Rxjava方式
        rxjava();
        //接口回调方式
//        executeCall();
    }

    /**
     * 异步回掉方式
     */
    private void executeCall() {
        QuickCompress quickCompress = new QuickCompress.Builder()
                .maxHeight(1024)
                .maxWidth(768)
                .quality(80)
                .compressFormat(Bitmap.CompressFormat.JPEG)
                .outputPath(getPath()).build();
        quickCompress.execute(new File(path), new CompressCallback() {
            @Override
            public void onComplete(File Images) {
                Bitmap factory = BitmapFactory.decodeFile(Images.getAbsolutePath());
                image.setImageBitmap(factory);
            }

            @Override
            public void onFail(String info) {
                Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
            }

        });
    }


    /**
     * Rxjava方式调用
     */
    private void rxjava() {
        Flowable.just(path).subscribeOn(Schedulers.io()).map(new Function<String, File>() {
            @Override
            public File apply(@NonNull String path) throws Exception {
                Log.v("test", "currentThread:=" + Thread.currentThread().getName());
                QuickCompress quickCompress = new QuickCompress.Builder()
                        .maxHeight(1024)
                        .maxWidth(768)
                        .quality(80)
                        .compressFormat(Bitmap.CompressFormat.JPEG)
                        .outputPath(getPath()).build();
                return quickCompress.load(new File(path));
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<File>() {
            @Override
            public void accept(@NonNull File file) throws Exception {
                Bitmap factory = BitmapFactory.decodeFile(file.getAbsolutePath());
                image.setImageBitmap(factory);
            }
        });
    }


    private String getPath() {
        File file1 = getExternalStorageDirectory();
        String fileName = System.currentTimeMillis() + ".jpg";
        return new File(file1.getAbsolutePath() + File.separator + fileName).getAbsolutePath();
    }


    public int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {     //API 19
            return bitmap.getAllocationByteCount();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {//API 12
            return bitmap.getByteCount();
        } else {
            return bitmap.getRowBytes() * bitmap.getHeight(); //earlier version
        }
    }
}
