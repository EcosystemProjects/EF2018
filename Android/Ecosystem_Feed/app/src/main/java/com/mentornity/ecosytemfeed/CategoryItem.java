package com.mentornity.ecosytemfeed;

public class CategoryItem {
    //Category Item is used Categories.java
    private int id, postNumber, followerNumber;
    private String title ,seourl;
    private Boolean isFollowed;

    public void toggleIsFollowed(){
        if(isFollowed){
            isFollowed = false;
        }else{
            isFollowed = true;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getPostNumber() {
        return postNumber;
    }

    public int getFollowerNumber() {
        return followerNumber;
    }

    public Boolean getIsFollowed(){
        return isFollowed;
    }

    public CategoryItem(int id, String title, String seourl, int postNumber, int followerNumber, Boolean isFollowed) {
        this.id = id;
        this.title = title;
        this.seourl = seourl;
        this.postNumber = postNumber;
        this.followerNumber = followerNumber;
        this.isFollowed = isFollowed;
    }
}
