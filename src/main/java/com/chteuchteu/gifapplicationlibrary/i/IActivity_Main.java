package com.chteuchteu.gifapplicationlibrary.i;

import android.widget.ProgressBar;

public interface IActivity_Main {
    public void refreshListView();
    public ProgressBar getProgressBar();

	public void onGifsListChanged();

	// Fragment
    public void onListItemClick(int position);
}
