package com.yuvi.hamroui.simplelist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yuvi.hamroui.AdapterModel;
import com.yuvi.hamroui.BaseActivity;
import com.yuvi.hamroui.OnItemClickListener;
import com.yuvi.hamroui.Utils;
import com.yuvi.hamroui.news.NewsActivity;

import org.json.JSONObject;

/**
 * Created by yubaraj on 2/4/18.
 */

public class SimpleListActivity extends BaseActivity implements OnItemClickListener {
    @Override
    protected void addwithBaseOnCreate(Bundle savedInstanceState, FrameLayout frameLayout, final AdapterModel model) {
        super.addwithBaseOnCreate(savedInstanceState, frameLayout, model);
        setTitle(model.label);
        RecyclerView rv = new RecyclerView(this);
        rv.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final TextView tv_nodata = new TextView(this);
        LinearLayout.LayoutParams textviewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tv_nodata.setLayoutParams(textviewParams);
        tv_nodata.setTextColor(Color.parseColor("#212121"));
        tv_nodata.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv_nodata.setGravity(Gravity.CENTER);
        tv_nodata.setVisibility(View.GONE);

        frameLayout.addView(tv_nodata);
        frameLayout.addView(rv);

        SimpleListAdapter adapter = new SimpleListAdapter(this, model, this) {
            @Override
            protected void showLoadingProgress() {
                super.showLoadingProgress();
                Utils.log(NewsActivity.class, "show Progress is called");
                tv_nodata.setVisibility(View.VISIBLE);
                tv_nodata.setText("Loading " + model.label + " please wait");
            }

            @Override
            protected void hideLoadingProgress(String mesg) {
                super.hideLoadingProgress(mesg);
                if (TextUtils.isEmpty(mesg)) {
                    tv_nodata.setVisibility(View.GONE);
                } else {
                    if (getItemCount() == 0) {
                        tv_nodata.setText(mesg);
                    }
                }
            }

            @Override
            protected void onFailed(String message) {
                super.onFailed(message);
                Log.d("SimpleListActivity", "Failed, Message = " + message);
            }

            @Override
            protected void onLoadingMoreComplete() {
                super.onLoadingMoreComplete();
                Toast.makeText(getApplicationContext(), "No more " + model.label, Toast.LENGTH_SHORT).show();
            }
        };
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.setAdapter(adapter);
        adapter.setOnLoadMoreListener(rv);
    }

    @Override
    protected void errorOnCreated(String error) {
        super.errorOnCreated(error);
        toast(error);
    }

    @Override
    public void onItemClicked(JSONObject dataJSON) {
        startActivity(new Intent(this, SimpleWebViewActivity.class)
                .putExtra("title", dataJSON.optString("title"))
                .putExtra("desc", dataJSON.optString("description")));
    }
}
