package com.example.linkusapp.model.vo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TimerInfo {
    @SerializedName("code")
    private int code;
    @SerializedName("jsonArray")
    private List<Timer> timer;

    public int getCode() {
        return code;
    }

    public List<Timer> getTimer() {
        return timer;
    }
}
