package com.example.linkusapp.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkusapp.R;
import com.example.linkusapp.model.vo.Chat;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {

    private List<Chat> items;
    private Context mContext;
    private String myNickName;
    public ChatAdapter(Context context, List<Chat> items,String myNickName) {
        Log.d("adapter", "ChatAdapter: ");
        this.mContext = context;
        this.items = items;
        this.myNickName = myNickName;
    }

    public void updateItem(List<Chat> items){
        Log.d("updateItem", "updateItem: ");
        this.items = items;
        notifyDataSetChanged();
    }

    public class MyChatViewHolder extends RecyclerView.ViewHolder{
        private TextView txtMsg, txtTime;
        public MyChatViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMsg = itemView.findViewById(R.id.txt_msg);
            txtTime = itemView.findViewById(R.id.txt_time);
        }
    }

    public class YourChatViewHolder extends RecyclerView.ViewHolder{
        private TextView txtYourName,txtYourMsg, txtYourTime;
        private ImageView youtProfile;
        public YourChatViewHolder(@NonNull View itemView) {
            super(itemView);
            txtYourName = itemView.findViewById(R.id.txt_your_name);
            txtYourMsg = itemView.findViewById(R.id.txt_your_msg);
            txtYourTime = itemView.findViewById(R.id.txt_your_time);
            youtProfile = itemView.findViewById(R.id.img_profile);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_chat_room,parent,false);
            return new MyChatViewHolder(view);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_your_chat_room,parent,false);
            return new YourChatViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.i("onBindViewHolder","onBindViewHolder");
            if(holder instanceof MyChatViewHolder){
                Chat chat = items.get(position);
                ((MyChatViewHolder)(holder)).txtMsg.setText(chat.getMsg());
                ((MyChatViewHolder)(holder)).txtTime.setText(chat.getMsgTime());
            }else{
                Chat chat = items.get(position);
                ((YourChatViewHolder)(holder)).youtProfile.setImageResource(R.mipmap.app_icon);
                ((YourChatViewHolder)(holder)).txtYourName.setText(chat.getMsgFrom());
                ((YourChatViewHolder)(holder)).txtYourMsg.setText(chat.getMsg());
                ((YourChatViewHolder)(holder)).txtYourTime.setText(chat.getMsgTime());
            }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("Asdfsdafasf", items.get(position).getMsgFrom()+" , "+myNickName);
        if(items.get(position).getMsgFrom().equals(myNickName)){
            return 1;
        }else{
            return 2;
        }
    }
}
