package com.example.linkusapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class SharedPreference {

    SharedPreferences prefs;

    public SharedPreference(Context context) {
        prefs = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

    //일반 로그인 현재 유저계정 관련 메소드
    //-----------------------------------------------------------
    public void putCookies(HashSet<String> cookies) {
        prefs.edit().putStringSet("cookies", cookies).apply();
    }

    public Set<String> getCookies() {
        return prefs.getStringSet("cookies", new HashSet<String>());
    }

    public void removeCookies() {
        prefs.edit().remove("cookies").apply();
    }

    //자동로그인 관련 메소드
    //-----------------------------------------------------------
    public void putInfoAutoLogin(boolean value) {
        prefs.edit().putBoolean("isAutoLogin", value).apply();
    }

    public boolean getInfoAutoLogin() {
        return prefs.getBoolean("isAutoLogin", false);
    }

    public void cancelAutoLogin() {
        prefs.edit().remove("isAutoLogin").apply();
    }

    //로그인 방식 관련 메소드
    //-----------------------------------------------------------
    public void putLoginMethod(String value) {
        prefs.edit().putString("loginMethod", value).apply();
    }

    public String getLoginMethod() {
        return prefs.getString("loginMethod", "");
    }

    public void removeLoginMethod() {
        prefs.edit().remove("loginMethod").apply();
    }
    //회원 정보 얻기
    public void putAddress(String value) {
        prefs.edit().putString("address", value).apply();
    }
    public String getAddress() {
        return prefs.getString("address","");
    }

    public void putNickname(String value){
        prefs.edit().putString("nickname",value).apply();
    }
    public String getNickname() {
        return prefs.getString("nickname","");
    }
    public void removeNickname() {
        prefs.edit().remove("adress").apply();
    }
}
