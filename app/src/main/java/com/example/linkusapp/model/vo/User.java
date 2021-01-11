package com.example.linkusapp.model.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("USER_ID")
    @Expose
    private String userId;
    @SerializedName("USER_NAME")
    @Expose
    private String userName;
    @SerializedName("PASSWORD")
    @Expose
    private String password;
    @SerializedName("EMAIL")
    @Expose
    private String email;
    @SerializedName("GENDER")
    @Expose
    private String gender;
    @SerializedName("AGE")
    @Expose
    private String age;
    @SerializedName("ADDRESS")
    @Expose
    private String address;
    @SerializedName("LOGIN_METHOD")
    @Expose
    private String loginMethod;
    @SerializedName("USER_NICKNAME")
    @Expose
    private String userNickname;

    public String getUserNickname() {
        return userNickname;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getLoginMethod() {
        return loginMethod;
    }
}
