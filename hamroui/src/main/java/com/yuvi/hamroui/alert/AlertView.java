package com.yuvi.hamroui.alert;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuvi.hamroui.R;
import com.yuvi.hamroui.Utils;

/**
 * Created by yubaraj on 12/22/17.
 */

public class AlertView extends LinearLayout {
    AlertData data;

    public AlertView(Context context) {
        super(context);
        init();
    }

    public AlertView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.setOrientation(HORIZONTAL);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);
        this.setMinimumHeight(Utils.pxFromDp(getContext(), 48));
        this.setGravity(Gravity.CENTER_VERTICAL);
        this.setPadding(Utils.pxFromDp(getContext(), 8), Utils.pxFromDp(getContext(), 8), Utils.pxFromDp(getContext(), 8), Utils.pxFromDp(getContext(), 8));

        TextView tv_title = new TextView(getContext());
        ImageView iv_close = new ImageView(getContext());

        layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;

        tv_title.setLayoutParams(layoutParams);
        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv_title.setTextColor(Color.parseColor("#FFFFFF"));

        layoutParams = new LayoutParams(Utils.pxFromDp(getContext(), 40), Utils.pxFromDp(getContext(), 40));
        iv_close.setLayoutParams(layoutParams);
        iv_close.setImageResource(R.drawable.ic_action_close);
        this.addView(tv_title);
        this.addView(iv_close);
        iv_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertView.this.setVisibility(GONE);
            }
        });
        this.setVisibility(GONE);
    }

    public void setData(final AlertData data) {
        this.data = data;
        if (data != null) {
            if (!TextUtils.isEmpty(data.getInfo()))
                ((TextView) this.getChildAt(0)).setText(data.getInfo());
            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (!TextUtils.isEmpty(data.getLink())) {
                            getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data.getLink()))
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

            this.setVisibility(VISIBLE);
        }
    }


    public void setTextColor(int color) {
        ((TextView) this.getChildAt(0)).setTextColor(color);
    }

    public void setCloseIconColor(int color) {
        ((ImageView) this.getChildAt(1)).setColorFilter(color);
    }


}
