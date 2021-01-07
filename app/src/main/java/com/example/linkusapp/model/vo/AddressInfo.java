package com.example.linkusapp.model.vo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddressInfo {
    @SerializedName("code")
    private String code;

    @SerializedName("jsonArray")
    private List<Address> address;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public AddressInfo(String code, List<Address> address) {
        this.code = code;
        this.address = address;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }
}
