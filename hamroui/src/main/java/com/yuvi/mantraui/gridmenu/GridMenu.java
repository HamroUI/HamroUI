package com.yuvi.mantraui.gridmenu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yubaraj on 1/7/18.
 */

public class GridMenu {
    public String title, icon, link;

    public GridMenu(String title, String icon, String link) {
        this.title = title;
        this.icon = icon;
        this.link = link;
    }

    public static List<GridMenu> toList(JSONArray menuArray) {
        List<GridMenu> list = new ArrayList<>();
        for(int i = 0; i < menuArray.length(); i++){
            JSONObject menu = menuArray.optJSONObject(i);
            list.add(new GridMenu(menu.optString("title"), menu.optString("icon"), menu.optString("link")));
        }
        return list;
    }
}
