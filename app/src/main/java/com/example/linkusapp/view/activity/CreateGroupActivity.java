package com.example.linkusapp.view.activity;

import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.linkusapp.R;
import com.example.linkusapp.viewModel.CreateGrpViewModel;
import com.google.android.material.snackbar.Snackbar;


import java.util.Calendar;

public class CreateGroupActivity extends AppCompatActivity {


    ImageButton backBt, freeJoinBt, approvalJoinBt;
    EditText edtGroupName, edtGroupExplanation, edtGroupPurpose, edtMemberLimit;
    Button nextBt, startDateBt, endDateBt, resetPeriodBt, createGroupBt, gNameChkBt;
    TextView addressText;
    Spinner partSpinner;
    private static int year, month, day;
    private Calendar calendar;
    private String buttonId;
    private String joinTag = null;
    private String part = "어학";
    private boolean gNameChkFlg = false;
    private String gName = "";
    //viewModel
    CreateGrpViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        //뷰 초기화
        backBt = (ImageButton) findViewById(R.id.btn_back);
        edtGroupName = (EditText) findViewById(R.id.edt_group_name);
        edtGroupExplanation = (EditText) findViewById(R.id.edt_group_explanation);
        edtGroupPurpose = (EditText) findViewById(R.id.edt_group_purpose);
        edtMemberLimit = (EditText) findViewById(R.id.edt_member_limit);
        nextBt = (Button) findViewById(R.id.btn_create_group);
        startDateBt = (Button) findViewById(R.id.btn_start_date);
        endDateBt = (Button) findViewById(R.id.btn_end_date);
        resetPeriodBt = (Button) findViewById(R.id.btn_reset_period);
        createGroupBt = (Button) findViewById(R.id.btn_create_group);
        gNameChkBt = (Button) findViewById(R.id.btn_g_name_chk);
        freeJoinBt = (ImageButton) findViewById(R.id.btn_free_join);
        approvalJoinBt = (ImageButton) findViewById(R.id.btn_approval_join);
        addressText = (TextView) findViewById(R.id.study_address);
        partSpinner = (Spinner) findViewById(R.id.spinner_part);

        //viewModel 초기화
        viewModel = new ViewModelProvider(this).get(CreateGrpViewModel.class);
        //유저정보 호출
        viewModel.getUserInfo();
        viewModel.userLiveData.observe(this, userInfo -> {
            if (userInfo.getUser() != null) {
                addressText.setText(userInfo.getUser().getAddress());
            }
        });
        //오늘 날짜 초기화
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        //가입체크버튼 초기색상 gray400으로 설정
        int color = ContextCompat.getColor(getApplicationContext(), R.color.gray400);
        freeJoinBt.setColorFilter(color);
        approvalJoinBt.setColorFilter(color);

        gNameChkBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gName = edtGroupName.getText().toString().trim();
                if (!validChk(gName)) {
                    Snackbar.make(findViewById(R.id.create_group_page), "올바르지 않은 그룹명입니다.", Snackbar.LENGTH_SHORT).show();
                } else {
                    viewModel.chkGname(gName);
                }
            }
        });

        viewModel.chkGnameRes.observe(this, response -> {
            if (response.equals("200")) {
                Snackbar.make(findViewById(R.id.create_group_page), "사용가능한 그룹명입니다.", Snackbar.LENGTH_SHORT).show();
                gNameChkFlg = true;
            } else {
                Snackbar.make(findViewById(R.id.create_group_page), "이미 존재하는 그룹명입니다.", Snackbar.LENGTH_SHORT).show();
                gName = "";
                gNameChkFlg = false;
            }
        });
        partSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                if (position != 0) {
                    part = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Snackbar.make(findViewById(R.id.create_group_page), "분야를 선택해주세요", Snackbar.LENGTH_SHORT).show();
            }
        });

        //스터디활동 시작일 클릭
        startDateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickHandler(v);
                buttonId = v.getTag().toString();
            }
        });
        //스터디활동 종료일 클릭
        endDateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getTag();
                OnClickHandler(v);
                buttonId = v.getTag().toString();
            }
        });
        //스터디 할동기간 초기화
        resetPeriodBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                startDateBt.setEnabled(true);
                startDateBt.setBackgroundResource(R.drawable.form_button_date_no);
                startDateBt.setText("시작일");
                startDateBt.setTextColor(getResources().getColor(R.color.gray400));
                endDateBt.setEnabled(true);
                endDateBt.setBackgroundResource(R.drawable.form_button_date_no);
                endDateBt.setText("종료일");
                endDateBt.setTextColor(getResources().getColor(R.color.gray400));
            }
        });
        //스터디 가입방법(자유)버튼클릭
        freeJoinBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!freeJoinBt.getTag().toString().equals(joinTag)) {
                    int color = ContextCompat.getColor(getApplicationContext(), R.color.red500);
                    freeJoinBt.setColorFilter(color);
                    color = ContextCompat.getColor(getApplicationContext(), R.color.gray400);
                    approvalJoinBt.setColorFilter(color);
                    joinTag = freeJoinBt.getTag().toString();
                } else {
                    int color = ContextCompat.getColor(getApplicationContext(), R.color.gray400);
                    freeJoinBt.setColorFilter(color);
                    joinTag = null;
                }
            }
        });
        //스터디 가입방법(승인)버튼클릭
        approvalJoinBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!approvalJoinBt.getTag().toString().equals(joinTag)) {
                    int color = ContextCompat.getColor(getApplicationContext(), R.color.red500);
                    approvalJoinBt.setColorFilter(color);
                    color = ContextCompat.getColor(getApplicationContext(), R.color.gray400);
                    freeJoinBt.setColorFilter(color);
                    joinTag = approvalJoinBt.getTag().toString();
                } else {
                    int color = ContextCompat.getColor(getApplicationContext(), R.color.gray400);
                    approvalJoinBt.setColorFilter(color);
                    joinTag = null;
                }

            }
        });
        //그룹생성 버튼 클릭
        createGroupBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gNameChkFlg) {
                    String groupName = edtGroupName.getText().toString().trim();
                    if (!gName.equals(groupName)) {
                        gName = "";
                        Snackbar.make(findViewById(R.id.create_group_page), "그룹명 중복확인을 해주세요.", Snackbar.LENGTH_SHORT).show();
                        gNameChkFlg = false;
                    } else {
                        String groupExplanation = edtGroupExplanation.getText().toString().trim();
                        String groupPurpose = edtGroupPurpose.getText().toString().trim();
                        String memberLimit = edtMemberLimit.getText().toString().trim();
                        String startDate = startDateBt.getText().toString().trim();
                        String endDate = endDateBt.getText().toString().trim();
                        String joinMethod = joinTag;
                        if (startDate.matches("^[가-힣]+$") || endDate.matches("^[가-힣]+$")) {
                            startDate = "미정";
                            endDate = "미정";
                        }
                        //부정확한 그룹명인지 아닌지 체크
                        if (!validChk(gName)) {
                            Snackbar.make(findViewById(R.id.create_group_page), "올바르지 않은 그룹명입니다.", Snackbar.LENGTH_SHORT).show();
                        } else if (Integer.parseInt(memberLimit) < 2 || Integer.parseInt(memberLimit) > 20) {
                            Snackbar.make(findViewById(R.id.create_group_page), "인원 제한이 올바르지 않습니다.", Snackbar.LENGTH_SHORT).show();

                        } else {
                            viewModel.createGroup(gName, groupExplanation, part, groupPurpose, memberLimit, startDate, endDate, joinMethod);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            overridePendingTransition(R.anim.left_in, R.anim.right_out);
                            finish();
                        }
                    }
                } else {
                    Snackbar.make(findViewById(R.id.create_group_page), "그룹명 중복확인을 해주세요.", Snackbar.LENGTH_SHORT).show();

                }


            }
        });

        viewModel.createGroupRes.observe(this, code -> {
            if (code.equals("200")) {
                Snackbar.make(findViewById(R.id.create_group_page), "그룹생성완료", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(findViewById(R.id.create_group_page), "그룹생성 실패", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    //DatePickerDialog 생성코드
    public void OnClickHandler(View view) {
        DatePickerDialog dialog = new DatePickerDefaultLight(this, R.style.CustomDatePickerDialog, listener, year, month, day);
        dialog.getDatePicker().setCalendarViewShown(false);
        dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.form_date_picker);
        dialog.show();
    }

    //DatePickerDialog에서 날짜 선택하고 확인버튼 눌렀을 때 이벤트 설정
    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int _year, int monthOfYear, int dayOfMonth) {
            if (buttonId.equals(startDateBt.getTag().toString())) {
                startDateBt.setText(_year + " / " + (monthOfYear + 1) + " / " + dayOfMonth);
                startDateBt.setTextColor(getResources().getColor(R.color.white));
                startDateBt.setBackgroundResource(R.drawable.form_button_date_ok);
                startDateBt.setEnabled(false);

            } else {
                endDateBt.setText(_year + " / " + (monthOfYear + 1) + " / " + dayOfMonth);
                endDateBt.setTextColor(getResources().getColor(R.color.white));
                endDateBt.setBackgroundResource(R.drawable.form_button_date_ok);
                endDateBt.setEnabled(false);

            }
            calendar.set(Calendar.YEAR, _year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            view.setMinDate(calendar.getTimeInMillis());

        }
    };

    public static class DatePickerDefaultLight extends DatePickerDialog {
        DatePickerDialog datePickerDialog;

        public DatePickerDefaultLight(Context context, @StyleRes int themeResId,
                                      DatePickerDialog.OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth) {
            super(context, themeResId, listener, year, monthOfYear, dayOfMonth);
            this.datePickerDialog = new DatePickerDialog(context, themeResId, listener, year, monthOfYear, dayOfMonth);

        }
    }

    private boolean validChk(String gName){
        boolean result = false;
        if (!gName.matches("^[a-zA-Z0-9가-힣]+$")){
            result = false;
        }else{
            result = true;
        }
        return result;
    }
}