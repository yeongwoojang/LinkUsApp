package com.example.linkusapp.model.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Timer {
    @SerializedName("USER_NICKNAME")
    private String userNick;
    @SerializedName("STUDY_TIME")
    private String studyTime;
    @SerializedName("STUDY_DATE")
    private String studyDate;

    public String getUserNick() {
        return userNick;
    }

    public String getStudyTime() {
        return studyTime;
    }

    public String getStudyDate() {
        return studyDate;
    }
}
