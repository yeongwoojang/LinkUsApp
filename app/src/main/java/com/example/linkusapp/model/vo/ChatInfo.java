package com.example.linkusapp.model.vo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatInfo {
    @SerializedName("code")
    private int code;
    @SerializedName("jsonArray")
    private List<Chat> jsonArray;

    public int getCode() {
        return code;
    }

    public List<Chat> getJsonArray() {
        return jsonArray;
    }
}
