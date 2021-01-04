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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckedTextView;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    private static final String TAG = HomeActivity.class.getSimpleName();

    //------------------카카오 로그인용----------------------------
    private SessionCallback sessionCallback;
    private CallbackManager mCallbackManager;
    //------------------카카오 로그인용----------------------------

    //----------------페이스북 로그인용----------------------------
    private LoginCallBack mLoginCallback;
    //----------------페이스북 로그인용---------------------------

    //----------------구글로그인용---------------------------------
    private GoogleSignInClient mSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private static final int RC_GET_TOKEN = 9002;
    //----------------구글로그인용---------------------------------

    //----------------View-----------------------------------------
    private TextView goToJoinBtn;
    private Button signinbtn;
    private ImageButton kakaoLoginBtn;
    private ImageButton facebookLoginBtn;
    private ImageButton googleSignBtn;
    private EditText idEditText, pwEditText;
    private TextView findPassword;
    private CheckedTextView autoLoginBox;
    //----------------View-----------------------------------------

    //----------------viewModel------------------------------------
    private LoginViewModel viewModel;
    //----------------viewModel------------------------------------

    private InputMethodManager imm;
    private boolean isAutoLogin = false;
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        updateUI(account);
        refreshIdToken();


    }

    @Override
    protected void onResume() {
        super.onResume();
//        refreshIdToken();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();

        goToJoinBtn = (TextView) findViewById(R.id.go_to_join_btn);
        signinbtn = (Button) findViewById(R.id.sign_in_btn);
        facebookLoginBtn = (ImageButton) findViewById(R.id.facebook_login_btn);
        kakaoLoginBtn = (ImageButton) findViewById(R.id.kakao_login_btn);
        googleSignBtn = (ImageButton) findViewById(R.id.google_login_btn);
        idEditText = (EditText) findViewById(R.id.id_et);
        pwEditText = (EditText) findViewById(R.id.pw_et);
        findPassword = (TextView) findViewById(R.id.find_password);
        autoLoginBox = (CheckedTextView)findViewById(R.id.chk_auto_login);

        //이전 로그인에서 자동로그인을 체크하지 않았다면
        if(!viewModel.isAutoLogin()){
            //공유프리퍼런스에 있는 유저 아이디를 삭제하여 자동로그인을 해제한다.
            viewModel.removeUserIdPref();
        }else{ //이전 로그인에서 자동로그인을 체크했다면
            //공유프리퍼런스에 있는 유저아이디가 유효한지 확인하여
            //자동로그인을 실행한다.
            String userId =viewModel.getLoginSession();
            if(!userId.equals(" ")){
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
            }
        }
        validateServerClientID();
        //구글 로그인
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mSignInClient = GoogleSignIn.getClient(this, gso);
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//        mSignInClient = GoogleSignIn.getClient(this, gso);

        /*google 로그인*/
        googleSignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIdToken();
            }
        });

        /*facebook 로그인*/
        mCallbackManager = CallbackManager.Factory.create();
        mLoginCallback = new LoginCallBack();
        facebookLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookLogin();
            }
        });

        /*kakaoTalk 로그인*/
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
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
            }
        });
        /*일반로그인 버튼*/
        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(pwEditText.getWindowToken(),0);
                String userId = idEditText.getText().toString().trim();
                String userPw = pwEditText.getText().toString().trim();
                //자동로그인 체크했을 시 공유프리퍼런스에 자동로그인 여부 저장
                viewModel.autoLogin(isAutoLogin);
                viewModel.login(userId, userPw);
            }
        });

        //자동로그인 체크박스 클릭이벤트
        autoLoginBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckedTextView) v).isChecked()){
                    ((CheckedTextView) v).setChecked(false);
                    isAutoLogin = false;

                }else{
                    ((CheckedTextView) v).setChecked(true);
                    isAutoLogin = true;
                }

            }
        });
        viewModel.loginRsLD.observe(this, code -> {
            if (code.equals("200")) {
                Log.d("RESULT", "onCreate: 성공");
                Snackbar.make(findViewById(R.id.home_layout), "로그인 성공", Snackbar.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            } else if (code.equals("204")) {
                Log.d("RESULT", "onCreate: 204 에러");
                Snackbar.make(findViewById(R.id.home_layout), "존재하지 않는 계정입니다.", Snackbar.LENGTH_SHORT).show();
            } else if (code.equals("205")) {
                Log.d("RESULT", "onCreate: 205 에러");
                Snackbar.make(findViewById(R.id.home_layout), "비밀번호가 틀립니다.", Snackbar.LENGTH_SHORT).show();
            } else {
                Log.d("RESULT", "onCreate: 실패");
                Snackbar.make(findViewById(R.id.home_layout), "로그인 실패", Snackbar.LENGTH_SHORT).show();
            }
        });

        findPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
            }
        });


    }

    //구글 로그인 화면을 띄우는 메소드
    private void getIdToken() {
        /*
        사용자가 장치에서 Google 계정을 선택할 수 있도록 계정 선택기를 표시.
        ID 토큰 또는 프로필 또는 이메일만 요청하는 경우 여기에 동의 화면이 표시되지 않는다.
         */
        Intent signInIntent = mSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GET_TOKEN);
    }

    //구글로그인용 ID토큰 리프레쉬하는 메소드
    private void refreshIdToken() {
       /*
        로그인 새로고침, 이미 토큰을 가지고 있을경우 이 메서드는 즉시 완료되며
        이전에 로그인하지 않았거나 로그인이 만료되었을 경우 다시 로그인하고 유효한 토큰을 가져온다.
        */
        mSignInClient.silentSignIn()
                .addOnCompleteListener(this, new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        handleSignInResult(task);
                    }
                });
    }

    //적절한 serverClientID인지 확인하는 메소드(구글)
    private void validateServerClientID() {
        String serverClientId = getString(R.string.server_client_id);
        String suffix = ".apps.googleusercontent.com";
        if (!serverClientId.trim().endsWith(suffix)) {
            String message = "Invalid server client ID in strings.xml, must end with " + suffix;

            Log.w(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }
    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            Log.d(TAG, "handleSignInResult: "+idToken);

            // TODO(developer): send ID Token to server and validate
            viewModel.sendGoogleIdToken(idToken);
            updateUI(account);
        } catch (ApiException e) {
            Log.w(TAG, "handleSignInResult:error", e);
            updateUI(null);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        if (requestCode == RC_GET_TOKEN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

//        if (requestCode == RC_SIGN_IN) {
//            Log.d(TAG, "onActivityResult: resultOk");
//
//            Task<GoogleSignInAccount> task =
//                    GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
//            if (task.isSuccessful()) {
//                Log.d(TAG, "onActivityResult: googleSignIn Success");
//
//                // Sign in succeeded, proceed with account
//                GoogleSignInAccount acct = task.getResult();
//            } else {
//                // Sign in failed, handle failure and update UI
//                // ...
//            }
//        }
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

                    if (result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다: " + errorResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Toast.makeText(getApplicationContext(), "세션이 닫혔습니다. 다시 시도해 주세요: " + errorResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    Log.d("asdasd", "onSuccess: " + result.getNickname());
                    Log.d("asdasd", "onSuccess: " + result.getId());

                    intent.putExtra("name", result.getNickname());
                    intent.putExtra("profile", result.getProfileImagePath());
                    startActivity(intent);
                    finish();
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void facebookLogin() {
        Log.d("execute", "facebookLogin");
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), "페북 로그인 성공", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }

            @Override
            public void onCancel() {
                Log.e("Cancel", "페북 Login 취소");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("Error", "페북 Login 에러");
            }
        });
    }


    private void signInByGoogle() {
        // Launches the sign in flow, the result is returned in onActivityResult
        Intent intent = mSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        try {
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//
//            // Signed in successfully, show authenticated UI.
//            updateUI(account);
//        } catch (ApiException e) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
//        }
//    }

    //Change UI according to user data.
    public void updateUI(GoogleSignInAccount account) {

        if (account != null) {
            Toast.makeText(this, "U Signed In successfully", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            finish();
        } else {
            Toast.makeText(this, "U Didnt signed in", Toast.LENGTH_LONG).show();
        }

    }
}