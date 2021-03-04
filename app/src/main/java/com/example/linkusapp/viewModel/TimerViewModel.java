package com.example.linkusapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.linkusapp.model.vo.TimerInfo;
import com.example.linkusapp.repository.RetrofitClient;
import com.example.linkusapp.repository.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimerViewModel extends BaseViewModel {
//    private ServiceApi service;

    public MutableLiveData<String> insertTimerLD = new MutableLiveData<>();
    public MutableLiveData<String> updateTimerLD = new MutableLiveData<>();
//    public MutableLiveData<TimerInfo> entireRecordLD = new MutableLiveData<>();
    public MutableLiveData<TimerInfo> todayRecordLD = new MutableLiveData<>();

    public TimerViewModel(@NonNull Application application){
        super(application);
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

    public void updateTimer(String userNick, String time){
        service.updateTimer(userNick,time).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                updateTimerLD.postValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

//    public void getEntireRecord(String userNick){
//        service.getEntireRecord(userNick).enqueue(new Callback<TimerInfo>() {
//            @Override
//            public void onResponse(Call<TimerInfo> call, Response<TimerInfo> response) {
//                entireRecordLD.postValue(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<TimerInfo> call, Throwable t) {
//
//            }
//        });
//    }

    public void getTodayRecord(String userNick){
        service.getTodayRecord(userNick).enqueue(new Callback<TimerInfo>() {
            @Override
            public void onResponse(Call<TimerInfo> call, Response<TimerInfo> response) {
                todayRecordLD.postValue(response.body());
            }

            @Override
            public void onFailure(Call<TimerInfo> call, Throwable t) {

            }
        });
    }

}
