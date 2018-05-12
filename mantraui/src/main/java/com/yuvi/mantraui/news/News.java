package com.yuvi.mantraui.news;

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
import com.yuvi.mantraui.AdapterModel;
import com.yuvi.mantraui.Pref;
import com.yuvi.mantraui.slider.SliderModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class News extends ViewModel{
    String id, image, published_date, source, description, name;

    public News(){

    }


    public News(String id, String name, String image, String published_date, String source, String description) {
        this.image = image;
        this.published_date = published_date;
        this.source = source;
        this.description = description;
        this.name = name;
        this.id = id;
    }

    public News(JSONObject mJSON){
        this.image = mJSON.optString("img");
        this.source = mJSON.optString("source");
        this.description = mJSON.optString("description");
        this.id = mJSON.optString("id");
        this.published_date = mJSON.optString("published_date");
        this.name = mJSON.optString("name");
    }


    MutableLiveData<String> newsData =
            new MutableLiveData<>();

    public MutableLiveData<String> getNews() {
        return newsData;
    }




    public void loadNews(AdapterModel adapterModel, Context context) {
        final Pref pref = new Pref(context);
        String gData = pref.getPreferences("ndata");
        if (!TextUtils.isEmpty(gData)) {
            newsData.postValue(gData);
        }

        DataRequestPair requestPair = DataRequestPair.create();
        for (String key :
                adapterModel.requestMap.keySet()) {
            requestPair.put(key, adapterModel.requestMap.get(key));

        }
        DataRequest request = DataRequest.getInstance();
        request.addUrl(adapterModel.url);
        request.addMethod(Method.POST);
        request.addHeaders(new String[]{"X-App-PKG"}, new String[]{adapterModel.packageName});
        request.addDataRequestPair(requestPair);

        DataRequestManager<String> requestManager = DataRequestManager.getInstance(context, String.class);
        requestManager.addRequestBody(request);

        requestManager.addOnDataRecieveListner(new OnDataRecievedListener() {
            @Override
            public void onDataRecieved(Response response, Object object) {
                if (response == Response.OK) {
                    if (object != null && !TextUtils.isEmpty(object.toString())) {
                        newsData.postValue(object.toString());
                        pref.setPreferences("ndata", object.toString());
                    }
                } else {
                    newsData.postValue("");
                }

            }
        });
        requestManager.sync();
    }
}
