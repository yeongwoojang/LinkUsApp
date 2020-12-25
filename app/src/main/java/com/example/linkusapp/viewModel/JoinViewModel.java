package com.example.linkusapp.viewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.linkusapp.repository.RetrofitClient;
import com.example.linkusapp.repository.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinViewModel extends ViewModel {

    private ServiceApi service =
            RetrofitClient.getClient().create(ServiceApi.class);

    public MutableLiveData<String> joinRsLD = new MutableLiveData<String>();

    public void join(String userName,String userId,String password, String email, String birth,String gender){
        service.join(userName,userId,password,email,birth,gender)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String result = response.body();
                        Log.d(result, "onResponse: ");
                        joinRsLD.postValue(result);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        //통신오류
                    }
                });
    }
}
