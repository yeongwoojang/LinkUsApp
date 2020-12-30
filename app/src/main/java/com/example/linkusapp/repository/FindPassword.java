package com.example.linkusapp.repository;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.FormUrlEncoded;

public class FindPassword {

    @SerializedName("code")
    private String code;

    @SerializedName("password")
    private String password;

    @SerializedName("email")
    private String email;

    public String getEmail() {return email;}

    public String getCode() {return code;}

    public String getPassword() {return password;}
}
