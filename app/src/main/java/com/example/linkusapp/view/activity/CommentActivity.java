package com.example.linkusapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.linkusapp.R;

import org.w3c.dom.Text;

public class CommentActivity extends Activity {

    private TextView writer;
    private EditText commentEt;
    private Button comfirm,cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_comment);

        writer = (TextView)findViewById(R.id.writer_tv);
        commentEt = (EditText)findViewById(R.id.comment_et);
        comfirm = (Button)findViewById(R.id.complete_btn);
        cancel = (Button)findViewById(R.id.cancel_btn);
        /*확인버튼*/
        comfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String comment = commentEt.getText().toString();
                /*서버통신*/
            }
        });
        /*취소버튼*/
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d("dfasdf", "onClick: ");
                finish();
            }
        });
    }
}