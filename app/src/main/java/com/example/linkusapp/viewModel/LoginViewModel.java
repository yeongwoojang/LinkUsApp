package com.example.linkusapp.viewModel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.linkusapp.model.vo.FindPassword;
import com.example.linkusapp.repository.RetrofitClient;
import com.example.linkusapp.repository.ServiceApi;
import com.example.linkusapp.util.GMailSender;
import com.example.linkusapp.util.SharedPreference;

import java.util.Iterator;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    private ServiceApi serviceApi;
    private SharedPreference prefs;
    public MutableLiveData<String> loginRsLD = new MutableLiveData<String>();
    public MutableLiveData<FindPassword> findPwRsLD = new MutableLiveData<FindPassword>();
    public MutableLiveData<Integer> sendMailRes = new MutableLiveData<Integer>();

    public LoginViewModel(@NonNull Application application){
        super(application);
        serviceApi = RetrofitClient.getClient(application).create(ServiceApi.class);
        prefs = new SharedPreference(application);
    }

    public void login(String userId,String password) {
        serviceApi.login(userId, password).enqueue(new Callback<String>() {
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
        serviceApi.findPw(userId,email).enqueue(new Callback<FindPassword>() {
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
        serviceApi.sendGoogleIdToken(idToken).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                Log.d("OAuth2", "onResponse: "+result);
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
                Log.d("SESSION", "getLoginSession: $userSession");
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

}