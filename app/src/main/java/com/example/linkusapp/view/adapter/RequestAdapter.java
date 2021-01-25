package com.example.linkusapp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkusapp.R;
import com.example.linkusapp.model.vo.LeaderGroup;
import com.example.linkusapp.model.vo.User;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder>{

     private List<User> items;

    public class RequestViewHolder extends RecyclerView.ViewHolder{

        private TextView nickNameText, ageText, genderText, addressText;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            nickNameText = itemView.findViewById(R.id.user_nickname);
            ageText = itemView.findViewById(R.id.user_age);
            genderText = itemView.findViewById(R.id.user_gender);
            addressText = itemView.findViewById(R.id.user_address);
        }
    }

    public void updateItem(List<User> items){
        this.items = items;
        notifyDataSetChanged();
    }

    public RequestAdapter(List<User> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request,parent,false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        User user = items.get(position);
        holder.nickNameText.setText(user.getUserNickname());
        holder.ageText.setText(user.getAge());
        holder.genderText.setText(user.getGender());
        holder.addressText.setText(user.getAddress());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
