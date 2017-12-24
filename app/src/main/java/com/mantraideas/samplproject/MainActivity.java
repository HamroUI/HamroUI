package com.mantraideas.samplproject;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yuvi.mantraui.alert.AlertData;
import com.yuvi.mantraui.alert.AlertView;
import com.yuvi.mantraui.news.NewsActivity;


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
        String pn = this.getPackageName();
        String url = "http://aa.hamroapi.com/v1";
        String nc = "{\"action\":\"news\",\"start\":\"0\", \"limit\":\"10\"}";
        startActivity(new Intent(getApplicationContext(), NewsActivity.class)
        .putExtra("pn", pn)
        .putExtra("url", url)
        .putExtra("nc", nc));

    }
}
