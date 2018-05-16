package com.yuvi.hamroui.video;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.yuvi.hamroui.R;
import com.yuvi.hamroui.Utils;

/**
 * Created by yubaraj on 12/28/17.
 */

public class VideoErrorDialogueFragment extends DialogFragment {

    OnVideoErrorActionListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.listener = (OnVideoErrorActionListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.listener = (OnVideoErrorActionListener) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.video_error_dialogue, null);
        ImageView iv = view.findViewById(R.id.iv_thumbnail);
        CardView cv_back = view.findViewById(R.id.cv_back);
        CardView cv_youtube = view.findViewById(R.id.cv_youtube);
        ProgressBar prgbar = view.findViewById(R.id.prgbar);

        final String videoId = getArguments().getString("yid");
        String imageUrl = Utils.getUrlFromYoutubeKey(videoId);
        Utils.loadImageWithGlide(getActivity(), imageUrl, iv, prgbar);

        cv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGoback();
            }
        });
        cv_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onOpenWithYoutube(videoId);
            }
        });
        dialog.setView(view);
        return dialog;

    }
}
