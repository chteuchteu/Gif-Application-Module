package com.chteuchteu.gifapplicationlibrary.async;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import com.chteuchteu.gifapplicationlibrary.R;
import com.chteuchteu.gifapplicationlibrary.hlpr.CacheUtil;
import com.chteuchteu.gifapplicationlibrary.hlpr.MainUtil;
import com.chteuchteu.gifapplicationlibrary.obj.Gif;
import com.chteuchteu.gifapplicationlibrary.obj.GifApplicationBundle;

import java.util.List;

public class NotificationsPollTask extends AsyncTask<Void, Void, Void> {
	private Service service;
	private Context context;
	private GifApplicationBundle bundle;

	private int nbUnseenGifs = 0;
	private List<Gif> gifs;

	public NotificationsPollTask(Service service, GifApplicationBundle bundle) {
		this.service = service;
		this.context = service;
		this.bundle = bundle;
	}

	@Override
	protected Void doInBackground(Void... params) {
		gifs = this.bundle.getDataSourceParser().parseDataSource(
				this.bundle.getDataSourceUrl()
		);

		String lastUnseenGif = MainUtil.Prefs.getPref(context, "lastViewed");
		if (gifs.size() > 0) {
			for (Gif g : gifs) {
				if (g.getArticleUrl().equals(lastUnseenGif))
					break;
				else
					nbUnseenGifs++;
			}
		}

		if (nbUnseenGifs > 0)
			CacheUtil.saveGifs(context, gifs);

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// Check if there are new gifs, and if a notification for them hasn't been displayed yet
		boolean notif = (nbUnseenGifs > 0 &&
				(MainUtil.Prefs.getPref(context, "lastNotifiedGif").equals("")
						|| gifs.size() > 0 && !gifs.get(0).getGifUrl().equals(MainUtil.Prefs.getPref(context, "lastNotifiedGif"))));

		if (notif) {
			// Save the last gif
			if (gifs.size() > 0)
				MainUtil.Prefs.setPref(context, "lastNotifiedGif", gifs.get(0).getGifUrl());

			String text = nbUnseenGifs > 1 ? context.getString(R.string.new_gifs).replace("X", String.valueOf(nbUnseenGifs))
					: context.getString(R.string.new_gif);

			NotificationCompat.Builder builder =
					new NotificationCompat.Builder(context)
							.setSmallIcon(R.drawable.ic_notifications)
							.setNumber(nbUnseenGifs)
							.setContentTitle(bundle.getAppName())
							.setAutoCancel(true)
							.setContentText(text);
			int NOTIFICATION_ID = 1664;

			Intent targetIntent = new Intent(context, bundle.getNotificationsIntentTarget());
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			builder.setContentIntent(contentIntent);
			NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			nManager.notify(NOTIFICATION_ID, builder.build());
		}

		service.stopSelf();
	}
}
