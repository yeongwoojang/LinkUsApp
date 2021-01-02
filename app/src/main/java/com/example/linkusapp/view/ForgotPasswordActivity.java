package com.example.linkusapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.linkusapp.R;
import com.example.linkusapp.util.GMailSender;
import com.example.linkusapp.viewModel.LoginViewModel;
import com.google.android.material.snackbar.Snackbar;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText idEt,emailEt;
    private Button findBtn;

    private InputMethodManager imm;

    private LoginViewModel viewModel;
    private GMailSender gMailSender = new GMailSender("sbtmxhs@gmail.com", "jang7856");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        idEt = (EditText) findViewById(R.id.id_et);
        emailEt = (EditText) findViewById(R.id.email_et);
        findBtn = (Button) findViewById(R.id.find_btn);

        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(emailEt.getWindowToken(),0);
                String id = idEt.getText().toString().trim();
                String email = emailEt.getText().toString().trim();
                viewModel.findPw(id,email);
            }
        });

        viewModel.findPwRsLD.observe(this, findData -> {
            if(findData.equals("404")){
                Log.d("RESULT", "onCreate: 실패");
                Snackbar.make(findViewById(R.id.find_layout), "비밀번호 찾기 실패", Snackbar.LENGTH_SHORT).show();
            }
            else if(findData.equals("204")){
                Log.d("RESULT", "onCreate: 204 에러");
                Snackbar.make(findViewById(R.id.find_layout), "계정의 이메일과 일치하지 않습니다.", Snackbar.LENGTH_SHORT).show();
            }
            else{
                Log.d("RESULT", "onCreate: 성공");
                Snackbar.make(findViewById(R.id.find_layout), "이메일주소로 비밀번호를 전송했습니다.", Snackbar.LENGTH_SHORT).show();
                viewModel.sendMail(gMailSender,emailEt.getText().toString().trim(), findData.getPassword());
            }
        });
    }


}