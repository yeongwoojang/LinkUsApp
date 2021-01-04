package com.example.linkusapp.repository;

import com.example.linkusapp.model.vo.FindPassword;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServiceApi {

    @FormUrlEncoded
    @POST("/android/join")
    Call<String> join(
            @Field("userName") String userName,
            @Field("userId") String userId,
            @Field("password") String password,
            @Field("userEmail") String userEmail
    );

    @GET("/android/idChk")
    Call<String> chkId(@Query("userId") String userId);

    @FormUrlEncoded
    @POST("/android/login")
    Call<String> login(@Field("userId") String userId,@Field("password") String password);

    @GET("/android/findPw")
    Call<FindPassword> findPw(@Query("userId") String userId, @Query("email") String email);

    @FormUrlEncoded
    @POST("/android/googleIdToken")
    Call<String> sendGoogleIdToken(@Field("idToken") String idToken);

    @GET("/android/userInfoChk")
    Call<String> chkUserInfo(@Query("userId") String userId);
}