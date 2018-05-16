package com.yuvi.hamroui.video;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.yuvi.hamroui.Pref;
import com.yuvi.hamroui.R;


/**
 * Created by yubaraj on 12/25/17.
 */

public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, OnVideoErrorActionListener, YouTubePlayer.PlayerStateChangeListener {

    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    String yId = "";
    Pref pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_youtubeplayer);
        youTubeView = findViewById(R.id.youtube_view);

        yId = getIntent().getStringExtra("yid");
        pref = new Pref(this);
        String youtubeAPIKey = pref.getPreferences(Pref.KEY_YOUTUBE_ID);
        if (!TextUtils.isEmpty(youtubeAPIKey)) {
            //API Key
            youTubeView.initialize(youtubeAPIKey, this);
        } else {
            Log.w("Player", "Youtube id is missing in app config");
        }

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        player.setPlayerStateChangeListener(this);
        if (!wasRestored) {
            player.cueVideo(yId); // Plays https://www.youtube.com/watch?v=yid
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }

        Log.d("PlayerActivity", "error = " + errorReason.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(getString(R.string.youtubekey), this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

    @Override
    public void onGoback() {
        onBackPressed();
    }

    @Override
    public void onOpenWithYoutube(String youtubeId) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + youtubeId)));
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoaded(String s) {

    }

    @Override
    public void onAdStarted() {

    }

    @Override
    public void onVideoStarted() {

    }

    @Override
    public void onVideoEnded() {

    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {
        Log.d("PlayerActivity", "errorMesg = " + errorReason.name());
        // first set the screen to portrait , usually the player video is in landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        VideoErrorDialogueFragment fragment = new VideoErrorDialogueFragment();
        Bundle bundle = new Bundle();
        bundle.putString("yid", yId);
        fragment.setArguments(bundle);
        fragment.show(getFragmentManager(), "errordialog");
    }
}
