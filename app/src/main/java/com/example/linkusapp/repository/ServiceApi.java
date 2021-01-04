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

    @GET("/android/nickNameChk")
    Call<String> nickNameChk(@Query("userNickname") String userNickname);

    @FormUrlEncoded
    @POST("/android/googleIdToken")
    Call<String> sendGoogleIdToken(@Field("idToken") String idToken);

    @FormUrlEncoded
    @POST("/android/socialLogin")
    Call<String> putSocialLogin(@Field("userName") String userName, @Field("userId") String userId, @Field("loginMethod") String loginMethod);

    @GET("/android/chkScdUserInfo")
    Call<String> chkScdUserInfo(@Query("userId") String userId, @Query("loginMethod") String loginMethod);


    @FormUrlEncoded
    @POST("/android/userInfo")
    Call<String> saveInfo(
            @Field("userId") String userId,
            @Field("nickname") String nickname,
            @Field("age") String age,
            @Field("gender") String gender,
            @Field("address") String address,
            @Field("loginMethod") String loginMethod
    );
}