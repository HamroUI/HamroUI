package com.yuvi.hamroui.simplelist;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.yuvi.hamroui.R;

import org.json.JSONObject;

/**
 * Created by yubaraj on 2/4/18.
 */

public class SimpleListViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_title, tv_desc;

    public SimpleListViewHolder(View itemView) {
        super(itemView);
        tv_title = itemView.findViewById(R.id.tv_title);
        tv_desc = itemView.findViewById(R.id.tv_desc);
    }

    public void bindView(JSONObject jsonObject) {
        if (jsonObject.has("title")) {
            tv_title.setText(jsonObject.optString("title"));
        }
        if (jsonObject.has("description")) {
            tv_desc.setText(Html.fromHtml(jsonObject.optString("description")));
        }
    }
}
