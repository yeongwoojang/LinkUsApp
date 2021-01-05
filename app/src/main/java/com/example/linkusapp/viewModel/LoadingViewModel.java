package com.example.linkusapp.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.linkusapp.repository.RetrofitClient;
import com.example.linkusapp.repository.ServiceApi;
import com.example.linkusapp.util.SharedPreference;


import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadingViewModel extends AndroidViewModel {

    public MutableLiveData<String> resultCode = new MutableLiveData<String>();
    public MutableLiveData<String> userIdLiveData = new MutableLiveData<>();

    private ServiceApi service;
    private SharedPreference prefs;

    public LoadingViewModel(@NonNull Application application){
        super(application);
        service  = RetrofitClient.getClient(application).create(ServiceApi.class);
        prefs = new SharedPreference(application);
    }
    public void chkScdUserInfo(String userId,String loginMethod){
        service.chkScdUserInfo(userId, loginMethod).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                resultCode.postValue(result);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    public String getLoginMethod(){
        return prefs.getLoginMethod();
    }

    public void getLoginSession() {
        String userId = " ";
        Iterator<String> iterator = prefs.getCookies().iterator();
        if (iterator != null) {
           while (iterator.hasNext()) {
                userId = iterator.next();
                userId = userId.split(";")[0].split("=")[1];
                Log.d("SESSION", "getLoginSession: " +userId);
            }
        }
        userIdLiveData.postValue(userId);
    }
}
