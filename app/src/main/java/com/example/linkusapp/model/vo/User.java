package com.example.linkusapp.model.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    private static final long serialVersionUID =1L;

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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLoginMethod(String loginMethod) {
        this.loginMethod = loginMethod;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }
}