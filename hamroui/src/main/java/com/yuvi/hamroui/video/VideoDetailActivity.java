package com.yuvi.hamroui.video;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.mantraideas.simplehttp.datamanager.util.DmUtilities;
import com.yuvi.hamroui.AdapterModel;
import com.yuvi.hamroui.BaseActivity;
import com.yuvi.hamroui.Pref;
import com.yuvi.hamroui.R;
import com.yuvi.hamroui.Utils;
import com.yuvi.hamroui.news.News;

import org.json.JSONObject;

public class VideoDetailActivity extends BaseActivity {
    WebView wv;
    ImageView banner, playButton;
//    String videoID = "";

    @Override
    protected void addwithBaseOnCreate(Bundle savedInstanceState, FrameLayout frameLayout, AdapterModel model) {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_videodetail, null);
        frameLayout.addView(view);
        wv = findViewById(R.id.wv_video_detail);
        banner = findViewById(R.id.iv_video_detail_banner);
        playButton = findViewById(R.id.iv_video_detail_play);

        Utils.log(getClass(), "fromApp = " + fromApp);

        if (!fromApp && !TextUtils.isEmpty(getIntent().getDataString()) && DmUtilities.isNetworkConnected(this)) {
            Uri uri = getIntent().getData();
            for(String key : uri.getQueryParameterNames()){
                model.requestMap.put(key, uri.getQueryParameter(key));
            }
//            videoID = Uri.parse(getIntent().getDataString()).getQueryParameter("id");
//            if (model.requestMap.containsKey("id")) {
//                model.requestMap.put("id", videoID);
//            }
            Videos videos = ViewModelProviders.of(this).get(Videos.class);
            videos.loadVideo(model, this);
            videos.getVideos().observe(this, new Observer<String>() {
                @Override
                public void onChanged(@Nullable String s) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        updateView(new Videos(jsonObject));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            showProgressDialog("Loading videos from server, Please wait");
        }

        if (fromApp) {
            String data = getIntent().getStringExtra("data");
            Utils.log(getClass(), "data = " + data);
            try {
                JSONObject mJSON = new JSONObject(data);
                updateView(new Videos(mJSON));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void updateView(final Videos video) {
        hideProgressDialog();
        getSupportActionBar().setTitle(video.name);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadDataWithBaseURL(null, video.description, "text/html", "UTF-8", null);
        String imageUrl = "https://img.youtube.com/vi/" + video.youtubeId + "/mqdefault.jpg";
        Utils.loadImageWithGlide(this, imageUrl, banner, null);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PlayerActivity.class)
                        .putExtra("yid", video.youtubeId));
            }
        });

    }
}