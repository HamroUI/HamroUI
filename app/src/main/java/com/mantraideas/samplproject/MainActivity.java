package com.mantraideas.samplproject;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yuvi.mantraui.BaseMainActivity;
import com.yuvi.mantraui.Utils;
import com.yuvi.mantraui.alert.AlertData;
import com.yuvi.mantraui.alert.AlertView;
import com.yuvi.mantraui.audio.AudioActivity;
import com.yuvi.mantraui.gallery.GalleryActivity;
import com.yuvi.mantraui.news.NewsActivity;
import com.yuvi.mantraui.video.VideoListActivity;

import java.io.File;
import java.io.InputStream;


public class MainActivity extends BaseMainActivity {
    @Override
    public InputStream getAppconfigFile() {
        return Utils.getInputStreamFromFile(this, "AppConfig.json");
    }
}
