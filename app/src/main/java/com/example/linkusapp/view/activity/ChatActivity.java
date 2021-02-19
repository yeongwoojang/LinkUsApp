package com.example.linkusapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.linkusapp.R;
import com.example.linkusapp.model.vo.Chat;
import com.example.linkusapp.view.adapter.ChatAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView chatRoom;
    private EditText msgContent;
    private Button sendButton;

    private ChatAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatRoom = (RecyclerView)findViewById(R.id.chat_room);
        msgContent = (EditText)findViewById(R.id.chat_content);
        sendButton = (Button)findViewById(R.id.btn_send_msg);

        List<Chat> items = new ArrayList<>();
        adapter = new ChatAdapter(getApplicationContext(),items);
        chatRoom.setAdapter(adapter);
        chatRoom.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        chatRoom.setHasFixedSize(true);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage(){
       long now =  System.currentTimeMillis();
       Date date = new Date(now);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = sdf.format(date);
        Chat item = new Chat("장영우",msgContent.getText().toString(),"example",getTime);
        adapter.addItem(item);

        msgContent.setText("");
    }
}