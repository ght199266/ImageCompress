package com.lly.imagecompress.call;

import java.io.File;

/**
 * CallBack[v 1.0.0]
 * classes:com.lly.imagecompress.CallBack
 *
 * @author lileiyi
 * @date 2017/11/27
 * @time 16:18
 * @description
 */

public interface CompressCallback {

    void onComplete(File file);

    void onFail(String info);
}
