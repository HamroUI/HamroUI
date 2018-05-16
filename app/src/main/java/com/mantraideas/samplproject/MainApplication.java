package com.mantraideas.samplproject;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.yuvi.hamroui.Pref;

import io.fabric.sdk.android.Fabric;

/**
 * Created by yubaraj on 3/20/18.
 */

public class MainApplication extends Application {
    public static Context context;
    private final String TAG = "MainApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        Pref pref = new Pref(this);
        if (!pref.containsKey("notification") || pref.getBoolPreferences("notification")) {
            FirebaseMessaging.getInstance().subscribeToTopic("subscribe");
        }

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)           // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);
        this.context = getApplicationContext();
    }
}
