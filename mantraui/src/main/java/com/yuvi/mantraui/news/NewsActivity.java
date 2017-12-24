package com.yuvi.mantraui.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.yuvi.mantraui.R;

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
        setContentView(R.layout.activity_news);
        RecyclerView rv_news = findViewById(R.id.rv_news);
        String packageName = getIntent().getStringExtra("pn");
        String confingJSON = getIntent().getStringExtra("nc");
        String url = getIntent().getStringExtra("url");
        HashMap<String, String> newsMap = new HashMap<>();
        try {
            JSONObject mJSON = new JSONObject(confingJSON);
            Iterator<String> keys = mJSON.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                newsMap.put(key, mJSON.optString(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        NewsAdapter adapter = new NewsAdapter(this, url, packageName, newsMap, false){
            @Override
            protected void onFailed(String message) {
                super.onFailed(message);
                Log.d("NewsActivity", "Failed, Message = " + message);
            }
        };
        rv_news.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_news.setAdapter(adapter);
    }
}
