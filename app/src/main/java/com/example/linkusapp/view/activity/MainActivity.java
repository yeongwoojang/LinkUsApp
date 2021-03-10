package com.example.linkusapp.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.linkusapp.R;
import com.example.linkusapp.databinding.ActivityMainBinding;
import com.example.linkusapp.model.vo.User;
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
    private ActivityMainBinding binding;

    //viewModel
    private LoginViewModel viewModel;
    private GoogleSignInClient mSignInClient;

    /*fragment*/
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private MainFragment mainFragment = new MainFragment();
    private BoardFragment boardFragment = new BoardFragment();
    private MyPageFragment myPageFragment = new MyPageFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        /*slidingUpPanelLayout*/
//        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.slidingView);
//        slidingUpPanelLayout.setTouchEnabled(false);


//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:/*메인페이지*/
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, mainFragment).commitAllowingStateLoss();
                        break;
                    case R.id.board:/*그룹 페이지*/
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, boardFragment).commitAllowingStateLoss();
                        break;
                    case R.id.mypage:/*마이페이지*/
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, myPageFragment).commitAllowingStateLoss();
                        break;
                }
                return true;
            }
        });
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
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
        viewModel.userLiveData.observe(this, userInfo -> {
            viewModel.putUserInfo(userInfo.getUser());
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, mainFragment).commitAllowingStateLoss();
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
                            Log.d("token", "onComplete: " + token);
                            viewModel.registrationAppToken(token, viewModel.getUserInfoFromShared().getUserNickname());
                        }
                    });
        });
    }
}