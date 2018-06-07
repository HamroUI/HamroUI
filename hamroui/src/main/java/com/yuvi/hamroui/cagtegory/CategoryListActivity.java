package com.yuvi.hamroui.cagtegory;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mantraideas.simplehttp.datamanager.dmmodel.Response;
import com.yuvi.hamroui.AdapterModel;
import com.yuvi.hamroui.BaseActivity;
import com.yuvi.hamroui.BaseRecyclerViewAdapter;
import com.yuvi.hamroui.BaseSimpleRecyclerViewAdapter;
import com.yuvi.hamroui.R;
import com.yuvi.hamroui.Utils;

import org.json.JSONObject;

public class CategoryListActivity extends BaseActivity {
    @Override
    protected void addwithBaseOnCreate(Bundle savedInstanceState, FrameLayout frameLayout, AdapterModel model) {
        super.addwithBaseOnCreate(savedInstanceState, frameLayout, model);

        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        frameLayout.addView(recyclerView);
        BaseRecyclerViewAdapter<CategoryViewHolder> adapter = new BaseRecyclerViewAdapter<CategoryViewHolder>(this, model) {

            @Override
            public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = getLayoutInflater().inflate(R.layout.row_category, parent, false);
                return new CategoryViewHolder(view);
            }

            @Override
            public void onBindViewHolder(CategoryViewHolder holder, int position) {
                final JSONObject data = getData(position);
                Utils.log(getClass(), "data = " + data.toString());
                final String title = data.optString("name");
                holder.bindView(title, data.optString("icon"));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // manage the deeplink here
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data.optString("url")+"&m_title="+title+"&fromapp=true"))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }
                });
            }

            @Override
            protected void showLoadingProgress() {
                super.showLoadingProgress();
                showProgressDialog("Loading category please wait");
            }

            @Override
            protected void hideLoadingProgress(String mesg) {
                super.hideLoadingProgress(mesg);
                hideProgressDialog();
            }
        };
        adapter.queryData();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
