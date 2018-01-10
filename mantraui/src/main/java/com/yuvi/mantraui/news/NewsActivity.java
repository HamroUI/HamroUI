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

public class NewsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        FrameLayout rl = new FrameLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        rl.setLayoutParams(layoutParams);

        RecyclerView rv_news = new RecyclerView(this);
        rv_news.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final TextView tv_nodata = new TextView(this);
        LinearLayout.LayoutParams textviewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tv_nodata.setLayoutParams(textviewParams);
        tv_nodata.setTextColor(Color.parseColor("#212121"));
        tv_nodata.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv_nodata.setGravity(Gravity.CENTER);
        tv_nodata.setVisibility(View.GONE);

        rl.addView(tv_nodata);
        rl.addView(rv_news);
        setContentView(linearLayout);


        setTitle("News");
        Pref pref = new Pref(this);
        String newsConfig = pref.getPreferences("NewsActivity");
        String packageName = pref.getPreferences("pkgname");
        String url = pref.getPreferences("baseurl");
        boolean persist = false;
        Utils.log(NewsActivity.class, "packageName = " + packageName + " url = " + url + " newsConfig = " + newsConfig);
        HashMap<String, String> newsMap = new HashMap<>();
        String showBannerAddOn = "";
        try {
            JSONObject newsConfigJSON = new JSONObject(newsConfig);
            JSONObject requestJSON = newsConfigJSON.optJSONObject("request");
            if (DmUtilities.isNetworkConnected(this) && newsConfigJSON.has("showbanneraddon") && !TextUtils.isEmpty(newsConfigJSON.optString("showbanneraddon"))) {
                showBannerAddOn = newsConfigJSON.optString("showbanneraddon");
                MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
                AdView mAdView = new AdView(this);
                mAdView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                mAdView.setAdSize(AdSize.SMART_BANNER);
                mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
                if (TextUtils.equals(showBannerAddOn, "top")) {
                    linearLayout.addView(mAdView, 0);
                    linearLayout.addView(rl, 1);
                } else if (TextUtils.equals(showBannerAddOn, "bottom")) {
                    linearLayout.addView(rl, 0);
                    linearLayout.addView(mAdView, 1);
                }
            } else {
                linearLayout.addView(rl);
            }

            if (newsConfigJSON.has("url") && !TextUtils.isEmpty(newsConfigJSON.optString("url"))) {
                url = newsConfigJSON.optString("url");
            }
            if (newsConfigJSON.has("persist")) {
                persist = newsConfigJSON.optBoolean("persist");
            }
            Iterator<String> keys = requestJSON.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                newsMap.put(key, requestJSON.optString(key));
            }
            AdapterModel model = new AdapterModel(newsMap, url, packageName, newsConfigJSON.optBoolean("hasPagination"), persist);
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        if (TextUtils.isEmpty(showBannerAddOn)) {
//            // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
//            MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
//            AdView mAdView = new AdView(this);
//            mAdView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            mAdView.setAdSize(AdSize.SMART_BANNER);
//            mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
//
//
////            AdView mAdView = findViewById(R.id.adView);
////            AdRequest adRequest = new AdRequest.Builder().build();
////            mAdView.loadAd(adRequest);
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
