package com.example.linkusapp.repository;

import com.example.linkusapp.model.vo.AddressInfo;
import com.example.linkusapp.model.vo.BoardInfo;
import com.example.linkusapp.model.vo.FindPassword;
import com.example.linkusapp.model.vo.UserInfo;

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

    @GET("/android/getUserInfo")
    Call<UserInfo> getUserInfo(
            @Query("loginMethod") String loginMethod
    );

    @FormUrlEncoded
    @POST("/android/createGroup")
    Call<String> createGroup(
            @Field("g_name") String gName,
            @Field("g_explanation") String gExplanation,
            @Field("g_part") String gPart,
            @Field("g_purpose") String gPurpose,
            @Field("g_start_date") String gStartDate,
            @Field("g_end_date") String gEndDate,
            @Field("g_join_method") String gJoinMetho,
            @Field("loginMethod") String loginMethod
    );

    // 스터디 그룹(게시글) 전체를 서버에 요청
    @GET("/android/boardAll")
    Call<BoardInfo> getAllBoard();




    @FormUrlEncoded
    @POST("/android/withdraw")
    Call<String> withDraw(
        @Field("userId")      String userId,
        @Field("loginMethod") String loginMethod
    );

    @FormUrlEncoded
    @POST("/android/addAddress")
    Call<String> addAddress(
            @Field("userNickname") String userNickname,
            @Field("address") String address
    );

    @FormUrlEncoded
    @POST("/android/updateAddress")
    Call<String> updateAddress(
            @Field("userNickname") String userNickname,
            @Field("address") String address
    );

    @GET("/android/userAddress")
    Call<AddressInfo> userAddress(@Query("userNickname") String userNickname);

    @FormUrlEncoded
    @POST("/android/updateUserInfo")
    Call<String> updateUserInfo(@Field("userNickname") String userNickname,@Field("userPassword") String userPassword);
}