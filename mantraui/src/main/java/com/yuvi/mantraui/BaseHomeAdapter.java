package com.yuvi.mantraui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(getItemView(), parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindView(holder.itemView, jsonArray.optJSONObject(position));
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

}
