package com.example.linkusapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.linkusapp.R;

public class ChatActivity extends AppCompatActivity {

    private EditText edtMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        edtMsg = (EditText)findViewById(R.id.edt_msg);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //액티비티 시작하자마자 키보드 올리기
        imm.showSoftInput(edtMsg,0);
        edtMsg.postDelayed(new Runnable() {
            @Override
            public void run() {
                edtMsg.requestFocus();
                imm.showSoftInput(edtMsg,0);
            }
        },100);
    }
}