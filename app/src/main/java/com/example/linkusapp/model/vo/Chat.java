package com.example.linkusapp.model.vo;

import com.google.gson.annotations.SerializedName;

import kotlin.jvm.Transient;

public class Chat {

    @SerializedName("ROOM_ID")
    private int roomId;

    @SerializedName("MSG")
    private String msg;

    @SerializedName("MSG_TIME")
    private String msgTime;

    @SerializedName("MSG_TO")
    private String msgTo;

    @SerializedName("MSG_FROM")
    private String msgFrom;


    public Chat(String msg, String msgTime, String msgTo, String msgFrom) {
        this.msg = msg;
        this.msgTime = msgTime;
        this.msgTo = msgTo;
        this.msgFrom = msgFrom;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getMsg() {
        return msg;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public String getMsgTo() {
        return msgTo;
    }

    public String getMsgFrom() {
        return msgFrom;
    }
}
