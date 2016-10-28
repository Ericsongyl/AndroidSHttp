package com.zsy.core.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListViewUtility {
	Context mContext;

	public ListViewUtility(Context context) {
		mContext = context;
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		try {
			ListAdapter listAdapter = listView.getAdapter();
			if (listAdapter == null) {
				// pre-condition
				return;
			}
			int totalHeight = 0;
			for (int i = 0; i < listAdapter.getCount(); i++) {
				View listItem = listAdapter.getView(i, null, listView);
				if (listItem != null) {
					listItem.measure(0, 0);
					totalHeight += listItem.getMeasuredHeight();
				}
			}
			ViewGroup.LayoutParams params = listView.getLayoutParams();
			params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
			listView.setLayoutParams(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String formatStr(int id, int content) {
		return String.format(mContext.getString(id), String.valueOf(content));
	}

	public String formatStr(int id, String content) {
		return String.format(mContext.getString(id), content);
	}
}