package com.example.linkusapp.model.vo;

import com.google.gson.annotations.SerializedName;

public class Reply {

    @SerializedName("B_NAME")
    private String name;

    @SerializedName("B_WRITER")
    private String writer;

    @SerializedName("B_COMMENT")
    private String comment;

    @SerializedName("B_WRITETIME")
    private String writeTime;

}
