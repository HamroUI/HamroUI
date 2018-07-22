package com.yuvi.hamroui.audio;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yuvi.hamroui.R;

import org.json.JSONObject;

public class AudioListViewHolder extends RecyclerView.ViewHolder {
    TextView tv_name;
    ImageView iv_play;

    public AudioListViewHolder(View itemView) {
        super(itemView);
        tv_name = itemView.findViewById(R.id.tv_name);
        iv_play = itemView.findViewById(R.id.iv_play);
    }

    public void bindView(JSONObject mJSON) {
        tv_name.setText(mJSON.optString("name"));
    }
}
