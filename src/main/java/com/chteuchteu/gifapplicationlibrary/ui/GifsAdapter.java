package com.chteuchteu.gifapplicationlibrary.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentStatePagerAdapter;

import com.chteuchteu.gifapplicationlibrary.obj.Gif;

import java.util.HashMap;
import java.util.List;

public class GifsAdapter extends FragmentStatePagerAdapter {
    private List<Gif> gifs;
    private HashMap<Integer, Fragment_Gif> fragments;

    public GifsAdapter(List<Gif> gifs, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.gifs = gifs;
        this.fragments = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments.containsKey(position))
            return fragments.get(position);

        Fragment_Gif newFragment = Fragment_Gif.init(position);
        fragments.put(position, newFragment);
        return newFragment;
    }

    @Override
    public int getCount() {
        return gifs.size();
    }
}
