package com.example.linkusapp.viewModel;

import android.app.Application;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.linkusapp.repository.RetrofitClient;
import com.example.linkusapp.repository.ServiceApi;
import com.example.linkusapp.util.GMailSender;


import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinViewModel extends AndroidViewModel {

    private ServiceApi service;

    public MutableLiveData<String> joinRsLD = new MutableLiveData<String>();
    public MutableLiveData<String> idChkResLD = new MutableLiveData<String>();

    public MutableLiveData<String> count = new MutableLiveData<String>();
    public CountDownTimer countDownTimer;
    public MutableLiveData<Integer> sendMailRes = new MutableLiveData<Integer>();


    public JoinViewModel(@NonNull Application application){
        super(application);
        service = RetrofitClient.getClient(application.getApplicationContext()).create(ServiceApi.class);

    }

    public void join(String userName, String userId, String password, String userEmail) {
        service.join(userName, userId, password,userEmail)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String result = response.body();
                        joinRsLD.postValue(result);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        //통신오류
                    }
                });
    }

    public void idChk(String userId) {
        service.chkId(userId)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String result = response.body();
                        idChkResLD.postValue(result);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
    }

    public Boolean isInputAll(
            String userId,
            String userPw,
            String userPw2,
            String userName,
            String userEmail,
            String certification
    ) {
        boolean value = false;
        if (userId.equals("")
                && userPw.equals("")
                && userPw2.equals("")
                && userName.equals("")
                && userEmail.equals("")
                && certification.equals("")
        ){
            value = true;
        }
        return value;
    }

    public void sendMail(GMailSender gMailSender, String email,String emailCode){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    gMailSender.sendMail("LinkUs 이메일 인증코드 입니다.", emailCode, email);
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


    public void countDown(){

        final int MILLISINFUTURE = 180 * 1000; //총 시간 (180초 = 3분)
        final int COUNT_DOWN_INTERVAL = 1000; //onTick 메소드를 호출할 간격 (1초)

        countDownTimer = new CountDownTimer(MILLISINFUTURE,COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                long emailAuthCount = millisUntilFinished / 1000;

                if ((emailAuthCount - ((emailAuthCount / 60) * 60)) >= 10) { //초가 10보다 크면 그냥 출력
                    count.postValue((emailAuthCount / 60) + " : " + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                } else { //초가 10보다 작으면 앞에 '0' 붙여서 같이 출력. ex) 02,03,04...
                    count.postValue((emailAuthCount / 60) + " : 0" + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                }
            }

            @Override
            public void onFinish() {
                count.setValue("");
            }
        }.start();
    }
}