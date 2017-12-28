package com.mantraideas.samplproject;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yuvi.mantraui.alert.AlertData;
import com.yuvi.mantraui.alert.AlertView;
import com.yuvi.mantraui.gallery.GalleryActivity;
import com.yuvi.mantraui.news.NewsActivity;
import com.yuvi.mantraui.video.VideoListActivity;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // AlertView
        AlertData alertData = new AlertData("this is alert test", "");
        AlertView alertView = findViewById(R.id.alertview);
        alertView.setData(alertData);
        alertView.setCloseIconColor(Color.parseColor("#FFFFFF"));

        // testNewsActivity
        findViewById(R.id.btn_news).setOnClickListener(v -> {
            String pn = MainActivity.this.getPackageName();
            String url = "http://aa.hamroapi.com/v1";
            String nc = "{\"action\":\"news\",\"start\":\"0\", \"limit\":\"10\"}";
            startActivity(new Intent(getApplicationContext(), NewsActivity.class)
                    .putExtra("pn", pn)
                    .putExtra("url", url)
                    .putExtra("nc", nc));
        });

        // testVideoActivity
        findViewById(R.id.btn_videos).setOnClickListener(v -> {
            String pn = MainActivity.this.getPackageName();
            String url = "http://vs.hamroapi.com/v4";
            String nc = "{\"action\":\"get_videos\",\"start\":\"0\", \"limit\":\"10\"}";
            startActivity(new Intent(getApplicationContext(), VideoListActivity.class)
                    .putExtra("pn", pn)
                    .putExtra("url", url)
                    .putExtra("nc", nc));
        });

        //Gallery

        findViewById(R.id.btn_gallery).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, GalleryActivity.class));
        });

    }
}
