package com.yuvi.mantraui;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.yuvi.mantraui.slider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
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
    JSONArray actionBarArray = new JSONArray();
    int ACTIONBAR = 100;
    int BOTTOMSHEET = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = getHomeLayout();
        Toolbar toolbar = getToolbar();
        linearLayout.addView(toolbar);
        View view = linearLayout;

        try {
            appConfigJSON = new JSONObject(Utils.readFileFromInputStream(getAppconfigFile()));
            HashMap<String, String> homeMap = new HashMap<>();
            Iterator<String> iterator = appConfigJSON.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                switch (key) {
                    case "sidebar":
                        view = setDrawableLayout(linearLayout);
                        break;
                    case "home":
                        manageHome(appConfigJSON.optJSONObject("home"));
                        break;
                    case "bottomsheet":
                        linearLayout.addView(getBottomSheetNavigation());
                        break;
                    case "modulesconfig":
                        break;

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        setContentView(view);
        setSupportActionBar(toolbar);

        if (view instanceof DrawerLayout) {
            ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, (DrawerLayout) view, toolbar, R.string.app_name, R.string.app_name) {

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

            ((DrawerLayout) view).addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
        }
//        getSupportFragmentManager().beginTransaction().replace(R.id.ll_container, getSliderView()).commit();
    }

    private void manageHome(JSONObject homeJSON) {
        if (homeJSON.has("actionbar")) {
            actionBarArray = homeJSON.optJSONArray("actionbar");
        }
    }

    private Toolbar getToolbar() {
        Toolbar toolbar = new Toolbar(this);
        toolbar.setLayoutParams(new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.pxFromDp(this, 50)));
        toolbar.setBackgroundColor(Color.parseColor("#3F51B5"));
        toolbar.setId(R.id.toolbar);
        toolbar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Light);
        toolbar.getContext().setTheme(R.style.ThemeOverlay_AppCompat_Dark_ActionBar);
        return toolbar;
    }

    private DrawerLayout setDrawableLayout(LinearLayout layout) {
        DrawerLayout drawerLayout = new DrawerLayout(this);
        DrawerLayout.LayoutParams layoutParams = new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        drawerLayout.setLayoutParams(layoutParams);
        drawerLayout.setId(R.id.drawable);
        layout.addView(getFrameLayout());
        drawerLayout.addView(layout);
        drawerLayout.addView(getNavigationView());
        return drawerLayout;
    }

    private LinearLayout getHomeLayout() {
        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setBackgroundColor(Color.parseColor("#F5F5F5"));
        linearLayout.setId(R.id.ll_container);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        return linearLayout;
    }

    private FrameLayout getFrameLayout() {
        FrameLayout frameLayout = new FrameLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        layoutParams.weight = 1;
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
        DrawerLayout.LayoutParams navLayout = new DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.WRAP_CONTENT, DrawerLayout.LayoutParams.MATCH_PARENT, Gravity.START);
        navigationView.setLayoutParams(navLayout);
        navigationView.setBackgroundColor(Color.parseColor("#F5F5F5"));
        navigationView.setItemTextColor(myColorStateList);
        navigationView.setId(R.id.navview);
        updateMenusItemOfNavigationView(navigationView);
        return navigationView;
    }


    private BottomNavigationView getBottomSheetNavigation() {
        BottomNavigationView bottomNavigationView = new BottomNavigationView(this);
        bottomNavigationView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.pxFromDp(this, 50)));
        bottomNavigationView.setId(R.id.bottomsheet);
        bottomNavigationView.setItemTextColor(myColorStateList);
        bottomNavigationView.setItemIconTintList(myColorStateList);
        bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.h_blue_500));
        Menu menu = bottomNavigationView.getMenu();
        menu.add(0, Menu.FIRST, Menu.NONE, "TEXT");
        menu.add(0, Menu.FIRST + 1, Menu.NONE, "Sample");
        MenuItem menuItem = menu.findItem(Menu.FIRST);
        return bottomNavigationView;
    }

    private Fragment getSliderView() {
        SliderView view = new SliderView();
        return view;
    }

    class LoadIcon extends AsyncTask<Void, Void, Bitmap> {
        MenuItem menuItem;
        String url;

        public LoadIcon(MenuItem menuItem, String url) {
            this.menuItem = menuItem;
            this.url = url;
            Utils.log(this.getClass(), "Loading :: " + url + " in menuItem");
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            try {
                Bitmap bitmap = Glide.with(getApplicationContext())
                        .load(url)
                        .asBitmap()
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            BitmapDrawable mBitmapDrawable = new BitmapDrawable(getResources(), bitmap);
            menuItem.setIcon(mBitmapDrawable);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d("BaseMain", "actionBarArray =" + actionBarArray.length());
        menu.clear();
        for (int i = 0; i < actionBarArray.length(); i++) {
            JSONObject menuJSON = actionBarArray.optJSONObject(i);
            menu.add(0, ACTIONBAR + i, 1000 + i, menuJSON.optString("title"));
            MenuItem menuItem = menu.findItem(ACTIONBAR + i);
            if (menuJSON.optBoolean("showalways")) {
                menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            }
            if (menuJSON.has("icon") && !TextUtils.isEmpty(menuJSON.optString("icon"))) {
                new LoadIcon(menuItem, menuJSON.optString("icon")).execute();
            }
        }
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


    //    The first dimension is an array of state sets, the second ist the state set itself. The colors array lists the colors for each matching state set, therefore the length of the colors array has to match the first dimension of the states array (or it will crash when the state is "used"). Here and example:
    ColorStateList myColorStateList = new ColorStateList(
            new int[][]{
                    new int[]{android.R.attr.state_checked}, //1
                    new int[]{-android.R.attr.state_checked}, //2
            },
            new int[]{
                    R.color.h_grey_500, //1
                    R.color.h_red_500, //2
            }
    );
}
