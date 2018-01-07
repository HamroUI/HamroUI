package com.yuvi.mantraui.gridmenu;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuvi.mantraui.BaseRecyclerViewAdapter;
import com.yuvi.mantraui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yubaraj on 1/7/18.
 */

public class GridMenuView extends RecyclerView {
    List<GridMenu> gridMenuList;
    OnGridMenuSelectedListener listener;

    public GridMenuView(Context context) {
        super(context);
        init();
        this.gridMenuList = new ArrayList<>();
    }

    private void init() {
        setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        this.setAdapter(new Adapter<ViewHolder>() {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new GridMenuViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gridmenu, parent, false));
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, final int position) {
                if (holder instanceof GridMenuViewHolder) {
                    ((GridMenuViewHolder) holder).bindView(gridMenuList.get(position));
                }
                holder.itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onSelected(gridMenuList.get(position));
                        }
                    }
                });
            }

            @Override
            public int getItemCount() {
                return gridMenuList.size();
            }
        });
    }

    public void setOnGridMenuSelectedListener(OnGridMenuSelectedListener listener) {
        this.listener = listener;
    }

    public void updateGridMenu(List<GridMenu> menuList, boolean clearAndUpdate) {
        if (clearAndUpdate) {
            this.gridMenuList.clear();
            this.gridMenuList = menuList;
        } else {
            gridMenuList.addAll(menuList);
        }
        this.getAdapter().notifyDataSetChanged();
    }
}