package com.yuvi.mantraui.news;

import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mantraideas.simplehttp.datamanager.util.DmUtilities;
import com.yuvi.mantraui.AdapterModel;
import com.yuvi.mantraui.BaseActivity;
import com.yuvi.mantraui.R;
import com.yuvi.mantraui.Utils;

import org.json.JSONObject;

public class NewsDetailActivity extends BaseActivity {
    TextView tv_news_source, tv_news_date;
    WebView wv_news;
    ImageView iv_news;
    boolean isFromDeeplink = false;
    String newsId = "";
    String title = "", desc = "", image = "", published_date = "", source = "";
    boolean fromApp = true;

    ProgressDialog pd = null;

    @Override
    protected void addwithBaseOnCreate(Bundle savedInstanceState, FrameLayout frameLayout, AdapterModel model) {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_news_detail, null);
        frameLayout.addView(view);
        fromApp = getIntent().hasExtra("fromApp") && getIntent().getBooleanExtra("fromApp", true);
        tv_news_source = view.findViewById(R.id.tv_news_source);
        tv_news_date = view.findViewById(R.id.tv_pub_date);
        wv_news = view.findViewById(R.id.wv_news);
        iv_news = view.findViewById(R.id.iv_newsimage);

        if (!fromApp && !TextUtils.isEmpty(getIntent().getDataString()) && DmUtilities.isNetworkConnected(this)) {
            isFromDeeplink = true;
            newsId = Uri.parse(getIntent().getDataString()).getQueryParameter("id");
            if (model.requestMap.containsKey("id")) {
                model.requestMap.put("id", newsId);
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
            showProgressDialog();
        } else {
            String data = getIntent().getStringExtra("data");
            Utils.log(getClass(), "data = " + data);
            try{
               JSONObject mJSON = new JSONObject(data);
                updateView(new News(mJSON));
            }catch (Exception e){e.printStackTrace();}
        }
    }

    private void showProgressDialog() {
        if (pd == null)
            pd = new ProgressDialog(this);
        pd.setMessage("Loading news from server, Please wait");
        pd.show();

    }

    private void hideProgressDialog() {
        if (pd != null)
            pd.dismiss();
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


    @Override
    public void onBackPressed() {
        if (!fromApp && isFromDeeplink) {
            startActivity(new Intent(this, NewsActivity.class));
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
