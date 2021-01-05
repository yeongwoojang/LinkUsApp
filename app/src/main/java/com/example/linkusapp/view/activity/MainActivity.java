package com.example.linkusapp.view;

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
import com.example.linkusapp.viewModel.LoginViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
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
