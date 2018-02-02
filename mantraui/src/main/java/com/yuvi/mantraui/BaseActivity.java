package com.yuvi.mantraui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.mantraideas.simplehttp.datamanager.util.DmUtilities;
import com.yuvi.mantraui.news.NewsActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by yubaraj on 2/2/18.
 */

public class BaseActivity extends AppCompatActivity {
    protected Pref pref;
    String packageName, url;
    boolean persist = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        pref = new Pref(this);

        FrameLayout frameLayout = new FrameLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        frameLayout.setLayoutParams(layoutParams);
        setContentView(linearLayout);

        String newsConfig = pref.getPreferences(this.getClass().getSimpleName());

        packageName = pref.getPreferences("pkgname");
        url = pref.getPreferences("baseurl");

        Utils.log(BaseActivity.class, this.getClass().getSimpleName());
//        String newsConfig = pref.getPreferences("NewsActivity");
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
                    linearLayout.addView(frameLayout, 1);
                } else if (TextUtils.equals(showBannerAddOn, "bottom")) {
                    linearLayout.addView(frameLayout, 0);
                    linearLayout.addView(mAdView, 1);
                }
            } else {
                linearLayout.addView(frameLayout);
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

            addwithBaseOnCreate(savedInstanceState, frameLayout, model);
        } catch (Exception e) {
            e.printStackTrace();
            errorOnCreated(e.getMessage());

        }
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void addwithBaseOnCreate(Bundle savedInstanceState, FrameLayout rl, AdapterModel model) {

    }

    protected void errorOnCreated(String error) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
