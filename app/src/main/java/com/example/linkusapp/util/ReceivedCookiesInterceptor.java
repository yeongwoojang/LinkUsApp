package com.example.linkusapp.util;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Response;

public class ReceivedCookiesInterceptor implements Interceptor {

    SharedPreference prefs;
    public ReceivedCookiesInterceptor(Context context) {
        this.prefs = new SharedPreference(context);
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response  originalResponse = chain.proceed(chain.request());
        if(!originalResponse.headers("Set-Cookie").isEmpty()){
            HashSet<String> cookies = new HashSet<String>();

            for(String header : originalResponse.headers("Set-Cookie")){
                cookies.add(header);
                Log.d("COOKIE", "intercept: "+cookies.toString());
            }
            prefs.putCookies(cookies);
        }
        return originalResponse;
    }
}