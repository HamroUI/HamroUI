package com.yuvi.mantraui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.transition.Slide;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.yuvi.mantraui.slider.SliderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Created by yubaraj on 12/31/17.
 */

public abstract class BaseMainActivity extends AppCompatActivity {
    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // AlertView
//        AlertData alertData = new AlertData("this is alert test", "");
//        AlertView alertView = findViewById(R.id.alertview);
//        alertView.setData(alertData);
//        alertView.setCloseIconColor(Color.parseColor("#FFFFFF"));
//
//        // testNewsActivity
//        findViewById(R.id.btn_news).setOnClickListener(v -> {
//            String pn = MainActivity.this.getPackageName();
//            String url = "http://aa.hamroapi.com/v1";
//            String nc = "{\"action\":\"news\",\"start\":\"0\", \"limit\":\"10\"}";
//            startActivity(new Intent(getApplicationContext(), NewsActivity.class)
//                    .putExtra("pn", pn)
//                    .putExtra("url", url)
//                    .putExtra("nc", nc));
//        });
//
//        // testVideoActivity
//        findViewById(R.id.btn_videos).setOnClickListener(v -> {
//            String pn = MainActivity.this.getPackageName();
//            String url = "http://vs.hamroapi.com/v4";
//            String nc = "{\"action\":\"get_videos\",\"start\":\"0\", \"limit\":\"10\"}";
//            startActivity(new Intent(getApplicationContext(), VideoListActivity.class)
//                    .putExtra("pn", pn)
//                    .putExtra("url", url)
//                    .putExtra("nc", nc));
//        });
//
//        // testAudio
//         findViewById(R.id.btn_audio).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AudioActivity.class)));
//
//        //testGallery
//        findViewById(R.id.btn_gallery).setOnClickListener(v -> {
//            startActivity(new Intent(MainActivity.this, GalleryActivity.class));
//        });
//
//    }

    JSONObject appConfigJSON;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            appConfigJSON = new JSONObject(Utils.readFileFromInputStream(getAppconfigFile()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setContentView(getDrawableLayout());
        Log.d("BaseMainActivity", "appconfig = " + appConfigJSON.toString());
        getSupportFragmentManager().beginTransaction().replace(R.id.container, getSliderView()).commit();
    }

    private Toolbar getToolbar() {
        Toolbar toolbar = new Toolbar(this);
        toolbar.setLayoutParams(new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.pxFromDp(this, 50)));
        toolbar.setBackgroundColor(Color.parseColor("#3F51B5"));
        toolbar.setId(R.id.toolbar);
        return toolbar;
    }

    private DrawerLayout getDrawableLayout() {
        DrawerLayout drawerLayout = new DrawerLayout(this);
        DrawerLayout.LayoutParams layoutParams = new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        drawerLayout.setLayoutParams(layoutParams);
        drawerLayout.setId(R.id.drawable);

        LinearLayout linearLayout = getHomeLayout();
        Toolbar toolbar = getToolbar();
        linearLayout.addView(toolbar);
        linearLayout.addView(getFrameLayout());
        drawerLayout.addView(linearLayout);
        drawerLayout.addView(getNavigationView());
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar , R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.d("BaseMainActivity", "drawer opened");
            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        return drawerLayout;
    }

    private LinearLayout getHomeLayout() {
        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setId(R.id.ll_container);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        return linearLayout;
    }

    private FrameLayout getFrameLayout() {
        FrameLayout frameLayout = new FrameLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        frameLayout.setLayoutParams(layoutParams);
        frameLayout.setId(R.id.container);
        return frameLayout;
    }

    private void updateMenusItemOfNavigationView(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < 10; i++) {
            menu.add(R.string.app_name);
        }
    }

    private NavigationView getNavigationView() {
        NavigationView navigationView = new NavigationView(this);
        navigationView.setMinimumWidth(Utils.pxFromDp(this, 300));
        DrawerLayout.LayoutParams navLayout = new DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.WRAP_CONTENT,   DrawerLayout.LayoutParams.MATCH_PARENT, Gravity.START);
        navigationView.setLayoutParams(navLayout);
        navigationView.setBackgroundColor(Color.parseColor("#FF0000"));
        navigationView.setId(R.id.navview);
        navigationView.inflateMenu(R.menu.home_menu);
        updateMenusItemOfNavigationView(navigationView);
        return navigationView;
    }


    private BottomNavigationView getBottomSheetNavigation() {
        BottomNavigationView bottomNavigationView = new BottomNavigationView(this);
        bottomNavigationView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        bottomNavigationView.setId(R.id.bottomsheet);
        return bottomNavigationView;
    }

    private Fragment getSliderView() {
        SliderView view = new SliderView();
        return view;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public abstract InputStream getAppconfigFile();
}
