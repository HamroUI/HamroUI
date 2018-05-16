package com.yuvi.hamroui.banner;

import org.json.JSONObject;

public class Banner {

    String name, html, url;
    int active;

    private Banner() {

    }

    public static Banner instance() {
        return new Banner();
    }

    public Banner toObject(JSONObject jsonObject) {
        this.name = jsonObject.optString("name");
        this.html = jsonObject.optString("html");
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

    public String getHtml() {
        return html;
    }

    public void setHtml(String image) {
        this.html = image;
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
