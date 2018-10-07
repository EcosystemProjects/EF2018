package com.mentornity.ecosytemfeed;

import android.graphics.Bitmap;
//It is used in Category details->Followers
public class FollowerItem  {
    private Bitmap profileImage;
    private String UserName;

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public String getUserName() {
        return UserName;
    }

    public FollowerItem(Bitmap profileImage, String userName) {
        this.profileImage = profileImage;
        UserName = userName;
    }
}
