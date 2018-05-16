package com.yuvi.hamroui.gallery;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuvi.hamroui.R;

public class GalleryViewHolder extends RecyclerView.ViewHolder {
    ImageView iv;
    TextView tv_caption;

    public GalleryViewHolder(View itemView) {
        super(itemView);
        iv = itemView.findViewById(R.id.thubnail);
        tv_caption = itemView.findViewById(R.id.tv_title);
    }

}
