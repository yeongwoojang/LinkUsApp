package com.example.linkusapp.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.linkusapp.R;
import com.example.linkusapp.viewModel.LoginViewModel;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.auth.ISessionCallback;


public class AddUserInfoActivity extends AppCompatActivity {

    private ImageButton preBtn;
    private Button saveBtn;
    private EditText nicknameEt,searchAddressEt;
    private Button checkNicknameBtn,searchAddressBtn;
    private Spinner setAge;
    private String age;
    private RadioGroup genderRadio;
    private String gender = "M";
    private String userNickname;
    private String currentId;

    private long backKeyPressed = 0;
    private Toast backBtClickToast;
    private GoogleSignInClient mSignInClient;
    private LoginViewModel viewModel;
    private static final  int SEARCH_ADDRESS_ACTIVITY = 10000;

    private GoogleSignInClient googleSignInClient;
    private CallbackManager callbackManager;
    private ISessionCallback sessionCallback;

    /*닉네임중복확인 유무*/
    boolean isCertify = false;
    private static final String TAG = AddUserInfoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_info);

        saveBtn = (Button)findViewById(R.id.save_btn);
        preBtn = (ImageButton)findViewById(R.id.previous_btn);
        nicknameEt = (EditText)findViewById(R.id.set_nickname);
        checkNicknameBtn = (Button)findViewById(R.id.nickname_check_btn);
        genderRadio = (RadioGroup)findViewById(R.id.join_radio_group);
        searchAddressEt = (EditText)findViewById(R.id.search_address_et);
        searchAddressBtn =(Button) findViewById(R.id.search_address_btn);
        setAge= (Spinner) findViewById(R.id.spinner_age);

        saveBtn.setPaintFlags(saveBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        currentId = viewModel.getLoginSession();


        Log.d("loginSession", "onCreate: "+viewModel.getLoginSession());
        //어떤 방시으로 로그인 된 계정인지 체크
        String loginMethod = viewModel.getLoginMethod();
        Log.d("asdasda", "onCreate: "+loginMethod);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mSignInClient =  GoogleSignIn.getClient(this, gso);

        /*나이*/
        setAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                if(position != 0){
                    age = parent.getItemAtPosition(position).toString();
                    Snackbar.make(findViewById(R.id.add_user_info), age, Snackbar.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Snackbar.make(findViewById(R.id.add_user_info), "나이를 선택해주세요.", Snackbar.LENGTH_SHORT).show();
            }
        });
        /*닉네임 체크*/
        checkNicknameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNickname = nicknameEt.getText().toString().trim();
                if (!userNickname.equals("") && userNickname.length() <= 10) {
                    viewModel.nickNameChk(userNickname);
                    isCertify = true;
                } else {
                    nicknameEt.setText(" ");
                    Snackbar.make(findViewById(R.id.add_user_info), "조건에 맞는 닉네임을 입력해주세요", Snackbar.LENGTH_SHORT).show();
                    isCertify = false;
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
                Intent intent = new Intent(getApplicationContext(), SearchAddress.class);
                startActivityForResult(intent,SEARCH_ADDRESS_ACTIVITY);
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
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = searchAddressEt.getText().toString();
                if(!isCertify){
                    Snackbar.make(findViewById(R.id.add_user_info), "닉네임 중복검사 실시해주세요.", Snackbar.LENGTH_SHORT).show();
                }
                else if (searchAddressEt.getText().toString().trim().equals("")){
                    Snackbar.make(findViewById(R.id.add_user_info), "주소 검색 실시해주세요.", Snackbar.LENGTH_SHORT).show();
                }
                else{
                    viewModel.saveInfo(currentId,userNickname,age,gender,address,loginMethod);
                    Snackbar.make(findViewById(R.id.add_user_info), "회원님의 정보가 저장되었습니다.", Snackbar.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        });
        viewModel.addUserInfoResLD.observe(this, code -> {
            if(code.equals("200")){
                Log.d("RESULT", "onCreate: 성공");
                Snackbar.make(findViewById(R.id.add_user_info), "정보 저장 성공", Snackbar.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();
            }
            else{
                Log.d("RESULT", "onCreate: 실패");
                Snackbar.make(findViewById(R.id.add_user_info), "정보저장 실패", Snackbar.LENGTH_SHORT).show();
            }
        });
        genderRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.gender_man) {
                    gender = "M";
                } else {
                    gender = "W";
                }
            }
        });
        /*이전 버튼*/
        preBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (loginMethod){
                    case  "일반" :{
                        //일반 로그아웃
                        viewModel.cancelAutoLogin();
                        break;
                    }
                    case "Google" :{
                        //구글 로그아웃
                        googleSignOut();
                        break;
                    }
                    case "Facebook" :{
                        //facebook 로그아웃
                        LoginManager.getInstance().logOut();
                        break;
                    }
                    case "Kakao" :{
                        UserApiClient.getInstance().logout(error ->{
                            if(error !=null){
                                Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error);
                            }else{
                                Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨");
                            }
                            return null;
                        });
                        break;
                    }
                }
                viewModel.removeUserIdPref();
//                viewModel.removeLoginMethod();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();
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

    //로그아웃하기
    private void googleSignOut() {
        mSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
//                updateUI(null);
            }
        });
    }
}