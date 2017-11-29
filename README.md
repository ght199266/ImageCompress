# Android 图片压缩工具

#### 支持异步调用和Rxjava二种方式调用

##### 异步方式

~~~java
 QuickCompress quickCompress =
                new QuickCompress.Builder()
                .maxHeight(1024)
                .maxWidth(768)
                .quality(75)
                .compressFormat(Bitmap.CompressFormat.JPEG)
                .outputPath(getPath()).build();
        quickCompress.execute(new File(ImagePath), new CompressCallback() {

            @Override
            public void onComplete(File file) {


            }

            @Override
            public void onFail(String info) {

            }
        });
~~~


##### RxJava方式

~~~java
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

            }
        });
~~~

