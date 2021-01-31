package com.example.linkusapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.example.linkusapp.R;
import com.example.linkusapp.viewModel.CommentViewModel;
import com.google.android.material.snackbar.Snackbar;

public class CommentActivity extends AppCompatActivity{

    private TextView writerTv;
    private EditText commentEt;
    private Button comfirm,cancel;
    private CommentViewModel viewModel;
    private CheckedTextView checkedSecret;
    private String bName;
    private boolean isSecret = false;
    private String writer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_comment);
        Intent intent = getIntent();
        bName = intent.getExtras().getString("groupName");

        writerTv = (TextView) findViewById(R.id.writer_tv);
        commentEt = (EditText) findViewById(R.id.comment_et);
        comfirm = (Button) findViewById(R.id.complete_btn);
        cancel = (Button) findViewById(R.id.cancel_btn);
//        checkedSecret = (CheckedTextView) findViewById(R.id.chk_secret_write);
        viewModel = new ViewModelProvider(this).get(CommentViewModel.class);
        writer = viewModel.getUserInfoFromShared().getUserNickname();
        writerTv.setText(writer);
        /*checkedSecret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSecret = checkedSecret.isChecked();
                Log.d("왜 안돼 시발","그니까");
            }
        });*/
        /*확인버튼*/
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = commentEt.getText().toString();
                viewModel.insertComment(bName,writer,comment,isSecret);
                finish();
            }
        });
        /*취소버튼*/
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        viewModel.insertCommentRsLD.observe(this,code ->{
            if(code.equals("200")){
                Snackbar.make(findViewById(R.id.comment_activity),"댓글 등록 완료",Snackbar.LENGTH_SHORT).show();
            }else{
                Snackbar.make(findViewById(R.id.comment_activity),"댓글 등록 실패",Snackbar.LENGTH_SHORT).show();
            }
        } );
    }
}