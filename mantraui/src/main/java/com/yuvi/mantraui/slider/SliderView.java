package com.yuvi.mantraui.slider;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yuvi.mantraui.R;
import com.yuvi.mantraui.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by yubaraj on 12/21/17.
 */

public class SliderView extends Fragment {
    ViewPager pager;
    Handler handler = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_slider, container, false);
        return view;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pager = getView().findViewById(R.id.pager);
        SliderIndicator indicator = getView().findViewById(R.id.sliderIndicator);
        final List<SliderModel> sliderModels = new ArrayList<>();
        String data = "[\n" +
                "        {\n" +
                "            \"name\": \"A footballing experience for all, at Qatar 2022\",\n" +
                "            \"image\": \"http://cdn.hamroapi.com/resize?url=http://cdn.hamroapi.com/res/hamroapi/1513671119801-16.jpg&w=640&h=400\",\n" +
                "            \"url\": \"wc22://web?url=http://www.fifa.com/worldcup/news/y=2017/m=12/news=a-footballing-experience-for-all-at-qatar-2022-2923861.html\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Qatar World Cup 2022 - Official Trailer\",\n" +
                "            \"image\": \"http://cdn.hamroapi.com/resize?url=http://cdn.hamroapi.com/res/hamroapi/1513670974184-84.jpg&w=640&h=400\",\n" +
                "            \"url\": \"wc22://youtube?id=oM0Je0MLVI0\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Qatar looking into using Iranian island for World Cup 2022\",\n" +
                "            \"image\": \"http://cdn.hamroapi.com/resize?url=http://cdn.hamroapi.com/res/hamroapi/1513671044112-82.jpg&w=640&h=400\",\n" +
                "            \"url\": \"wc22://web?url=http://english.alarabiya.net/en/features/2017/12/17/Qatar-looking-into-using-Iranian-island-for-World-Cup-2022.html\"\n" +
                "        }\n" +
                "    ]";
        try {
            JSONArray jsonArray = new JSONArray(data);
            Utils.log(SliderView.class, "dataLength = " + jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.optJSONObject(i);
                sliderModels.add(new SliderModel(json.optString("image"), json.optString("url"), json.optString("name")));
            }
            SliderAdapter adapter = new SliderAdapter(getFragmentManager(), sliderModels);
            pager.setAdapter(adapter);
            Utils.log(this.getClass(), "adapterSize = " + adapter.getCount());
            indicator.addWithSlider(pager);
            handler = new Handler();
            handler.postDelayed(runnable, 4000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Utils.log(SliderView.class, "isDoing = " + (pager.getCurrentItem() + 1 < pager.getAdapter().getCount()));
            if (pager.getCurrentItem() + 1 < pager.getAdapter().getCount()) {
                pager.setCurrentItem(pager.getCurrentItem() + 1, true);
            } else {
                pager.setCurrentItem(0, true);
            }
            handler.postDelayed(runnable, 4000);
        }
    };

    class SliderAdapter extends FragmentStatePagerAdapter {
        List<SliderModel> dataList;

        public SliderAdapter(FragmentManager manager, List<SliderModel> dataList) {
            super(manager);
            this.dataList = dataList;
            Utils.log(this.getClass(), "dataList = " + dataList.size());
        }

        @Override
        public Fragment getItem(int position) {
            SliderModel sliderModel = dataList.get(position);
            Bundle bundle = new Bundle();
            bundle.putString("link", sliderModel.link);
            bundle.putString("url", sliderModel.imageUrl);
            SliderFragment fragment = new SliderFragment();
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return dataList.get(position).caption;
        }
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("SliderView", "sliderView is paused");
        if (handler != null) {
            handler.removeCallbacks(runnable);
            handler = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("SliderView", "slider is resumed");
        if (handler == null) {
            handler = new Handler();
            handler.post(runnable);
        }
    }
}
