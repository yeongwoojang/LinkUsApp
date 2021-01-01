package com.example.linkusapp.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.linkusapp.R;
import com.example.linkusapp.SearchAddress;
import com.example.linkusapp.viewModel.JoinViewModel;
import com.facebook.login.LoginManager;
import com.google.android.material.snackbar.Snackbar;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

public class AddUserInfoActivity extends AppCompatActivity {

    private Button nextBtn,preBtn;
    private EditText nickname,searchAddressEt;
    private Button checkNicknameBtn,searchAddressBtn;
    private long backKeyPressed = 0;
    private Toast backBtClickToast;
    private JoinViewModel viewModel;
    private RadioGroup genderRadio;
    private String gender = "M";
    private static final  int SEARCH_ADDRESS_ACTIVITY = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_info);

        nextBtn = (Button)findViewById(R.id.save_btn);
        preBtn = (Button)findViewById(R.id.previous_btn);
        nickname = (EditText)findViewById(R.id.set_nickname);
        checkNicknameBtn = (Button)findViewById(R.id.nickname_check_btn);
        genderRadio = (RadioGroup)findViewById(R.id.join_radio_group);
        searchAddressEt = (EditText)findViewById(R.id.search_address_et);
        searchAddressBtn =(Button) findViewById(R.id.search_address_btn);

        nextBtn.setPaintFlags(nextBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        preBtn.setPaintFlags(preBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        viewModel = new ViewModelProvider(this).get(JoinViewModel.class);

        /*닉네임 체크*/
        checkNicknameBtn.setOnClickListener(new View.OnClickListener() {
            String userNickname;
            @Override
            public void onClick(View view) {
                userNickname = nickname.getText().toString().trim();
                if (!userNickname.equals("") && userNickname.length() <= 10) {
                    viewModel.nickNameChk(userNickname);
                } else {
                    nickname.setText(" ");
                    Snackbar.make(findViewById(R.id.add_user_info), "조건에 맞는 닉네임을 입력해주세요", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        viewModel.nickChkResLD.observe(this, code -> {
            if (code.equals("200")) {
                Snackbar.make(findViewById(R.id.add_user_info), "사용가능한 닉네임입니다.", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(findViewById(R.id.add_user_info), "이미 사용중인 닉네임입니다.", Snackbar.LENGTH_SHORT).show();
            }
        });
        /*내지역 설정*/
        searchAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        searchAddressEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchAddress.class);
                startActivityForResult(intent,SEARCH_ADDRESS_ACTIVITY);
            }
        });
        /*2차 회원 정보 저장버튼*/
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        /*이전 버튼*/
        preBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case SEARCH_ADDRESS_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    String data = intent.getExtras().getString("data");
                    if (data != null) {
                        Log.d("주소 데이터",data);
                        searchAddressEt.setText(data);
                    }
                }
                break;
        }
    }

    /*로그아웃,이전버튼*/
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressed + 2000) {
            backKeyPressed = System.currentTimeMillis();
            backBtClickToast = Toast.makeText(this, "\'뒤로가기\' 버튼을 한번 더 누르시면 로그아웃 됩니다.", Toast.LENGTH_SHORT);
            backBtClickToast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressed + 2000) {
            UserManagement.getInstance()
                    .requestLogout(new LogoutResponseCallback() {
                        @Override
                        public void onCompleteLogout() {
                            Toast.makeText(AddUserInfoActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
            LoginManager.getInstance().logOut();
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
            finish();
            backBtClickToast.cancel();
        } else {
            super.onBackPressed();
        }
    }
}