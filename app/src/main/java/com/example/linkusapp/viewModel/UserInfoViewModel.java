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

public class UserInfoViewModel extends AndroidViewModel {

    private ServiceApi service;
    public UserInfoViewModel(@NonNull Application application){
        super(application);
        service = RetrofitClient.getClient(application.getApplicationContext()).create(ServiceApi.class);

    }

//    public MutableLiveData<String> addUserInfoResLD = new MutableLiveData<String>();

//    public void saveInfo(String userId,String nickname,String age,String gender,String address){
//        service.saveInfo(userId,nickname,age,gender,address)
//                .enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//                        String result = response.body();
//                        addUserInfoResLD.postValue(result);
//                    }
//
//                    @Override
//                    public void onFailure(Call<String> call, Throwable t) {
//
//                    }
//                });
//    }

}