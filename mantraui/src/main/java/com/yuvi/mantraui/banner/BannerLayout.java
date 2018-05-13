package com.yuvi.mantraui.banner;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yuvi.mantraui.R;
import com.yuvi.mantraui.Utils;

public class BannerLayout extends LinearLayout {
    Banner banner;

    public BannerLayout(Context context) {
        super(context);
        init();
        this.setVisibility(GONE);
    }

    private void init() {
        LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);
        this.setOrientation(VERTICAL);

        WebView wv_banner = new WebView(getContext());
        wv_banner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utils.pxFromDp(getContext(), 75)));
//        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        wv_banner.setId(R.id.banner);
        this.addView(wv_banner);
    }

    public BannerLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public void addBanner(final Banner banner) {
        this.banner = banner;
        Utils.log(this.getClass(), "image = " + banner.getHtml());
        if (!TextUtils.isEmpty(banner.getHtml()) && banner.getActive() == 1) {
            this.setVisibility(VISIBLE);
//            Utils.loadImageWithGlide(getContext(), banner.getImage(), (ImageView) this.findViewById(R.id.bannerimage), null);
            Utils.log(getClass(), "Content loaded");
            WebView webView = this.findViewById(R.id.banner);
//            String html = "<img style=\"height:100%;\" src=\"http://cdn.hamroapi.com/res/app/1526180579032-10.jpg\" />";
            webView.loadDataWithBaseURL(null, banner.getHtml(), "text/html", "UTF-8", null);
        }

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    if (!TextUtils.isEmpty(banner.url)) {
                        try {
                            getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(banner.getUrl()))
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                            Log.w("BannerLayout", "Invalid url " + banner.getUrl());
                        }
                    }
                    return true;
                }
                return false;
            }
        });

    }
}
