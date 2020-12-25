package com.example.linkusapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.linkusapp.R;

public class HomeActivity extends AppCompatActivity {

    private TextView goToJoinBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        goToJoinBt = (TextView)findViewById(R.id.go_to_join_bt);
        goToJoinBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),JoinActivity.class));
            }
        });
    }
}