package com.example.linkusapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.linkusapp.model.vo.CommentInfo;
import com.example.linkusapp.model.vo.User;
import com.example.linkusapp.model.vo.UserInfo;
import com.example.linkusapp.repository.RetrofitClient;
import com.example.linkusapp.repository.ServiceApi;
import com.example.linkusapp.util.SharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class CommentViewModel extends BaseViewModel {
//    private ServiceApi service;
//    private SharedPreference prefs;

    public MutableLiveData<String> insertCommentRsLD = new MutableLiveData<String>();
    public MutableLiveData<CommentInfo> getCommentRsLD = new MutableLiveData<CommentInfo>();
    public MutableLiveData<UserInfo> userNickNameRsLD = new MutableLiveData<UserInfo>();

    public CommentViewModel(@NonNull Application application) {
        super(application);
//        this.service = RetrofitClient.getClient(application).create(ServiceApi.class);
//        this.prefs = new SharedPreference(application);
    }
    /*닉네임 불러오는 메소드*/
    public User getUserInfoFromShared(){
        return prefs.getUserInfo();
    }

    public void insertComment(String bName, String bWriter, String bComment, boolean bSecret){
        service.insertComment(bName,bWriter,bComment,bSecret).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String code = response.body();
                insertCommentRsLD.postValue(code);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void getComment(String bName){
        service.getComment(bName).enqueue(new Callback<CommentInfo>() {
            @Override
            public void onResponse(Call<CommentInfo> call, Response<CommentInfo> response) {
                CommentInfo result = response.body();
                getCommentRsLD.postValue(result);
            }

            @Override
            public void onFailure(Call<CommentInfo> call, Throwable t) {

            }
        });
    }

    public void chkReader(String userNickName){
        service.chkReader(userNickName).enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                UserInfo result = response.body();
                userNickNameRsLD.postValue(result);
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {

            }
        });
    }
}