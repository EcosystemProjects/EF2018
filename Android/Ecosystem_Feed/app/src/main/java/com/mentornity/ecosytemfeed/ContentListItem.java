package com.mentornity.ecosytemfeed;

import android.graphics.Bitmap;

import org.json.JSONObject;


public class ContentListItem {
    private Bitmap content_img;
    private String regionAndEcosystem,category,content;
    private Boolean isDeleteVisible;//It is true for only on Settings->MY posts screen.
    private String imgUrl;
    private JSONObject jsonObject;
    //it is used in AllFeed.java,Feed.java,Categories.java and Settings.java
    public ContentListItem(String regionAndEcosystem, String category, String content, Boolean isDeleteVisible, String imgUrl, JSONObject jsonObject) {
        this.regionAndEcosystem = regionAndEcosystem;
        this.category = category;
        this.content = content;
        this.isDeleteVisible = isDeleteVisible;
        this.imgUrl = imgUrl;
        this.jsonObject=jsonObject;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Boolean getDeleteVisible() {
        return isDeleteVisible;
    }

    public Bitmap getContent_img() {
        return content_img;
    }


    public void setContent_img(Bitmap content_img) {
        this.content_img = content_img;
    }

    public String getRegionAndEcosystem() {
        return regionAndEcosystem;
    }

    public String getCategory() {
        return category;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }
}
