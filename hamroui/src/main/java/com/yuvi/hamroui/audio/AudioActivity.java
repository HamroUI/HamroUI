package com.yuvi.hamroui.audio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.yuvi.hamroui.AdapterModel;
import com.yuvi.hamroui.BaseActivity;
import com.yuvi.hamroui.BaseRecyclerViewAdapter;
import com.yuvi.hamroui.OnItemClickListener;
import com.yuvi.hamroui.R;
import com.yuvi.hamroui.Utils;
import com.yuvi.hamroui.video.VideoAdapter;
import com.yuvi.hamroui.video.VideoDetailActivity;

import org.json.JSONObject;

public class AudioActivity extends BaseActivity implements OnItemClickListener {

    @Override
    protected void addwithBaseOnCreate(Bundle savedInstanceState, FrameLayout frameLayout, AdapterModel model) {
        super.addwithBaseOnCreate(savedInstanceState, frameLayout, model);
        setTitle("AudioList");
        Utils.log(getClass(), "audioactivity opened");

        RecyclerView recyclerView = new RecyclerView(this);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        recyclerView.setLayoutParams(layoutParams);
        frameLayout.addView(recyclerView);

        AudioAdapter adapter = new AudioAdapter(getApplicationContext(), model) {
            @Override
            protected void onLoadingMoreComplete() {
                super.onLoadingMoreComplete();
                Toast.makeText(getApplicationContext(), "No more audios", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onFailed(String message) {
                super.onFailed(message);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void showLoadingProgress() {
                super.showLoadingProgress();
                showProgressDialog("Loading audioList, Please wait");
            }

            @Override
            protected void hideLoadingProgress(String mesg) {
                super.hideLoadingProgress(mesg);
                hideProgressDialog();
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter.setOnLoadMoreListener(recyclerView);
        adapter.setOnItemCLickListener(this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onItemClicked(JSONObject dataJSON) {
        startActivity(new Intent(getApplicationContext(), AudioDetailActivity.class)
                .putExtra("fromApp", true)
                .putExtra("data", dataJSON.toString())
        );
    }
}
