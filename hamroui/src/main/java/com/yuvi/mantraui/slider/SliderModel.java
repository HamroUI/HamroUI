package com.yuvi.mantraui.slider;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.mantraideas.simplehttp.datamanager.DataRequestManager;
import com.mantraideas.simplehttp.datamanager.OnDataRecievedListener;
import com.mantraideas.simplehttp.datamanager.dmmodel.DataRequest;
import com.mantraideas.simplehttp.datamanager.dmmodel.DataRequestPair;
import com.mantraideas.simplehttp.datamanager.dmmodel.Method;
import com.mantraideas.simplehttp.datamanager.dmmodel.Response;
import com.yuvi.mantraui.AdapterModel;
import com.yuvi.mantraui.ConfigModel;
import com.yuvi.mantraui.Pref;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by yubaraj on 12/21/17.
 */

public class SliderModel extends ViewModel {
    public String imageUrl, link, caption;

    public SliderModel(){

    }
    public SliderModel(String imageUrl, String link, String caption) {
        this.imageUrl = imageUrl;
        this.link = link;
        this.caption = caption;
    }

    public SliderModel(JSONObject jsonObject) {
        this.imageUrl = jsonObject.optString("img");
        this.caption = jsonObject.optString("name");
        this.link = jsonObject.optString("url");
    }

    MutableLiveData<String> galleryData =
            new MutableLiveData<>();

    public MutableLiveData<String> getGalleryData() {
        return galleryData;
    }

    public static List<SliderModel> toList(String data) {
        List<SliderModel> sliderModelList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                sliderModelList.add(new SliderModel(jsonArray.optJSONObject(i)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sliderModelList;
    }


    public void loadGallery(AdapterModel adapterModel, Context context) {
        final Pref pref = new Pref(context);
        String gData = pref.getPreferences("gdata");
        if (!TextUtils.isEmpty(gData)) {
            galleryData.postValue(gData);
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
                        galleryData.postValue(object.toString());
                        pref.setPreferences("gdata", object.toString());
                    }
                } else {
                    galleryData.postValue("");
                }

            }
        });
        requestManager.sync();
    }
}
