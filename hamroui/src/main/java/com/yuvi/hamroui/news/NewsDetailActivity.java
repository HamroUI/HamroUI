package com.yuvi.hamroui.news;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mantraideas.simplehttp.datamanager.util.DmUtilities;
import com.yuvi.hamroui.AdapterModel;
import com.yuvi.hamroui.BaseActivity;
import com.yuvi.hamroui.R;
import com.yuvi.hamroui.Utils;

import org.json.JSONObject;

public class NewsDetailActivity extends BaseActivity {
    TextView tv_news_source, tv_news_date;
    WebView wv_news;
    ImageView iv_news;
//    String newsId = "";

    @Override
    protected void addwithBaseOnCreate(Bundle savedInstanceState, FrameLayout frameLayout, AdapterModel model) {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_news_detail, null);
        frameLayout.addView(view);
        tv_news_source = view.findViewById(R.id.tv_news_source);
        tv_news_date = view.findViewById(R.id.tv_pub_date);
        wv_news = view.findViewById(R.id.wv_news);
        iv_news = view.findViewById(R.id.iv_newsimage);

        if (!fromApp && !TextUtils.isEmpty(getIntent().getDataString()) && DmUtilities.isNetworkConnected(this)) {
//            newsId = Uri.parse(getIntent().getDataString()).getQueryParameter("id");
//            if (model.requestMap.containsKey("id")) {
//                model.requestMap.put("id", newsId);
//            }
            Uri uri = getIntent().getData();
            for (String key : uri.getQueryParameterNames()) {
                model.requestMap.put(key, uri.getQueryParameter(key));
            }
            News news = ViewModelProviders.of(this).get(News.class);
            news.loadNews(model, this);
            news.getNews().observe(this, new Observer<String>() {
                @Override
                public void onChanged(@Nullable String s) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        updateView(new News(jsonObject));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            showProgressDialog("Loading news from server, Please wait");
        }
        if (fromApp) {
            String data = getIntent().getStringExtra("data");
            Utils.log(getClass(), "data = " + data);
            try {
                JSONObject mJSON = new JSONObject(data);
                updateView(new News(mJSON));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void updateView(News news) {
        hideProgressDialog();
        getSupportActionBar().setTitle(news.name);
        tv_news_source.setText(news.source);
        tv_news_date.setText("Published : " + Utils.getRelativeTime(news.published_date, "yyyy-MM-dd HH:mm:ss"));
        Utils.loadImageWithGlide(this, news.image, iv_news, null);
        wv_news.getSettings().setJavaScriptEnabled(true);
        wv_news.loadDataWithBaseURL("", news.description, "text/html", "UTF-8", null);
    }
}
