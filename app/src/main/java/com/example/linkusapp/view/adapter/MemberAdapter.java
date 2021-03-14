package com.example.linkusapp.view.adapter;

import android.app.Activity;
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
import com.example.linkusapp.databinding.ItemMemberBinding;
import com.example.linkusapp.databinding.ItemSelectStudyBinding;
import com.example.linkusapp.model.vo.User;
import com.example.linkusapp.view.activity.ChatActivity;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder>{

    private MemberAdapter thisObject = this;

    private Context mContext;
    private List<User> items;
    private String myNickname;
    private String gName;
    public MemberAdapter(Context context, String gName,List<User> items, String myNickname) {
        this.mContext = context;
        this.gName = gName;
        this.items = items;
        this.myNickname = myNickname;
    }

    public void updateItem(List<User> items){
        this.items = items;
        notifyDataSetChanged();
    }
    public class MemberViewHolder extends RecyclerView.ViewHolder{
        private ItemMemberBinding binding;

        public MemberViewHolder(ItemMemberBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.setAdapter(thisObject);
        }

        void bind(User user, int position){
            binding.setUser(user);
            binding.setPosition(position);
        }
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMemberBinding binding = ItemMemberBinding.inflate(inflater, parent, false);
        return new MemberAdapter.MemberViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        User user = items.get(position);
        holder.bind(user,position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void sendBtnClickEvent(int position){
        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.putExtra("gName",gName);
        intent.putExtra("myNickName",myNickname);
        intent.putExtra("yourNickName",items.get(position).getUserNickname());
        mContext.startActivity(intent);
        Activity activity = (Activity)mContext;
        activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
}