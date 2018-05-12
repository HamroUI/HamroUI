package com.yuvi.mantraui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
    boolean persist = false, hasPagination = false;
    String label = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        pref = new Pref(this);
        this.label = this.getClass().getSimpleName();

        FrameLayout frameLayout = new FrameLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        frameLayout.setLayoutParams(layoutParams);
        setContentView(linearLayout);

        String config = pref.getPreferences(this.getClass().getSimpleName());

        packageName = pref.getPreferences("pkgname");
        url = pref.getPreferences("baseurl");

        Utils.log(BaseActivity.class, this.getClass().getSimpleName());
        String packageName = pref.getPreferences("pkgname");
        String url = pref.getPreferences("baseurl");
        Utils.log(NewsActivity.class, "packageName = " + packageName + " url = " + url + " newsConfig = " + config);
        HashMap<String, String> requestMap = new HashMap<>();
        String showBannerAddOn = "";
        try {
            JSONObject configJSON = new JSONObject(config);
            JSONObject requestJSON = configJSON.optJSONObject("request");
            String admob_id = pref.getPreferences(Pref.KEY_ADMOB_ID);
            String banneradd_id = pref.getPreferences(Pref.KEY_BANNER_ID);

            if (DmUtilities.isNetworkConnected(this)
                    && !TextUtils.isEmpty(admob_id)
                    && !TextUtils.isEmpty(banneradd_id)
                    && configJSON.has("showbanneraddon")
                    && !TextUtils.isEmpty(configJSON.optString("showbanneraddon"))) {

                showBannerAddOn = configJSON.optString("showbanneraddon");
                MobileAds.initialize(this, admob_id);
                AdView mAdView = new AdView(this);
                mAdView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                mAdView.setAdSize(AdSize.SMART_BANNER);
                mAdView.setAdUnitId(banneradd_id);
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
                Log.w("Adview", "check internet connection or admobid or bannerid is missing in config");
                linearLayout.addView(frameLayout);
            }

            if (configJSON.has("url") && !TextUtils.isEmpty(configJSON.optString("url"))) {
                url = configJSON.optString("url");
            }
            if (configJSON.has("persist")) {
                persist = configJSON.optBoolean("persist");
            }
            if (configJSON.has("label")) {
                label = configJSON.optString("label");
            }
            if (configJSON.has("hasPagination")) {
                hasPagination = configJSON.optBoolean("hasPagination", false);
            }
            Iterator<String> keys = requestJSON.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                requestMap.put(key, requestJSON.optString(key));
            }

            AdapterModel model = new AdapterModel(label, requestMap, url, packageName, hasPagination, persist);

            addwithBaseOnCreate(savedInstanceState, frameLayout, model);
        } catch (Exception e) {
            e.printStackTrace();
            errorOnCreated(e.getMessage());

        }
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void addwithBaseOnCreate(Bundle savedInstanceState, FrameLayout frameLayout, AdapterModel model) {

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
