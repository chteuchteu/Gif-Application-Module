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
    public static List<Gif> getGifs(Context context) {
        String[] stringGifs = MainUtil.Prefs.getPref(context, "gifs").split(";;");
        List<Gif> list = new ArrayList<>();
        for (String string : stringGifs) {
            Gif g = new Gif(string.split("::")[0], string.split("::")[1],
                    string.split("::")[2], string.split("::")[3]);
            list.add(g);
        }
        return list;
    }

    public static void saveGifs(Context context, List<Gif> gifs) {
        String str = "";
        int i=0;
        for (Gif g : gifs) {
            str += g.getName() + "::" + g.getArticleUrl() + "::" + g.getGifUrl() + "::" + g.getDate();
            if (i != gifs.size()-1)
                str += ";;";
            i++;
        }
        MainUtil.Prefs.setPref(context, "gifs", str);
    }

    public static void clearCache(Context context, String sdFolderName) {
        String path = Environment.getExternalStorageDirectory().toString() + "/" + sdFolderName +"/";
        File dir = new File(path);
        File files[] = dir.listFiles();
        int crt = 0;
        if (files != null) {
            for (File f : files) {
                f.delete();
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

    public static boolean removeUncompleteGifs(String sdFolderName, Context context, List<Gif> l) {
        boolean needSave = false;
        for (Gif g : l) {
            if (g.getState() == Gif.ST_DOWNLOADING) {
                File f = new File(g.getEntiereFileName(sdFolderName, false));
                if (f.exists())
                    f.delete();
                g.setState(Gif.ST_EMPTY);
                needSave = true;
            }
        }
        if (needSave)
            saveGifs(context, l);
        return needSave;
    }
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
