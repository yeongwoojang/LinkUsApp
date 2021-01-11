package com.example.linkusapp.model.vo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BoardPartInfo {

    @SerializedName("code")
    private int code;

    @SerializedName("jsonArray")
    private List<Board> boardList;

    public int getCode(){return code;}

    public List<Board> getJsonArray() {
        return boardList;
    }
}