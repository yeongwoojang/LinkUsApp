package com.example.linkusapp.viewModel;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.linkusapp.model.vo.FindPassword;
import com.example.linkusapp.model.vo.Profile;
import com.example.linkusapp.model.vo.User;
import com.example.linkusapp.model.vo.UserInfo;
import com.example.linkusapp.repository.RetrofitClient;
import com.example.linkusapp.repository.ServiceApi;
import com.example.linkusapp.util.GMailSender;
import com.example.linkusapp.util.SharedPreference;
import com.kakao.util.helper.log.Tag;

import java.net.URI;
import java.util.Iterator;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends BaseViewModel {

//    private ServiceApi serviceApi;
//    private SharedPreference prefs;
    public MutableLiveData<String> loginRsLD = new MutableLiveData<String>();
    public MutableLiveData<FindPassword> findPwRsLD = new MutableLiveData<FindPassword>();

    public MutableLiveData<Integer> sendMailRes = new MutableLiveData<Integer>();
    public MutableLiveData<String> nickChkResLD = new MutableLiveData<String>();
    public MutableLiveData<String> addUserInfoResLD = new MutableLiveData<String>();

    public MutableLiveData<String> withDrawREDLD = new MutableLiveData<String>();
    public MutableLiveData<UserInfo> getUserInfoRsLD = new MutableLiveData<>();
    public MutableLiveData<String> updateAddressRsLD = new MutableLiveData<String>();
    public MutableLiveData<String> updateUserInfoRsLD = new MutableLiveData<String>();
    private MutableLiveData<String> tkInsertRes = new MutableLiveData<>();
    public MutableLiveData<UserInfo> userLiveData = new MutableLiveData<UserInfo>();
    public MutableLiveData<String> insertProfileLiveData = new MutableLiveData<String>();
    public MutableLiveData<Profile> getProfileLiveData = new MutableLiveData<Profile>();
    public MutableLiveData<FindPassword> decryptPwLiveData = new MutableLiveData<FindPassword>();




    public LoginViewModel(@NonNull Application application){
        super(application);
//        serviceApi = RetrofitClient.getClient(application).create(ServiceApi.class);
//        prefs = new SharedPreference(application);
    }

    public void login(String userId,String password) {
        service.login(userId, password).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                loginRsLD.postValue(result);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }
    public void findPw(String userId,String email){
        service.findPw(userId,email).enqueue(new Callback<FindPassword>() {
            @Override
            public void onResponse(Call<FindPassword> call, Response<FindPassword> response) {
                FindPassword result =response.body();
                findPwRsLD.postValue(result);
            }

            @Override
            public void onFailure(Call<FindPassword> call, Throwable t) {
                t.getMessage();
            }
        });
    }

    public void sendMail(GMailSender gMailSender, String email,String password){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    gMailSender.sendMail("LinkUs 패스워드 찾기 Test", password, email);
                    sendMailRes.postValue(1000);
                } catch (
                        SendFailedException e) {
                    sendMailRes.postValue(1001);
                } catch (
                        MessagingException e) {
                    sendMailRes.postValue(1002);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendGoogleIdToken(String idToken){
        service.sendGoogleIdToken(idToken).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                Log.d("a", "onResponse: "+result);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void putSocialLogin(String userName, String userId, String loginMethod){
        service.putSocialLogin(userName,userId,loginMethod).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                Log.d("a", "onResponse: "+result);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void autoLogin(boolean value){
        prefs.putInfoAutoLogin(value);
    }

    public boolean isAutoLogin(){
        return prefs.getInfoAutoLogin();
    }

    public String getLoginSession() {
        String userSession= " ";
        Iterator<String> iterator = prefs.getCookies().iterator();
        if (iterator != null) {
            while (iterator.hasNext()) {
                userSession = iterator.next();
                userSession = userSession.split(";")[0].split("=")[1];
                Log.d("SESSION", "getLoginSession: " +userSession);
            }
        }
        return userSession;
    }

    public void removeUserIdPref(){
        prefs.removeCookies();
    }
    public void cancelAutoLogin(){
        prefs.cancelAutoLogin();
    }

    public String getLoginMethod(){
        return prefs.getLoginMethod();
    }

    public void putLoginMethod(String value){
        prefs.putLoginMethod(value);
    }

    public void nickNameChk(String userNickname) {
        service.nickNameChk(userNickname)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String result = response.body();
                        nickChkResLD.postValue(result);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
    }

    public void saveInfo(String userId,String nickname,String age,String gender,String address,String loginMethod){
        service.saveInfo(userId,nickname,age,gender,address,loginMethod)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String result = response.body();
                        addUserInfoResLD.postValue(result);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
    }
    /*탈퇴하기*/
    public void withDraw(String userId,String loginMethod){
        service.withDraw(userId,loginMethod).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                withDrawREDLD.postValue(result);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    public void registrationAppToken(String appToken,String nickName){
        service.registrationAppToken(appToken,nickName,prefs.getLoginMethod()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                tkInsertRes.postValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    public void updateUserInfo(String userNickname, String userPassword, String loginMethod){
        service.updateUserInfo(userNickname,userPassword,loginMethod).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String code = response.body();
                updateUserInfoRsLD.postValue(code);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    public void getUserInfoFromDB(){
        service.getUserInfo(prefs.getLoginMethod()).enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                UserInfo result = response.body();
                userLiveData.postValue(result);
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {

            }
        });
    }

    public void insertProfile(String userNickname, Uri profileUri){
        service.insertProfile(userNickname,profileUri).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                insertProfileLiveData.postValue(result);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void getProfile(String userNickname){
        service.getProfile(userNickname).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                Profile result = response.body();
                getProfileLiveData.postValue(result);
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {

            }
        });
    }
    public void decryptPw(String userId){
        service.decryptPw(userId).enqueue(new Callback<FindPassword>() {
            @Override
            public void onResponse(Call<FindPassword> call, Response<FindPassword> response) {
                FindPassword result = response.body();

            }

            @Override
            public void onFailure(Call<FindPassword> call, Throwable t) {

            }
        });
    }
}