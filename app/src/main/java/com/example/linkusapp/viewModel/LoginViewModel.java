package com.example.linkusapp.viewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.linkusapp.repository.FindPassword;
import com.example.linkusapp.repository.RetrofitClient;
import com.example.linkusapp.repository.ServiceApi;
import com.example.linkusapp.util.GMailSender;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    private ServiceApi serviceApi= RetrofitClient.getClient().create(ServiceApi.class);


    public MutableLiveData<String> loginRsLD = new MutableLiveData<String>();
    public MutableLiveData<String> findPwRsLD = new MutableLiveData<String>();

    public MutableLiveData<Integer> sendMailRes = new MutableLiveData<Integer>();

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
                findPwRsLD.postValue(result.getPassword());
                Log.d("Result",result.getPassword());
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

}
