package com.chteuchteu.gifapplicationlibrary.i;

import android.widget.ProgressBar;

public interface IActivity_Main {
    public void refreshListView();
    public ProgressBar getProgressBar();

	public void onGifsListChanged();

	// Fragment
    public void onListItemClick(int position);

	/**
	 * Called whenever the Fragment_Gifs currently displayed gif changes
	 */
	public void onFragmentGifsItemChanged(int newItem);
}
