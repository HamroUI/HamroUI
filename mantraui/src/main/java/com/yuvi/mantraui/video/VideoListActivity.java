package com.yuvi.mantraui.video;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.yuvi.mantraui.AdapterModel;
import com.yuvi.mantraui.BaseActivity;
import com.yuvi.mantraui.OnItemClickListener;
import com.yuvi.mantraui.R;

import org.json.JSONObject;


/**
 * Created by yubaraj on 12/25/17.
 */

public class VideoListActivity extends BaseActivity implements OnItemClickListener {

    @Override
    protected void addwithBaseOnCreate(Bundle savedInstanceState, FrameLayout frameLayout, AdapterModel model) {
        super.addwithBaseOnCreate(savedInstanceState, frameLayout, model);
        setTitle("Videos");
        View view = getLayoutInflater().inflate(R.layout.activity_video, null);
        frameLayout.addView(view);

        RecyclerView rv_video = view.findViewById(R.id.rv_video);
        VideoAdapter adapter = new VideoAdapter(getApplicationContext(), model) {
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
    protected void errorOnCreated(String error) {
        super.errorOnCreated(error);
        toast(error);
    }

    @Override
    public void onItemClicked(JSONObject dataJSON) {
        String youtubeID = dataJSON.optString("youtubeID");
        startActivity(new Intent(getApplicationContext(), PlayerActivity.class)
                .putExtra("yid", youtubeID)
        );
    }
}
