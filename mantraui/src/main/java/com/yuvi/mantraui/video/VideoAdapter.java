package com.yuvi.mantraui.video;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuvi.mantraui.AdapterModel;
import com.yuvi.mantraui.BaseRecyclerViewAdapter;
import com.yuvi.mantraui.OnItemClickListener;
import com.yuvi.mantraui.ProgressBarViewHolder;
import com.yuvi.mantraui.R;

import org.json.JSONObject;

/**
 * Created by yubaraj on 12/25/17.
 */

public class VideoAdapter extends BaseRecyclerViewAdapter<RecyclerView.ViewHolder> {
    OnItemClickListener listener = null;

    public VideoAdapter(Context context, AdapterModel model) {
        super(context, model);
        queryData();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_DATA) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_video, parent, false);
            return new VideoItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar, parent, false);
            return new ProgressBarViewHolder(view);
        }
    }

    public void setOnItemCLickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VideoItemViewHolder) {
            final JSONObject data = getData(position);
            ((VideoItemViewHolder) holder).bindView(data);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        listener.onItemClicked(data);
                    }
                }
            });
        }
    }
}
