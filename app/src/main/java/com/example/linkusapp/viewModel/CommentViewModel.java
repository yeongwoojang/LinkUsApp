package com.example.linkusapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.linkusapp.repository.RetrofitClient;
import com.example.linkusapp.repository.ServiceApi;
import com.example.linkusapp.util.SharedPreference;

public class CommentViewModel extends AndroidViewModel {
    private ServiceApi service;
    private SharedPreference prefs;

    public CommentViewModel(@NonNull Application application) {
        super(application);
        this.service = RetrofitClient.getClient(application).create(ServiceApi.class);
        this.prefs = new SharedPreference(application);
    }



}
