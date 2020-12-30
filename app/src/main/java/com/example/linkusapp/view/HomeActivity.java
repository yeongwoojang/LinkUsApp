package com.example.linkusapp.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linkusapp.R;
import com.example.linkusapp.facebook.LoginCallBack;
import com.example.linkusapp.viewModel.LoginViewModel;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.material.snackbar.Snackbar;
import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

import java.util.Arrays;

public class HomeActivity extends AppCompatActivity {

    private SessionCallback sessionCallback;
    private CallbackManager mCallbackManager;
    private LoginCallBack mLoginCallback;

    private TextView goToJoinBtn;
    private Button signinbtn;
    private ImageButton kakaoLoginBtn;
    private ImageButton facebookLoginBtn;
    private EditText idEditText,pwEditText;
    private TextView findPassword;

    private LoginViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();

        goToJoinBtn = (TextView) findViewById(R.id.go_to_join_btn);
        signinbtn = (Button) findViewById(R.id.sign_in_btn);
        facebookLoginBtn = (ImageButton)findViewById(R.id.facebook_login_btn);
        kakaoLoginBtn = (ImageButton)findViewById(R.id.kakao_login_btn);
        idEditText = (EditText)findViewById(R.id.id_et);
        pwEditText = (EditText)findViewById(R.id.pw_et);
        findPassword = (TextView)findViewById(R.id.find_password);


        /*facebook 로그인*/
        mCallbackManager = CallbackManager.Factory.create();
        mLoginCallback = new LoginCallBack();
        facebookLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookLogin();
            }
        });
        /*카카오톡 로그인*/
        kakaoLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                com.kakao.auth.Session.getCurrentSession().open(AuthType.KAKAO_TALK, HomeActivity.this);
            }
        });
        /*회원가입*/
        goToJoinBtn.setPaintFlags(goToJoinBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        goToJoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), JoinActivity.class));
//                overridePendingTransition(R.anim.right_in,R.anim.left_out);
                finish();
            }
        });
        /*로그인 버튼*/
        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = idEditText.getText().toString().trim();
                String userPw = pwEditText.getText().toString().trim();
                viewModel.login(userId,userPw);
            }
        });
        viewModel.loginRsLD.observe(this,code -> {
            if(code.equals("200")){
                Log.d("RESULT", "onCreate: 성공");
                Snackbar.make(findViewById(R.id.home_layout), "로그인 성공", Snackbar.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }else if(code.equals("204")){
                Log.d("RESULT", "onCreate: 204 에러");
                Snackbar.make(findViewById(R.id.home_layout), "존재하지 않는 계정입니다.", Snackbar.LENGTH_SHORT).show();
            }else if(code.equals("205")){
                Log.d("RESULT", "onCreate: 205 에러");
                Snackbar.make(findViewById(R.id.home_layout), "비밀번호가 틀립니다.", Snackbar.LENGTH_SHORT).show();
            }
            else {
                Log.d("RESULT", "onCreate: 실패");
                Snackbar.make(findViewById(R.id.home_layout), "로그인 실패", Snackbar.LENGTH_SHORT).show();
            }
        });

        findPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ForgotPasswordActivity.class));
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode,data)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    int result = errorResult.getErrorCode();

                    if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"로그인 도중 오류가 발생했습니다: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Toast.makeText(getApplicationContext(),"세션이 닫혔습니다. 다시 시도해 주세요: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    Log.d("asdasd", "onSuccess: "+result.getNickname());
                    Log.d("asdasd", "onSuccess: "+result.getId());

                    intent.putExtra("name", result.getNickname());
                    intent.putExtra("profile", result.getProfileImagePath());
                    startActivity(intent);
                    finish();
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    private void facebookLogin(){
        Log.d("execute","facebookLogin");
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(),"페북 로그인 성공",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }

            @Override
            public void onCancel() {
                Log.e("Cancel","페북 Login 취소");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("Error", "페북 Login 에러");
            }
        });
    }
   /*private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }*/
}