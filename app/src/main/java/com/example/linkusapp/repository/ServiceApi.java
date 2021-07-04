package com.example.linkusapp.repository;

import android.graphics.Bitmap;
import android.net.Uri;

import com.example.linkusapp.model.vo.AddressInfo;
import com.example.linkusapp.model.vo.BoardInfo;
import com.example.linkusapp.model.vo.ChatInfo;
import com.example.linkusapp.model.vo.CommentInfo;
import com.example.linkusapp.model.vo.FindPassword;
import com.example.linkusapp.model.vo.LeaderGroupInfo;
import com.example.linkusapp.model.vo.Profile;
import com.example.linkusapp.model.vo.TimerInfo;
import com.example.linkusapp.model.vo.UserInfo;
import com.example.linkusapp.model.vo.UsersInfo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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

    @GET("/android/login")
    Call<FindPassword> login(@Query("userId") String userId);

    @FormUrlEncoded
    @POST("/android/findPw")
    Call<String> findPw(@Field("userId") String userId,@Field("email") String email ,@Field("password") String password);

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

    @GET("/android/deleteGroup")
    Call<String> deleteGroup(@Query("g_name") String gName);

    // 스터디 그룹(게시글) 전체를 서버에 요청
    @GET("/android/boardAll")
    Call<BoardInfo> getAllBoard();

    // 분야별 스터디 그룹을 서버에 요청
    @GET("/android/boardPart")
    Call<BoardInfo> getPartBoard(@Query("gPart") String gPart);

    @GET("/android/boardSearch")
    Call<BoardInfo> getSearchBoard(@Query("keyword1") String keyword1, @Query("keyword2") String keyword2);

    @GET("/android/boardRefresh")
    Call<BoardInfo> getRefreshBoard();

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
    Call<String> updateUserInfo(@Field("userNickname") String userNickname,@Field("userPassword") String userPassword,@Field("loginMethod") String loginMethod);

    @GET("/android/userBoardAll")
    Call<BoardInfo> userBoardAll(@Query("userNickname") String userNickname);

    @GET("/android/optionBoard")
    Call<BoardInfo> optionBoard(@Query("g_part") String gPart, @Query("address") String address);

    @GET("/android/getGroupMember")
    Call<UsersInfo> getGroupMember(@Query("gName") String gName);

    @FormUrlEncoded
    @POST("/android/joinGroup")
    Call<String> joinGroup(@Field("gName") String gName, @Field("gMemberId") String gMemberId, @Field("gMemberNick") String gMemberNick);

    @GET("/android/getLeaderGroup")
    Call<LeaderGroupInfo> getLeaderGroup(@Query("userNick")String userNick);

    @GET("/android/getReqUser")
    Call<UsersInfo> getReqUser(@Query("gName") String gName);

//-------------------------CommentViewModel 메소드------------------------
    /*comment 추가*/
    @FormUrlEncoded
    @POST("android/insertComment")
    Call<String> insertComment(@Field("bName") String bName,@Field("bWriter") String bWriter,@Field("bComment") String bComment);

    @FormUrlEncoded
    @POST("/android/insertReply")
    Call<String> insertReply(@Field("bName") String bName, @Field("bWriter") String bWriter,@Field("bComment") String bComment,@Field("bRpyWriter") String rpyWriter,@Field("bRpy") String bRpy);

    /*comment 불러오기*/
    @GET("android/getComment")
    Call<CommentInfo> getComment(@Query("bName") String bName);

    @GET("/android/getReply")
    Call<CommentInfo> getReply(@Query("bName") String bName, @Query("bWriter") String bWriter, @Query("bComment") String bComment);

    @GET("/android/gerEntireReply")
    Call<CommentInfo> getEntireReply(@Query("bName") String bName);

    @FormUrlEncoded
    @POST("android/updateNotice")
    Call<String> updateNotice(@Field("gName") String gName,@Field("notice") String notice);

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

    @FormUrlEncoded
    @POST("/android/acceptJoin")
    Call<Void> acceptJoin(
      @Field("targetUser") String nickName
    );

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

    @DELETE("/android/deleteRequest")
    Call<String> deleteRequest(@Query("gName")String gName, @Query("userNick")String userNick);

    //유저의 대표 스터디 그룹을 선택했을 때 USER테이블의 대표 스터디그룹 컬럼을 업데이트 하는 메소드
    @PUT("/android/updatesSelected")
    Call<String> updateSelected(@Query("userNick") String userNick,@Query("gName") String gName);

    //유저가 선택한 대표 스터디 그룹의 정보를 불러오는 메소드
    @GET("/android/getSelected")
    Call<BoardInfo> getSelectedGroup(@Query("userNick") String userNick);


    //공부시간 최초기록
    @FormUrlEncoded
    @POST("/android/insertTimer")
    Call<String> insertTimer(@Field("userNick") String userNick, @Field("time") String time);

    //공부시간 업데이트
    @PUT("/android/updateTimer")
    Call<String> updateTimer(@Query("userNick") String userNick, @Query("time") String time);

    //사용자의 전체 공부시간 조회
    @GET("/android/entireRecord")
    Call<TimerInfo> getEntireRecord(@Query("userNick") String userNick);

    //오늘 공부한 시간 조회
    @GET("/android/getTodayRecord")
    Call<TimerInfo> getTodayRecord(@Query("userNick") String userNick);

    @FormUrlEncoded
    @POST("/android/sendMessage")
    Call<String> sendMessage(
            @Field("gName") String gName,
            @Field("myNickName")String myNickName,
            @Field("yourNickName")String yourNickName,
            @Field("msg") String msg,
            @Field("msgTime")String msgTime);

    @GET("/android/getMessageList")
    Call<ChatInfo>getMessageList(@Query("gName") String gName, @Query("myNickName") String myNickName, @Query("yourNickName") String yourNickName);
}
