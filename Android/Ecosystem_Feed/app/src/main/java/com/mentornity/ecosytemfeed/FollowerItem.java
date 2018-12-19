package com.mentornity.ecosytemfeed;

import android.graphics.Bitmap;
//It is used in Category details->Followers
public class FollowerItem  {
    private String profileImage;
    private String userName;
    private String authid;
    private String oneSignalUserid;

    public FollowerItem(String profileImage, String userName, String authid, String oneSignalUserid) {
        this.profileImage = profileImage;
        this.userName = userName;
        this.authid = authid;
        this.oneSignalUserid = oneSignalUserid;
    }

    public String getAuthid() {
        return authid;
    }

    public void setAuthid(String authid) {
        this.authid = authid;
    }

    public String getOneSignalUserid() {
        return oneSignalUserid;
    }

    public void setOneSignalUserid(String oneSignalUserid) {
        this.oneSignalUserid = oneSignalUserid;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getUserName() {
        return userName;
    }

}
