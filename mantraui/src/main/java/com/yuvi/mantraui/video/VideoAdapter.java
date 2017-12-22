package com.yuvi.mantraui.video;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.yuvi.mantraui.BaseRecyclerViewAdapter;

/**
 * Created by yubaraj on 12/22/17.
 */

public class VideoAdapter extends BaseRecyclerViewAdapter<RecyclerView.ViewHolder>{

    public VideoAdapter(String url, boolean hasPagination) {
        super(url, hasPagination);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
