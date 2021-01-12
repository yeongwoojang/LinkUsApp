package com.example.linkusapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.linkusapp.model.vo.UserInfo;
import com.example.linkusapp.repository.RetrofitClient;
import com.example.linkusapp.repository.ServiceApi;
import com.example.linkusapp.util.SharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateGrpViewModel extends AndroidViewModel {
    private ServiceApi service;
    private SharedPreference prefs;

    public MutableLiveData<UserInfo> userLiveData = new MutableLiveData<UserInfo>();
    public MutableLiveData<String> resultCode = new MutableLiveData<>();

    public CreateGrpViewModel(@NonNull Application application) {
        super(application);
        this.service = RetrofitClient.getClient(application).create(ServiceApi.class);
        this.prefs = new SharedPreference(application);
    }
    public void getUserInfo(){
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
    public void createGroup(String gName, String gExplanation, String gPart, String gPurpose, String gStartDate, String gEndDate, String gJoinMethod){
        service.createGroup(gName,gExplanation,gPart,gPurpose,gStartDate,gEndDate,gJoinMethod,prefs.getLoginMethod()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                resultCode.postValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void test(){
        service.createGroup().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}
