package com.yuvi.hamroui.video;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.yuvi.hamroui.AdapterModel;
import com.yuvi.hamroui.BaseActivity;
import com.yuvi.hamroui.OnItemClickListener;
import com.yuvi.hamroui.R;

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
        try {
            if (!TextUtils.isEmpty(getIntent().getDataString())) {
                Uri uri = getIntent().getData();
                if (uri.getQueryParameterNames() != null) {
                    for (String key : uri.getQueryParameterNames()) {
                        if (TextUtils.equals(key, "m_title")) {
                            getSupportActionBar().setTitle(uri.getQueryParameter(key));
                        } else if (TextUtils.equals(key, "fromapp")) {
                            fromApp = uri.getBooleanQueryParameter(key, false);
                        } else {
                            model.requestMap.put(key, uri.getQueryParameter(key));
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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

            @Override
            protected void showLoadingProgress() {
                super.showLoadingProgress();
                showProgressDialog("Loading videolist, Please wait");
            }

            @Override
            protected void hideLoadingProgress(String mesg) {
                super.hideLoadingProgress(mesg);
                hideProgressDialog();
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
//        String youtubeID = dataJSON.optString("youtubeID");
        startActivity(new Intent(getApplicationContext(), VideoDetailActivity.class)
                .putExtra("fromApp", true)
                .putExtra("data", dataJSON.toString())
        );
    }
}
