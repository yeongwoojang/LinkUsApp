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

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Chat> items = new ArrayList<>();

    public ChatAdapter(Context context, List<Chat> arrayList) {
        this.mContext = context;
        this.items = arrayList;
    }

    public void addItem(Chat item){
        if(items != null){
            items.add(item);
            notifyDataSetChanged();
        }
    }

    //내가 입력한 채팅 뷰홀더
    public class MyChatViewHolder extends RecyclerView.ViewHolder{
        TextView chatText;
        TextView chatTime;
        public MyChatViewHolder(@NonNull View itemView) {
            super(itemView);
            chatText = itemView.findViewById(R.id.my_chat_Text);
            chatTime = itemView.findViewById(R.id.my_chat_Time);
        }
    }

    //상대가 입력한 채팅 뷰홀더
    public class YourChatViewHolder extends RecyclerView.ViewHolder{
        ImageView chatYourImg;
        TextView chatYourName;
        TextView chatText;
        TextView chatTime;
        public YourChatViewHolder(@NonNull View itemView) {
            super(itemView);
            chatYourImg = itemView.findViewById(R.id.chat_You_Image);
            chatYourName = itemView.findViewById(R.id.chat_You_Name);
            chatText = itemView.findViewById(R.id.your_chat_Text);
            chatTime = itemView.findViewById(R.id.your_chat_Time);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == 1){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_my_group,parent,false);
            return new MyChatViewHolder(view);
        }else{
            view = LayoutInflater.from(mContext).inflate(R.layout.item_your_chat,parent,false);
            return new YourChatViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d("Type", holder.getClass().getName());
        if(holder instanceof MyChatViewHolder){
            ((MyChatViewHolder)(holder)).chatText.setText(items.get(position).getScript());
            ((MyChatViewHolder)(holder)).chatTime.setText(items.get(position).getDate_time());
        }else if(holder instanceof YourChatViewHolder){
            ((YourChatViewHolder)(holder)).chatYourImg.setImageResource(R.mipmap.ic_launcher);
            ((YourChatViewHolder)(holder)).chatYourName.setText(items.get(position).getName());
            ((YourChatViewHolder)(holder)).chatText.setText(items.get(position).getScript());
            ((YourChatViewHolder)(holder)).chatTime.setText(items.get(position).getDate_time());

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        if(items.get(position).getName().equals("장영우")){
            viewType = 1;
        }else{
            viewType = 2;
        }
        return viewType;
    }
}
