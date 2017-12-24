package com.yuvi.mantraui.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuvi.mantraui.BaseRecyclerViewAdapter;
import com.yuvi.mantraui.R;

import java.util.HashMap;

/**
 * Created by yubaraj on 12/22/17.
 */

public class NewsAdapter extends BaseRecyclerViewAdapter<NewsViewHolder> {

    public NewsAdapter(Context context, String url, String packageName, HashMap<String, String> requestMap, boolean hasPagination) {
        super(context, requestMap, hasPagination);
        queryData(url, packageName);
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        holder.bindView(getData(position));
    }
}
