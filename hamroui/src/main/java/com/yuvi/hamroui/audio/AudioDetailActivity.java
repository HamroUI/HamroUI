package com.yuvi.hamroui.audio;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.yuvi.hamroui.AdapterModel;
import com.yuvi.hamroui.BaseActivity;
import com.yuvi.hamroui.Utils;

import org.json.JSONObject;

/**
 * Created by yubaraj on 12/28/17.
 */

public class AudioDetailActivity extends BaseActivity {
    private MediaPlayerService player;
    boolean serviceBound = false;
    public static final String Broadcast_PLAY_NEW_AUDIO = "com.yuvi.mantraui.audio.PlayNewAudio";

    @Override
    protected void addwithBaseOnCreate(Bundle savedInstanceState, FrameLayout frameLayout, AdapterModel model) {
        String data = getIntent().getStringExtra("data");
        try{
            JSONObject audioJSON = new JSONObject(data);
            String path  = audioJSON.optString("path");
            String title = audioJSON.optString("name");
            setTitle(title);
            Utils.log(getClass(), "path = " + path);
            if (checkAndRequestPermission()) {
                playAudio(path);
            }
        }catch (Exception e){e.printStackTrace();}

    }

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }


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
                    playAudio("https://upload.wikimedia.org/wikipedia/commons/6/6c/Grieg_Lyric_Pieces_Kobold.ogg");
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

    //Binding this Client to the AudioPlayer Service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;

            Toast.makeText(AudioDetailActivity.this, "Service Bound", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    private void playAudio(String media) {
        //Check is service is active
        if (!serviceBound) {
            Intent playerIntent = new Intent(this, MediaPlayerService.class);
            playerIntent.putExtra("media", media);
            startService(playerIntent);
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            //Service is active
            //Send media with BroadcastReceiver
            //Service is active
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            sendBroadcast(broadcastIntent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("ServiceState", serviceBound);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        serviceBound = savedInstanceState.getBoolean("ServiceState");
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (serviceBound) {
//            unbindService(serviceConnection);
//            //service is active
//            player.stopSelf();
//        }
//    }
}
