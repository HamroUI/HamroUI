package com.yuvi.mantraui.news;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.mantraideas.simplehttp.datamanager.util.DmUtilities;
import com.yuvi.mantraui.AdapterModel;
import com.yuvi.mantraui.BaseActivity;
import com.yuvi.mantraui.Pref;
import com.yuvi.mantraui.R;
import com.yuvi.mantraui.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by yubaraj on 12/21/17.
 */

public class NewsActivity extends BaseActivity {
    @Override
    protected void addwithBaseOnCreate(Bundle savedInstanceState, FrameLayout frameLayout, AdapterModel model) {
        super.addwithBaseOnCreate(savedInstanceState, frameLayout, model);
        setTitle("News");
        RecyclerView rv_news = new RecyclerView(this);
        rv_news.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final TextView tv_nodata = new TextView(this);
        LinearLayout.LayoutParams textviewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tv_nodata.setLayoutParams(textviewParams);
        tv_nodata.setTextColor(Color.parseColor("#212121"));
        tv_nodata.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv_nodata.setGravity(Gravity.CENTER);
        tv_nodata.setVisibility(View.GONE);

        frameLayout.addView(tv_nodata);
        frameLayout.addView(rv_news);

        NewsAdapter adapter = new NewsAdapter(this, model) {
            @Override
            protected void showLoadingProgress() {
                super.showLoadingProgress();
                Utils.log(NewsActivity.class, "show Progress is called");
                tv_nodata.setVisibility(View.VISIBLE);
                tv_nodata.setText("Loading News please wait");
            }

            @Override
            protected void hideLoadingProgress(String mesg) {
                super.hideLoadingProgress(mesg);
                if (TextUtils.isEmpty(mesg)) {
                    tv_nodata.setVisibility(View.GONE);
                } else {
                    if (getItemCount() == 0) {
                        tv_nodata.setText(mesg);
                    }
                }
            }

            @Override
            protected void onFailed(String message) {
                super.onFailed(message);
                Log.d("NewsActivity", "Failed, Message = " + message);
            }

            @Override
            protected void onLoadingMoreComplete() {
                super.onLoadingMoreComplete();
                Toast.makeText(getApplicationContext(), "No more news", Toast.LENGTH_SHORT).show();
            }
        };
        rv_news.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_news.setAdapter(adapter);
        adapter.setOnLoadMoreListener(rv_news);
    }

    @Override
    protected void errorOnCreated(String error) {
        super.errorOnCreated(error);
        toast(error);
    }
}
