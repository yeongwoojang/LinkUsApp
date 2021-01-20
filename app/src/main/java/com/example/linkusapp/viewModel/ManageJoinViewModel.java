package com.example.linkusapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.linkusapp.model.vo.LeaderGroupInfo;
import com.example.linkusapp.model.vo.User;
import com.example.linkusapp.repository.RetrofitClient;
import com.example.linkusapp.repository.ServiceApi;
import com.example.linkusapp.util.SharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageJoinViewModel extends AndroidViewModel {
    private ServiceApi service;
    private SharedPreference prefs;

    public MutableLiveData<LeaderGroupInfo> leaderGroupRes = new MutableLiveData<>();
    public ManageJoinViewModel(@NonNull Application application) {
        super(application);
        service = RetrofitClient.getClient(application).create(ServiceApi.class);
        prefs= new SharedPreference(application);
    }

    public User getUserInfoFromShared(){
        return prefs.getUserInfo();
    }

    public void getLeaderGroup(String userNick){
        service.getLeaderGroup(userNick).enqueue(new Callback<LeaderGroupInfo>() {
            @Override
            public void onResponse(Call<LeaderGroupInfo> call, Response<LeaderGroupInfo> response) {
                LeaderGroupInfo result = response.body();
                leaderGroupRes.postValue(result);
            }

            @Override
            public void onFailure(Call<LeaderGroupInfo> call, Throwable t) {

            }
        });
    }
}
