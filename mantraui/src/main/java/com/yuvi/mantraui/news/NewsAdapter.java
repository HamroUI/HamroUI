package com.yuvi.mantraui.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuvi.mantraui.AdapterModel;
import com.yuvi.mantraui.BaseRecyclerViewAdapter;
import com.yuvi.mantraui.ProgressBarViewHolder;
import com.yuvi.mantraui.R;

import java.util.HashMap;

/**
 * Created by yubaraj on 12/22/17.
 */

public class NewsAdapter extends BaseRecyclerViewAdapter<RecyclerView.ViewHolder> {
    public NewsAdapter(Context context, AdapterModel adapterModel) {
        super(context, adapterModel);
        queryData();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_DATA) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_news, parent, false);
            return new NewsViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar, parent, false);
            return new ProgressBarViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NewsViewHolder) {
            ((NewsViewHolder) holder).bindView(getData(position));
        }
    }
}
