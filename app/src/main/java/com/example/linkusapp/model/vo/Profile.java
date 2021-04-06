package com.example.linkusapp.model.vo;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

public class Profile {
    @SerializedName("code")
    private String code;

    @SerializedName("profileUri")
    private String profileUri;

    public String getCode() {
        return code;
    }

    public String getProfileUri() {
        return profileUri;
    }
}
