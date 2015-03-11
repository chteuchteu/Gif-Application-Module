package com.chteuchteu.gifapplicationlibrary.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chteuchteu.gifapplicationlibrary.GifApplicationSingleton;
import com.chteuchteu.gifapplicationlibrary.R;
import com.chteuchteu.gifapplicationlibrary.async.GifDownloader;
import com.chteuchteu.gifapplicationlibrary.hlpr.CacheUtil;
import com.chteuchteu.gifapplicationlibrary.hlpr.GifUtil;
import com.chteuchteu.gifapplicationlibrary.hlpr.MainUtil;
import com.chteuchteu.gifapplicationlibrary.i.IActivity_Gif;
import com.chteuchteu.gifapplicationlibrary.obj.Gif;

import java.io.File;

public class Super_Activity_Gif extends ActionBarActivity implements IActivity_Gif {
    private Activity activity;
    private Context context;
    private Toolbar toolbar;
    private GifApplicationSingleton gas;

    private float deltaY;
    private int pos;
    private Gif gif;
    private GifDownloader gifDownloader;
    private WebView webView;
    private boolean textsShown;

    private static final int SWITCH_NEXT = 1;
    private static final int SWITCH_PREVIOUS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);
        gas = GifApplicationSingleton.getInstance();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activity = this;
        context = this;

        Intent thisIntent = getIntent();
        pos = 0;
        if (thisIntent != null && thisIntent.getExtras() != null
                && thisIntent.getExtras().containsKey("pos"))
            pos = thisIntent.getExtras().getInt("pos");

        gif = gas.getGifs().get(pos);

        textsShown = true;

        TextView header_nom = (TextView) findViewById(R.id.header_nom);
        header_nom.setText(gif.getName());
        if (header_nom.getText().toString().length() / 32 > 4) // nb lines
            header_nom.setLineSpacing(-10, 1);
        else if (header_nom.getText().toString().length() / 32 > 6)
            header_nom.setLineSpacing(-25, 1);

        webView = (WebView) findViewById(R.id.wv);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.setVerticalFadingEdgeEnabled(false);
        webView.setHorizontalFadingEdgeEnabled(false);
        webView.setBackgroundColor(0x00000000);

        TextView gif_precedent = (TextView) findViewById(R.id.gif_precedent);
        TextView gif_suivant = (TextView) findViewById(R.id.gif_suivant);
        if (pos == 0)
            gif_precedent.setVisibility(View.GONE);
        if (pos == gas.getGifs().size()-1)
            gif_suivant.setVisibility(View.GONE);
        gif_precedent.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { switchGif(SWITCH_PREVIOUS); } });
        gif_suivant.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { switchGif(SWITCH_NEXT); } });

        findViewById(R.id.actions_container).post(new Runnable(){
            public void run() {
                deltaY = findViewById(R.id.actions_container).getHeight()/2;
            }
        });
        findViewById(R.id.onclick_catcher).setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { toggleTexts(); } });

        MainUtil.Fonts.setFont(this, (TextView) findViewById(R.id.header_nom), MainUtil.Fonts.CustomFont.Roboto_Light);
        MainUtil.Fonts.setFont(this, (TextView) findViewById(R.id.gif_precedent), MainUtil.Fonts.CustomFont.Roboto_Regular);
        MainUtil.Fonts.setFont(this, (TextView) findViewById(R.id.gif_suivant), MainUtil.Fonts.CustomFont.Roboto_Regular);

        loadGif();
    }

    @Override
    protected void onPause() {
        // Another activity comes into the foreground
        super.onPause();

        stopThread();
    }

    private void loadGif() {
        File photo = new File(gif.getEntiereFileName(gas.getBundle().getSdDirectory(), false));
        stopThread();
        webView.setVisibility(View.GONE);
        if (!photo.exists()) {
            gifDownloader = new GifDownloader(this, gif);
            gifDownloader.execute();
        } else {
            if (gif.getState() != Gif.ST_COMPLETE)
                gif.setState(Gif.ST_COMPLETE);
            String imagePath = gif.getEntiereFileName(gas.getBundle().getSdDirectory(), true);

            webView.loadDataWithBaseURL("", GifUtil.getHtml(imagePath), "text/html", "utf-8", "");

            webView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView v, String u) {
                    webView.setVisibility(View.VISIBLE);
                    AlphaAnimation a = new AlphaAnimation(0.0f, 1.0f);
                    a.setStartOffset(250);
                    a.setDuration(350);
                    a.setFillEnabled(true);
                    a.setFillAfter(true);
                    webView.startAnimation(a);
                }
            });
        }
    }

    private void switchGif(int which) {
        if (!textsShown) {
            toggleTexts();
            return;
        }

        stopThread();
        int targetPos = which == SWITCH_NEXT ? pos + 1 : pos - 1;

        if (targetPos >= 0 && targetPos < gas.getGifs().size()-1) {
            gif = gas.getGifs().get(targetPos);

            if (webView.getVisibility() == View.VISIBLE) {
                AlphaAnimation an = new AlphaAnimation(1.0f, 0.0f);
                an.setDuration(150);
                an.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) { }
                    @Override public void onAnimationRepeat(Animation animation) { }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        webView.setVisibility(View.GONE);
                    }
                });
                webView.startAnimation(an);
            }

            pos = targetPos;
            findViewById(R.id.gif_precedent).setVisibility(pos == 0 ? View.GONE : View.VISIBLE);
            findViewById(R.id.gif_suivant).setVisibility(pos == gas.getGifs().size()-1 ? View.GONE : View.VISIBLE);
            ((TextView) findViewById(R.id.header_nom)).setText(gif.getName());

            loadGif();
        }
    }

    private void stopThread() {
        if (gifDownloader != null) {
            boolean isDownloading = gifDownloader.isDownloading();
            gifDownloader.cancel(true);
            if (isDownloading) {
                File photo = new File(gif.getEntiereFileName(gas.getBundle().getSdDirectory(), false));
                if (photo.exists())
                    photo.delete();
            }
        }
    }

    private void toggleTexts() {
        TextView title = (TextView) findViewById(R.id.header_nom);
        RelativeLayout actions = (RelativeLayout) findViewById(R.id.actions_container);
        LinearLayout titleContainer = (LinearLayout) findViewById(R.id.header_nom_container);

        AlphaAnimation a;
        if (textsShown)
            a = new AlphaAnimation(1.0f, 0.0f);
        else
            a = new AlphaAnimation(0.0f, 1.0f);
        a.setDuration(250);
        a.setFillEnabled(true);
        a.setFillAfter(true);
        title.startAnimation(a);
        actions.startAnimation(a);
        titleContainer.startAnimation(a);

        // Put the gif a little bit higher
        deltaY = findViewById(R.id.actions_container).getHeight()/2;
        if (textsShown)
            deltaY = -deltaY;

        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, deltaY);
        anim.setDuration(250);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) { }
            @Override public void onAnimationRepeat(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                LinearLayout ll = (LinearLayout) findViewById(R.id.wv_container);
                RelativeLayout act = (RelativeLayout) findViewById(R.id.actions_container);
                if (textsShown)
                    ll.layout(ll.getLeft(), ll.getTop()+act.getHeight()/2, ll.getRight(), ll.getBottom());
                else
                    ll.layout(ll.getLeft(), ll.getTop()-act.getHeight()/2, ll.getRight(), ll.getBottom());
            }
        });
        anim.setFillEnabled(true);
        anim.setFillAfter(false);
        anim.setFillBefore(false);
        findViewById(R.id.wv_container).startAnimation(anim);

        textsShown = !textsShown;
    }

	@Override
	public ProgressBar getProgressBar() {
		return (ProgressBar) findViewById(R.id.pb);
	}

	@Override
	public WebView getWebView() {
		return (WebView) findViewById(R.id.wv);
	}

	@Override
    public void onBackPressed() {
        //stopThread();
        finish();
        MainUtil.Transitions.setTransition(this, MainUtil.Transitions.TransitionStyle.SHALLOWER);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        CacheUtil.removeUncompleteGifs(gas.getBundle().getSdDirectory(), this, gas.getGifs());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gifs, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    if (item.getItemId() == android.R.id.home) {
		    stopThread();
		    startActivity(new Intent(Super_Activity_Gif.this, Super_Activity_Main.class));
		    MainUtil.Transitions.setTransition(this, MainUtil.Transitions.TransitionStyle.SHALLOWER);
		    return true;
	    } else if (item.getItemId() == R.id.menu_refresh) {
		    stopThread();

		    gifDownloader = new GifDownloader(this, gif);

		    AlphaAnimation an = new AlphaAnimation(1.0f, 0.0f);
		    an.setDuration(150);
		    an.setFillEnabled(true);
		    an.setFillAfter(true);
		    an.setAnimationListener(new Animation.AnimationListener() {
			    @Override
			    public void onAnimationStart(Animation animation) {
			    }

			    @Override
			    public void onAnimationRepeat(Animation animation) {
			    }

			    @Override
			    public void onAnimationEnd(Animation animation) {
				    findViewById(R.id.pb).setVisibility(View.VISIBLE);
			    }
		    });
		    webView.startAnimation(an);

		    gifDownloader.execute();
		    return true;
	    } else if (item.getItemId() == R.id.menu_openwebsite) {
	        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(gif.getArticleUrl()));
			startActivity(browserIntent);
			return true;
	    } else if (item.getItemId() == R.id.menu_share) {
		    Intent share = new Intent(android.content.Intent.ACTION_SEND);
		    share.setType("text/plain");
		    share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    share.putExtra(Intent.EXTRA_SUBJECT, gif.getName());
		    share.putExtra(Intent.EXTRA_TEXT, gif.getArticleUrl());
		    startActivity(Intent.createChooser(share, getString(R.string.menu_share)));
		    return true;
	    }
        return super.onOptionsItemSelected(item);
    }
}
