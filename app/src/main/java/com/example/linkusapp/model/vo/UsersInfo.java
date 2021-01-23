package com.example.linkusapp.model.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UsersInfo {
    @SerializedName("code")
    private int code;
    @SerializedName("jsonArray")
    @Expose
    private List<User> users;

    public int getCode() {
        return code;
    }

    public List<User> getUsers() {
        return users;
    }
}
