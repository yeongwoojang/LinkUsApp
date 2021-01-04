package com.example.linkusapp.model.vo;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.FormUrlEncoded;

public class FindPassword {

    @SerializedName("code")
    private String code;

    @SerializedName("password")
    private String password;

    public String getCode() {return code;}

    public String getPassword() {return password;}
}