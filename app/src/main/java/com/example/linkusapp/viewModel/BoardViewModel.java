package com.example.linkusapp.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.linkusapp.model.vo.Board;
import com.example.linkusapp.model.vo.BoardInfo;
import com.example.linkusapp.model.vo.User;
import com.example.linkusapp.model.vo.UsersInfo;
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
    public MutableLiveData<BoardInfo> boardPartRsLD = new MutableLiveData<BoardInfo>();
    public MutableLiveData<BoardInfo> boardSearchRsLD = new MutableLiveData<BoardInfo>();
    public MutableLiveData<BoardInfo> boardAddressRsLD = new MutableLiveData<BoardInfo>();
    public MutableLiveData<BoardInfo> boardConditionRsLD = new MutableLiveData<BoardInfo>();
    public MutableLiveData<BoardInfo> userGroupRsLD = new MutableLiveData<BoardInfo>();
    public MutableLiveData<BoardInfo> allAddressRsLD = new MutableLiveData<BoardInfo>();
    public MutableLiveData<BoardInfo> optionBoardRsLD = new MutableLiveData<BoardInfo>();
    public MutableLiveData<String> updateSelectedLD = new MutableLiveData<>();
    public MutableLiveData<BoardInfo> selectedGroupLD = new MutableLiveData<>();
    public MutableLiveData<UsersInfo> groupMembersLD = new MutableLiveData<>();


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

    public void getPartBoard(String gPart){
        service.getPartBoard(gPart).enqueue(new Callback<BoardInfo>() {
            @Override
            public void onResponse(Call<BoardInfo> call, Response<BoardInfo> response) {
                BoardInfo result = response.body();
                Log.d("onResponse: ",result.toString());
                boardPartRsLD.postValue(result);
            }
            @Override
            public void onFailure(Call<BoardInfo> call, Throwable t) {

            }
        });
    }

    public void getSearchBoard(String keyword){
        service.getSearchBoard(keyword).enqueue(new Callback<BoardInfo>(){

            @Override
            public void onResponse(Call<BoardInfo> call, Response<BoardInfo> response) {
                BoardInfo result = response.body();
                boardSearchRsLD.postValue(result);
            }

            @Override
            public void onFailure(Call<BoardInfo> call, Throwable t) {

            }
        });
    }

    public void userBoardAll(String userNickname){
        service.userBoardAll(userNickname).enqueue(new Callback<BoardInfo>() {
            @Override
            public void onResponse(Call<BoardInfo> call, Response<BoardInfo> response) {
                BoardInfo result = response.body();
                userGroupRsLD.postValue(result);
            }

            @Override
            public void onFailure(Call<BoardInfo> call, Throwable t) {

            }
        });
    }
    public void optionBoard(String gPart,String address){
        service.optionBoard(gPart,address).enqueue(new Callback<BoardInfo>() {
            @Override
            public void onResponse(Call<BoardInfo> call, Response<BoardInfo> response) {
                BoardInfo result = response.body();
                optionBoardRsLD.postValue(result);
            }

            @Override
            public void onFailure(Call<BoardInfo> call, Throwable t) {

            }
        });
    }

    public void updateSelected(String userNick, String gName){
        service.updateSelected(userNick,gName).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                updateSelectedLD.postValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void  getSelectedGroup(String userNick){
        service.getSelectedGroup(userNick).enqueue(new Callback<BoardInfo>() {
            @Override
            public void onResponse(Call<BoardInfo> call, Response<BoardInfo> response) {
               BoardInfo result = response.body();
                selectedGroupLD.postValue(result);
            }

            @Override
            public void onFailure(Call<BoardInfo> call, Throwable t) {

            }
        });
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

    public User getUserInfoFromShared(){
        return prefs.getUserInfo();
    }
}