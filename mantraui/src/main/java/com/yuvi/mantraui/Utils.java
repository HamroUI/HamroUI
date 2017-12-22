package com.yuvi.mantraui;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Created by yubaraj on 12/21/17.
 */

public class Utils {
    public static void log(Class<?> mClass, String message) {
        if (BuildConfig.DEBUG) {
            Log.d(mClass.getSimpleName(), message);
        }
    }
    public static int dpFromPx(Context context, float px) {
        return (int) (px /context.getResources().getDisplayMetrics().density);
    }

    public static int pxFromDp(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public static int pxFromSp(Context context, int sp) {
        return (int) (sp * context.getResources().getDisplayMetrics().scaledDensity);
    }

    public static void loadImageWithGlide(Context context, String url, ImageView iv_thumbnail, final ProgressBar progressBar) {
        Glide.with(context).
                load(url)
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_close_clear_cancel)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                        return false;
                    }
                })
                .crossFade(1000)
                .into(iv_thumbnail);
    }

}
