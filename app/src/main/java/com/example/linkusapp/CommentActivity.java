package com.example.linkusapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CommentActivity extends Activity {

    private TextView comment;
    private Button comfirm,cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_comment);

//        comment = (TextView)findViewById(R.id.comment_tv);
        comfirm = (Button)findViewById(R.id.complete_btn);
        cancel = (Button)findViewById(R.id.cancel_btn);

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d("dfasdf", "onClick: ");
                finish();
            }
        });
    }
}