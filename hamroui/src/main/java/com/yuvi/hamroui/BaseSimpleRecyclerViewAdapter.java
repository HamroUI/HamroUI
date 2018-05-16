package com.yuvi.hamroui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class BaseSimpleRecyclerViewAdapter<L, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    List<L> l;
    int layout;

    public BaseSimpleRecyclerViewAdapter(List<L> l, int layout) {
        this.l = l;
        this.layout = layout;
        Log.d("BaseRecyclerViewAdapter", "listSize = " + l.size());
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
       return attachViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        viewBinded(holder, l.get(position));
    }

    @Override
    public int getItemCount() {
        return l.size();
    }

    public abstract void viewBinded(VH vh, L l);
    public abstract VH attachViewHolder(View view);
}
