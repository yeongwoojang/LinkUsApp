package com.example.linkusapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.linkusapp.model.vo.BoardInfo;
import com.example.linkusapp.repository.RetrofitClient;
import com.example.linkusapp.repository.ServiceApi;
import com.example.linkusapp.util.SharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardViewModel extends AndroidViewModel {
    ServiceApi service;
    private SharedPreference prefs;

    public MutableLiveData<BoardInfo> boardRsLD = new MutableLiveData<BoardInfo>();

    public BoardViewModel(@NonNull Application application) {
        super(application);
        service = RetrofitClient.getClient(application).create(ServiceApi.class);
        prefs = new SharedPreference(application);
    }

    public void getAllBoard(){
        service.getAllBoard().enqueue(new Callback<BoardInfo>() {
            @Override
            public void onResponse(Call<BoardInfo> call, Response<BoardInfo> response) {
                BoardInfo result = response.body();
                boardRsLD.postValue(result);
            }

            @Override
            public void onFailure(Call<BoardInfo> call, Throwable t) {

            }
        });
    }
}