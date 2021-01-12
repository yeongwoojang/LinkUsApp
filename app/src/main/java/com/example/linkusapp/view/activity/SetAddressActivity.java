package com.example.linkusapp.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.linkusapp.R;
import com.example.linkusapp.model.vo.UserAddress;
import com.example.linkusapp.util.GpsTracker;
import com.example.linkusapp.view.adapter.AddressAdapter;
import com.example.linkusapp.viewModel.MyPageViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SetAddressActivity extends AppCompatActivity {

    private GpsTracker gpsTracker;
    private EditText addressET;
    private Button currentBtn,correctBtn;
    private MyPageViewModel viewModel;
    private String nickname;
    private RecyclerView recentAddressView;
    private List<UserAddress> addressList = new ArrayList<>();
    /*주소 검색*/
    private static final  int SEARCH_ADDRESS_ACTIVITY = 10000;

    /*현위치 검색*/
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_address);

        addressET = (EditText) findViewById(R.id.address_et);
        currentBtn = (Button) findViewById(R.id.current_address_Btn);
        correctBtn = (Button) findViewById(R.id.correct_btn);
        recentAddressView = (RecyclerView) findViewById(R.id.recent_address_rv);
        viewModel = new ViewModelProvider(this).get(MyPageViewModel.class);
        Intent intent = getIntent();
        nickname = intent.getExtras().get("nickname").toString();

        /*툴바 뒤로가기 버튼*/
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();
            }
        });
        /*주소 검색*/
        addressET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddressActivity.class);
                startActivityForResult(intent,SEARCH_ADDRESS_ACTIVITY);
            }
        });
        /*현위치로 주소 설정*/
        if(!checkLocationServicesStatus()){
            showDialogForLocationServiceSetting();
        }else{
            checkRunTimePermission();
        }
        currentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gpsTracker = new GpsTracker(SetAddressActivity.this);

                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();

                String address = getCurrentAddress(latitude,longitude);
                String[] arrAddress = address.split(" ");
                if(arrAddress[0].equals("대한민국")){
                    arrAddress[0] = "경기";
                }
                addressET.setText(arrAddress[0]+','+arrAddress[1]);
            }
        });
        /*최근 주소*/
        AddressAdapter addressAdapter = new AddressAdapter(addressList,this, viewModel,nickname);
        recentAddressView.setAdapter(addressAdapter);
        recentAddressView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        viewModel.userAddress(viewModel.getNickname());

        viewModel.userAddressRsLD.observe(this,addressInfo -> {
            if (addressInfo.getCode().equals("200")){
                addressAdapter.updateItem(addressInfo.getJsonArray());
            }else{
                Snackbar.make(findViewById(R.id.set_address_layout), "스터디 그룹이 존재하지 않습니다.", Snackbar.LENGTH_SHORT).show();
            }
        });
        /*주소 변경*/
        correctBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newAddress = addressET.getText().toString();
                if(newAddress.trim().equals("")){
                    Snackbar.make(findViewById(R.id.set_address_layout), "주소 변경 사항이 없습니다.", Snackbar.LENGTH_SHORT).show();
                }else{
//                   주소 db수정, 주소db에 추가
                    viewModel.addAddress(nickname,newAddress);
                    viewModel.updateAddress(nickname,newAddress);
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    overridePendingTransition(R.anim.left_in, R.anim.right_out);
                    finish();
                }
            }
        });
        viewModel.updateAddressRsLD.observe(this, code -> {
            if(code.equals("200")){
                Log.d("RESULT", "onCreate: 성공");
                Snackbar.make(findViewById(R.id.set_address_layout), "주소 수정 성공.", Snackbar.LENGTH_SHORT).show();
            }else{
                Log.d("RESULT", "onCreate: 실패");
                Snackbar.make(findViewById(R.id.set_address_layout), "에러가 발생했습니다.", Snackbar.LENGTH_SHORT).show();
            }
        });
        viewModel.addAddressRsLD.observe(this, code -> {
            if(code.equals("200")){
                Log.d("RESULT", "onCreate: 성공");
                Snackbar.make(findViewById(R.id.set_address_layout), "주소 추가 성공.", Snackbar.LENGTH_SHORT).show();
            }else{
                Log.d("RESULT", "onCreate: 실패");
                Snackbar.make(findViewById(R.id.set_address_layout), "에러가 발생했습니다.", Snackbar.LENGTH_SHORT).show();
            }
        });

    }
    /*ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드*/
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {

                //위치 값을 가져올 수 있음
                ;
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(SetAddressActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                }else {

                    Toast.makeText(SetAddressActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }
    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(SetAddressActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(SetAddressActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음



        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(SetAddressActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(SetAddressActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(SetAddressActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(SetAddressActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }


    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }



        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";

    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SetAddressActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }
                break;
            case SEARCH_ADDRESS_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    String address = data.getExtras().getString("data");
                    if (data != null) {
                        Log.d("주소 데이터",address);
                        addressET.setText(address);
                    }
                }
                break;
        }
    }

    /*gps켰는지 검사 함수*/
    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}