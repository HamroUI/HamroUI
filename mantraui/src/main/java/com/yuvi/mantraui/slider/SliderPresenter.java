package com.yuvi.mantraui.slider;

import android.support.v4.app.FragmentManager;

import java.util.List;

public interface SliderPresenter {
    void onAttached(List<SliderModel> list, FragmentManager fm);
    void onDetatched();
}