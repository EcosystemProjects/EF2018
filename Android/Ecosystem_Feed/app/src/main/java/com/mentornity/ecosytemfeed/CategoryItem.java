package com.mentornity.ecosytemfeed;

public class CategoryItem {
    //Category Item is used Categories.java
    private String title,postNumber, followerNumber;
    private Boolean isFollowed;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostNumber() {
        return postNumber;
    }

    public String getFollowerNumber() {
        return followerNumber;
    }

    public Boolean getIsFollowed(){
        return isFollowed;
    }

    public CategoryItem(String title, String postNumber, String followerNumber, Boolean isFollowed) {
        this.title = title;
        this.postNumber = postNumber;
        this.followerNumber = followerNumber;
        this.isFollowed = isFollowed;
    }
}
