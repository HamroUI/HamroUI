package com.yuvi.hamroui.cast;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yuvi.hamroui.R;
import com.yuvi.hamroui.Utils;

public class CastViewHolder extends RecyclerView.ViewHolder {
    ImageView iv_cast;
    TextView tv_cast_name, tv_cast_subtitle;
    ProgressBar prgbar;

    public CastViewHolder(View itemView) {
        super(itemView);
        iv_cast = itemView.findViewById(R.id.iv_cast);
        tv_cast_name = itemView.findViewById(R.id.tv_cast_name);
        tv_cast_subtitle = itemView.findViewById(R.id.tv_cast_subtitle);
        prgbar = itemView.findViewById(R.id.prgbar);
    }

    public void bindDataWithView(Cast cast){
       tv_cast_name.setText(cast.name);
       tv_cast_subtitle.setText(cast.subtitle);
        Utils.loadImageWithGlide(itemView.getContext(), cast.img, iv_cast, prgbar);
    }
}
