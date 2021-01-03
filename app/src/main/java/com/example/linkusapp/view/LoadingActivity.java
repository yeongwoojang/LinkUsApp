package com.example.linkusapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.linkusapp.R;
import com.example.linkusapp.viewModel.LoadingViewModel;

public class LoadingActivity extends AppCompatActivity {

    LoadingViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        viewModel = new ViewModelProvider(this).get(LoadingViewModel.class);

    }
}