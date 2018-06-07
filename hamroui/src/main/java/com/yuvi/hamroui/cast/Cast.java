package com.yuvi.hamroui.cast;

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
import com.yuvi.hamroui.gallery.Gallery;
import com.yuvi.hamroui.slider.SliderModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Cast extends ViewModel {
    String id, name, description, img, count;
    List<Gallery> gallery;
    String galleryImages;
    String subtitle;


    public Cast(){

    }

    public Cast(JSONObject jsonObject){
        this.id = jsonObject.optString("id");
        this.name = jsonObject.optString("name");
        this.img = jsonObject.optString("img");
        this.subtitle = jsonObject.optString("subtitle");
        this.count = jsonObject.optString("count");
        this.description = jsonObject.optString("description");
        galleryImages = jsonObject.optString("gallery");
        JSONArray jsonArray = jsonObject.optJSONArray("gallery");
        this.gallery = new Gallery().toList(jsonArray);
    }

    MutableLiveData<String> galleryData =
            new MutableLiveData<>();

    public MutableLiveData<String> getCastData() {
        return galleryData;
    }

    public static List<Cast> toList(String data) {
        List<Cast> castList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                castList.add(new Cast(jsonArray.optJSONObject(i)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return castList;
    }


    public void loadCastData(final AdapterModel adapterModel, Context context, boolean isFromApp) {
        final Pref pref = new Pref(context);
        String gData = pref.getPreferences("cdata");
        if (adapterModel.persist && !TextUtils.isEmpty(gData) && isFromApp) {
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
                        if(adapterModel.persist) {
                            pref.setPreferences("cdata", object.toString());
                        }
                    }
                } else {
                    galleryData.postValue("");
                }

            }
        });
        requestManager.sync();
    }

}
