package com.mentornity.ecosytemfeed;

import android.graphics.Bitmap;

import org.json.JSONObject;


public class ContentListItem {
    private Bitmap content_img;
    private String regionAndEcosystem,category,description,title,shareUrl,date;
    private Boolean isDeleteVisible;//It is true for only on Settings->MY posts screen.
    private String imgUrl,seourl;
    //it is used in AllFeed.java,Feed.java,Categories.java and Settings.java
    public ContentListItem(String title, String regionAndEcosystem, String category, String description, Boolean isDeleteVisible, String imgUrl, String seourl, String date, String shareUrl) {
        this.title = title;
        this.regionAndEcosystem = regionAndEcosystem;
        this.category = category;
        this.description = description;
        this.isDeleteVisible = isDeleteVisible;
        this.imgUrl = imgUrl;
        this.seourl = seourl;
        this.date = date;
        this.shareUrl = shareUrl;
    }

    public String getRegionAndEcosystem() {
        return regionAndEcosystem;
    }

    public void setRegionAndEcosystem(String regionAndEcosystem) {
        this.regionAndEcosystem = regionAndEcosystem;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDeleteVisible(Boolean deleteVisible) {
        isDeleteVisible = deleteVisible;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSeourl() {
        return seourl;
    }

    public void setSeourl(String seourl) {
        this.seourl = seourl;
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

    public String getCategory() {
        return category;
    }

}
