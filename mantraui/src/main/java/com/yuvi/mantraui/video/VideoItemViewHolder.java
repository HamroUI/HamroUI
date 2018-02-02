package com.yuvi.mantraui.video;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yuvi.mantraui.R;
import com.yuvi.mantraui.Utils;

import org.json.JSONObject;

public class VideoItemViewHolder extends RecyclerView.ViewHolder {
    ImageView iv_thumbnail;
    TextView tv_title, tv_date;
    ProgressBar prgbar;

    public VideoItemViewHolder(View view) {
        super(view);
        prgbar = view.findViewById(R.id.prgbar);
        iv_thumbnail = view.findViewById(R.id.iv_thumbnail);
        tv_date = view.findViewById(R.id.tv_date);
        tv_title = view.findViewById(R.id.tv_title);
    }

    public void bindView(JSONObject jsonObject){
        Utils.log(this.getClass(), "data = " + jsonObject.toString());
        tv_title.setText(jsonObject.optString("name"));
        tv_date.setText(Utils.getRelativeTime(jsonObject.optString("published_date"), "yyyy-MM-dd"));
        String imageUrl = Utils.getUrlFromYoutubeKey(jsonObject.optString("youtubeID"));
        Utils.log(this.getClass(), "imageUrl = " + imageUrl + " publisheddate = " + jsonObject.optString("published_date"));
        Utils.loadImageWithGlide(itemView.getContext(), imageUrl, iv_thumbnail, prgbar);
    }
}