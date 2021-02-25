package com.example.linkusapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.linkusapp.repository.RetrofitClient;
import com.example.linkusapp.repository.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimerViewModel extends AndroidViewModel {
    private ServiceApi service;

    public MutableLiveData<String> insertTimerLD = new MutableLiveData<>();
    public TimerViewModel(@NonNull Application application){
        super(application);
        service = RetrofitClient.getClient(application).create(ServiceApi.class);
    }

    public void insertTimer(String userNick, String time){
        service.insertTimer(userNick,time).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                insertTimerLD.postValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

}
