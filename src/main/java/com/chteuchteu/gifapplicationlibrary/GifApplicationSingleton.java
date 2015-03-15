package com.chteuchteu.gifapplicationlibrary;

import android.content.Context;

import com.chteuchteu.gifapplicationlibrary.hlpr.CacheUtil;
import com.chteuchteu.gifapplicationlibrary.hlpr.SQLiteHelper;
import com.chteuchteu.gifapplicationlibrary.obj.Gif;
import com.chteuchteu.gifapplicationlibrary.obj.GifApplicationBundle;

import java.util.ArrayList;
import java.util.List;

public class GifApplicationSingleton {
    private static GifApplicationSingleton instance;

    private GifApplicationBundle bundle;

	private List<Gif> gifs;
	private SQLiteHelper sqLiteHelper;

    private GifApplicationSingleton(Context context, GifApplicationBundle bundle) {
        this.bundle = bundle;
        this.gifs = new ArrayList<>();
	    this.sqLiteHelper = new SQLiteHelper(context);
        CacheUtil.createDirectoryIfNecessary(bundle.getSdDirectory());
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
        this.gifs.addAll(this.sqLiteHelper.getGifs());
    }

    public void setGifs(List<Gif> gifs) {
        this.gifs.clear();
        this.gifs.addAll(gifs);
    }
    public List<Gif> getGifs() { return this.gifs; }
    public Gif getFirstGif() { return this.gifs.size() > 0 ? this.gifs.get(0) : null; }

    public GifApplicationBundle getBundle() { return this.bundle; }

	public SQLiteHelper getSqLiteHelper() { return instance.sqLiteHelper; }
	public static SQLiteHelper getSqLiteHelper(Context context) { return new SQLiteHelper(context); }
}
