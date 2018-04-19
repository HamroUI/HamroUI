package com.yuvi.mantraui.video;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.mantraideas.simplehttp.datamanager.util.DmUtilities;
import com.yuvi.mantraui.Pref;
import com.yuvi.mantraui.R;
import com.yuvi.mantraui.Utils;

import org.json.JSONObject;

public class VideoDetailActivity extends AppCompatActivity {
    WebView wv;
    ImageView banner, playButton;
    String data = "";
    Pref pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videodetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        wv = findViewById(R.id.wv_video_detail);
        banner = findViewById(R.id.iv_video_detail_banner);
        playButton = findViewById(R.id.iv_video_detail_play);
        pref = new Pref(this);

        try {
//            if(!TextUtils.isEmpty(getIntent().getDataString())){
//                Uri uri = Uri.parse(getIntent().getDataString());
//                Utils.log(this.getClass(), "hasURIData = " + uri.getQueryParameterNames().contains("data"));
//                if(uri.getQueryParameterNames().contains("data")){
//                    data = uri.getQueryParameter(data);
////                    Utils.log(this.getClass(), "data found = "+ uri.toString());
//                    JSONObject mJSON = new JSONObject(data);
//                    Utils.log(this.getClass(), "data found = "+ mJSON.toString());
//                    return;
//                }
//            }else {
            boolean hasData = getIntent() != null && getIntent().hasExtra("data");
            if (hasData) {
                data = getIntent().getStringExtra("data");
            }
//            }
            Utils.log(this.getClass(), "data = " + data);

            JSONObject dataJSON = new JSONObject(data);
            String title = dataJSON.optString("title");
            setTitle(title);

            String description = dataJSON.optString("description");
            wv.getSettings().setJavaScriptEnabled(true);
            wv.loadDataWithBaseURL(null, description, "text/html", "UTF-8", null);

            final String youtubeId = dataJSON.optString("youtubeID");
            String imageUrl = "https://img.youtube.com/vi/" + youtubeId + "/mqdefault.jpg";
            Utils.loadImageWithGlide(this, imageUrl, banner, null);

            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), PlayerActivity.class)
                            .putExtra("yid", youtubeId));
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

        if (DmUtilities.isNetworkConnected(this)) {
           String banneradd_id = pref.getPreferences(Pref.KEY_BANNER_ID);
           String admob_id = pref.getPreferences(Pref.KEY_ADMOB_ID);
            MobileAds.initialize(this, admob_id);
            AdView mAdView = new AdView(this);
            mAdView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mAdView.setAdSize(AdSize.SMART_BANNER);
            mAdView.setAdUnitId(banneradd_id);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            ((LinearLayout)findViewById(R.id.ll_ads)).addView(mAdView);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
