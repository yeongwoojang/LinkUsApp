package com.example.linkusapp.util;

import android.content.Context;

import java.io.IOException;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookiesInterceptor implements Interceptor {

    SharedPreference prefs;
    public AddCookiesInterceptor(Context context) {
        this.prefs  = new SharedPreference(context);
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        Set<String> cookies = prefs.getCookies();

        if(cookies!=null){
            for(String cookie : cookies){
                builder.addHeader("Cookie",cookie);
            }
        }

        builder.removeHeader("User-Agent").addHeader("User-Agent","Android");
        return chain.proceed(builder.build());
    }
}
