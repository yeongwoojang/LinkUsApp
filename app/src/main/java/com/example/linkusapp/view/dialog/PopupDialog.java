package com.example.linkusapp.view.dialog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.linkusapp.R;

public class PopupDialog extends Activity {

    private TextView date,studyTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_dialog);
        date = (TextView)findViewById(R.id.txt_date);
        Intent intent = getIntent();
        String mDate = intent.getStringExtra("selectedDate");
        date.setText(mDate);
    }
}