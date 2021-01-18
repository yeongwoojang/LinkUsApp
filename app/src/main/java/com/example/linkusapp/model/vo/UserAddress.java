package com.example.linkusapp.model.vo;

import com.google.gson.annotations.SerializedName;

public class UserAddress {

    @SerializedName("A_USER")
    private String nickname;

    @SerializedName("A_ADDRESS")
    private String address;

    public String getNickname() {
        return nickname;
    }

    public String getAddress() {
        return address;
    }

}
