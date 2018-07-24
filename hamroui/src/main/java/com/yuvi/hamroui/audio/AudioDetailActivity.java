package com.yuvi.hamroui.audio;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.yuvi.hamroui.AdapterModel;
import com.yuvi.hamroui.BaseActivity;
import com.yuvi.hamroui.R;
import com.yuvi.hamroui.Utils;

import org.json.JSONObject;

/**
 * Created by yubaraj on 12/28/17.
 */

public class AudioDetailActivity extends BaseActivity {
    //    private MediaPlayerService player;
//    boolean serviceBound = false;
    public static final String Broadcast_PLAY_NEW_AUDIO = "com.yuvi.mantraui.audio.PlayNewAudio";
    public static final String Broadcast_REMOVE_SERVICE = "com.yuvi.mantraui.audio.RemoveService";
    String path = "";
    //  View view;
    ImageView iv_thumbnail;
    ImageView iv_next, iv_prev, iv_play;
    SeekBar seekBar;
    private boolean mUserIsSeeking = false;
    private PlayerAdapter mPlayerAdapter;
    boolean isPlaying = false;
    int currentMediaPos = 0;
//    ProgressBar prgBar;


    @Override
    protected void addwithBaseOnCreate(Bundle savedInstanceState, FrameLayout frameLayout, AdapterModel model) {
        super.addwithBaseOnCreate(savedInstanceState, frameLayout, model);
        View view = getLayoutInflater().inflate(R.layout.activity_audiodetail, null);
        frameLayout.addView(view);

        String data = getIntent().getStringExtra("data");
        try {
            JSONObject audioJSON = new JSONObject(data);
            path = audioJSON.optString("path");
            String title = audioJSON.optString("name");
            setTitle(title);
            Utils.log(getClass(), "path = " + path);
            if (checkAndRequestPermission()) {
                //  playAudio(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        initializeUI();
        initializeSeekbar();
        initializePlaybackController();
        mPlayerAdapter.loadMedia(path);
        mPlayerAdapter.play();

        Utils.log(getClass(), "savedInstance is null = " + (savedInstanceState == null));

//        Intent broadcastIntent = new Intent(Broadcast_REMOVE_SERVICE);
//        sendBroadcast(broadcastIntent);

//        Utils.log(getClass(), "servicebound = " + serviceBound);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Utils.log(getClass(), "OnStopCalled, media playing = " + mPlayerAdapter.isPlaying());
        if (isChangingConfigurations() || mPlayerAdapter.isPlaying()) {
            Utils.log(getClass(), "onStop: don't release MediaPlayer as screen is rotating & playing");
        } else {
            mPlayerAdapter.release();
            Utils.log(getClass(), "onStop: release MediaPlayer");
        }
    }

    private void initializeSeekbar() {
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int userSelectedPosition = 0;

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        mUserIsSeeking = true;
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            userSelectedPosition = progress;
                        }
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mUserIsSeeking = false;
                        mPlayerAdapter.seekTo(userSelectedPosition);
                    }
                });
    }

    private void initializeUI() {
        iv_thumbnail = findViewById(R.id.iv_thumbnail);
        seekBar = findViewById(R.id.seekbar);
        iv_next = findViewById(R.id.iv_next);
        iv_prev = findViewById(R.id.iv_prev);
        iv_play = findViewById(R.id.iv_play);
//        prgBar = findViewById(R.id.prgBar);

        iv_play.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mPlayerAdapter.isPlaying()) {
                            mPlayerAdapter.pause();
                            iv_play.setImageResource(android.R.drawable.ic_media_play);
                        } else {
                            mPlayerAdapter.play();
                            iv_play.setImageResource(android.R.drawable.ic_media_pause);
                        }
                        isPlaying = !isPlaying;
                    }
                });
    }

    private void initializePlaybackController() {
        MediaPlayerHolder mMediaPlayerHolder = new MediaPlayerHolder(this);
        Utils.log(getClass(), "initializePlaybackController: created MediaPlayerHolder");
        mMediaPlayerHolder.setPlaybackInfoListener(new PlaybackListener());
        mPlayerAdapter = mMediaPlayerHolder;
        Utils.log(getClass(), "initializePlaybackController: MediaPlayerHolder progress callback set");
    }

    public class PlaybackListener extends PlaybackInfoListener {

        @Override
        public void onDurationChanged(int duration) {
            seekBar.setMax(duration);
            Utils.log(getClass(), String.format("setPlaybackDuration: setMax(%d)", duration));
        }

        @Override
        public void onPositionChanged(int position) {
            if (!mUserIsSeeking) {
                seekBar.setProgress(position);
                currentMediaPos = position;
                Utils.log(getClass(), String.format("setPlaybackPosition: setProgress(%d)", position));
            }
        }

        @Override
        public void onStateChanged(@State int state) {
            String stateToString = PlaybackInfoListener.convertStateToString(state);
            onLogUpdated(String.format("onStateChanged(%s)", stateToString));
        }

        @Override
        public void onPlaybackCompleted() {
//            prgBar.setVisibility(View.GONE);
            Utils.log(AudioDetailActivity.this.getClass(), " playback loaded");
        }

        @Override
        public void onLogUpdated(String message) {
            Utils.log(getClass(), "message = " + message);
        }
    }


    private boolean checkAndRequestPermission() {
        // Here, thisActivity is the current activity

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.MEDIA_CONTENT_CONTROL)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_PHONE_STATE) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.MEDIA_CONTENT_CONTROL)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.MEDIA_CONTENT_CONTROL},
                        300);
            }
            return false;
        } else {
            // Permission has already been granted
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 300: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // if (!TextUtils.isEmpty(path))
                    //  playAudio(path);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

//    //Binding this Client to the AudioPlayer Service
//    private ServiceConnection serviceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            // We've bound to LocalService, cast the IBinder and get LocalService instance
//            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
//            player = binder.getService();
//            serviceBound = true;
//            Toast.makeText(AudioDetailActivity.this, "Service Bound", Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            serviceBound = false;
//        }
//    };

//    private void playAudio(String media) {
//        //Check is service is active
//        Utils.log(getClass(), "playAudio, servicebound = " + serviceBound);
//        if (!serviceBound) {
//            Intent playerIntent = new Intent(this, MediaPlayerService.class);
//            playerIntent.putExtra("media", media);
//            playerIntent.putExtra("currentPos", currentMediaPos + "");
//            startService(playerIntent);
//            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
//        } else {
//            //Service is active
//            //Send media with BroadcastReceiver
//            //Service is active
//            //Send a broadcast to the service -> PLAY_NEW_AUDIO
//            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
//            sendBroadcast(broadcastIntent);
//        }
//    }

//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        savedInstanceState.putBoolean("ServiceState", serviceBound);
//        super.onSaveInstanceState(savedInstanceState);
//    }
//
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        serviceBound = savedInstanceState.getBoolean("ServiceState");
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerAdapter.release();
    }
}
