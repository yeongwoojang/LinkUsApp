package com.example.linkusapp.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkusapp.R;
import com.example.linkusapp.model.vo.User;
import com.example.linkusapp.view.activity.ChatActivity;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder>implements View.OnClickListener {
    private Context mContext;
    private List<User> items;
    private String myNickname;
    public MemberAdapter(Context context, List<User> items, String myNickname) {
        this.mContext = context;
        this.items = items;
        this.myNickname = myNickname;
    }

    public void updateItem(List<User> items){
        this.items = items;
        notifyDataSetChanged();
    }
    public class MemberViewHolder extends RecyclerView.ViewHolder{
        private TextView txtUserName;
        private ImageButton btnSendMsg;
        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.txt_username);
            btnSendMsg = itemView.findViewById(R.id.btn_send_msg);
        }
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member,parent,false);
        return new MemberAdapter.MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        User user = items.get(position);
        holder.txtUserName.setText(user.getUserNickname());
        holder.btnSendMsg.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onClick(View v) {
        mContext.startActivity(new Intent(mContext, ChatActivity.class));
    }
}
