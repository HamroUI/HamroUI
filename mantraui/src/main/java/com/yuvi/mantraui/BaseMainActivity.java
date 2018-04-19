package com.yuvi.mantraui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.yuvi.mantraui.alert.AlertData;
import com.yuvi.mantraui.alert.AlertView;
import com.yuvi.mantraui.gridmenu.GridMenu;
import com.yuvi.mantraui.gridmenu.GridMenuView;
import com.yuvi.mantraui.gridmenu.OnGridMenuSelectedListener;
import com.yuvi.mantraui.home.BaseHomeAdapter;
import com.yuvi.mantraui.slider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by yubaraj on 12/31/17.
 */

public abstract class BaseMainActivity extends AppCompatActivity implements OnGridMenuSelectedListener/*, NavigationView.OnNavigationItemSelectedListener */ {
    JSONObject appConfigJSON;
    JSONArray actionBarArray = new JSONArray();
    int ACTIONBAR = 100;
    int BOTTOMSHEET = 1000;
    boolean isSideBarLeft = true;
    String primaryColor = "#3F51B5", primarrDarkColor = "#303F9F";
    String secondaryColor = "#FF4081";
    LinearLayout linearLayout;
    Pref pref;
    String packageName = "";
    HashMap<Integer, String> menuMap = new HashMap<>();
    TreeMap<String, String> configMap = new TreeMap<>();
    TreeMap<String, String> headerMap = new TreeMap<>();
    JSONObject homeRequestJSON = null;
    String homeUrl = "";
    AlertView alertView = null;
    HashMap<String, RecyclerView> moduelsMap = new HashMap<>();


    @Override
    public void onSelected(GridMenu menu) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(menu.link)));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = new Pref(this);
        linearLayout = getHomeLayout();
        Toolbar toolbar = getToolbar();
        linearLayout.addView(toolbar);
        linearLayout.addView(getFrameLayout());
        View view = linearLayout;
        boolean isAppconfigUpdated = false;
        try {
            appConfigJSON = new JSONObject(getAppconfigFile());
            if (appConfigJSON.has("version") && appConfigJSON.optInt("version") > pref.getIntPreferences("version")) {
                pref.clearAll();
                pref.setIntPreferences("version", appConfigJSON.optInt("version"));
                isAppconfigUpdated = true;
            }
            if (appConfigJSON.has("pkgname") && !TextUtils.isEmpty(appConfigJSON.optString("pkgname"))) {
                packageName = appConfigJSON.optString("pkgname");
                pref.setPreferences("pkgname", packageName);
            }
            if (appConfigJSON.has("baseurl") && !TextUtils.isEmpty(appConfigJSON.optString("baseurl"))) {
                pref.setPreferences("baseurl", appConfigJSON.optString("baseurl"));
            }
            if (appConfigJSON.has("primarycolor") && !TextUtils.isEmpty(appConfigJSON.optString("primarycolor"))) {
                primaryColor = appConfigJSON.optString("primarycolor");
                appConfigJSON.remove("primarycolor");
            }
            if (appConfigJSON.has("secondarycolor") && !TextUtils.isEmpty(appConfigJSON.optString("secondarycolor"))) {
                secondaryColor = appConfigJSON.optString("secondarycolor");
                appConfigJSON.remove("secondarycolor");
            }
            if (appConfigJSON.has("primarydark") && !TextUtils.isEmpty(appConfigJSON.optString("primarydark"))) {
                primarrDarkColor = appConfigJSON.optString("primarydark");
                appConfigJSON.remove(primarrDarkColor);
            }
            if (appConfigJSON.has("admob_id")) {
                pref.setPreferences(Pref.KEY_ADMOB_ID, appConfigJSON.optString("admob_id"));
            }

            if (appConfigJSON.has("banner_id")) {
                pref.setPreferences(Pref.KEY_BANNER_ID, appConfigJSON.optString("banner_id"));
            }
            if (appConfigJSON.has("fullscreen_id")) {
                pref.setPreferences(Pref.KEY_INTERESTIAL_ID, appConfigJSON.optString("fullscreen_id"));
            }
            if (appConfigJSON.has("youtubeAPIKey")) {
                pref.setPreferences(Pref.KEY_YOUTUBE_ID, appConfigJSON.optString("youtubeAPIKey"));
            }
            Iterator<String> iterator = appConfigJSON.keys();

            while (iterator.hasNext()) {
                String key = iterator.next();
                switch (key) {
                    case "sidebar":
                        view = setDrawableLayout(appConfigJSON.optJSONObject("sidebar"));
                        break;
                    case "home":
                        manageHome(appConfigJSON.optJSONObject("home"));
                        homeRequestJSON = appConfigJSON.optJSONObject("home").optJSONObject("request");
                        homeUrl = appConfigJSON.optJSONObject("home").optString("url");
                        if(TextUtils.isEmpty(homeUrl)){
                            homeUrl =  appConfigJSON.optString("baseurl");
                        }

                        break;
                    case "bottomsheet":
                        JSONArray bottomsheetArray = appConfigJSON.optJSONArray("bottomsheet");
                        linearLayout.addView(getBottomSheetNavigation(bottomsheetArray));
                        break;
                    case "modulesconfig":
                        Utils.log(BaseMainActivity.class, "isAppconfigUpdated = " + isAppconfigUpdated);
                        if (isAppconfigUpdated) {
                            JSONObject modulesconfigJSON = appConfigJSON.optJSONObject("modulesconfig");
                            Iterator<String> configKeys = modulesconfigJSON.keys();
                            while (configKeys.hasNext()) {
                                String configKey = configKeys.next();
                                pref.setPreferences(configKey, modulesconfigJSON.optString(configKey));
                            }
                        }
                        break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        setContentView(view);
        setSupportActionBar(toolbar);

        if (view instanceof DrawerLayout) {
            final DrawerLayout drawerLayout = (DrawerLayout) view;
            ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {

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

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (isSideBarLeft) {
                        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                            drawerLayout.closeDrawer(Gravity.LEFT);
                        } else {
                            drawerLayout.openDrawer(Gravity.LEFT);
                        }
                    } else {
                        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                            drawerLayout.closeDrawer(Gravity.RIGHT);
                        } else {
                            drawerLayout.openDrawer(Gravity.RIGHT);
                        }
                    }

                }
            });
        }

        ConfigModel configModel = ViewModelProviders.of(this).get(ConfigModel.class);
        if (homeRequestJSON != null) {
            Iterator<String> homeJSONIterator = homeRequestJSON.keys();
            while (homeJSONIterator.hasNext()) {
                String reqKey = homeJSONIterator.next();
                configMap.put(reqKey, homeRequestJSON.optString(reqKey));
            }
        }

        headerMap.put("X-App-pkg", packageName);
        configModel.loadConfig(this, homeUrl, headerMap, configMap);
        configModel.getConfigData().observe(this, new Observer<ConfigModel>() {
            @Override
            public void onChanged(@Nullable ConfigModel configModel) {
                Log.d("BaseMainFragment", "data is " + configModel.data);
                if(configModel.success){
                   updateView(configModel.data);
                }else {
                    Toast.makeText(getApplicationContext(), configModel.data, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void updateView(String config){
        try{
            JSONObject configJSON = new JSONObject(config);
            String alert = configJSON.optString("alert");
            String alert_link = configJSON.optString("alert_link");
            if(!TextUtils.isEmpty(alert) && alertView != null){
               alertView.setData(new AlertData(alert, alert_link));
            }
            for(String key : moduelsMap.keySet()){
                if(configJSON.has(key)){
                    JSONArray jsonArray = configJSON.optJSONArray(key);
                    ((BaseHomeAdapter)moduelsMap.get(key).getAdapter()).updateData(jsonArray, false);
                }
            }

        }catch (Exception e){e.printStackTrace();}

    }

    private void manageHome(JSONObject homeJSON) {
        // Manage the actionbar
        if (homeJSON.has("actionbar")) {
            actionBarArray = homeJSON.optJSONArray("actionbar");
        }
        NestedScrollView scrollView = new NestedScrollView(this);
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout mLayout = new LinearLayout(this);
        mLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mLayout.setOrientation(LinearLayout.VERTICAL);
        mLayout.setId(R.id.home);

        FrameLayout frameLayout = (FrameLayout) linearLayout.findViewById(R.id.container);
        Utils.log(BaseMainActivity.class, "tag = " + frameLayout.getTag());

        if (homeJSON.has("alert") && homeJSON.optBoolean("alert")) {
            alertView = new AlertView(this);
            alertView.setBackgroundColor(Color.parseColor(secondaryColor));
            mLayout.addView(alertView);
            alertView.setTextColor(Color.parseColor("#F5F5F5"));
            alertView.setData(new AlertData("SampleProject test", ""));
        }
        if (homeJSON.has("slider") && homeJSON.optBoolean("slider")) {
            LinearLayout sliderLayout = new LinearLayout(this);
            sliderLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.pxFromDp(this, 150)));
            sliderLayout.setId(R.id.sliderlayout);
            SliderView sliderView = getSliderView();
            getSupportFragmentManager().beginTransaction().add(R.id.sliderlayout, sliderView, "slider").commit();
            mLayout.addView(sliderLayout);
        }
        if (homeJSON.has("grid") && homeJSON.optJSONObject("grid") != null) {
            JSONObject gridJSON = homeJSON.optJSONObject("grid");
            GridMenuView gridMenuView = null;
            if (gridJSON.has("type")) {
                if (TextUtils.equals(gridJSON.optString("type"), "grid")) {
                    int horizontalCount = 2;
                    if (gridJSON.has("max_horizontal")) {
                        horizontalCount = gridJSON.optInt("max_horizontal");
                        gridMenuView = new GridMenuView(this, horizontalCount);
                    }
                } else if (TextUtils.equals(gridJSON.optString("type"), "horizontal")) {
                    gridMenuView = new GridMenuView(this);
                }
            }
            if (gridMenuView != null) {
                gridMenuView.setOnGridMenuSelectedListener(this);
                JSONArray menuArray = gridJSON.optJSONArray("menu");
                List<GridMenu> gridMenuList = GridMenu.toList(menuArray);
                gridMenuView.updateGridMenu(gridMenuList, false);
                mLayout.addView(gridMenuView);
            }
        }

        if (homeJSON.has("modules")) {
            JSONArray modulesArray = homeJSON.optJSONArray("modules");
            for (int i = 0; i < modulesArray.length(); i++) {
                final JSONObject typeObject = modulesArray.optJSONObject(i);
                Utils.log(BaseMainActivity.class, "modules type : " + typeObject.toString());

                CardView cardView = new CardView(this);
                cardView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                cardView.setUseCompatPadding(true);
                cardView.setCardElevation(Utils.pxFromDp(this, 2));
                cardView.setCardBackgroundColor(Color.parseColor("#F5F5F4"));

                LinearLayout modulesLayout = new LinearLayout(this);
                modulesLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                modulesLayout.setOrientation(LinearLayout.VERTICAL);
                modulesLayout.setPadding(Utils.pxFromDp(this, 8), Utils.pxFromDp(this, 8), Utils.pxFromDp(this, 8), Utils.pxFromDp(this, 8));

                LinearLayout titleLayout = new LinearLayout(this);
                if (!typeObject.has("show_header") || typeObject.optBoolean("show_header")) {
                    titleLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.pxFromDp(this, 40)));
                    titleLayout.setOrientation(LinearLayout.HORIZONTAL);
                    titleLayout.setGravity(Gravity.CENTER_VERTICAL);
                    TextView tv_title = new TextView(this);
                    LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                    titleParams.weight = 1;
                    tv_title.setLayoutParams(titleParams);
                    tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    tv_title.setTextColor(Color.parseColor("#212121"));
                    tv_title.setText(typeObject.optString("title"));
                    titleLayout.addView(tv_title, 0);
                    Utils.log(this.getClass(), "showall = " + typeObject.optString("showall"));

                    if (typeObject.optBoolean("showall")) {
                        Utils.log(this.getClass(), "Showall is enabled");
                        TextView tv_showall = new TextView(this);
                        LinearLayout.LayoutParams showallParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        tv_showall.setLayoutParams(showallParams);
                        tv_showall.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        tv_showall.setTextColor(Color.parseColor("#727272"));
                        tv_showall.setText("SHOW ALL");
                        titleLayout.addView(tv_showall, 1);
                    }
                }

                RecyclerView recyclerView = new RecyclerView(this);
                recyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                //TODO need to workout to parse the homemodules layout json
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

                if (typeObject.has("orientation") && TextUtils.equals(typeObject.optString("orientation"), "horizontal")) {
                    layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                }

                BaseHomeAdapter adapter = new BaseHomeAdapter(Utils.getSampleJSONArray()) {
                    @Override
                    public int getItemView() {
                        return Utils.layoutMap.get(typeObject.optString("layout"));
                    }

                    @Override
                    public void bindView(View view, JSONObject jsonObject) {
                        super.bindView(view, jsonObject);
                        TextView tv_title = view.findViewById(R.id.tv_title);
                        tv_title.setText(jsonObject.optString("title"));
                        ImageView thumbNail = view.findViewById(R.id.thubnail);
                        Utils.log(BaseMainActivity.this.getClass(),  jsonObject.optString("img"));
                        Utils.loadImageWithGlide(getApplicationContext(), jsonObject.optString("img"), thumbNail, null);
                        TextView tv_desc = view.findViewById(R.id.tv_desc);
                        TextView tv_date = view.findViewById(R.id.tv_date);
                        tv_date.setText(jsonObject.optString("published_date"));
                        tv_desc.setText(Html.fromHtml(jsonObject.optString("description")));
                    }
                };
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                moduelsMap.put(typeObject.optString("type"), recyclerView);
                modulesLayout.addView(titleLayout, 0);
                modulesLayout.addView(recyclerView, 1);
                cardView.addView(modulesLayout);
                mLayout.addView(cardView);
            }
        }
        scrollView.addView(mLayout);
        frameLayout.addView(scrollView);
    }

    private Toolbar getToolbar() {
        Toolbar toolbar = new Toolbar(this);
        toolbar.setLayoutParams(new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.pxFromDp(this, 50)));
        toolbar.setBackgroundColor(Color.parseColor(primaryColor));
        toolbar.setId(R.id.toolbar);
        toolbar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Light);
        toolbar.getContext().setTheme(R.style.ThemeOverlay_AppCompat_Dark_ActionBar);
        return toolbar;
    }

    private DrawerLayout setDrawableLayout(JSONObject sidebarJSON) {
        JSONArray menuArray = sidebarJSON.optJSONArray("menu");
        int width = sidebarJSON.optInt("width");
        String align = sidebarJSON.optString("align");
        String header = "";
        if (sidebarJSON.has("header")) {
            header = sidebarJSON.optString("header");
        }

        DrawerLayout drawerLayout = new DrawerLayout(this);
        DrawerLayout.LayoutParams layoutParams = new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        drawerLayout.setLayoutParams(layoutParams);
        drawerLayout.setId(R.id.drawable);
        drawerLayout.addView(linearLayout);
        drawerLayout.addView(getNavigationView(width, align, header, menuArray));
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


    private NavigationView getNavigationView(float width, String align, String header, JSONArray menuArray) {
        Utils.log(BaseMainActivity.class, "width = " + width + " align" + align + " menuArray = " + menuArray);
        NavigationView navigationView = new NavigationView(this);
        navigationView.setId(R.id.navview);
        isSideBarLeft = align.equals("left");
        DrawerLayout.LayoutParams navLayout = new DrawerLayout.LayoutParams(Utils.pxFromDp(this, width), DrawerLayout.LayoutParams.MATCH_PARENT, isSideBarLeft ? Gravity.START : Gravity.END);
        navigationView.setLayoutParams(navLayout);
        navigationView.setBackgroundColor(Color.parseColor("#F5F5F5"));
        navigationView.getContext().setTheme(R.style.NavigationViewStyle);
        navigationView.setItemTextColor(navColorStateList);
        if (!TextUtils.isEmpty(header)) {
            View view = getLayoutInflater().inflate(R.layout.layout_nav_header, null);
            navigationView.addHeaderView(view);
            Utils.loadImageWithGlide(this, header, (ImageView) view.findViewById(R.id.iv_nav_header), (ProgressBar) view.findViewById(R.id.prgbar));
        }
        getMenuFromJSONArray(navigationView.getMenu(), menuArray, Menu.FIRST);
//        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return handleMenu(item);
            }
        });
        return navigationView;
    }

    ColorStateList navColorStateList = new ColorStateList(
            new int[][]{
                    new int[]{android.R.attr.state_pressed},
                    new int[]{}
            },
            new int[]{
                    Color.parseColor(primaryColor),
                    Color.parseColor("#727272")
            }
    );

    private BottomNavigationView getBottomSheetNavigation(JSONArray menuArray) {
        Utils.log(BaseMainActivity.class, "bottomsheetArray = " + menuArray.length());
        BottomNavigationView bottomNavigationView = new BottomNavigationView(this);
        bottomNavigationView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.pxFromDp(this, 50)));
        bottomNavigationView.setId(R.id.bottomsheet);
        bottomNavigationView.setBackgroundColor(Color.parseColor("#F5F5F5"));
        getMenuFromJSONArray(bottomNavigationView.getMenu(), menuArray, BOTTOMSHEET);
        bottomNavigationView.setItemTextColor(myColorStateList);
        bottomNavigationView.setItemIconTintList(myColorStateList);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return handleMenu(item);
            }
        });

        Utils.removeShiftMode(bottomNavigationView);
        return bottomNavigationView;
    }

    private SliderView getSliderView() {
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
        menu = getMenuFromJSONArray(menu, actionBarArray, ACTIONBAR);
        return super.onPrepareOptionsMenu(menu);
    }

    private Menu getMenuFromJSONArray(Menu menu, JSONArray jsonArray, int MENUSTART) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject menuJSON = jsonArray.optJSONObject(i);
            if (menuJSON.has("submenu")) {
                JSONArray itemArray = menuJSON.optJSONArray("items");
                SubMenu subMenu = menu.addSubMenu(menuJSON.optString("submenu"));
                for (int j = 0; j < itemArray.length(); j++) {
                    JSONObject itemMenuJSON = itemArray.optJSONObject(j);
                    subMenu.add(0, ++MENUSTART, 100 + MENUSTART, itemMenuJSON.optString("title"));

                    if (menuJSON.has("link") && !TextUtils.isEmpty(menuJSON.optString("link"))) {
                        menuMap.put(MENUSTART, menuJSON.optString("link"));
                    }
                    MenuItem menuItem = subMenu.findItem(MENUSTART);
                    if (itemMenuJSON.has("showalways") && itemMenuJSON.optBoolean("showalways")) {
                        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                    }
                    if (itemMenuJSON.has("icon") && !TextUtils.isEmpty(itemMenuJSON.optString("icon"))) {
                        new LoadIcon(menuItem, itemMenuJSON.optString("icon")).execute();
                    }
                }
            } else {
                menu.add(Menu.NONE, ++MENUSTART, 1000 + i, menuJSON.optString("title"));
                MenuItem menuItem = menu.findItem(MENUSTART);

                if (menuJSON.has("link") && !TextUtils.isEmpty(menuJSON.optString("link"))) {
                    menuMap.put(MENUSTART, menuJSON.optString("link"));
                }
                if (menuJSON.has("showalways") && menuJSON.optBoolean("showalways")) {
                    menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                }
                if (menuJSON.has("icon") && !TextUtils.isEmpty(menuJSON.optString("icon"))) {
                    new LoadIcon(menuItem, menuJSON.optString("icon")).execute();
                }
            }


        }
        return menu;
    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        return handleMenu(item);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return handleMenu(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private boolean handleMenu(MenuItem item) {
        boolean isHandled = false;
        if (menuMap.containsKey(item.getItemId())) {
            String link = menuMap.get(item.getItemId());
            try {
                startActivity(new Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse(link)));
                isHandled = true;
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isHandled;
    }

    public abstract String getAppconfigFile();

    //    The first dimension is an array of state sets, the second ist the state set itself. The colors array lists the colors for each matching state set, therefore the length of the colors array has to match the first dimension of the states array (or it will crash when the state is "used"). Here and example:
    ColorStateList myColorStateList = new ColorStateList(
            new int[][]{
                    new int[]{android.R.attr.state_checked}, //1
                    new int[]{-android.R.attr.state_checked}, //2
            },
            new int[]{
                    Color.parseColor("#727272"), //1
                    R.color.h_green_500, //2
            }
    );


}
