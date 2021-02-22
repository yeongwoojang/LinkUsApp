package com.example.linkusapp.model.vo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommentInfo {

    @SerializedName("code")
    private int code;

    @SerializedName("jsonArray")
    private List<Comment> jsonArray;

    public int getCode() {
        return code;
    }

    public List<Comment> getJsonArray() {
        return jsonArray;
    }

}
