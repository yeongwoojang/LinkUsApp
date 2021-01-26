package com.example.linkusapp.model.vo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommentInfo {

    @SerializedName("code")
    private String code;

    @SerializedName("jsonArray")
    private List<Comment> jsonArray;

    public String getCode() {
        return code;
    }

    public List<Comment> getJsonArray() {
        return jsonArray;
    }

}
