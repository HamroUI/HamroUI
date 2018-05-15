package com.yuvi.mantraui.gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yuvi.mantraui.R;
import com.yuvi.mantraui.Utils;
import com.yuvi.mantraui.slider.SliderIndicator;
import com.yuvi.mantraui.slider.SliderModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yubaraj on 12/21/17.
 */

public class GalleryDetailActivity extends AppCompatActivity {
    ViewPager pager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_detail);
        pager = findViewById(R.id.pager);
        String pos = "0";
        if(getIntent().hasExtra("pos")) {
            pos = getIntent().getStringExtra("pos");
        }
        String data = getIntent().getStringExtra("data");
        if(data.startsWith("{") && data.endsWith("}")){
            data = "[" + data + "]";
        }
        SliderIndicator indicator = findViewById(R.id.sliderIndicator);
        final List<SliderModel> sliderModels = new ArrayList<>();
//        String data = "[\n" +
//                "        {\n" +
//                "            \"name\": \"A footballing experience for all, at Qatar 2022\",\n" +
//                "            \"img\": \"http://cdn.hamroapi.com/resize?url=http://cdn.hamroapi.com/res/hamroapi/1513671119801-16.jpg&w=640&h=400\",\n" +
//                "            \"url\": \"wc22://web?url=http://www.fifa.com/worldcup/news/y=2017/m=12/news=a-footballing-experience-for-all-at-qatar-2022-2923861.html\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"name\": \"Qatar World Cup 2022 - Official Trailer\",\n" +
//                "            \"img\": \"http://cdn.hamroapi.com/resize?url=http://cdn.hamroapi.com/res/hamroapi/1513670974184-84.jpg&w=640&h=400\",\n" +
//                "            \"url\": \"wc22://youtube?id=oM0Je0MLVI0\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"name\": \"Qatar looking into using Iranian island for World Cup 2022\",\n" +
//                "            \"img\": \"http://cdn.hamroapi.com/resize?url=http://cdn.hamroapi.com/res/hamroapi/1513671044112-82.jpg&w=640&h=400\",\n" +
//                "            \"url\": \"wc22://web?url=http://english.alarabiya.net/en/features/2017/12/17/Qatar-looking-into-using-Iranian-island-for-World-Cup-2022.html\"\n" +
//                "        }\n" +
//                "    ]";
        try {
            JSONArray jsonArray = new JSONArray(data);
            Utils.log(GalleryDetailActivity.class, "dataLength = " + jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.optJSONObject(i);
                sliderModels.add(new SliderModel(json.optString("img"), json.optString("url"), json.optString("name")));
            }
            SliderAdapter adapter = new SliderAdapter(getSupportFragmentManager(), sliderModels);
            pager.setAdapter(adapter);
            pager.setCurrentItem(Integer.parseInt(pos));
            Utils.log(this.getClass(), "adapterSize = " + adapter.getCount());
            indicator.addWithSlider(pager);
        } catch (Exception e) {
            e.printStackTrace();
        }

        findViewById(R.id.fab_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


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
            GalleryFragment fragment = new GalleryFragment();
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
    public void onDestroy() {
        super.onDestroy();
    }
}
