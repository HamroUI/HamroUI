package com.yuvi.mantraui.home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yuvi.mantraui.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by yubaraj on 1/5/18.
 */

public abstract class BaseHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    JSONArray jsonArray;

    public BaseHomeAdapter(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public void updateData(JSONArray mJsonArray, boolean append) {
        if (append) {
            Utils.concatJSONArray(jsonArray, mJsonArray);
        } else {
            this.jsonArray = mJsonArray;
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getItemView(), parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final JSONObject data = jsonArray.optJSONObject(position);
        bindView(holder.itemView, data);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClick(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public abstract int getItemView();

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void bindView(View view, JSONObject jsonObject) {

    }

    public void OnClick(JSONObject jsonObject) {

    }
}
