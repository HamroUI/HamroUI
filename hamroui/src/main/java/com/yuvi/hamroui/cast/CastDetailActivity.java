package com.yuvi.hamroui.cast;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yuvi.hamroui.AdapterModel;
import com.yuvi.hamroui.BaseActivity;
import com.yuvi.hamroui.BaseSimpleRecyclerViewAdapter;
import com.yuvi.hamroui.R;
import com.yuvi.hamroui.Utils;
import com.yuvi.hamroui.gallery.Gallery;
import com.yuvi.hamroui.gallery.GalleryDetailActivity;
import com.yuvi.hamroui.gallery.GalleryViewHolder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class CastDetailActivity extends BaseActivity {
    WebView wv;
    ImageView iv;
    ProgressBar prgbar;
    RecyclerView recyclerView;

    @Override
    protected void addwithBaseOnCreate(Bundle savedInstanceState, FrameLayout frameLayout, AdapterModel model) {
        super.addwithBaseOnCreate(savedInstanceState, frameLayout, model);
        View view = getLayoutInflater().inflate(R.layout.activity_cast_detail, null);
        frameLayout.addView(view);
        wv = view.findViewById(R.id.wv_cast_desc);
        iv = view.findViewById(R.id.iv_cast);
        prgbar = view.findViewById(R.id.prgbar);
        recyclerView = view.findViewById(R.id.rv_more_images);


        if (fromApp) {
            String desc = getIntent().getStringExtra("desc");
            String images = getIntent().getStringExtra("images");
            String thumbnail = getIntent().getStringExtra("thumbnail");
            String title = getIntent().getStringExtra("title");
            updateUI(images, desc, thumbnail, title);
        } else {
            Uri uri = getIntent().getData();
            for(String key : uri.getQueryParameterNames()){
                model.requestMap.put(key, uri.getQueryParameter(key));
            }
//            if (!TextUtils.isEmpty(uri.getQueryParameter("id"))) {
//                String id = uri.getQueryParameter("id");
//                model.requestMap.put("id", id);
                Cast cast = ViewModelProviders.of(this).get(Cast.class);
                cast.loadCastData(model, this, fromApp);
                cast.getCastData().observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        try {
                            JSONObject mJSON = new JSONObject(s);
                            Cast mCast = new Cast(mJSON);
                            updateUI(mCast.galleryImages, mCast.description, mCast.img, mCast.name);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
//            }
        }
    }

    private void updateUI(final String images, String desc, String thumbnail, String title) {
        getSupportActionBar().setTitle(title);
        wv.loadDataWithBaseURL("", desc, "text/html", "UTF-8", null);
        Utils.loadImageWithGlide(getApplicationContext(), thumbnail, iv, prgbar);

        // add the images in the adapter and notify the recycler view in the horizontal

        try {
            List<Gallery> galleryList = new Gallery().toList(images);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            BaseSimpleRecyclerViewAdapter<Gallery, GalleryViewHolder> adapter = new BaseSimpleRecyclerViewAdapter<Gallery, GalleryViewHolder>(galleryList, R.layout.layout_type_gallery_1) {
                @Override
                public void viewBinded(final GalleryViewHolder galleryViewHolder, final Gallery gallery) {
                    galleryViewHolder.bindView(gallery);
                    galleryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(CastDetailActivity.this, GalleryDetailActivity.class)
                            .putExtra("pos", galleryViewHolder.getAdapterPosition())
                            .putExtra("fromApp", true)
                            .putExtra("data", images));
                        }
                    });
                }

                @Override
                public GalleryViewHolder attachViewHolder(View view) {
                    return new GalleryViewHolder(view);
                }
            };
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
