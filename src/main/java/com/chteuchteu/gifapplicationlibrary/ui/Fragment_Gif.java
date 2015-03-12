package com.chteuchteu.gifapplicationlibrary.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chteuchteu.gifapplicationlibrary.GifApplicationSingleton;
import com.chteuchteu.gifapplicationlibrary.R;
import com.chteuchteu.gifapplicationlibrary.async.GifDownloader;
import com.chteuchteu.gifapplicationlibrary.hlpr.GifUtil;
import com.chteuchteu.gifapplicationlibrary.hlpr.MainUtil;
import com.chteuchteu.gifapplicationlibrary.obj.Gif;

import java.io.File;

public class Fragment_Gif extends Fragment {
    public static final String KEY_INDEX = "gifIndex";
    private GifApplicationSingleton gas;
    private Context context;

    private Gif gif;
    private View view;
    private ViewGroup webViewContainer;

    private boolean textsShown;
    private WebView webView;
    private ProgressBar progressBar;
    private GifDownloader gifDownloader;

    public static Fragment_Gif init(int gifIndex) {
        Fragment_Gif newFragment = new Fragment_Gif();
        Bundle arguments = new Bundle();
        arguments.putInt(KEY_INDEX, gifIndex);
        newFragment.setArguments(arguments);
        return newFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.gas = GifApplicationSingleton.getInstance();
        this.textsShown = true;

        Bundle args = getArguments();
        int gifIndex = args.getInt(KEY_INDEX, -1);
        this.gif = gas.getGifs().get(gifIndex);

        view = inflater.inflate(R.layout.fragment_gif, container, false);

        TextView header_nom = (TextView) view.findViewById(R.id.header_nom);
        header_nom.setText(gif.getName());
        if (header_nom.getText().toString().length() / 32 > 4) // nb lines
            header_nom.setLineSpacing(-10, 1);
        else if (header_nom.getText().toString().length() / 32 > 6)
            header_nom.setLineSpacing(-25, 1);

        progressBar = (ProgressBar) view.findViewById(R.id.pb);

        webViewContainer = (ViewGroup) view.findViewById(R.id.wvContainer);
        webView = getWebView(getActivity().getApplicationContext());
        webViewContainer.addView(webView);

        MainUtil.Fonts.setFont(context, (TextView) view.findViewById(R.id.header_nom), MainUtil.Fonts.CustomFont.Roboto_Light);

        view.findViewById(R.id.onclick_catcher).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTexts();
            }
        });

        loadGif();

        return view;
    }

    @SuppressLint("SetJavaScriptEnabled")
    public static WebView getWebView(Context applicationContext) {
        WebView webView = new WebView(applicationContext);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.setVerticalFadingEdgeEnabled(false);
        webView.setHorizontalFadingEdgeEnabled(false);
        webView.setBackgroundColor(0x00000000);
        return webView;
    }

    private void loadGif() {
        File photo = new File(gif.getEntiereFileName(gas.getBundle().getSdDirectory(), false));
        stopThread();
        webView.setVisibility(View.GONE);
        if (!photo.exists()) {
            gifDownloader = new GifDownloader(this, context, gif);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        webViewContainer.removeAllViews();
        webView.destroy();
    }

    public void refreshGif() {
        stopThread();

        gifDownloader = new GifDownloader(this, context, gif);

        AlphaAnimation an = new AlphaAnimation(1.0f, 0.0f);
        an.setDuration(150);
        an.setFillEnabled(true);
        an.setFillAfter(true);
        an.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) { }
            @Override public void onAnimationRepeat(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        webView.startAnimation(an);

        gifDownloader.execute();
    }

    private boolean stopThread() {
        if (gifDownloader != null) {
            boolean isDownloading = gifDownloader.isDownloading();
            gifDownloader.cancel(true);
            if (isDownloading) {
                File photo = new File(gif.getEntiereFileName(gas.getBundle().getSdDirectory(), false));
                if (photo.exists())
                    return photo.delete();
            }
        }
        return true;
    }

    private void toggleTexts() {
        TextView title = (TextView) view.findViewById(R.id.header_nom);
        LinearLayout titleContainer = (LinearLayout) view.findViewById(R.id.header_nom_container);

        AlphaAnimation a;
        if (textsShown)
            a = new AlphaAnimation(1.0f, 0.0f);
        else
            a = new AlphaAnimation(0.0f, 1.0f);
        a.setDuration(250);
        a.setFillEnabled(true);
        a.setFillAfter(true);
        title.startAnimation(a);
        titleContainer.startAnimation(a);

        textsShown = !textsShown;
    }

    public ProgressBar getProgressBar() { return this.progressBar; }
    public WebView getWebView() { return this.webView; }
    public Gif getGif() { return this.gif; }
}
