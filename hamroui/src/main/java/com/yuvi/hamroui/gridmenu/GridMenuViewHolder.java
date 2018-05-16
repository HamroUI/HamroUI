package com.yuvi.hamroui.gridmenu;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuvi.hamroui.R;
import com.yuvi.hamroui.Utils;

/**
 * Created by yubaraj on 1/7/18.
 */

public class GridMenuViewHolder extends RecyclerView.ViewHolder {
    TextView tv_title;
    ImageView iv_icon;
    CardView cv_menu;

    public GridMenuViewHolder(View itemView) {
        super(itemView);
        tv_title = itemView.findViewById(R.id.tv_title);
        iv_icon = itemView.findViewById(R.id.iv_icon);
        cv_menu = itemView.findViewById(R.id.cv_menu);
    }

    public void bindView(GridMenu menu) {
        tv_title.setText(menu.title);
        Utils.loadImageWithGlide(itemView.getContext(), menu.icon, iv_icon, null);
    }
}
