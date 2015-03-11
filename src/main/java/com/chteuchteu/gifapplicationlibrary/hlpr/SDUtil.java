package com.chteuchteu.gifapplicationlibrary.hlpr;

import android.os.Environment;

import java.io.File;

public class SDUtil {
    public static boolean createDirectoryIfNecessary(String sdFolderName) {
        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/" + sdFolderName + "/");
        return !dir.exists() && dir.mkdirs();
    }
}
