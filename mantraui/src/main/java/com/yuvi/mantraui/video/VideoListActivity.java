package com.yuvi.mantraui.video;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.yuvi.mantraui.AdapterModel;
import com.yuvi.mantraui.OnItemClickListener;
import com.yuvi.mantraui.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by yubaraj on 12/25/17.
 */

public class VideoListActivity extends AppCompatActivity  implements OnItemClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        String packageName = getIntent().getStringExtra("pn");
        String confingJSON = getIntent().getStringExtra("nc");
        String url = getIntent().getStringExtra("url");
        HashMap<String, String> videoMap = new HashMap<>();
        try {
            JSONObject mJSON = new JSONObject(confingJSON);
            Iterator<String> keys = mJSON.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                videoMap.put(key, mJSON.optString(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        RecyclerView rv_video = findViewById(R.id.rv_video);
        AdapterModel model = new AdapterModel(videoMap, url, packageName, true);
        VideoAdapter adapter = new VideoAdapter(getApplicationContext(), model){
            @Override
            protected void onLoadingMoreComplete() {
                super.onLoadingMoreComplete();
                Toast.makeText(getApplicationContext(), "No more videos", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onFailed(String message) {
                super.onFailed(message);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        };
        rv_video.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_video.setAdapter(adapter);
        adapter.setOnLoadMoreListener(rv_video);
        adapter.setOnItemCLickListener(this);
    }

    @Override
    public void onItemClicked(JSONObject dataJSON) {
        startActivity(new Intent(getApplicationContext(), PlayerActivity.class)
                .putExtra("videoId", dataJSON.optString("youtubeID"))
        );
    }
}
