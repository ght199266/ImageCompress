package com.lly.imagecompress;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lly.library.QuickCompress;
import com.lly.library.call.CompressCallback;

import java.io.File;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static android.os.Environment.getExternalStorageDirectory;

public class MainActivity extends AppCompatActivity {
    ImageView image, image2;
    TextView tv_a, tv_b;


    private String ImagePath;
    private static final int REQUEST_IMAGE_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.image);
        image2 = (ImageView) findViewById(R.id.image2);

        tv_a = (TextView) findViewById(R.id.tv_a);
        tv_b = (TextView) findViewById(R.id.tv_b);


    }

    public void comress(View view) {
        //Rxjava方式
        rxJava();
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
                .quality(75)
                .compressFormat(Bitmap.CompressFormat.JPEG)
                .outputPath(getPath()).build();
        quickCompress.execute(new File(ImagePath), new CompressCallback() {
            @Override
            public void onComplete(File file) {
                Bitmap factory = BitmapFactory.decodeFile(file.getAbsolutePath());
                image2.setImageBitmap(factory);
                tv_b.setText("图片大小：" + Utils.getFileSize(file));
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
    private void rxJava() {
        Flowable.just(ImagePath).subscribeOn(Schedulers.io()).map(new Function<String, File>() {
            @Override
            public File apply(@NonNull String path) throws Exception {
                QuickCompress quickCompress = new QuickCompress.Builder()
                        .maxHeight(1024)
                        .maxWidth(768)
                        .quality(75)
                        .compressFormat(Bitmap.CompressFormat.JPEG)
                        .outputPath(getPath()).build();
                return quickCompress.load(new File(path));
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<File>() {
            @Override
            public void accept(@NonNull File file) throws Exception {
                Bitmap factory = BitmapFactory.decodeFile(file.getAbsolutePath());
                image2.setImageBitmap(factory);
                tv_b.setText("图片大小：" + Utils.getFileSize(file));
            }
        });
    }


    /**
     * 图片输出路径
     *
     * @return
     */
    private String getPath() {
        File file1 = getExternalStorageDirectory();
        String fileName = System.currentTimeMillis() + ".jpg";
        return new File(file1.getAbsolutePath() + File.separator + fileName).getAbsolutePath();
    }


    /**
     * 选择相册
     */
    public void select(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            ImagePath = c.getString(columnIndex);
            Bitmap bitmap = BitmapFactory.decodeFile(ImagePath);
            image.setImageBitmap(bitmap);
            tv_a.setText("图片大小：" + Utils.getFileSize(new File(ImagePath)));
            c.close();
        }
    }


}
