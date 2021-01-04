package com.example.linkusapp.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
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
import com.example.linkusapp.viewModel.LoginViewModel;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
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
import com.kakao.sdk.auth.LoginClient;
import com.kakao.sdk.user.UserApiClient;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    //------------------카카오 로그인용----------------------------
//    private SessionCallback sessionCallback;

    //------------------카카오 로그인용----------------------------

    //----------------페이스북 로그인용----------------------------
    private CallbackManager mCallbackManager;
    //----------------페이스북 로그인용---------------------------
    private String fbToken;
    boolean isFacebookLogged;
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
    private Context mContext;

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");

        refreshIdToken();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        mContext = this;
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);


        goToJoinBtn = (TextView) findViewById(R.id.go_to_join_btn);
        signinbtn = (Button) findViewById(R.id.sign_in_btn);
        facebookLoginBtn = (ImageButton) findViewById(R.id.facebook_login_btn);
        kakaoLoginBtn = (ImageButton) findViewById(R.id.kakao_login_btn);
        googleSignBtn = (ImageButton) findViewById(R.id.google_login_btn);
        idEditText = (EditText) findViewById(R.id.id_et);
        pwEditText = (EditText) findViewById(R.id.pw_et);
        findPassword = (TextView) findViewById(R.id.find_password);
        autoLoginBox = (CheckedTextView) findViewById(R.id.chk_auto_login);

        //일반 자동로그인 코드
        generalAutoLogin();

        //페이스북 자동로그인 코드
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        isFacebookLogged = accessToken != null && !accessToken.isExpired();
        if (isFacebookLogged) {
            viewModel.putLoginMethod("facebook");
            startActivity(new Intent(getApplicationContext(), AddUserInfoActivity.class));
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            finish();
        }

        //카카오톡 자동로그인 코드
        UserApiClient.getInstance().me((user, meError) -> {
            if (meError != null) {
                Log.e(TAG, "사용자 정보 요청 실패", meError);
            } else if (user != null) {
                if (user.getKakaoAccount().getEmail() != null) {
                    startActivity(new Intent(HomeActivity.this, AddUserInfoActivity.class));
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    finish();
                }
            }
            return null;
        });


        validateServerClientID();
        //구글 로그인
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mSignInClient = GoogleSignIn.getClient(this, gso);

        /*google 로그인*/
        googleSignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInByGoogle();
            }
        });

        /*facebook 로그인*/
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                fbToken = accessToken.getToken();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    if(object.has("email")){
                                        Log.d("facebook email", object.toString());
                                        viewModel.putSocialLogin(object.getString("last_name")+object.getString("first_name")
                                                ,object.getString("email"),"Facebook");
                                        viewModel.putLoginMethod("facebook");
                                        startActivity(new Intent(getApplicationContext(), AddUserInfoActivity.class));
                                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                        finish();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                //파라매터 번들을 추가해서 필요한 요소들 받아오기
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,email");
                request.setParameters(parameters);
                request.executeAsync();
                Log.d("facebook Token", fbToken);

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
                // 카카오톡으로 로그인
                LoginClient.getInstance().loginWithKakaoAccount(mContext, (oAuthToken, throwable) -> {
                    if (throwable != null) {
                        Log.e(TAG, "로그인 실패", throwable);
                    } else {
                        Log.d(TAG, "로그인 성공");
                        // 사용자 정보 요청
                        UserApiClient.getInstance().me((user, meError) -> {
                            if (meError != null) {
                                Log.e(TAG, "사용자 정보 요청 실패", meError);
                            } else if (user != null) {
                                if (user.getKakaoAccount().getEmail() != null) {
                                    viewModel.putSocialLogin(user.getKakaoAccount().getProfile().getNickname(), user.getKakaoAccount().getEmail(),"Kakao");
                                    Log.i(TAG, "이메일 : " + user.getKakaoAccount().getEmail() + " , " + user.getKakaoAccount().getProfile().getNickname());
                                    viewModel.putLoginMethod("kakao");
                                    startActivity(new Intent(HomeActivity.this, AddUserInfoActivity.class));
                                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                    finish();
                                } else if (!user.getKakaoAccount().getEmailNeedsAgreement()) {
                                    Log.e(TAG, "사용자 계정에 이메일 없음. 꼭 필요하다면 동의항목 설정에서 수집 기능을 활성화 해보세요.");
                                } else if (user.getKakaoAccount().getEmailNeedsAgreement()) {
                                    Log.d(TAG, "사용자에게 이메일 제공 동의를 받아야 합니다.");
                                    List<String> scopes = new ArrayList<String>();
                                    scopes.add("account_email");
                                    Log.d("test", "사용자 정보 받아옴 ");
                                    LoginClient.getInstance().loginWithNewScopes(mContext, scopes, (oAuthToken1, throwable1) -> {
                                        if (throwable1 != null) {
                                            Log.e(TAG, "이메일 제공 동의 실패", throwable1);
                                        } else {
                                            Log.d("test", "요청 ");
                                        }
                                        return null;
                                    });
                                }
                            }
                            return null;
                        });
                    }
                    return null;
                });
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
                imm.hideSoftInputFromWindow(pwEditText.getWindowToken(), 0);
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
                if (((CheckedTextView) v).isChecked()) {
                    ((CheckedTextView) v).setChecked(false);
                    isAutoLogin = false;

                } else {
                    ((CheckedTextView) v).setChecked(true);
                    isAutoLogin = true;
                }

            }
        });

        //일반 로그인 요청시 응답이 오면 실행될 코드
        viewModel.loginRsLD.observe(this, code -> {
            if (code.equals("200")) {
                Snackbar.make(findViewById(R.id.home_layout), "로그인 성공", Snackbar.LENGTH_SHORT).show();
                viewModel.putLoginMethod("general");
                startActivity(new Intent(getApplicationContext(), AddUserInfoActivity.class));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
            } else if (code.equals("204")) {
                Snackbar.make(findViewById(R.id.home_layout), "존재하지 않는 계정입니다.", Snackbar.LENGTH_SHORT).show();
            } else if (code.equals("205")) {
                Snackbar.make(findViewById(R.id.home_layout), "비밀번호가 틀립니다.", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(findViewById(R.id.home_layout), "로그인 실패", Snackbar.LENGTH_SHORT).show();
            }
        });

        //비밀번호 찾기 텍스트 클릭 시 화면 이동
        findPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
    }

    //구글로그인용 ID토큰 리프레쉬하는 메소드
    private void refreshIdToken() {
       /*
        로그인 새로고침, 이미 토큰을 가지고 있을경우 이 메서드는 즉시 완료되며
        이전에 로그인하지 않았거나 로그인이 만료되었을 경우 다시 로그인하고 유효한 토큰을 가져온다.
        */
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            mSignInClient.silentSignIn()
                    .addOnCompleteListener(this, new OnCompleteListener<GoogleSignInAccount>() {
                        @Override
                        public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                            handleSignInResult(task);
                        }
                    });
        }

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
            Log.d(TAG, "handleSignInResult: "+"ASdasdsad");
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            viewModel.sendGoogleIdToken(idToken);
            updateUI(account);
        } catch (ApiException e) {
            updateUI(null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult: Kakao?");
        if (requestCode == RC_GET_TOKEN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Session.getCurrentSession().removeCallback(sessionCallback);
    }


    private void facebookLogin() {
        Log.d("execute", "facebookLogin");
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"));

    }


    private void signInByGoogle() {
        // Launches the sign in flow, the result is returned in onActivityResult
           /*
        사용자가 장치에서 Google 계정을 선택할 수 있도록 계정 선택기를 표시.
        ID 토큰 또는 프로필 또는 이메일만 요청하는 경우 여기에 동의 화면이 표시되지 않는다.
         */
        Intent signInIntent = mSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GET_TOKEN);
    }

    //Change UI according to user data.
    public void updateUI(GoogleSignInAccount account) {

        if (account != null) {
            Toast.makeText(this, "U Signed In successfully", Toast.LENGTH_LONG).show();
            viewModel.putLoginMethod("google");
            startActivity(new Intent(getApplicationContext(), AddUserInfoActivity.class));
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            finish();
        } else {
            Toast.makeText(this, "U Didnt signed in", Toast.LENGTH_LONG).show();
        }

    }
    public void generalAutoLogin(){
        //이전 로그인에서 자동로그인을 체크하지 않았다면
        if (!viewModel.isAutoLogin() && viewModel.getLoginMethod().equals("general")) {
            //공유프리퍼런스에 있는 유저 아이디를 삭제하여 자동로그인을 해제한다.
            viewModel.removeUserIdPref();
        } else if(viewModel.isAutoLogin() && viewModel.getLoginMethod().equals("general")){
            //이전 로그인에서 자동로그인을 체크했다면
            //공유프리퍼런스에 있는 유저아이디가 유효한지 확인하여
            //자동로그인을 실행한다.
            String userId = viewModel.getLoginSession();
            if (!userId.equals(" ")) {
                viewModel.putLoginMethod("general");
                startActivity(new Intent(getApplicationContext(), AddUserInfoActivity.class));
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
            }
        }
    }
}