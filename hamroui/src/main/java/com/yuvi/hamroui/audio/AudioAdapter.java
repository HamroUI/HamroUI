package com.yuvi.hamroui.audio;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuvi.hamroui.AdapterModel;
import com.yuvi.hamroui.BaseRecyclerViewAdapter;
import com.yuvi.hamroui.OnItemClickListener;
import com.yuvi.hamroui.ProgressBarViewHolder;
import com.yuvi.hamroui.R;
import com.yuvi.hamroui.Utils;
import com.yuvi.hamroui.video.VideoItemViewHolder;

import org.json.JSONObject;

/**
 * Created by yubaraj on 12/25/17.
 */

public class AudioAdapter extends BaseRecyclerViewAdapter<RecyclerView.ViewHolder> {
    OnItemClickListener listener = null;

    public AudioAdapter(Context context, AdapterModel model) {
        super(context, model);
        queryData();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_DATA) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_audiolist, parent, false);
            return new AudioListViewHolder(view);
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
        if (holder instanceof AudioListViewHolder) {
            final JSONObject data = getData(position);
            Utils.log(getClass(), "pos = " + position + " data = " + data.toString());
            ((AudioListViewHolder) holder).bindView(data);
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
