package com.chteuchteu.gifapplicationlibrary.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chteuchteu.gifapplicationlibrary.GifApplicationSingleton;
import com.chteuchteu.gifapplicationlibrary.R;

public class Fragment_Gifs extends Fragment {
    private FragmentManager fragmentManager;

    private ViewPager viewPager;
    private GifsAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        GifApplicationSingleton gas = GifApplicationSingleton.getInstance();

        View view = inflater.inflate(R.layout.fragment_gifs, container, false);

        adapter = new GifsAdapter(gas.getGifs(), fragmentManager);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);

        return view;
    }

    public void setShownGif(int gifIndex) {
        viewPager.setCurrentItem(gifIndex, false);
    }

    public void refreshCurrentGif() {
        int currentItem = viewPager.getCurrentItem();
        Fragment_Gif fragment = (Fragment_Gif) adapter.getItem(currentItem);
        fragment.refreshGif();
    }

    public int getCurrentPosition() {
        return viewPager.getCurrentItem();
    }
}
