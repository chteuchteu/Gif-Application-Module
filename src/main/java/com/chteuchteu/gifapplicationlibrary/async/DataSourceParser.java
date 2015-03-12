package com.chteuchteu.gifapplicationlibrary.async;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.chteuchteu.gifapplicationlibrary.GifApplicationSingleton;
import com.chteuchteu.gifapplicationlibrary.hlpr.CacheUtil;
import com.chteuchteu.gifapplicationlibrary.hlpr.GifUtil;
import com.chteuchteu.gifapplicationlibrary.hlpr.MainUtil;
import com.chteuchteu.gifapplicationlibrary.i.IActivity_Main;
import com.chteuchteu.gifapplicationlibrary.obj.Gif;
import com.chteuchteu.gifapplicationlibrary.ui.Super_Activity_Main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataSourceParser extends AsyncTask<Void, Integer, Void> {
    private boolean needsUpdate;
    private Context context;
    private IActivity_Main iActivity;
    private ProgressBar progressBar;

    public DataSourceParser(Super_Activity_Main activity) {
        this.needsUpdate = false;
        this.iActivity = activity;
        this.context = activity;
        this.progressBar = iActivity.getProgressBar();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setIndeterminate(true);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);

        progressBar.setProgress(progress[0]);
        progressBar.setIndeterminate(false);
    }

	public void manualPublishProgress(int val) {
		onProgressUpdate(val);
	}

    @Override
    protected Void doInBackground(Void... arg0) {
        GifApplicationSingleton gas = GifApplicationSingleton.getInstance();
        String dataSourceUrl = gas.getBundle().getDataSourceUrl();
        List<Gif> gifs = gas.getBundle().getDataSourceParser().parseDataSource(dataSourceUrl);

        if (gifs.size() == 0)
            return null;

        List<Gif> currentGifs = gas.getGifs();
        if (currentGifs.size() == 0 || currentGifs.size() != gifs.size() || currentGifs.size() > 0 && !currentGifs.get(0).equals(gifs.get(0))) {
            needsUpdate = true;

            List<Gif> newGifs = new ArrayList<>();
            for (int i=gifs.size()-1; i>=0; i--)
                newGifs.add(0, gifs.get(i));

            gas.setGifs(newGifs);

            CacheUtil.saveGifs(context, newGifs);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        progressBar.setVisibility(View.GONE);

        if (needsUpdate) {
	        iActivity.onGifsListChanged();
            iActivity.refreshListView();

            CacheUtil.saveLastViewed(context, GifApplicationSingleton.getInstance().getFirstGif());
        }

        MainUtil.Prefs.setPref(context, "lastGifsListUpdate", GifUtil.dateToString(new Date()));
    }
}
