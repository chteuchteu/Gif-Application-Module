package com.chteuchteu.gifapplicationlibrary.obj;

import android.os.Environment;

import com.chteuchteu.gifapplicationlibrary.hlpr.MainUtil;

import java.util.Calendar;

public class Gif {
	private long id;
    private String name;
    private String articleUrl;
    private String gifUrl;
    private Calendar date;
    private GifState state;

    public enum GifState { UNKNOWN, EMPTY, DOWNLOADING, COMPLETE }

    public Gif() {
	    this.id = -1;
        this.name = "";
        this.articleUrl = "";
        this.gifUrl = "";
        this.date = Calendar.getInstance();
        this.state = GifState.UNKNOWN;
    }

    public Gif(long id, String name, String articleUrl, String gifUrl, Calendar date) {
	    this.id = id;
        this.name = name;
        this.articleUrl = articleUrl;
        this.gifUrl = gifUrl;
        this.date = date;
	    this.state = GifState.UNKNOWN;
    }

    public boolean isValid() {
        return !name.equals("") && !gifUrl.equals("");
    }

    public boolean equals(Gif g) {
        return this.name.equals(g.getName())
                && this.articleUrl.equals(g.getArticleUrl())
                && this.gifUrl.equals(g.getGifUrl())
                && this.date.equals(g.getDate());
    }

    // http://lesjoiesdeletudiantinfo.com/3392/quand-tu-rentres-dans-une-salle-de-cours-en-t/
    // => quand-tu-rentres-dans-une-salle-de-cours-en-t
    public String getFileName() {
        String[] elements = this.articleUrl.split("/");
        return elements[elements.length-1];
    }

    /**
     * Returns the file type, without the leading "."
     */
    public String getFileType() {
        return MainUtil.getExtension(this.gifUrl);
    }

    public String getEntiereFileName(String sdFolderName, boolean withFilePrefix) {
        String path = "";
        if (withFilePrefix)
            path += "file://";
        path += Environment.getExternalStorageDirectory().getPath() + "/" + sdFolderName + "/" + getFileName() + "." + getFileType();
        return path;
    }

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getArticleUrl() {
        return articleUrl;
    }
    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getGifUrl() {
        return gifUrl;
    }
    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

    public Calendar getDate() {
        return date;
    }
    public void setDate(Calendar date) {
        this.date = date;
    }

    public GifState getState() {
        return state;
    }
    public void setState(GifState state) {
        this.state = state;
    }
}
