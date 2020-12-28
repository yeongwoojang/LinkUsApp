package com.example.linkusapp.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.linkusapp.repository.RetrofitClient;
import com.example.linkusapp.repository.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {
    String test =null;

    private ServiceApi service =
            RetrofitClient.getClient().create(ServiceApi.class);

    public MutableLiveData<String> loginRsLD = new MutableLiveData<String>();

    public void login(String userId, String password){
        service.login(userId, password).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                loginRsLD.postValue(result);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // 통신오류
            }
        });
    }
}