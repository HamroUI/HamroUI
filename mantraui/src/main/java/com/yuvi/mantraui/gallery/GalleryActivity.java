package com.yuvi.mantraui.gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.yuvi.mantraui.AdapterModel;
import com.yuvi.mantraui.BaseActivity;
import com.yuvi.mantraui.R;

/**
 * Created by yubaraj on 12/28/17.
 */

public class GalleryActivity extends BaseActivity {
    @Override
    protected void addwithBaseOnCreate(Bundle savedInstanceState, FrameLayout frameLayout, AdapterModel model) {
        super.addwithBaseOnCreate(savedInstanceState, frameLayout, model);
        frameLayout.setId(R.id.container);
        setTitle(getString(R.string.gallery));
        GalleryView galleryView = new GalleryView();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, galleryView).commit();
    }

    @Override
    protected void errorOnCreated(String error) {
        super.errorOnCreated(error);
        toast(error);
    }
}
