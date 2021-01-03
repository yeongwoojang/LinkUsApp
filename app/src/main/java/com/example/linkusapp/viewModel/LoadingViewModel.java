package com.example.linkusapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.linkusapp.repository.RetrofitClient;
import com.example.linkusapp.repository.ServiceApi;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadingViewModel extends AndroidViewModel {

    MutableLiveData<String> resultCode = new MutableLiveData<String>();

    private ServiceApi service;

    public LoadingViewModel(@NonNull Application application){
        super(application);
        service  = RetrofitClient.getClient(application.getApplicationContext()).create(ServiceApi.class);
    }
    public void chkUserInfo(String userId){
        service.chkUserInfo(userId).enqueue(new Callback<String>() {
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
}
