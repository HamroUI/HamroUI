package com.yuvi.hamroui.gallery;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Gallery {
    String id,name,img;

    public Gallery(){

    }
    private Gallery(JSONObject jsonObject){
        this.id = jsonObject.optString("id");
        this.name = jsonObject.optString("name");
        this.img = jsonObject.optString("img");
    }

    public List<Gallery> toList(String galleryData){
        try{
            JSONArray galleryArray = new JSONArray(galleryData);
            return toList(galleryArray);
        }catch (Exception e){e.printStackTrace();}
        return new ArrayList<Gallery>();
    }

    public List<Gallery> toList(JSONArray galleryData){
        List<Gallery> galleryList = new ArrayList<>();
        try{
            for (int i = 0; i < galleryData.length(); i++) {
                galleryList.add(new Gallery(galleryData.optJSONObject(i)));
            }
        }catch (Exception e){e.printStackTrace();}
        return galleryList;
    }
}
