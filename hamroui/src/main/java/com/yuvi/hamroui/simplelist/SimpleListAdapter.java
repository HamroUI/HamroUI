package com.yuvi.hamroui.simplelist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yuvi.hamroui.AdapterModel;
import com.yuvi.hamroui.BaseRecyclerViewAdapter;
import com.yuvi.hamroui.OnItemClickListener;
import com.yuvi.hamroui.R;

import org.json.JSONObject;

/**
 * Created by yubaraj on 2/4/18.
 */

public class SimpleListAdapter extends BaseRecyclerViewAdapter {
    OnItemClickListener callback;

    public SimpleListAdapter(Context context, AdapterModel model, OnItemClickListener callback) {
        super(context, model);
        this.callback = callback;
        queryData();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_simple_list, parent, false);
        return new SimpleListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SimpleListViewHolder mHolder = (SimpleListViewHolder) holder;
        final JSONObject mJSON = getData(position);
        if (mJSON != null)
            mHolder.bindView(mJSON);
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null && mJSON != null) {
                    callback.onItemClicked(mJSON);
                }
            }
        });

    }
}
