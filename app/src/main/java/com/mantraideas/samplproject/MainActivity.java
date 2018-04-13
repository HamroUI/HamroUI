package com.mantraideas.samplproject;


import com.yuvi.mantraui.BaseMainActivity;
import com.yuvi.mantraui.Utils;



public class MainActivity extends BaseMainActivity {
    @Override
    public String getAppconfigFile() {
        return Utils.getInputStreamFromFile(this, "AppConfig.json");
    }
}
