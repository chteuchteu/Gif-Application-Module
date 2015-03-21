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
import com.chteuchteu.gifapplicationlibrary.i.IActivity_Main;

public class Fragment_Gifs extends Fragment {
	private IActivity_Main activity;
    private FragmentManager fragmentManager;

    private ViewPager viewPager;
    private GifsAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
	    this.activity = (IActivity_Main) activity;
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
	    viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
		    @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

		    @Override
		    public void onPageSelected(int position) {
				activity.onFragmentGifsItemChanged(position);
		    }

		    @Override public void onPageScrollStateChanged(int state) { }
	    });

        return view;
    }

	public void notifyDataSetChanged() {
		this.adapter.notifyDataSetChanged();
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
