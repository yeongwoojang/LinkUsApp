package com.example.linkusapp.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import com.example.linkusapp.R;
import com.example.linkusapp.view.fragment.BoardFragment;
import com.example.linkusapp.view.fragment.MainFragment;
import com.example.linkusapp.view.fragment.MyPageFragment;
import com.example.linkusapp.viewModel.LoginViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    //viewModel
    private LoginViewModel viewModel;
    private GoogleSignInClient mSignInClient;

    private Button logOut;
    private TabLayout tabLayout;
    private SlidingUpPanelLayout slidingUpPanelLayout;

    /*fragment*/
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private MainFragment mainFragment = new MainFragment();
    private BoardFragment boardFragment = new BoardFragment();
    private MyPageFragment myPageFragment = new MyPageFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*slidingUpPanelLayout*/
        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.slidingView);
        slidingUpPanelLayout.setTouchEnabled(false);

        /*fragment*/
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,mainFragment).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,mainFragment).commitAllowingStateLoss();
                        break;
                    case R.id.board:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,boardFragment).commitAllowingStateLoss();
                        break;
                    case R.id.mypage:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,myPageFragment).commitAllowingStateLoss();
                        break;
                }
                return true;
            }
        });

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        String loginMethod = viewModel.getLoginMethod();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            Log.d(TAG, "onCreate: " + personName + ", " + personEmail);
        }
        //DB에서 읽은 로그인한 유저 정보를 간편하게 사용하기 위해 SharedPreference에 저장
        viewModel.getUserInfoFromDB();
        viewModel.userLiveData.observe(this,userInfo -> {
            viewModel.putUserInfo(userInfo.getUser());
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w("firebase", "Fetching FCM registration token failed", task.getException());
                                return;
                            }
                            // Get new FCM registration token
                            String token = task.getResult();
                            Log.d("token", "onComplete: "+token);
                            viewModel.registrationAppToken(token,viewModel.getUserInfoFromShared().getUserNickname());
                        }
                    });
        });

        /*로그아웃 버튼*//*
        logOut = (Button) findViewById(R.id.logout);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loginMethod.equals("일반")){
                    viewModel.cancelAutoLogin();
                }else if(loginMethod.equals("Google")){
                    googleSignOut();
                }else if(loginMethod.equals("Facebook")){
                    viewModel.removeUserIdPref();
                    LoginManager.getInstance().logOut();

                }else{
                    UserApiClient.getInstance().logout(error -> {
                        if (error != null) {
                            Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error);

                        } else {
                            viewModel.removeUserIdPref();
                            Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨");
                        }
                        return null;
                    });
                }

//                viewModel.removeLoginMethod();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();
            }
        });*/

        /*findViewById(R.id.go_to_board).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BoardActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();
            }
        });*/
    }

    /*//로그아웃하기
    private void googleSignOut() {
        mSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
//                updateUI(null);
                viewModel.removeUserIdPref();
            }
        });
    }
    //구글로그인와 앱과의 연결 끊기
    private void revokeAccess() {
        mSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        updateUI(null);
                    }
                });
    }*/
}
