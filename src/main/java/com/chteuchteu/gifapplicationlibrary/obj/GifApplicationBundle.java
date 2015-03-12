package com.chteuchteu.gifapplicationlibrary.obj;

import com.chteuchteu.gifapplicationlibrary.i.IDataSourceParser;

/**
 * App configuration bundle
 */
public class GifApplicationBundle {
    /**
     * Name of the application. Shown in the Toolbar
     */
	private String appName;
    /**
     * Data source target URL.
     * Not directly used in the app, but transmitted as parameter
     *  in DataSourceParser.parseDataSource method
     */
    private String dataSourceUrl;
    /**
     * DataSourceParser interface. Must be implemented:
     *  the parseDataSource method will be used to get a Gif ArrayList
     */
    private IDataSourceParser dataSourceParser;
    /**
     * Gifs directory name on SD card. Must not contain trailing slash.
     */
    private String sdDirectory;
    /**
     * Text displayed on "about" MenuItem action
     */
	private String aboutText;
    /**
     * Activity set as Intent target when hitting the notification when
     *  there are new gifs
     */
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
