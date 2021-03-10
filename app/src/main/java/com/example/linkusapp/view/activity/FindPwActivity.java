package com.example.linkusapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.linkusapp.R;
import com.example.linkusapp.databinding.ActivityForgotPasswordBinding;
import com.example.linkusapp.util.GMailSender;
import com.example.linkusapp.viewModel.LoginViewModel;
import com.google.android.material.snackbar.Snackbar;

public class FindPwActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;

    private InputMethodManager imm;

    private LoginViewModel viewModel;
    private GMailSender gMailSender = new GMailSender("sbtmxhs@gmail.com", "jang7856");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        binding.findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(binding.emailEt.getWindowToken(),0);
                String id = binding.idEt.getText().toString().trim();
                String email = binding.emailEt.getText().toString().trim();
                viewModel.findPw(id,email);
            }
        });

        viewModel.findPwRsLD.observe(this, findData -> {
            if(findData.getCode().equals("404")){
                Snackbar.make(binding.findLayout, "에러가 발생했습니다.", Snackbar.LENGTH_SHORT).show();
            }
            else if(findData.getCode().equals("204")){
                Snackbar.make(binding.findLayout, "계정의 이메일과 일치하지 않습니다.", Snackbar.LENGTH_SHORT).show();
            }
            else{
                Snackbar.make(binding.findLayout, "이메일주소로 비밀번호를 전송했습니다.", Snackbar.LENGTH_SHORT).show();
                viewModel.sendMail(gMailSender,binding.emailEt.getText().toString().trim(),findData.getPassword());
            }
        });
    }
}