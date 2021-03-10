package com.example.linkusapp.model.vo;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

public class Profile {
    @SerializedName("code")
    private int code;

    @SerializedName("profileUri")
    private Uri profileUri;

    public int getCode() {
        return code;
    }

    public Uri getProfileUri() {
        return profileUri;
    }
}
