package com.example.linkusapp.model.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfo {
    @SerializedName("code")
    private int code;
    @SerializedName("userInfo")
    @Expose
    private User user;

    public int getCode() {
        return code;
    }

    public User getUser() {
        return user;
    }
}