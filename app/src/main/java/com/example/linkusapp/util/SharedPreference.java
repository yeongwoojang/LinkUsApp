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


    public void putCookies(HashSet<String> cookies) {
        prefs.edit().putStringSet("cookies", cookies).apply();
    }

    public Set<String> getCookies() {
        return prefs.getStringSet("cookies", new HashSet<String>());
    }

    public void removeCookies(){
        prefs.edit().remove("cookies").apply();
    }

    public void putInfoAutoLogin(boolean value){
        prefs.edit().putBoolean("isAutoLogin",value).apply();
    }

    public boolean getInfoAutoLogin(){
       return prefs.getBoolean("isAutoLogin",false);
    }
    public void cancelAutoLogin(){
        prefs.edit().remove("isAutoLogin").apply();
    }

}
