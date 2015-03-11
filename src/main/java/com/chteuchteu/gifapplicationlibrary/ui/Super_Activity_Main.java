package com.chteuchteu.gifapplicationlibrary.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.chteuchteu.gifapplicationlibrary.GifApplicationSingleton;
import com.chteuchteu.gifapplicationlibrary.R;
import com.chteuchteu.gifapplicationlibrary.async.DataSourceParser;
import com.chteuchteu.gifapplicationlibrary.hlpr.CacheUtil;
import com.chteuchteu.gifapplicationlibrary.hlpr.GifUtil;
import com.chteuchteu.gifapplicationlibrary.hlpr.MainUtil;
import com.chteuchteu.gifapplicationlibrary.i.IActivity_Main;
import com.chteuchteu.gifapplicationlibrary.obj.Gif;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Super_Activity_Main extends ActionBarActivity implements IActivity_Main {
    private GifApplicationSingleton gas;

	private ArrayList<HashMap<String, String>> list;
    public static int scrollY;
    private ListView lv_gifs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	    gas = GifApplicationSingleton.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

	    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lv_gifs = (ListView) findViewById(R.id.list);
        list = new ArrayList<>();

        lv_gifs.post(new Runnable() {
            @Override
            public void run() {
                if (scrollY != 0)
                    lv_gifs.setSelectionFromTop(scrollY, 0);
            }
        });

        refreshListView();
        launchUpdateIfNeeded();
    }

    @Override
    public void refreshListView() {
        ListView l = (ListView) findViewById(R.id.list);
        list.clear();

        for (Gif g : gas.getGifs()) {
            if (g.isValid()) {
                HashMap<String,String> item = new HashMap<>();
                item.put("line1", g.getName());
                item.put("line2", g.getDate());
                list.add(item);
            }
        }
        SimpleAdapter sa = new SimpleAdapter(Super_Activity_Main.this, list, R.layout.list_item, new String[] { "line1","line2" }, new int[] {R.id.line_a, R.id.line_b});
        l.setAdapter(sa);

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                itemClick(position);
            }
        });
        CacheUtil.saveLastViewed(this, gas.getFirstGif());
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
        }
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

    private void itemClick(int pos) {
        Intent intent = new Intent(Super_Activity_Main.this, Super_Activity_Gif.class);
        scrollY = ((ListView) findViewById(R.id.list)).getFirstVisiblePosition();
        intent.putExtra("pos", pos);
        startActivity(intent);
        MainUtil.Transitions.setTransition(this, MainUtil.Transitions.TransitionStyle.DEEPER);
    }

    @Override
    public ProgressBar getProgressBar() {
        return (ProgressBar) findViewById(R.id.pb);
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
            MainUtil.Prefs.setPref(this, "lastGifsListUpdate", "doitnow");
            new DataSourceParser(this).execute();
            return true;
        } else if (item.getItemId() == R.id.menu_about) {
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
        } else if (item.getItemId() == R.id.menu_clear_cache) {
            CacheUtil.clearCache(this, gas.getBundle().getSdDirectory());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
