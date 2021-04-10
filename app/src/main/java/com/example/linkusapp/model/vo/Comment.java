package com.example.linkusapp.model.vo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Comment implements Serializable {
    private static final long serialVersionUID =1L; /*객체 전체 넘길수 있따.*/

    @SerializedName("B_IDX")
    private int idx;

    @SerializedName("B_NAME")
    private String name;

    @SerializedName("B_WRITER")
    private String writer;

    @SerializedName("B_COMMENT")
    private String comment;

    @SerializedName("B_WRITETIME")
    private String writeTime;

    public int getIdx() {
        return idx;
    }

    public String getName() {
        return name;
    }

    public String getWriter() {
        return writer;
    }

    public String getComment() {
        return comment;
    }

    public String getWriteTime() {
        return writeTime;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setWriteTime(String writeTime) {
        this.writeTime = writeTime;
    }
}
