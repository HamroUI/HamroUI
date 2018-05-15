package com.yuvi.mantraui.gallery;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yuvi.mantraui.AdapterModel;
import com.yuvi.mantraui.BaseActivity;
import com.yuvi.mantraui.BaseSimpleRecyclerViewAdapter;
import com.yuvi.mantraui.ConfigModel;
import com.yuvi.mantraui.R;
import com.yuvi.mantraui.Utils;
import com.yuvi.mantraui.slider.SliderModel;

import java.util.List;

/**
 * Created by yubaraj on 12/28/17.
 */

public class GalleryActivity extends BaseActivity {
    @Override
    protected void addwithBaseOnCreate(Bundle savedInstanceState, FrameLayout frameLayout, AdapterModel model) {
        super.addwithBaseOnCreate(savedInstanceState, frameLayout, model);
//        frameLayout.setId(R.id.container);
        final RecyclerView recyclerView = new RecyclerView(this);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        recyclerView.setLayoutParams(layoutParams);
        recyclerView.setPadding(Utils.pxFromDp(this, 8), 0, Utils.pxFromDp(this, 8), 0);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        SliderModel sliderModel = ViewModelProviders.of(this).get(SliderModel.class);
        sliderModel.loadGallery(model, this);
        sliderModel.getGalleryData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String data) {
//                Log.d("BaseMainFragment", "data is " + configModel.data);
                if (!TextUtils.isEmpty(data)) {
                    // save the data if the preference is enabled in the appconfig
                    updateView(data, recyclerView);
                } else {
                    Toast.makeText(getApplicationContext(), "Gallery is empty", Toast.LENGTH_SHORT).show();
                }

            }
        });

        setTitle(getString(R.string.gallery));
        frameLayout.addView(recyclerView);
//        GalleryView galleryView = new GalleryView();

//        getSupportFragmentManager().beginTransaction().replace(R.id.container, galleryView).commit();


    }

    private void updateView(final String data, RecyclerView recyclerView) {
        try {
            List<SliderModel> sliderModelList = SliderModel.toList(data);
            Utils.log(this.getClass(), "UpdateView ::  galleryList size = " + sliderModelList.size());
            BaseSimpleRecyclerViewAdapter<SliderModel, GalleryViewHolder> adapter = new BaseSimpleRecyclerViewAdapter<SliderModel, GalleryViewHolder>(sliderModelList, R.layout.layout_type_gallery_1) {
                @Override
                public void viewBinded(final GalleryViewHolder galleryViewHolder, SliderModel sliderModel) {
                    if (!TextUtils.isEmpty(sliderModel.caption))
                        galleryViewHolder.tv_caption.setText(sliderModel.caption);
                    Utils.loadImageWithGlide(GalleryActivity.this, sliderModel.imageUrl, galleryViewHolder.iv, null);
                    galleryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int clickedPos = galleryViewHolder.getLayoutPosition();
                            startActivity(new Intent(GalleryActivity.this, GalleryDetailActivity.class)
                                    .putExtra("pos", clickedPos + "")
                                    .putExtra("data", data));
                        }
                    });
                }

                @Override
                public GalleryViewHolder attachViewHolder(View view) {
                    return new GalleryViewHolder(view);
                }
            };
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void errorOnCreated(String error) {
        super.errorOnCreated(error);
        toast(error);
    }
}
