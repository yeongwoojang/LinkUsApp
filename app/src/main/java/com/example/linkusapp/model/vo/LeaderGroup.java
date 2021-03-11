package com.example.linkusapp.model.vo;

import com.google.gson.annotations.SerializedName;

public class LeaderGroup {
    @SerializedName("G_NAME")
    private String name;

    @SerializedName("G_READER")
    private String leader;

    @SerializedName("REQ_COUNT")
    private int reqCount;

    public String getName() {
        return name;
    }

    public String getLeader() {
        return leader;
    }

    public int getReqCount() {
        return reqCount;
    }

    @Override
    public String toString() {
        return "LeaderGroup{" +
                "gName='" + name + '\'' +
                ", gLeader='" + leader + '\'' +
                ", reqCount=" + reqCount +
                '}';
    }
}
