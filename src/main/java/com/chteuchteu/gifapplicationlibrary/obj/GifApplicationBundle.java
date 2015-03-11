package com.chteuchteu.gifapplicationlibrary.obj;

import com.chteuchteu.gifapplicationlibrary.i.IDataSourceParser;

public class GifApplicationBundle {
    private String dataSourceUrl;
    private IDataSourceParser dataSourceParser;
    private String sdDirectory;

    public GifApplicationBundle(String dataSourceUrl,
                                IDataSourceParser dataSourceParser,
                                String sdDirectory) {
        this.dataSourceUrl = dataSourceUrl;
        this.dataSourceParser = dataSourceParser;
        this.sdDirectory = sdDirectory;
    }

    public String getDataSourceUrl() {
        return dataSourceUrl;
    }

    public IDataSourceParser getDataSourceParser() {
        return dataSourceParser;
    }

    public String getSdDirectory() {
        return sdDirectory;
    }
}
