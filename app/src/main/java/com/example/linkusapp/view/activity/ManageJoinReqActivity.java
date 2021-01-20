package com.example.linkusapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.linkusapp.R;
import com.example.linkusapp.view.adapter.MyGroupAdapter;

public class ManageJoinReqActivity extends AppCompatActivity {

    private RecyclerView myGroupRecyclerView;
    private MyGroupAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_join_req);

        myGroupRecyclerView = (RecyclerView)findViewById(R.id.my_group_recyclerview);
        myGroupRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
        adapter = new MyGroupAdapter();
        myGroupRecyclerView.setAdapter(adapter);


    }
}