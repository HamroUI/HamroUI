package com.yuvi.mantraui;

import android.support.v7.widget.RecyclerView;

import com.mantraideas.simplehttp.datamanager.OnDataRecievedListener;
import com.mantraideas.simplehttp.datamanager.dmmodel.Response;

/**
 * Created by yubaraj on 12/21/17.
 */

public abstract class BaseRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements OnDataRecievedListener {

    public BaseRecyclerViewAdapter(String url, boolean hasPagination) {

    }

    public void queryData(){

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onDataRecieved(Response response, Object object) {

    }


}
