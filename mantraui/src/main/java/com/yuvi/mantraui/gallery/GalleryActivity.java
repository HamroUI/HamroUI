package com.yuvi.mantraui.gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.yuvi.mantraui.R;

/**
 * Created by yubaraj on 12/28/17.
 */

public class GalleryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout ll = new FrameLayout(getApplicationContext());
        ll.setId(R.id.container);
        setContentView(ll);
        setTitle(getString(R.string.gallery));
        GalleryView galleryView = new GalleryView();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, galleryView).commit();
    }
}
