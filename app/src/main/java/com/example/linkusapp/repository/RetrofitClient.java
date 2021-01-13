package com.example.linkusapp.repository;

import android.content.Context;

import com.example.linkusapp.util.AddCookiesInterceptor;
import com.example.linkusapp.util.ReceivedCookiesInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private final static String BASE_URL = "http://ec2-15-164-129-208.ap-northeast-2.compute.amazonaws.com:3000";

    private static Retrofit retrofit = null;



    public static Retrofit getClient(Context context){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //쿠키를 sharedPreferences에 저장하고 가져온다.
                .addNetworkInterceptor(new AddCookiesInterceptor(context))
                .addNetworkInterceptor(new ReceivedCookiesInterceptor(context))
                .connectTimeout(1,TimeUnit.MINUTES)
                .readTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(15,TimeUnit.SECONDS)
                .build();

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}