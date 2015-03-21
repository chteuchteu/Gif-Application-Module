package com.chteuchteu.gifapplicationlibrary.ui;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chteuchteu.gifapplicationlibrary.GifApplicationSingleton;
import com.chteuchteu.gifapplicationlibrary.R;
import com.chteuchteu.gifapplicationlibrary.async.DataSourceParser;
import com.chteuchteu.gifapplicationlibrary.hlpr.CacheUtil;
import com.chteuchteu.gifapplicationlibrary.hlpr.GifUtil;
import com.chteuchteu.gifapplicationlibrary.hlpr.MainUtil;
import com.chteuchteu.gifapplicationlibrary.i.IActivity_Main;
import com.chteuchteu.gifapplicationlibrary.obj.Gif;

import java.util.Date;

public class Super_Activity_Main extends ActionBarActivity implements IActivity_Main {
    private GifApplicationSingleton gas;

    // Fragments
    private Fragment_List fragment_list;
    private View fragment_listContainer;
    private Fragment_Gifs fragment_gifs;
    private View fragment_gifsContainer;

	private enum LayoutStyle { FULLSCREEN, SIDE_BY_SIDE }
	private LayoutStyle layoutStyle;

    // MenuItems
    private MenuItem menu_list_refresh;
    private MenuItem menu_list_about;
    private MenuItem menu_list_notifications;
    private MenuItem menu_list_clearCache;
    private MenuItem menu_gif_share;
    private MenuItem menu_gif_openWebsite;
    private MenuItem menu_gif_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	    gas = GifApplicationSingleton.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

	    layoutStyle = isDeviceLarge(MainUtil.getDeviceSizeCategory(this)) ? LayoutStyle.SIDE_BY_SIDE : LayoutStyle.FULLSCREEN;

	    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragment_list = new Fragment_List();
        getSupportFragmentManager().beginTransaction().add(R.id.listFragment, fragment_list).commit();
        fragment_listContainer = findViewById(R.id.listFragment);

        fragment_gifs = new Fragment_Gifs();
        getSupportFragmentManager().beginTransaction().add(R.id.gifsFragment, fragment_gifs).commit();
        fragment_gifsContainer = findViewById(R.id.gifsFragment);

	    if (layoutStyle == LayoutStyle.FULLSCREEN)
		    fragment_gifsContainer.setVisibility(View.GONE);

        launchUpdateIfNeeded();
    }

    @Override
    public void refreshListView() {
        fragment_list.refreshListView();
    }

    @Override
    public void onBackPressed() {
        final View about = findViewById(R.id.about);
        if (about.getVisibility() == View.VISIBLE) {
            AlphaAnimation a = new AlphaAnimation(1.0f, 0.0f);
            a.setDuration(300);
            a.setAnimationListener(new Animation.AnimationListener() {
                @Override public void onAnimationStart(Animation animation) { }
                @Override public void onAnimationRepeat(Animation animation) { }
                @Override public void onAnimationEnd(Animation animation) {
                    about.setVisibility(View.GONE);
                    about.setOnClickListener(null);
                }
            });
            about.startAnimation(a);
        } else if (layoutStyle == LayoutStyle.FULLSCREEN && fragment_gifsContainer.getVisibility() == View.VISIBLE)
            backToList();
        else
            super.onBackPressed();
    }

    private void launchUpdateIfNeeded() {
        boolean letsFetch;

        String lastUpdate = MainUtil.Prefs.getPref(this, "lastGifsListUpdate");
        if (lastUpdate.equals("doitnow") || lastUpdate.equals(""))
            letsFetch = true;
        else {
            Date last = GifUtil.stringToDate(MainUtil.Prefs.getPref(this, "lastGifsListUpdate"));
            long nbSecs = GifUtil.getSecsDiff(last, new Date());
            long nbHours = nbSecs / 3600;
            letsFetch = nbHours > 12;
        }

        if (letsFetch)
            new DataSourceParser(this).execute();
    }

    @Override
    public void onListItemClick(int position) {
	    fragment_gifs.setShownGif(position);

	    if (layoutStyle == LayoutStyle.FULLSCREEN) {
		    fragment_listContainer.setVisibility(View.GONE);
		    fragment_gifsContainer.setVisibility(View.VISIBLE);

		    menu_list_refresh.setVisible(false);
		    menu_list_about.setVisible(false);
		    menu_list_notifications.setVisible(false);
		    menu_list_clearCache.setVisible(false);
		    menu_gif_share.setVisible(true);
		    menu_gif_openWebsite.setVisible(true);
		    menu_gif_refresh.setVisible(true);

		    getSupportActionBar().setTitle("");
		    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	    }
    }

    private void backToList() {
	    if (layoutStyle == LayoutStyle.FULLSCREEN) {
		    fragment_listContainer.setVisibility(View.VISIBLE);
		    fragment_gifsContainer.setVisibility(View.GONE);

		    menu_list_refresh.setVisible(true);
		    menu_list_about.setVisible(true);
		    menu_list_notifications.setVisible(true);
		    menu_list_clearCache.setVisible(true);
		    menu_gif_share.setVisible(false);
		    menu_gif_openWebsite.setVisible(false);
		    menu_gif_refresh.setVisible(false);

		    getSupportActionBar().setTitle(gas.getBundle().getAppName());
		    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
	    }
    }

	@Override
	public void onGifsListChanged() {
		fragment_gifs.notifyDataSetChanged();
	}

	@Override
    public ProgressBar getProgressBar() {
        return (ProgressBar) findViewById(R.id.pb);
    }

    private void enableNotifs() {
        MainUtil.Prefs.setPref(this, "notifs", true);

        int minutes = 180;
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, gas.getBundle().getNotificationsServiceClass());
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        am.cancel(pi);
        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + minutes*60*1000, minutes*60*1000, pi);
        if (menu_list_notifications != null)
            menu_list_notifications.setChecked(true);
        try {
            pi.send();
        } catch (PendingIntent.CanceledException e) {	e.printStackTrace(); }
    }

    private void disableNotifs() {
        MainUtil.Prefs.setPref(this, "notifs", false);
        if (menu_list_notifications != null)
            menu_list_notifications.setChecked(false);
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            backToList();
        } else if (item.getItemId() == R.id.menu_list_refresh) {
            MainUtil.Prefs.setPref(this, "lastGifsListUpdate", "doitnow");
            new DataSourceParser(this).execute();
            return true;
        } else if (item.getItemId() == R.id.menu_list_notifications) {
            item.setChecked(!item.isChecked());
            if (item.isChecked())
                enableNotifs();
            else
                disableNotifs();
        } else if (item.getItemId() == R.id.menu_list_about) {
            final LinearLayout l = (LinearLayout) findViewById(R.id.about);
	        TextView aboutTv = (TextView) findViewById(R.id.about_textView);
	        aboutTv.setText(gas.getBundle().getAboutText());
            if (l.getVisibility() == View.GONE) {
                MainUtil.Fonts.setFont(this, l, MainUtil.Fonts.CustomFont.Roboto_Regular);
                l.setVisibility(View.VISIBLE);
                AlphaAnimation a = new AlphaAnimation(0.0f, 1.0f);
                a.setDuration(500);
                a.setFillAfter(true);
                a.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) { }
                    @Override public void onAnimationRepeat(Animation animation) { }
                    @Override public void onAnimationEnd(Animation animation) { }
                });
                l.startAnimation(a);
                l.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlphaAnimation a = new AlphaAnimation(1.0f, 0.0f);
                        a.setDuration(300);
                        a.setAnimationListener(new Animation.AnimationListener() {
                            @Override public void onAnimationStart(Animation animation) { }
                            @Override public void onAnimationRepeat(Animation animation) { }
                            @Override public void onAnimationEnd(Animation animation) {
                                l.setVisibility(View.GONE);
                                l.setOnClickListener(null);
                            }
                        });
                        l.startAnimation(a);
                    }
                });
            } else {
                AlphaAnimation a = new AlphaAnimation(1.0f, 0.0f);
                a.setDuration(300);
                a.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) { }
                    @Override public void onAnimationRepeat(Animation animation) { }
                    @Override public void onAnimationEnd(Animation animation) {
                        l.setVisibility(View.GONE);
                        l.setOnClickListener(null);
                    }
                });
                l.startAnimation(a);
            }
            return true;
        } else if (item.getItemId() == R.id.menu_list_clearCache) {
            CacheUtil.clearCache(this, gas.getBundle().getSdDirectory());
            return true;
        } else if (item.getItemId() == R.id.menu_gif_share) {
            Gif gif = gas.getGifs().get(fragment_gifs.getCurrentPosition());
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            share.putExtra(Intent.EXTRA_SUBJECT, gif.getName());
            share.putExtra(Intent.EXTRA_TEXT, gif.getArticleUrl());
            startActivity(Intent.createChooser(share, getString(R.string.menu_share)));
            return true;
        } else if (item.getItemId() == R.id.menu_gif_openWebsite) {
            Gif gif = gas.getGifs().get(fragment_gifs.getCurrentPosition());
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(gif.getArticleUrl()));
            startActivity(browserIntent);
            return true;
        } else if (item.getItemId() == R.id.menu_gif_refresh) {
            fragment_gifs.refreshCurrentGif();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        menu_list_refresh = menu.findItem(R.id.menu_list_refresh);
        menu_list_about = menu.findItem(R.id.menu_list_about);
        menu_list_notifications = menu.findItem(R.id.menu_list_notifications);
        menu_list_clearCache = menu.findItem(R.id.menu_list_clearCache);
        menu_gif_share = menu.findItem(R.id.menu_gif_share);
        menu_gif_openWebsite = menu.findItem(R.id.menu_gif_openWebsite);
        menu_gif_refresh = menu.findItem(R.id.menu_gif_refresh);

	    if (layoutStyle == LayoutStyle.SIDE_BY_SIDE) {
		    // Show all menu items on SIDE_BY_SIDE layout
		    menu_list_refresh.setVisible(true);
		    menu_list_about.setVisible(true);
		    menu_list_notifications.setVisible(true);
		    menu_list_clearCache.setVisible(true);
		    menu_gif_share.setVisible(true);
		    menu_gif_openWebsite.setVisible(true);
		    menu_gif_refresh.setVisible(true);
	    }

        return true;
    }

	private static boolean isDeviceLarge(MainUtil.DeviceSizeCategory deviceSizeCategory) {
		return deviceSizeCategory == MainUtil.DeviceSizeCategory.LARGE || deviceSizeCategory == MainUtil.DeviceSizeCategory.XLARGE;
	}
}
