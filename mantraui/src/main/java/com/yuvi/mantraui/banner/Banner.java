package com.yuvi.mantraui.banner;

import org.json.JSONObject;

public class Banner {

    String name, image, url;
    int active;

    private Banner() {

    }

    public static Banner instance() {
        return new Banner();
    }

    public Banner toObject(JSONObject jsonObject) {
        this.name = jsonObject.optString("name");
        this.image = jsonObject.optString("img");
        this.url = jsonObject.optString("url");
        this.active = jsonObject.optInt("active");
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

}
