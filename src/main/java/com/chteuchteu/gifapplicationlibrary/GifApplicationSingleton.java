package com.chteuchteu.gifapplicationlibrary;

import android.content.Context;

import com.chteuchteu.gifapplicationlibrary.hlpr.CacheUtil;
import com.chteuchteu.gifapplicationlibrary.hlpr.MainUtil;
import com.chteuchteu.gifapplicationlibrary.hlpr.SDUtil;
import com.chteuchteu.gifapplicationlibrary.obj.Gif;
import com.chteuchteu.gifapplicationlibrary.obj.GifApplicationBundle;

import java.util.ArrayList;
import java.util.List;

public class GifApplicationSingleton {
    private static GifApplicationSingleton instance;

    private GifApplicationBundle bundle;

    private Context context;
    private List<Gif> gifs;

    private GifApplicationSingleton(Context context, GifApplicationBundle bundle) {
        this.context = context;
        this.bundle = bundle;
        this.gifs = new ArrayList<>();
        SDUtil.createDirectoryIfNecessary(bundle.getSdDirectory());
        loadGifsFromCache();
    }

    public static GifApplicationSingleton getInstance() {
        return instance;
    }

    public static GifApplicationSingleton create(Context context, GifApplicationBundle bundle) {
        instance = new GifApplicationSingleton(context, bundle);
        return instance;
    }

    private void loadGifsFromCache() {
        if (MainUtil.Prefs.getPref(context, "gifs").equals(""))
            return;

        this.gifs.clear();
        List<Gif> gifs = CacheUtil.getGifs(context);
        this.gifs.addAll(gifs);
    }

    public void setGifs(List<Gif> gifs) {
        this.gifs.clear();
        this.gifs.addAll(gifs);
    }
    public List<Gif> getGifs() { return this.gifs; }
    public Gif getFirstGif() { return this.gifs.size() > 0 ? this.gifs.get(0) : null; }

    public GifApplicationBundle getBundle() { return this.bundle; }

    public static void throwUnimplementedException() {
        throw new RuntimeException("Not implemented method");
    }
}
