package com.yuvi.hamroui.cagtegory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Category {

    String id, name, img, url;

    public Category() {

    }

    public Category(JSONObject jsonObject) {
        this.id = jsonObject.optString("id");
        this.name = jsonObject.optString("name");
        this.img = jsonObject.optString("img");
        this.url = jsonObject.optString("url");
    }

    public static List<Category> getCategoryList(String data) {
        List<Category> categoryList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                categoryList.add(new Category(jsonArray.optJSONObject(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryList;
    }
}
