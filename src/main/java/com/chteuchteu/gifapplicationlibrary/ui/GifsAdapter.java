package com.chteuchteu.gifapplicationlibrary.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.chteuchteu.gifapplicationlibrary.obj.Gif;

import java.util.List;

public class GifsAdapter extends FragmentStatePagerAdapter {
    private List<Gif> gifs;

    public GifsAdapter(List<Gif> gifs, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.gifs = gifs;
    }

    @Override
    public Fragment getItem(int position) {
        return Fragment_Gif.init(position);
    }

    @Override
    public int getCount() {
        return gifs.size();
    }
}
