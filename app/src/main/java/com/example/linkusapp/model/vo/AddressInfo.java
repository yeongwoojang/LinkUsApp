package com.example.linkusapp.model.vo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddressInfo {
    @SerializedName("code")
    private String code;

    @SerializedName("jsonArray")
    private List<UserAddress> jsonArray;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<UserAddress> getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(List<UserAddress> jsonArray) {
        this.jsonArray = jsonArray;
    }
}
