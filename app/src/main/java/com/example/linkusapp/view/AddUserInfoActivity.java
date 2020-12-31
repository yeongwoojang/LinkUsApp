package com.example.linkusapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Button;

import com.example.linkusapp.R;

public class AddUserInfoActivity extends AppCompatActivity {

    private Button nextBtn,preBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_info);

        nextBtn = (Button)findViewById(R.id.next_btn);
        preBtn = (Button)findViewById(R.id.previous_btn);

        nextBtn.setPaintFlags(nextBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        preBtn.setPaintFlags(preBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


    }
}