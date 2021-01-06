package com.example.linkusapp.view.activity;

import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.linkusapp.R;


import java.util.Calendar;
import java.util.Date;

public class CreateGroupActivity extends AppCompatActivity {


    ImageButton backBt,freeJoinBt,approvalJoinBt;
    EditText edtGroupName, edtGroupExplanation, edtGroupPurpose;
    Button nextBt, startDateBt, endDateBt, resetPeriodBt, createGroupBt;
    private static int year, month, day;
    private Calendar calendar;
    private  String buttonId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        backBt = (ImageButton) findViewById(R.id.btn_back);
        edtGroupName = (EditText) findViewById(R.id.edt_group_name);
        edtGroupExplanation = (EditText) findViewById(R.id.edt_group_explanation);
        edtGroupPurpose = (EditText) findViewById(R.id.edt_group_purpose);
        nextBt = (Button) findViewById(R.id.btn_create_group);
        startDateBt = (Button) findViewById(R.id.btn_start_date);
        endDateBt = (Button) findViewById(R.id.btn_end_date);
        resetPeriodBt = (Button)findViewById(R.id.btn_reset_period);
        createGroupBt = (Button)findViewById(R.id.btn_create_group);
        freeJoinBt = (ImageButton)findViewById(R.id.btn_free_join);
        approvalJoinBt = (ImageButton)findViewById(R.id.btn_approval_join);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        int color = ContextCompat.getColor(getApplicationContext(), R.color.gray400);
        freeJoinBt.setColorFilter(color);
        approvalJoinBt.setColorFilter(color);


        startDateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickHandler(v);
                buttonId = v.getTag().toString();
            }
        });
        endDateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getTag();
                OnClickHandler(v);
                buttonId = v.getTag().toString();
            }
        });

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

        freeJoinBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int color = ContextCompat.getColor(getApplicationContext(), R.color.red500);
                freeJoinBt.setColorFilter(color);
            }
        });

        approvalJoinBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int color = ContextCompat.getColor(getApplicationContext(), R.color.red500);
                approvalJoinBt.setColorFilter(color);
            }
        });
    }

    public void OnClickHandler(View view) {
        DatePickerDialog dialog = new DatePickerDefaultLight(this, R.style.CustomDatePickerDialog, listener, year, month, day);
        dialog.getDatePicker().setCalendarViewShown(false);
        dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.form_date_picker);
        dialog.show();
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int _year, int monthOfYear, int dayOfMonth) {
            Log.d("what view?", "onDateSet: "+buttonId);
            if(buttonId.equals(startDateBt.getTag().toString())){
                startDateBt.setText(_year+" / "+(monthOfYear+1)+" / "+dayOfMonth);
                startDateBt.setTextColor(getResources().getColor(R.color.white));
                startDateBt.setBackgroundResource(R.drawable.form_button_date_ok);
                Log.d("TIME", "onDateSet: "+(monthOfYear+1));
                startDateBt.setEnabled(false);

                Log.d("TIME", "onDateSet: "+calendar.getTime());
            }else{
                endDateBt.setText(_year+" / "+(monthOfYear+1)+" / "+dayOfMonth);
                endDateBt.setTextColor(getResources().getColor(R.color.white));
                endDateBt.setBackgroundResource(R.drawable.form_button_date_ok);
                endDateBt.setEnabled(false);

            }
            calendar.set(Calendar.YEAR,_year);
            calendar.set(Calendar.MONTH,monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
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
}