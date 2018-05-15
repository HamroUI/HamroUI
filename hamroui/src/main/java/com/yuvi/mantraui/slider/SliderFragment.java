package com.yuvi.mantraui.slider;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.yuvi.mantraui.R;
import com.yuvi.mantraui.Utils;

/**
 * Created by yubaraj on 12/21/17.
 */

public class SliderFragment extends Fragment {
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
        Utils.log(SliderFragment.class, "imageUrl = " + imageUrl);

        getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = "";
                Utils.log(SliderFragment.class, "link = " + link);
            }
        });

        ImageView iv = getView().findViewById(R.id.iv);
        ProgressBar prgbar = getView().findViewById(R.id.prgbar);
        Utils.loadImageWithGlide(getActivity(), imageUrl, iv, prgbar);
    }

}
