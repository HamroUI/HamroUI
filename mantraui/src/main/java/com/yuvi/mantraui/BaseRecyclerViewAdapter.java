package com.yuvi.mantraui;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.mantraideas.simplehttp.datamanager.DataRequestManager;
import com.mantraideas.simplehttp.datamanager.OnDataRecievedListener;
import com.mantraideas.simplehttp.datamanager.dmmodel.DataRequest;
import com.mantraideas.simplehttp.datamanager.dmmodel.DataRequestPair;
import com.mantraideas.simplehttp.datamanager.dmmodel.Method;
import com.mantraideas.simplehttp.datamanager.dmmodel.Response;
import com.mantraideas.simplehttp.datamanager.util.DmUtilities;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by yubaraj on 12/21/17.
 */

public abstract class BaseRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements OnDataRecievedListener, OnLoadMoreListener {

    Context context;
    JSONArray jsonArray = new JSONArray();
    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;
    private boolean loading = false, hasMoreData = false;
    int start = 0;
    AdapterModel model;
    Handler handler = new Handler();
    Pref pref;

    protected int TYPE_PROGRESS = 1, TYPE_DATA = 2;

    public BaseRecyclerViewAdapter(Context context, AdapterModel model) {
        this.model = model;
        this.context = context;
        pref = new Pref(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (model.hasPagination) {
            if (jsonArray.optJSONObject(position) == null) {
                return TYPE_PROGRESS;
            } else {
                return TYPE_DATA;
            }
        }
        return super.getItemViewType(position);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            notifyDataSetChanged();
        }
    };

    @Override
    public void onLoadMore() {
        try {
            jsonArray.put(jsonArray.length(), null);
            handler.post(runnable);
            model.requestMap.put("start", start + "");
            Utils.log(BaseRecyclerViewAdapter.class, "start = " + start);
            queryData();
            loading = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void queryData() {
        if (model.persist && start == 0) {
            String savedData = pref.getPreferences(DmUtilities.getSha1Hex(model.requestMap.toString()));
            if (!TextUtils.isEmpty(savedData)) {
                Utils.log(BaseRecyclerViewAdapter.class, "savedData = " + savedData);
                try {
                    jsonArray = new JSONArray(savedData);
                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        DataRequestPair requestPair = DataRequestPair.create();
        for (String key : model.requestMap.keySet()) {
            requestPair.put(key, model.requestMap.get(key));
        }
        Utils.log(this.getClass(), "requestPair = " + requestPair.toString());

        DataRequest request = DataRequest.getInstance();
        request.addUrl(model.url);
        request.addMethod(Method.POST);
        request.addHeaders(new String[]{"X-App-PKG"}, new String[]{model.packageName});
        request.addDataRequestPair(requestPair);

        DataRequestManager<String> requestManager = DataRequestManager.getInstance(context, String.class);
        requestManager.addRequestBody(request);
        requestManager.addOnDataRecieveListner(this);
        requestManager.sync();
        loading = true;

        if (start == 0 && jsonArray.length() == 0) {
            showLoadingProgress();
        }
    }

    public void setOnLoadMoreListener(RecyclerView recyclerView) {
        if (model.hasPagination) {
            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                        .getLayoutManager();
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView,
                                           int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        totalItemCount = linearLayoutManager.getItemCount();
                        lastVisibleItem = linearLayoutManager
                                .findLastVisibleItemPosition();
                        Utils.log(BaseRecyclerViewAdapter.class, "call OnMore = " + (!loading && hasMoreData && totalItemCount <= (lastVisibleItem + visibleThreshold)));
                        if (!loading && hasMoreData && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                            // End has been reached
                            // Do something
                            BaseRecyclerViewAdapter.this.onLoadMore();
                        }
                    }
                });
            }
        } else {
            Log.w("BaseRecyclerViewAdapter", "set pagination to true in AdapterModel to enable the loadmore ");
        }
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
                if (jsonArray.length() > 0 && jsonArray.optJSONObject(jsonArray.length() - 1) == null) {
                    jsonArray = Utils.remove(jsonArray, jsonArray.length() - 1);
                    Utils.log(BaseRecyclerViewAdapter.class, "progressBar removed");
                }
                JSONArray mArray = new JSONArray(object.toString());
                if (start == 0 && mArray.length() > 0) {
                    if (model.persist) {
                        pref.setPreferences(DmUtilities.getSha1Hex(model.requestMap.toString()), mArray.toString());
                        Utils.log(BaseRecyclerViewAdapter.class, "dataSaved");
                        jsonArray = new JSONArray();
                    }
                    hideLoadingProgress("");
                }
                loading = false;
                hasMoreData = (mArray.length() > 9);
                start += mArray.length();
                jsonArray = Utils.concatJSONArray(jsonArray, mArray);
                notifyDataSetChanged();
                if (!hasMoreData) {
                    onLoadingMoreComplete();
                }
                Utils.log(BaseRecyclerViewAdapter.class, "hasMoreData = " + hasMoreData + " isLoading = " + loading + " start = " + start);
            } catch (Exception e) {
                onFailed(e.getMessage());
                e.printStackTrace();
                hideLoadingProgress(e.getMessage());
            }

        } else {
            Toast.makeText(context, response.getMessage(), Toast.LENGTH_SHORT).show();
            onFailed(response.getMessage());
            hideLoadingProgress(response.getMessage());
        }

    }

    protected void onFailed(String message) {

    }

    protected void onLoadingMoreComplete() {

    }

    protected void showLoadingProgress() {

    }

    protected void hideLoadingProgress(String mesg) {

    }


}
