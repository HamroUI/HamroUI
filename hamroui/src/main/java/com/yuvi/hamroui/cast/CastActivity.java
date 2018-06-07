package com.yuvi.hamroui.cast;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.yuvi.hamroui.AdapterModel;
import com.yuvi.hamroui.BaseActivity;
import com.yuvi.hamroui.BaseSimpleRecyclerViewAdapter;
import com.yuvi.hamroui.R;

import java.util.ArrayList;
import java.util.List;

public class CastActivity extends BaseActivity {
    BaseSimpleRecyclerViewAdapter<Cast, CastViewHolder> castAdapter;
    List<Cast> castList;


    @Override
    protected void addwithBaseOnCreate(Bundle savedInstanceState, FrameLayout frameLayout, AdapterModel model) {
        super.addwithBaseOnCreate(savedInstanceState, frameLayout, model);
        RecyclerView recyclerView = new RecyclerView(this);
        castList = new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        frameLayout.addView(recyclerView);
        getSupportActionBar().setTitle("title");


        castAdapter = new BaseSimpleRecyclerViewAdapter<Cast, CastViewHolder>(castList, R.layout.row_cast) {
            @Override
            public void viewBinded(CastViewHolder castViewHolder, final Cast cast) {
                castViewHolder.bindDataWithView(cast);
                castViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), CastDetailActivity.class)
                        .putExtra("desc", cast.description)
                        .putExtra("images", cast.galleryImages)
                        .putExtra("thumbnail", cast.img)
                        .putExtra("title", cast.name)
                        .putExtra("fromApp", true));
                    }
                });
            }

            @Override
            public CastViewHolder attachViewHolder(View view) {
                return new CastViewHolder(view);
            }
        };

        recyclerView.setAdapter(castAdapter);

        Cast cast = ViewModelProviders.of(this).get(Cast.class);
        cast.loadCastData(model, this, true);
        cast.getCastData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                hideProgressDialog();
                if (!TextUtils.isEmpty(s)) {
                    castList.addAll(Cast.toList(s));
                    castAdapter.notifyDataSetChanged();
                }
            }
        });
        showProgressDialog("Loading cast data, Please wait");
    }
}
