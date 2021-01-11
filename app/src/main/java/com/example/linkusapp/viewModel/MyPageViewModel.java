package com.example.linkusapp.viewModel;

import android.app.Application;
import android.location.Address;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.linkusapp.model.vo.AddressInfo;
import com.example.linkusapp.model.vo.FindPassword;
import com.example.linkusapp.model.vo.UserInfo;
import com.example.linkusapp.repository.RetrofitClient;
import com.example.linkusapp.repository.ServiceApi;
import com.example.linkusapp.util.SharedPreference;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageViewModel extends AndroidViewModel {

    private ServiceApi serviceApi;
    private SharedPreference prefs;
    public MutableLiveData<String> addAddressRsLD = new MutableLiveData<String>();
    public MutableLiveData<String> updateAddressRsLD = new MutableLiveData<String>();
    public MutableLiveData<AddressInfo> userAddressRsLD = new MutableLiveData<AddressInfo>();



    public MyPageViewModel(@NonNull Application application) {
        super(application);
        serviceApi = RetrofitClient.getClient(application).create(ServiceApi.class);
        prefs = new SharedPreference(application);
    }
    public String getNickname() { return prefs.getNickname();}
    public void addAddress(String userNickname,String address){
        serviceApi.addAddress(userNickname,address).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String code = response.body();
                addAddressRsLD.postValue(code);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void updateAddress(String userNickname, String address){
        serviceApi.updateAddress(userNickname,address).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String code =response.body();
                updateAddressRsLD.postValue(code);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    public void userAddress(String userNickname){
        serviceApi.userAddress(userNickname).enqueue(new Callback<AddressInfo>() {
            @Override
            public void onResponse(Call<AddressInfo> call, Response<AddressInfo> response) {
                AddressInfo result = response.body();
                userAddressRsLD.postValue(result);
            }

            @Override
            public void onFailure(Call<AddressInfo> call, Throwable t) {

            }
        });
    }
}
