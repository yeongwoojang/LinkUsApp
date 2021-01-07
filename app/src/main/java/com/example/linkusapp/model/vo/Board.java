package com.example.linkusapp.model.vo;

import com.google.gson.annotations.SerializedName;

public class Board {

    @SerializedName("G_PART")
    private String gPart;

    @SerializedName("G_AREA")
    private String gArea;

    @SerializedName("G_READER")
    private String gReader;

    @SerializedName("G_NAME")
    private String gName;

    @SerializedName("G_GOAL")
    private String gGoal;

    @SerializedName("G_JOIN_METHOD")
    private String gJoinMethod;


    public String getgPart() {
        return gPart;
    }

    public void setgPart(String gPart) {
        this.gPart = gPart;
    }

    public String getgArea() {
        return gArea;
    }

    public void setgArea(String gArea) {
        this.gArea = gArea;
    }

    public String getgReader() {
        return gReader;
    }

    public void setgReader(String gReader) {
        this.gReader = gReader;
    }

    public String getgName() {
        return gName;
    }

    public void setgName(String gName) {
        this.gName = gName;
    }

    public String getgGoal() {
        return gGoal;
    }

    public void setgGoal(String gGoal) {
        this.gGoal = gGoal;
    }

    public String getgJoinMethod() { return gJoinMethod; }

    public void setgJoinMethod(String gJoinMethod) { this.gJoinMethod = gJoinMethod; }
}