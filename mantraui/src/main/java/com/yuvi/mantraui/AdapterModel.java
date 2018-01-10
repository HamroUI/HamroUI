package com.yuvi.mantraui;

import android.widget.Adapter;

import java.util.HashMap;

/**
 * Created by yubaraj on 12/25/17.
 */

public class AdapterModel {
    public HashMap<String, String> requestMap;
    public String url, packageName;
    public boolean hasPagination = false;
    public boolean persist = false;

    public AdapterModel(HashMap<String, String> requestMap, String url, String packageName, boolean hasPagination) {
        this.requestMap = requestMap;
        this.url = url;
        this.packageName = packageName;
        this.hasPagination = hasPagination;
    }

    public AdapterModel(HashMap<String, String> requestMap, String url, String packageName, boolean hasPagination, boolean persist) {
        this.requestMap = requestMap;
        this.url = url;
        this.packageName = packageName;
        this.hasPagination = hasPagination;
        this.persist = persist;
    }
}
