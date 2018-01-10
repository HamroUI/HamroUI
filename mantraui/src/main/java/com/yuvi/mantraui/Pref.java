package com.yuvi.mantraui;

/**
 * Created by Ermike on 2/17/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Pref {




    Context ctx;
    SharedPreferences preferences;

    public Pref(Context ctx) {
        this.ctx = ctx;
        preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public boolean containsKey(String key) {
        return preferences.contains(key);
    }

    public String getPreferences(String key) {
        return preferences.getString(key, "");
    }

    public boolean setPreferences(String key, String value) {
        return preferences.edit().putString(key, value).commit();
    }

    public int getIntPreferences(String key) {
        return preferences.getInt(key, 0);
    }

    public boolean setIntPreferences(String key, int value) {
        return preferences.edit().putInt(key, value).commit();
    }

    public long getLongPreferences(String key) {
        return preferences.getLong(key, 0l);
    }

    public boolean setLongPreferences(String key, long value) {
        return preferences.edit().putLong(key, value).commit();
    }

    public boolean getBoolPreferences(String key) {
        return preferences.getBoolean(key, false);
    }

    public boolean getBoolPreferences(String key, boolean fallback) {
        return preferences.getBoolean(key, fallback);
    }

    public boolean setBoolPreferences(String key, boolean value) {
        return preferences.edit().putBoolean(key, value).commit();
    }

    public boolean setFloatPreferences(String key, float value) {
        return preferences.edit().putFloat(key, value).commit();
    }

    public void clearAll() {
        preferences.edit().clear().apply();
    }

    public float getFloatPreference(String key) {
        return preferences.getFloat(key, 0.0f);
    }

    public boolean clearPreferences() {
        return preferences.edit().clear().commit();
    }

}

