package com.example.linkusapp.model.vo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Board implements Serializable {
    private static final long serialVersionUID =1L;

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

    @SerializedName("G_START_DATE")
    private String gStartDate;

    @SerializedName("G_END_DATE")
    private String gEndDate;

    @SerializedName("G_EXPLANATION")
    private String gExplanation;

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

    public String getgStartDate() {
        return gStartDate;
    }

    public String getgEndDate() {
        return gEndDate;
    }

    public String getgExplanation() {
        return gExplanation;
    }

    @Override
    public String toString() {
        return "Board{" +
                "gPart='" + gPart + '\'' +
                ", gArea='" + gArea + '\'' +
                ", gReader='" + gReader + '\'' +
                ", gName='" + gName + '\'' +
                ", gPurpose='" + gPurpose + '\'' +
                ", gJoinMethod='" + gJoinMethod + '\'' +
                ", gMemberCnt='" + gMemberCnt + '\'' +
                ", gStartDate='" + gStartDate + '\'' +
                ", gEndDate='" + gEndDate + '\'' +
                ", gExplanation='" + gExplanation + '\'' +
                '}';
    }
}