package com.yuvi.hamroui.cagtegory;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuvi.hamroui.R;
import com.yuvi.hamroui.Utils;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    TextView tv_title;
    ImageView imageView;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        tv_title = itemView.findViewById(R.id.tv_category_title);
        imageView = itemView.findViewById(R.id.iv_thumbnail);
    }

    public void bindView(String title, String iconUrl) {
        if (TextUtils.isEmpty(iconUrl)) {
            imageView.setVisibility(View.GONE);
        } else {
            Utils.loadImageWithGlide(itemView.getContext(), iconUrl, imageView, null);
        }
        if (!TextUtils.isEmpty(title))
            tv_title.setText(title);
    }
}
