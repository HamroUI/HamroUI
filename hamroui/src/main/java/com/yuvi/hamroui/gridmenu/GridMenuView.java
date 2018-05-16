package com.yuvi.hamroui.gridmenu;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yuvi.hamroui.R;
import com.yuvi.hamroui.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yubaraj on 1/7/18.
 */

public class GridMenuView extends RecyclerView {
    List<GridMenu> gridMenuList;
    OnGridMenuSelectedListener listener;
    LayoutManager layoutManager;
    boolean isGrid = false;

    public GridMenuView(Context context) {
        super(context);
        this.layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        init();
    }

    public GridMenuView(Context context, int spanCount) {
        super(context);
        this.layoutManager = new GridLayoutManager(context, spanCount);
        this.isGrid = true;
        init();
    }


    private void init() {
        this.gridMenuList = new ArrayList<>();
        setLayoutManager(layoutManager);
        this.setAdapter(new Adapter<ViewHolder>() {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gridmenu, parent, false);
                if(isGrid){
                    view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
                return new GridMenuViewHolder(view);
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                final int pos = position;
                if (holder instanceof GridMenuViewHolder) {
                    ((GridMenuViewHolder) holder).bindView(gridMenuList.get(pos));
                }
                holder.itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onSelected(gridMenuList.get(pos));
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

    //TODO make for dashboard


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
