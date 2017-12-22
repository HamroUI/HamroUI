package com.yuvi.mantraui.news;

import android.view.ViewGroup;

import com.yuvi.mantraui.BaseRecyclerViewAdapter;

/**
 * Created by yubaraj on 12/22/17.
 */

public class NewsAdapter extends BaseRecyclerViewAdapter<NewsViewHolder> {

    public NewsAdapter(String newsUrl, boolean hasPagination) {
        super(newsUrl, hasPagination);
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {

    }
}
