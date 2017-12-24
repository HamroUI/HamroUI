package com.yuvi.mantraui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.mantraideas.simplehttp.datamanager.DataRequestManager;
import com.mantraideas.simplehttp.datamanager.OnDataRecievedListener;
import com.mantraideas.simplehttp.datamanager.dmmodel.DataRequest;
import com.mantraideas.simplehttp.datamanager.dmmodel.DataRequestPair;
import com.mantraideas.simplehttp.datamanager.dmmodel.Method;
import com.mantraideas.simplehttp.datamanager.dmmodel.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yubaraj on 12/21/17.
 */

public abstract class BaseRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements OnDataRecievedListener {

    boolean hasPagination = false;
    HashMap<String, String> requestMap;
    Context context;
    JSONArray jsonArray = new JSONArray();

    public BaseRecyclerViewAdapter(Context context, HashMap<String, String> requestMap, boolean hasPagination) {
        this.hasPagination = hasPagination;
        this.requestMap = requestMap;
        this.context = context;

    }

    public void queryData(String baseUrl, String packageName) {
        DataRequestPair requestPair = DataRequestPair.create();
        for (String key : requestMap.keySet()) {
            requestPair.put(key, requestMap.get(key));
        }
        Utils.log(this.getClass(), "requestPair = " + requestPair.toString());

        DataRequest request = DataRequest.getInstance();
        request.addUrl(baseUrl);
        request.addMethod(Method.POST);
        request.addHeaders(new String[]{"X-App-PKG"}, new String[]{packageName});
        request.addDataRequestPair(requestPair);

        DataRequestManager<String> requestManager = DataRequestManager.getInstance(context, String.class);
        requestManager.addRequestBody(request);
        requestManager.addOnDataRecieveListner(this);
        requestManager.sync();
    }

    protected JSONObject getData(int index) {
        return jsonArray.optJSONObject(index);
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    @Override
    public void onDataRecieved(Response response, Object object) {
        if (response == Response.OK) {
            try {
                JSONArray mArray = new JSONArray(object.toString());
                jsonArray = Utils.concatJSONArray(jsonArray, mArray);
                notifyDataSetChanged();
            } catch (Exception e) {
                onFailed(e.getMessage());
                e.printStackTrace();
            }

        } else {
            Toast.makeText(context, response.getMessage(), Toast.LENGTH_SHORT).show();
            onFailed(response.getMessage());
        }

    }

    protected void onFailed(String message) {

    }


}
