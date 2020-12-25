package com.example.linkusapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import com.example.linkusapp.R;
import com.example.linkusapp.viewModel.JoinViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity {

    private EditText idEdt;
    private EditText pwEdt;
    private EditText pwEdt2;
    private EditText nameEdt;
    private EditText emailEdt;
    private EditText certificationEdt;
    private EditText birthEdt;
    private RadioGroup genderRadio;

    private Button idCheckBtn;
    private Button certificationBtn;
    private Button certificationCheckBtn;
    private Button joinBtn;
    private String gender = "M";

    private JoinViewModel viewModel;

    protected InputFilter filterAlphaNum = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");

            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };

    // 한글만 허용
    public InputFilter filterKor = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[ㄱ-ㅎ가-힣]+$");

            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        viewModel =  new ViewModelProvider(this).get(JoinViewModel.class);

        idEdt = (EditText)findViewById(R.id.join_id);
        pwEdt = (EditText)findViewById(R.id.join_password);
        pwEdt2 = (EditText)findViewById(R.id.join_password_check);
        nameEdt = (EditText)findViewById(R.id.join_name);
        emailEdt = (EditText)findViewById(R.id.join_email);
        certificationEdt = (EditText)findViewById(R.id.join_certification);
        birthEdt = (EditText)findViewById(R.id.join_birth);
        genderRadio = (RadioGroup)findViewById(R.id.join_radio_group);

        idCheckBtn = (Button)findViewById(R.id.join_id_check_btn);
        certificationBtn = (Button)findViewById(R.id.join_certification_btn);
        certificationCheckBtn = (Button)findViewById(R.id.join_certification_check_btn);
        joinBtn = (Button)findViewById(R.id.join_btn);


        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = idEdt.getText().toString().trim();
                String userPw = pwEdt.getText().toString().trim();
                String userPw2 = pwEdt2.getText().toString().trim();
                String userName = nameEdt.getText().toString().trim();
                String userEmail = emailEdt.getText().toString().trim();
                String certification = certificationEdt.getText().toString().trim();
                String userBirth = birthEdt.getText().toString().trim();

                if(!userId.equals("")
                        && userId.length()>=6
                        && !userPw.equals("")
                        && !userPw2.equals("")
                        && !userName.equals("")
                        && !userEmail.equals("")
                        && !certification.equals("")
                        && !userBirth.equals("")){
                    if(userId.matches("^[a-zA-Z0-9]+$")
                    && userName.matches("^[ㄱ-ㅎ가-힣]+$")
                    && userPw.equals(userPw2)){
                        viewModel.join(userName,userId,userPw,userEmail,userBirth,gender);
                    }
                }else{
                    Snackbar.make(findViewById(R.id.join_layout),"정보를 정확히 입력하세요.",Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.joinRsLD.observe(this,code ->{
            if(code.equals("200")){
                Log.d("RESULT", "onCreate: 성공");
                Snackbar.make(findViewById(R.id.join_layout),"회원가입 성공",Snackbar.LENGTH_SHORT).show();
            }else{
                Log.d("RESULT", "onCreate: 실패");
                Snackbar.make(findViewById(R.id.join_layout),"회원가입 실패",Snackbar.LENGTH_SHORT).show();

            }
        });

        genderRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.join_gender_man) {
                    gender = "M";
                }else {
                    gender = "W";
                }
            }
        });

    }
}