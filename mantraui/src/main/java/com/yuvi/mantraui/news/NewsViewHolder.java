package com.yuvi.mantraui.news;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yuvi.mantraui.R;
import com.yuvi.mantraui.Utils;
import com.yuvi.mantraui.simplelist.SimpleWebViewActivity;

import org.json.JSONObject;

/**
 * Created by yubaraj on 12/22/17.
 */

public class NewsViewHolder extends RecyclerView.ViewHolder {
    ImageView iv_image;
    TextView tv_news_title, tv_news_date, tv_news_desc;
    ProgressBar prgbar;
    CardView cv_image;

    public NewsViewHolder(final View itemView) {
        super(itemView);
        iv_image = itemView.findViewById(R.id.iv_image);
        tv_news_title = itemView.findViewById(R.id.tv_news_title);
        tv_news_date = itemView.findViewById(R.id.tv_news_date);
        tv_news_desc = itemView.findViewById(R.id.tv_news_desc);
        prgbar = itemView.findViewById(R.id.prgbar);
        cv_image = itemView.findViewById(R.id.cv_image);

    }

    public void bindView(JSONObject newsJSON) {
        String imageUrl = newsJSON.optString("image");
        if (TextUtils.isEmpty(imageUrl)) {
            cv_image.setVisibility(View.GONE);
        } else {
            cv_image.setVisibility(View.VISIBLE);
            Utils.loadImageWithGlide(itemView.getContext(), imageUrl, iv_image, prgbar);
        }
        tv_news_title.setText(newsJSON.optString("title"));
        tv_news_desc.setText(Html.fromHtml(newsJSON.optString("description")));
        tv_news_date.setText(newsJSON.optString("pubDate"));

    }

}
