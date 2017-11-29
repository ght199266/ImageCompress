package com.lly.imagecompress;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Utils[v 1.0.0]
 * classes:com.lly.imagecompress.Utils
 *
 * @author lileiyi
 * @date 2017/11/29
 * @time 10:25
 * @description
 */

public class Utils {


    /**
     * 获取文件大小
     */
    public static String getFileSize(File file) {
        String size = "";
        if (file.exists() && file.isFile()) {
            long fileS = file.length();
            DecimalFormat df = new DecimalFormat("#.00");
            if (fileS < 1024) {
                size = df.format((double) fileS) + "BT";
            } else if (fileS < 1048576) {
                size = df.format((double) fileS / 1024) + "KB";
            } else if (fileS < 1073741824) {
                size = df.format((double) fileS / 1048576) + "MB";
            } else {
                size = df.format((double) fileS / 1073741824) + "GB";
            }
        } else if (file.exists() && file.isDirectory()) {
            size = "";
        } else {
            size = "0BT";
        }
        return size;
    }
}
