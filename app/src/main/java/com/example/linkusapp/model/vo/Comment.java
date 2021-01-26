package com.example.linkusapp.model.vo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.annotation.Annotation;

public class Comment implements Serializable {
    private static final long serialVersionUID =1L; /*객체 전체 넘길수 있따.*/

    @SerializedName("B_IDX")
    private int bIdx;

    @SerializedName("B_NAME")
    private String bName;

    @SerializedName("B_WRITER")
    private int bWriter;

    @SerializedName("B_COMMENT")
    private int bComment;

    @SerializedName("B_WRITETIME")
    private int bWriteTime;

    @SerializedName("B_SECRET")
    private int bSecret;

    public int getbIdx() {
        return bIdx;
    }

    public String getbName() {
        return bName;
    }

    public int getbWriter() {
        return bWriter;
    }

    public int getbComment() {
        return bComment;
    }

    public int getbWriteTime() {
        return bWriteTime;
    }

    public int getbSecret() {
        return bSecret;
    }
}
