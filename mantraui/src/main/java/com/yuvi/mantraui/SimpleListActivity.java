package com.yuvi.mantraui;

import android.os.Bundle;
import android.widget.FrameLayout;

/**
 * Created by yubaraj on 2/4/18.
 */

public class SimpleListActivity extends BaseActivity {
    @Override
    protected void addwithBaseOnCreate(Bundle savedInstanceState, FrameLayout rl, AdapterModel model) {
        super.addwithBaseOnCreate(savedInstanceState, rl, model);


    }

    @Override
    protected void errorOnCreated(String error) {
        super.errorOnCreated(error);
    }
}
