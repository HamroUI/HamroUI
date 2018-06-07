package com.yuvi.hamroui.slider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.yuvi.hamroui.R;
import com.yuvi.hamroui.Utils;

/**
 * Created by yubaraj on 12/21/17.
 */

public class SliderFragment extends Fragment {
    String link = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slider, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String imageUrl = "";

        if (getArguments() == null) {
            return;
        }
        if (getArguments().containsKey("url")) {
            imageUrl = getArguments().getString("url");
        }

        if (getArguments().containsKey("link")) {
            link = getArguments().getString("link");
        }
        Utils.log(SliderFragment.class, "imageUrl = " + imageUrl);

        getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(link)) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link))
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        ImageView iv = getView().findViewById(R.id.iv);
        ProgressBar prgbar = getView().findViewById(R.id.prgbar);
        Utils.loadImageWithGlide(getActivity(), imageUrl, iv, prgbar);
    }

}
