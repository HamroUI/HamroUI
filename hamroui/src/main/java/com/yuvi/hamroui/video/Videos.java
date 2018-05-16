package com.yuvi.hamroui.video;

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
import com.yuvi.hamroui.AdapterModel;
import com.yuvi.hamroui.Pref;

import org.json.JSONObject;

public class Videos extends ViewModel {
    String id, image, published_date, description, name, youtubeId;

    public Videos() {

    }

    public Videos(String id, String name, String image, String published_date, String youtubeId, String description) {
        this.image = image;
        this.published_date = published_date;
        this.youtubeId = youtubeId;
        this.description = description;
        this.name = name;
        this.id = id;
    }

    public Videos(JSONObject mJSON) {
        this.image = mJSON.optString("img");
        this.youtubeId = mJSON.optString("youtubeID");
        this.description = mJSON.optString("description");
        this.id = mJSON.optString("id");
        this.published_date = mJSON.optString("published_date");
        this.name = mJSON.optString("name");
    }


    MutableLiveData<String> videoData =
            new MutableLiveData<>();

    public MutableLiveData<String> getVideos() {
        return videoData;
    }


    public void loadVideo(AdapterModel adapterModel, Context context) {
        final Pref pref = new Pref(context);
        String gData = pref.getPreferences("ndata");
        if (!TextUtils.isEmpty(gData)) {
            videoData.postValue(gData);
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
                        videoData.postValue(object.toString());
                        pref.setPreferences("nvideo", object.toString());
                    }
                } else {
                    videoData.postValue("");
                }

            }
        });
        requestManager.sync();
    }
}
