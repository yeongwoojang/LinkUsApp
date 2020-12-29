package com.example.linkusapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.linkusapp.R;
import com.example.linkusapp.viewModel.LoginViewModel;
import com.google.android.material.snackbar.Snackbar;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText idEt,emailEt;
    private Button findBtn;

    private LoginViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        idEt = (EditText) findViewById(R.id.id_et);
        emailEt = (EditText) findViewById(R.id.email_et);
        findBtn = (Button) findViewById(R.id.find_btn);

        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = idEt.getText().toString().trim();
                String email = emailEt.getText().toString().trim();
                viewModel.findPw(id,email);
            }
        });
        viewModel.findPwRsLD.observe(this, code -> {
            if(code.equals("200")){
                Log.d("RESULT", "onCreate: 성공");
                /*Snackbar.make(findViewById(R.id.find_layout), "비밀번호 찾기 성공", Snackbar.LENGTH_SHORT).show();*/
                Toast.makeText(getApplicationContext(), "비밀번호 찾기 성공", Toast.LENGTH_SHORT).show();
            }
            else if(code.equals("204")){
                Log.d("RESULT", "onCreate: 204 에러");
                /*Snackbar.make(findViewById(R.id.find_layout), "존재하지 않는 계정입니다.", Snackbar.LENGTH_SHORT).show();*/
                Toast.makeText(getApplicationContext(), "존재하지 않는 계정", Toast.LENGTH_SHORT).show();
            }
            else{
                Log.d("RESULT", "onCreate: 실패");
//                Snackbar.make(findViewById(R.id.find_layout), "비밀번호 찾기 실패", Snackbar.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "비밀번호 찾기 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }


}