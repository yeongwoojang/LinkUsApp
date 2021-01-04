package com.example.linkusapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.linkusapp.R;
import com.example.linkusapp.viewModel.PartAdapter;

import java.util.ArrayList;

public class BoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();
            }
        });

        findViewById(R.id.write_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), WriteActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();
            }
        });

        ArrayList<String> list = new ArrayList<>();
        String[] part = {"전체", "어학", "교양", "프로그래밍", "취업","취미", "자율", "기타"};
        for (int i=0; i<part.length; i++) {
            list.add(part[i]);
        }

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.part_recyclerview) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)) ;

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        PartAdapter adapter = new PartAdapter(list) ;
        recyclerView.setAdapter(adapter);


        // 분야 아이템 클릭시 event
        adapter.setOnItemClickListener(new PartAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                    if(position == 0){
                        Toast.makeText(getApplicationContext(),"전체",Toast.LENGTH_SHORT).show();
                    }else if(position == 1){
                        Toast.makeText(getApplicationContext(),"어학",Toast.LENGTH_SHORT).show();
                    }else if(position == 2){
                        Toast.makeText(getApplicationContext(),"교양",Toast.LENGTH_SHORT).show();
                    }else if(position == 3){
                        Toast.makeText(getApplicationContext(),"프로그래밍",Toast.LENGTH_SHORT).show();
                    }else if(position == 4){
                        Toast.makeText(getApplicationContext(),"취업",Toast.LENGTH_SHORT).show();
                    }else if(position == 5){
                        Toast.makeText(getApplicationContext(),"취미",Toast.LENGTH_SHORT).show();
                    }else if(position == 6) {
                        Toast.makeText(getApplicationContext(), "자율", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"기타",Toast.LENGTH_SHORT).show();
                    }
            }
        });


    }
}