package com.example.linkusapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.linkusapp.model.vo.User;
import com.example.linkusapp.model.vo.UsersInfo;
import com.example.linkusapp.repository.RetrofitClient;
import com.example.linkusapp.repository.ServiceApi;
import com.example.linkusapp.util.SharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseViewModel extends AndroidViewModel {
    protected ServiceApi service;
    protected SharedPreference prefs;
    public MutableLiveData<UsersInfo> groupMembersLD = new MutableLiveData<>();
    public MutableLiveData<String> joinGroupRes = new MutableLiveData<>();

    public BaseViewModel(@NonNull Application application) {
        super(application);
        service = RetrofitClient.getClient(application).create(ServiceApi.class);
        prefs = new SharedPreference(application);
    }

    public void getGroupMember(String gName){
        service.getGroupMember(gName).enqueue(new Callback<UsersInfo>() {
            @Override
            public void onResponse(Call<UsersInfo> call, Response<UsersInfo> response) {
                UsersInfo result = response.body();
                groupMembersLD.postValue(result);
            }

            @Override
            public void onFailure(Call<UsersInfo> call, Throwable t) {

            }
        });
    }

    public void joinGroup(String gName, String gMemberId, String gMemberNick){
        service.joinGroup(gName,gMemberId,gMemberNick).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                joinGroupRes.postValue(result);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public User getUserInfoFromShared(){
        return prefs.getUserInfo();
    }
}
