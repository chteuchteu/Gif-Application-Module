package com.chteuchteu.gifapplicationlibrary.obj;

import com.chteuchteu.gifapplicationlibrary.i.IDataSourceParser;

public class GifApplicationBundle {
	private String appName;
    private String dataSourceUrl;
    private IDataSourceParser dataSourceParser;
    private String sdDirectory;
	private String aboutText;
	private Class<?> notificationsIntentTarget;

    public GifApplicationBundle(String appName,
                                String dataSourceUrl,
                                IDataSourceParser dataSourceParser,
                                String sdDirectory,
                                String aboutText,
                                Class<?> notificationsIntentTarget) {
	    this.appName = appName;
        this.dataSourceUrl = dataSourceUrl;
        this.dataSourceParser = dataSourceParser;
        this.sdDirectory = sdDirectory;
	    this.aboutText = aboutText;
	    this.notificationsIntentTarget = notificationsIntentTarget;
    }

	public String getAppName() {
		return appName;
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

	public Class<?> getNotificationsIntentTarget() {
		return notificationsIntentTarget;
	}
}
