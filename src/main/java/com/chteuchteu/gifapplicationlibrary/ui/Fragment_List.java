package com.chteuchteu.gifapplicationlibrary.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.chteuchteu.gifapplicationlibrary.GifApplicationSingleton;
import com.chteuchteu.gifapplicationlibrary.R;
import com.chteuchteu.gifapplicationlibrary.hlpr.CacheUtil;
import com.chteuchteu.gifapplicationlibrary.i.IActivity_Main;
import com.chteuchteu.gifapplicationlibrary.obj.Gif;

import java.util.ArrayList;
import java.util.HashMap;

public class Fragment_List extends Fragment {
    private IActivity_Main iActivity;
    private Context context;
    private GifApplicationSingleton gas;

    private ListView listView;
    private ArrayList<HashMap<String, String>> list;

    public Fragment_List() {
        this.list = new ArrayList<>();
        this.gas = GifApplicationSingleton.getInstance();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
        this.iActivity = (IActivity_Main) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        this.listView = (ListView) view.findViewById(R.id.list);

        refreshListView();

        return view;
    }

    public void refreshListView() {
        this.list.clear();

        for (Gif g : gas.getGifs()) {
            if (g.isValid()) {
                HashMap<String,String> item = new HashMap<>();
                item.put("line1", g.getName());
                item.put("line2", g.getDate());
                list.add(item);
            }
        }
        SimpleAdapter sa = new SimpleAdapter(context, list, R.layout.list_item,
                new String[] { "line1","line2" }, new int[] {R.id.line_a, R.id.line_b});
        listView.setAdapter(sa);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                iActivity.onListItemClick(position);
            }
        });
        CacheUtil.saveLastViewed(context, gas.getFirstGif());
    }
}
