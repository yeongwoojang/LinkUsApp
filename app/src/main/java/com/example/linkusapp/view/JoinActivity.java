package com.example.linkusapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import com.example.linkusapp.R;
import com.example.linkusapp.viewModel.JoinViewModel;
import com.google.android.material.snackbar.Snackbar;

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
    private Button joinIdCheckBtn;
    private String gender = "M";

    private JoinViewModel viewModel;

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
        joinIdCheckBtn = (Button)findViewById(R.id.join_id_check_btn);
        joinBtn = (Button)findViewById(R.id.join_btn);


        joinIdCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = idEdt.getText().toString().trim();
                if(!userId.equals("") && userId.matches("^[a-zA-Z0-9]+$")&& userId.length()>=6){
                    viewModel.idChk(userId);
                }else{
                    idEdt.setText(" ");
                    Snackbar.make(findViewById(R.id.join_layout),"올바른 아이디를 입력해주세요",Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.idChkResLD.observe(this,code -> {
            if(code.equals("200")){
                Snackbar.make(findViewById(R.id.join_layout),"사용가능한 아이디입니다.",Snackbar.LENGTH_SHORT).show();
            }else{
                Snackbar.make(findViewById(R.id.join_layout),"이미 사용중인 아이디입니다.",Snackbar.LENGTH_SHORT).show();
            }
        });


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

                if(userId.equals("")
                        && userPw.equals("")
                        && userPw2.equals("")
                        && userName.equals("")
                        && userEmail.equals("")
                        && certification.equals("")
                        && userBirth.equals("")){
                    Snackbar.make(findViewById(R.id.join_layout),"정보를 정확히 입력하세요.",Snackbar.LENGTH_SHORT).show();
                }else if(userId.length()<6){
                    Snackbar.make(findViewById(R.id.join_layout),"아이디는 6자리 이상입니다.",Snackbar.LENGTH_SHORT).show();
                }else if(!userId.matches("^[a-zA-Z0-9]+$")){
                    Snackbar.make(findViewById(R.id.join_layout),"아이디는 영문과 숫자만 가능합니다.",Snackbar.LENGTH_SHORT).show();
                }else if(userPw.length()<8){
                    Snackbar.make(findViewById(R.id.join_layout),"비밀번호는 8자리 이상입니다.",Snackbar.LENGTH_SHORT).show();
                }else if(!userPw.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&]).{8,15}.$")){
                    Snackbar.make(findViewById(R.id.join_layout),"비밀번호는 영문,특수문자,숫자 조합입니다.",Snackbar.LENGTH_SHORT).show();
                }else if(!userPw.equals(userPw2)){
                    Snackbar.make(findViewById(R.id.join_layout),"비밀번호가 일치하지 않습니다.",Snackbar.LENGTH_SHORT).show();
                }else if(!userName.matches("^[ㄱ-ㅎ가-힣]+$")){
                    Snackbar.make(findViewById(R.id.join_layout),"부정확한 이름입니다.",Snackbar.LENGTH_SHORT).show();
                }else{
                    viewModel.join(userName,userId,userPw,userEmail,userBirth,gender);
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