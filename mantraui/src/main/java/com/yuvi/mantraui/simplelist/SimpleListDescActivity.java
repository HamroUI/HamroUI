package com.yuvi.mantraui.simplelist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.mantraideas.simplehttp.datamanager.util.DmUtilities;
import com.yuvi.mantraui.R;
import com.yuvi.mantraui.Utils;

/**
 * Created by yubaraj on 2/4/18.
 */

public class SimpleListDescActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_simpledesc);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        String title = getIntent().getStringExtra("title");
        String desc = getIntent().getStringExtra("desc");
        Utils.log(this.getClass(), "title = " + title);

        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }
        WebView wv_desc = findViewById(R.id.wv_desc);
        wv_desc.getSettings().setJavaScriptEnabled(true);
        wv_desc.loadDataWithBaseURL("", desc, "text/html", "UTF-8", null);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AdView adView = findViewById(R.id.adView);

        if (DmUtilities.isNetworkConnected(this)) {
            MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        } else {
            adView.setVisibility(View.GONE);
        }
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
