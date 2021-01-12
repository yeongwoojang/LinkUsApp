package com.example.linkusapp.model.vo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Board implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("G_PART")
    private String gPart;

    @SerializedName("ADDRESS")
    private String gArea;

    @SerializedName("G_READER")
    private String gReader;

    @SerializedName("G_NAME")
    private String gName;

    @SerializedName("G_PURPOSE")
    private String gPurpose;

    @SerializedName("G_JOIN_METHOD")
    private String gJoinMethod;

    @SerializedName("G_MEMBER_CNT")
    private String gMemberCnt;

    public String getgPart() {
        return gPart;
    }


    public String getgArea() {
        return gArea;
    }

    public String getgReader() {
        return gReader;
    }

    public String getgName() {
        return gName;
    }

    public String getgPurpose() {
        return gPurpose;
    }

    public String getgJoinMethod() { return gJoinMethod; }

    public String getgMemberCnt() {
        return gMemberCnt;
    }
}