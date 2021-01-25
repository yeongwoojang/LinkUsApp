package com.example.linkusapp.model.vo;

import com.google.gson.annotations.SerializedName;

public class LeaderGroup {
    @SerializedName("G_NAME")
    private String gName;

    @SerializedName("G_READER")
    private String gLeader;

    @SerializedName("REQ_COUNT")
    private int reqCount;

    public String getgName() {
        return gName;
    }

    public String getgLeader() {
        return gLeader;
    }

    public int getReqCount() {
        return reqCount;
    }

    @Override
    public String toString() {
        return "LeaderGroup{" +
                "gName='" + gName + '\'' +
                ", gLeader='" + gLeader + '\'' +
                ", reqCount=" + reqCount +
                '}';
    }
}
