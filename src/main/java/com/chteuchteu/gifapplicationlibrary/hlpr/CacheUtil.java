package com.chteuchteu.gifapplicationlibrary.hlpr;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.chteuchteu.gifapplicationlibrary.R;
import com.chteuchteu.gifapplicationlibrary.obj.Gif;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CacheUtil {
    public static boolean createDirectoryIfNecessary(String sdFolderName) {
        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/" + sdFolderName + "/");
        return !dir.exists() && dir.mkdirs();
    }

    public static void clearCache(Context context, String sdFolderName) {
        String path = Environment.getExternalStorageDirectory().toString() + "/" + sdFolderName +"/";
        File dir = new File(path);
        File files[] = dir.listFiles();
        int crt = 0;
        if (files != null) {
            for (File f : files) {
                if (f.delete())
                    crt++;
            }
        }
        String txt;
        if (crt == 0)
            txt = context.getText(R.string.cache_emptied_none).toString();
        else if (crt == 1)
            txt = context.getText(R.string.cache_emptied_sing).toString();
        else
            txt = context.getText(R.string.cache_emptied_plur).toString().replaceAll("#", crt + "");

        Toast.makeText(context, txt, Toast.LENGTH_SHORT).show();
    }

    public static void saveLastViewed(Context context, Gif firstGif) {
        if (firstGif != null)
            MainUtil.Prefs.setPref(context, "lastViewed", firstGif.getArticleUrl());
    }

    public static boolean removeUncompleteGifs(String sdFolderName, List<Gif> l) {
        boolean gifsDeleted = false;
        for (Gif g : l) {
            if (g.getState() == Gif.GifState.DOWNLOADING) {
                File f = new File(g.getEntiereFileName(sdFolderName, false));
                if (f.exists() && f.delete())
                    gifsDeleted = true;
                g.setState(Gif.GifState.EMPTY);
            }
        }

        return gifsDeleted;
    }
    // TODO clean method
	public static void removeOldGifs(String sdFolderName, List<Gif> l) {
		if (l != null && l.size() > 10) {
			String path = Environment.getExternalStorageDirectory().toString() + "/" + sdFolderName + "/";
			File dir = new File(path);
			File files[] = dir.listFiles();
			if (files != null) {
				List<File> toBeDeleted = new ArrayList<>();
				for (File f : files) {
					boolean shouldBeDeleted = true;
					int max = 15;
					if (l.size() < 15)	max = l.size();
					for (int i=0; i<max; i++) {
						String fileName = l.get(i).getFileName().replaceAll("/", "");
						if (f.getName().contains(fileName)) {
							shouldBeDeleted = false; break;
						}
					}
					if (shouldBeDeleted)
						toBeDeleted.add(f);
				}
				for (File f : toBeDeleted)
					f.delete();
			}
		}
	}
}
