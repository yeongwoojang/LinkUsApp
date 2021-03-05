package com.example.linkusapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.linkusapp.R;
import com.example.linkusapp.model.vo.Chat;
import com.example.linkusapp.view.adapter.ChatAdapter;
import com.example.linkusapp.viewModel.ChatViewModel;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity {

    private EditText edtMsg;
    private Button sendBtn;
    private RecyclerView chatRv;
    private ChatAdapter chatAdapter;
    private ChatViewModel viewModel;
    private String gName;
    private String myNickName;
    private String yourNickName;
    private Socket socket;
    private List<Chat> items = new ArrayList<>();
    {
        try {
            socket = IO.socket("http://ec2-15-164-129-208.ap-northeast-2.compute.amazonaws.com:3000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        edtMsg = (EditText)findViewById(R.id.edt_msg);
        chatRv = (RecyclerView)findViewById(R.id.chat_recyclerview);
        sendBtn = (Button)findViewById(R.id.btn_send_msg);

        Intent intent = getIntent();
        gName = intent.getStringExtra("gName");
        myNickName = intent.getStringExtra("myNickName");
        yourNickName = intent.getStringExtra("yourNickName");
        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        chatAdapter = new ChatAdapter(this,items,myNickName);
        chatRv.setAdapter(chatAdapter);
        chatRv.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));

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

        socket.connect();
        socket.on(Socket.EVENT_CONNECT,onConnect);
        socket.on("connectUser",onNewUser);
        socket.on("sendMessage",onNewMessage);

        JSONObject userObj = new JSONObject();
        try{
            userObj.put("myNickName",myNickName);
            userObj.put("roomName","1");
            socket.emit("connectUser",userObj);
        }catch (JSONException e){
            e.printStackTrace();
        }
//        viewModel.sendMsgResLD.observe(this, s -> {
//            Snackbar.make(findViewById(R.id.chat_layout),"메시지를 전송했습니다.",Snackbar.LENGTH_SHORT).show();
//        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String curTime = format.format(date);

        String msg = edtMsg.getText().toString().trim();
        if(TextUtils.isEmpty(msg)){
            return;
        }
        edtMsg.setText("");
        JSONObject chatObj = new JSONObject();
        try {
            chatObj.put("gName",gName);
            chatObj.put("myNickName",myNickName);
            chatObj.put("yourNickName",yourNickName);
            chatObj.put("msg",msg);
            chatObj.put("msgTime",curTime);
            chatObj.put("roomName","1");

        } catch (JSONException e) {
            e.printStackTrace();
        }
//        viewModel.sendMessage(gName,myNickName,yourNickName,msg,curTime);
        socket.emit("sendMessage",chatObj);
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i("Socket","Connect");
        }
    };

    private Emitter.Listener onNewUser = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int length = args.length;

                    if(length==0){
                        return;
                    }
                    String userName = args[0].toString();
                    try{
                        JSONObject obj = new JSONObject(userName);
                        userName = obj.getString("username");
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject chatobj = (JSONObject) args[0];
                    Log.d("onNewMessage", "run: ");
                    String gName;
                    String msg;
                    String myNickName;
                    String yourNickName;
                    String msgTime;
                    try{
                        gName = chatobj.getString("gName");
                        msg = chatobj.getString("msg");
                        myNickName = chatobj.getString("myNickName");
                        yourNickName = chatobj.getString("yourNickName");
                        msgTime = chatobj.getString("msgTime");

                        Chat chat = new Chat(msg,msgTime,yourNickName,myNickName);
                        items.add(chat);
                        chatAdapter.updateItem(items);
                    }catch (Exception e){
                        return;
                    }
                }
            });
        }
    };

}