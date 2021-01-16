package com.example.linkusapp.model.vo;

import com.google.gson.annotations.SerializedName;

public class MemberCount {
    @SerializedName("code")
    private int code;

    @SerializedName("memberCount")
    private int memberCount;

    public int getCode() {
        return code;
    }

    public int getMemberCount() {
        return memberCount;
    }
}
