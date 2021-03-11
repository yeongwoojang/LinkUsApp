package com.example.linkusapp.model.vo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Board implements Serializable {
    private static final long serialVersionUID =1L;

    @SerializedName("G_PART")
    private String part;

    @SerializedName("ADDRESS")
    private String area;

    @SerializedName("G_READER")
    private String leader;

    @SerializedName("G_NAME")
    private String title;

    @SerializedName("G_PURPOSE")
    private String purpose;

    @SerializedName("G_JOIN_METHOD")
    private String groupJoinMethod;

    @SerializedName("G_MEMBER_LIMIT")
    private String memberLimit;

    @SerializedName("G_START_DATE")
    private String startDate;

    @SerializedName("G_END_DATE")
    private String endDate;

    @SerializedName("G_EXPLANATION")
    private String explanation;

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getGroupJoinMethod() {
        return groupJoinMethod;
    }

    public void setGroupJoinMethod(String groupJoinMethod) {
        this.groupJoinMethod = groupJoinMethod;
    }

    public String getMemberLimit() {
        return memberLimit;
    }

    public void setMemberLimit(String memberLimit) {
        this.memberLimit = memberLimit;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}