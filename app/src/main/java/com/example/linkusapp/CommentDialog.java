package com.example.linkusapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.linkusapp.viewModel.CommentViewModel;
import java.util.Objects;

public class CommentDialog extends Dialog {
    private Context context;
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
        setContentView(R.layout.comment_dialog);

        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        commentEt = (EditText) findViewById(R.id.comment_et);
        comfirm = (Button) findViewById(R.id.complete_btn);
        cancel = (Button) findViewById(R.id.cancel_btn);
        checkedSecret = (CheckedTextView) findViewById(R.id.chk_secret_write);

        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(CommentViewModel.class);
       /* writer = viewModel.getUserInfoFromShared().getUserNickname();
        writerTv.setText(writer);*/
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
                /*String comment = commentEt.getText().toString();
                viewModel.insertComment(bName,writer,comment,isSecret);*/
                dismiss();
            }
        });
        /*취소버튼*/
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        /*viewModel.insertCommentRsLD.observe(this,code ->{
            if(code.equals("200")){
                Snackbar.make(findViewById(R.id.comment_activity),"댓글 등록 완료",Snackbar.LENGTH_SHORT).show();
            }else{
                Snackbar.make(findViewById(R.id.comment_activity),"댓글 등록 실패",Snackbar.LENGTH_SHORT).show();
            }
        } );*/
    }

    public CommentDialog(@NonNull Context context) {
        super(context);
        this.context=context;
    }
}
