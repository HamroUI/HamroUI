package com.mantraideas.samplproject;


import com.yuvi.hamroui.BaseMainActivity;
import com.yuvi.hamroui.Utils;



public class MainActivity extends BaseMainActivity {
    @Override
    public String getAppconfigFile() {
        return Utils.getInputStreamFromFile(this, "AppConfig.json");
    }
}
