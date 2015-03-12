package com.chteuchteu.gifapplicationlibrary.obj;

import android.os.Environment;

public class Gif {
    private String name = "";
    private String articleUrl = "";
    private String gifUrl = "";
    private String date = "";
    private int state = 0;

    public final static int ST_UNKNOWN = 0;
    public final static int ST_EMPTY = 1;
    public final static int ST_DOWNLOADING = 2;
    public final static int ST_COMPLETE = 3;

    public Gif() { }

    public Gif(String name, String articleUrl, String gifUrl, String date) {
        this.name = name;
        this.articleUrl = articleUrl;
        this.gifUrl = gifUrl;
        this.date = date;
    }

    public boolean isValid() {
        return !name.equals("") && !gifUrl.equals("");
    }

    public boolean equals(Gif g) {
        return this.name.equals(g.name) && !(!this.articleUrl.equals("") && !g.articleUrl.equals("") && !this.articleUrl.equals(g.articleUrl)) && !(!this.gifUrl.equals("") && !g.gifUrl.equals("") && !this.gifUrl.equals(g.gifUrl)) && !(!this.date.equals("") && !g.date.equals("") && !this.date.equals(g.date));
    }

    // http://lesjoiesdeletudiantinfo.com/3392/quand-tu-rentres-dans-une-salle-de-cours-en-t/
    // => quand-tu-rentres-dans-une-salle-de-cours-en-t
    public String getFileName() {
        String[] elements = this.articleUrl.split("/");
        return elements[elements.length-1];
    }

    public String getEntiereFileName(String sdFolderName, boolean withFilePrefix) {
        String path = "";
        if (withFilePrefix)
            path += "file://";
        path += Environment.getExternalStorageDirectory().getPath() + "/" + sdFolderName + "/" + getFileName() + ".gif";
        return path;
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

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }
}
