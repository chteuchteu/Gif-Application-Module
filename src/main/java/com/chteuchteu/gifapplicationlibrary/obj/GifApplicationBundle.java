package com.chteuchteu.gifapplicationlibrary.obj;

import com.chteuchteu.gifapplicationlibrary.i.IDataSourceParser;

public class GifApplicationBundle {
    private String dataSourceUrl;
    private IDataSourceParser dataSourceParser;
    private String sdDirectory;
	private String aboutText;

    public GifApplicationBundle(String dataSourceUrl,
                                IDataSourceParser dataSourceParser,
                                String sdDirectory,
                                String aboutText) {
        this.dataSourceUrl = dataSourceUrl;
        this.dataSourceParser = dataSourceParser;
        this.sdDirectory = sdDirectory;
	    this.aboutText = aboutText;
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

	public String getAboutText() {
		return aboutText;
	}
}
