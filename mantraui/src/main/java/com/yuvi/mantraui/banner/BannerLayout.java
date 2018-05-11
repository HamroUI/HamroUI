package com.yuvi.mantraui.banner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yuvi.mantraui.R;
import com.yuvi.mantraui.Utils;

public class BannerLayout extends LinearLayout {
    Banner banner;

    public BannerLayout(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.pxFromDp(getContext(), 150));
        this.setLayoutParams(layoutParams);
        this.setOrientation(VERTICAL);

        ImageView iv = new ImageView(getContext());
        iv.setLayoutParams(layoutParams);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setId(R.id.bannerimage);
        this.addView(iv);
    }

    public BannerLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void addBanner(final Banner banner) {
        this.banner = banner;
        Utils.log(this.getClass(), "image = " + banner.getImage());

        Utils.loadImageWithGlide(getContext(), banner.getImage(), (ImageView) this.findViewById(R.id.bannerimage), null);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(banner.url)) {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(banner.url))
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }
        });

    }


}
