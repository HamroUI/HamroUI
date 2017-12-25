package com.yuvi.mantraui.video;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.yuvi.mantraui.R;


/**
 * Created by yubaraj on 12/25/17.
 */

public class PlayerActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {
    String videoId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_youtubeplayer);
        YouTubePlayerView yp = findViewById(R.id.youtube);
        yp.initialize("AIzaSyCTUf60W9vEPITXmr-2KqfcDfHna11_AyU", this);
        videoId = getIntent().getStringExtra("videoId");

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.loadVideo(videoId);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
