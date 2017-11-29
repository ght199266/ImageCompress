# Android 图片压缩工具

支持异步调用和Rxjava二种方式调用

异步调用：


 QuickCompress quickCompress = new QuickCompress.Builder()
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
