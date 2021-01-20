package com.example.linkusapp.model.vo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LeaderGroupInfo {
    @SerializedName("code")
    private int code;

    @SerializedName("jsonArray")
    List<LeaderGroup> leaderGroupList;

    public int getCode() {
        return code;
    }

    public List<LeaderGroup> getLeaderGroupList() {
        return leaderGroupList;
    }

}
