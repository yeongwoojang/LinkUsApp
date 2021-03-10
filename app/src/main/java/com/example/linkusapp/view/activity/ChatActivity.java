package com.example.linkusapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.linkusapp.R;
import com.example.linkusapp.databinding.ActivityChatBinding;
import com.example.linkusapp.model.vo.Chat;
import com.example.linkusapp.util.SoftKeyboard;
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

import io.reactivex.rxjava3.core.Observable;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity {
    private SoftKeyboard softKeyboard;

    private ActivityChatBinding binding;
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
    protected void onDestroy() {
        super.onDestroy();
//        softKeyboard.unRegisterSoftKeyboardCallback();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        gName = intent.getStringExtra("gName");
        myNickName = intent.getStringExtra("myNickName");
        yourNickName = intent.getStringExtra("yourNickName");
        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        viewModel.getMessageList(gName, myNickName, yourNickName);
        chatAdapter = new ChatAdapter(items, myNickName);
        binding.chatRecyclerview.setAdapter(chatAdapter);
        binding.chatRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

        //액티비티 시작하자마자 키보드 올리기
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        softKeyboard = new SoftKeyboard(binding.chatLayout,imm);
//        softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {
//            @Override
//            public void onSoftKeyboardHide() {
//
//            }
//
//            @Override
//            public void onSoftKeyboardShow() {
//                new Handler(Looper.getMainLooper()).post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d("onSoftKeyboardShow", "run: ");
//                        binding.chatRecyclerview.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                binding.chatRecyclerview.scrollToPosition(chatAdapter.getItemCount()-1);
//                            }
//                        },100);
//                    }
//                });
//            }
//        });
//        imm.showSoftInput(binding.edtMsg, 0);
//        binding.edtMsg.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                binding.edtMsg.requestFocus();
//                imm.showSoftInput(binding.edtMsg, 0);
//            }
//        }, 50);


        socket.connect(); //소켓연결
        //소켓에 이벤트 설정
        socket.on(Socket.EVENT_CONNECT, onConnect);
        socket.on("sendMessage", onNewMessage);


        JSONObject userObj = new JSONObject();
        try {
            userObj.put("gName", gName);
            userObj.put("myNickName", myNickName);
            userObj.put("yourNickName", yourNickName);
            socket.emit("connectUser", userObj); //채팅방 들어가자마자 room에 유저 등록 -> room에 2명 이상이어야 통신 가능하기 때문
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //채팅 내용을 observe하여 recyclerview 업데이트
        viewModel.messageLD.observe(this, chatInfo -> {
            items = chatInfo.getJsonArray();
            chatAdapter.updateItem(items);
            binding.chatRecyclerview.scrollToPosition(chatAdapter.getItemCount()-1);
//            binding.chatRecyclerview.post(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//
//            });

        });

        //메시지 전송 버튼 클릭 이벤트
        binding.btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    //메시지를 보냈을 때 실행될 내용
    private void sendMessage() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String curTime = format.format(date);

        String msg = binding.edtMsg.getText().toString().trim();
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        binding.edtMsg.setText("");
        JSONObject chatObj = new JSONObject();
        try {
            chatObj.put("gName", gName);
            chatObj.put("myNickName", myNickName);
            chatObj.put("yourNickName", yourNickName);
            chatObj.put("msg", msg);
            chatObj.put("msgTime", curTime);
            chatObj.put("roomName", "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("sendMessage", chatObj); //서버에 메시지에대한 정보를 전송
    }

    //소켓에 연결되었을 때의 이벤트
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i("Socket", "Connect");
        }
    };

    //소켓에 메세지를 보냈을 때 실행되는 이벤트
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
                    try {
//                        gName = chatobj.getString("gName");
                        msg = chatobj.getString("msg");
                        myNickName = chatobj.getString("myNickName");
                        yourNickName = chatobj.getString("yourNickName");
                        msgTime = chatobj.getString("msgTime");

                        Chat chat = new Chat(msg, msgTime, yourNickName, myNickName);
                        items.add(chat);
                        chatAdapter.updateItem(items); //리사이 클러뷰를 새로운 채팅내용으로 업데이트
                        binding.chatRecyclerview.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.chatRecyclerview.scrollToPosition(chatAdapter.getItemCount()-1);
                            }
                        });
                    } catch (Exception e) {
                        return;
                    }
                }
            });
        }
    };

}