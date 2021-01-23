package com.example.linkusapp.repository;

import com.example.linkusapp.model.vo.AddressInfo;
import com.example.linkusapp.model.vo.BoardInfo;
import com.example.linkusapp.model.vo.FindPassword;
import com.example.linkusapp.model.vo.LeaderGroupInfo;
import com.example.linkusapp.model.vo.MemberCount;
import com.example.linkusapp.model.vo.User;
import com.example.linkusapp.model.vo.UserInfo;
import com.example.linkusapp.model.vo.UsersInfo;

import java.util.List;

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

    @GET("/android/chkGroupName")
    Call<String> chkGroupName(@Query("gName") String gName);

    @FormUrlEncoded
    @POST("/android/createGroup")
    Call<String> createGroup(
            @Field("g_name") String gName,
            @Field("g_explanation") String gExplanation,
            @Field("g_part") String gPart,
            @Field("g_purpose") String gPurpose,
            @Field("g_member_limit") String gMemberLimit,
            @Field("g_start_date") String gStartDate,
            @Field("g_end_date") String gEndDate,
            @Field("g_join_method") String gJoinMetho,
            @Field("loginMethod") String loginMethod
    );

    // 스터디 그룹(게시글) 전체를 서버에 요청
    @GET("/android/boardAll")
    Call<BoardInfo> getAllBoard();

    // 분야별 스터디 그룹을 서버에 요청
    @GET("/android/boardPart")
    Call<BoardInfo> getPartBoard(@Query("gPart") String gPart);

    @GET("/android/boardSearch")
    Call<BoardInfo> getSearchBoard(@Query("keyword") String keyword);

    @GET("/android/boardAddress")
    Call<BoardInfo> getAddressBoard(@Query("address") String address);

    @GET("/android/boardCondition")
    Call<BoardInfo> getConditionBoard(@Query("gPart") String gPart, @Query("address") String address);

    @FormUrlEncoded
    @POST("/android/withdraw")
    Call<String> withDraw(
            @Field("userId") String userId,
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
    Call<String> updateUserInfo(@Field("userNickname") String userNickname,@Field("userPassword") String userPassword, @Field("loginMethod") String loginMethod);

    @GET("/android/userBoardAll")
    Call<BoardInfo> userBoardAll(@Query("userNickname") String userNickname);

    @GET("/android/allAddress")
    Call<BoardInfo> allAddress();

    @GET("/android/optionBoard")
    Call<BoardInfo> optionBoard(@Query("g_part") String gPart, @Query("address") String address);

    @GET("/android/countGroupMember")
    Call<MemberCount> getMemberCount(@Query("gName") String gName);

    @FormUrlEncoded
    @POST("/android/joinGroup")
    Call<String> joinGroup(@Field("gName") String gName, @Field("gMemberId") String gMemberId, @Field("gMemberNick") String gMemberNick);

    @GET("/android/getLeaderGroup")
    Call<LeaderGroupInfo> getLeaderGroup(@Query("userNick")String userNick);

    @GET("/android/getReqUser")
    Call<UsersInfo> getReqUser(@Query("gName") String gName);

//-------------------------FCM 관련 메소드------------------------

    //fcm 전송메소드
    @FormUrlEncoded
    @POST("/android/requestJoin")
    Call<Void> requestJoin(
            @Field("targetUser") String nickname,
            @Field("userNick") String userNick,
            @Field("userAge") String userAge,
            @Field("userGender") String userGender,
            @Field("address") String address);

    //DB에 현재 유저의 앱토큰을 저장하는 메소드
    @FormUrlEncoded
    @POST("/android/registrationAppToken")
    Call<String> registrationAppToken(@Field("appToken")String appToken,@Field("nickname")String nickname,@Field("loginMethod")String loginMethod);
    @FormUrlEncoded
    @POST("/android/removeAddress")
    Call<String> removeAddress(@Field("userAddress") String userAddress);

    @FormUrlEncoded
    @POST("/android/insertRequest")
    Call<String> insertRequest(@Field("gName")String gName, @Field("userNick")String userNick);

}