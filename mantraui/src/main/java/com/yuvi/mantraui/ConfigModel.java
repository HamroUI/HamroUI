package com.yuvi.mantraui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.text.TextUtils;

import com.mantraideas.simplehttp.datamanager.DataRequestManager;
import com.mantraideas.simplehttp.datamanager.OnDataRecievedListener;
import com.mantraideas.simplehttp.datamanager.dmmodel.DataRequest;
import com.mantraideas.simplehttp.datamanager.dmmodel.DataRequestPair;
import com.mantraideas.simplehttp.datamanager.dmmodel.Method;
import com.mantraideas.simplehttp.datamanager.dmmodel.Response;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class ConfigModel extends ViewModel {
    MutableLiveData<ConfigModel> configData =
            new MutableLiveData<>();
    boolean success;
    String data;

    public ConfigModel() {

    }

    public MutableLiveData<ConfigModel> getConfigData(){
        return configData;
    }

    public void loadConfig(Context context, String url, TreeMap<String, String> headersMap, TreeMap<String, String> requestMap) {
        Pref pref = new Pref(context);
        String config = pref.getPreferences("config");
        if (!TextUtils.isEmpty(config)) {
            this.success = true;
            this.data = config;
            configData.postValue(this);
        }

        DataRequestPair requestPair = DataRequestPair.create();
        for (String key :
                requestMap.keySet()) {
            requestPair.put(key, requestMap.get(key));

        }
        DataRequest request = DataRequest.getInstance();
        request.addUrl(url);
        request.addMethod(Method.POST);
        request.addDataRequestPair(requestPair);
        String[] headersKey = new String[headersMap.size()];
        String[] headersValue = new String[headersMap.size()];
        int i = 0;
        for (String hKey : headersMap.keySet()) {
            headersKey[i] = hKey;
            headersValue[i] = headersMap.get(hKey);
        }
        request.addHeaders(headersKey, headersValue);

        DataRequestManager<String> requestManager = DataRequestManager.getInstance(context, String.class);
        requestManager.addRequestBody(request);
        requestManager.addOnDataRecieveListner(new OnDataRecievedListener() {
            @Override
            public void onDataRecieved(Response response, Object object) {
                if (response == Response.OK) {
                    if (object != null) {
                        ConfigModel.this.success = true;
                        ConfigModel.this.data = object.toString();
                        configData.postValue(ConfigModel.this);
                    }
                } else {
                    ConfigModel.this.success = false;
                    ConfigModel.this.data = response.getMessage();
                    configData.postValue(ConfigModel.this);
                }

            }
        });
        requestManager.sync();
    }

}
