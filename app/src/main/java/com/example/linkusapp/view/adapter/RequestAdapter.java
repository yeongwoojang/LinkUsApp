package com.example.linkusapp.view.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkusapp.R;
import com.example.linkusapp.databinding.ItemMemberBinding;
import com.example.linkusapp.databinding.ItemRequestBinding;
import com.example.linkusapp.model.vo.LeaderGroup;
import com.example.linkusapp.model.vo.User;
import com.example.linkusapp.viewModel.ManageJoinViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder>{

    private RequestAdapter thisObject = this;

    private List<User> items;
    private ManageJoinViewModel viewModel;
    private Context mContext;
    private Toast toast;
    private String gName ="";

    public class RequestViewHolder extends RecyclerView.ViewHolder{
        private ItemRequestBinding binding;

        public RequestViewHolder(ItemRequestBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.setAdapter(thisObject);
        }
        void bind(User user, int position){
            binding.setUser(user);
            binding.setPosition(position);
        }
    }

    public void updateItem(List<User> items){
        this.items = items;
        notifyDataSetChanged();
    }
    public void removeItem(int position){
        notifyItemRemoved(position);
    }

    public RequestAdapter(List<User> items, ManageJoinViewModel viewModel, Context context) {
        this.items = items;
        this.viewModel = viewModel;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemRequestBinding binding = ItemRequestBinding.inflate(inflater, parent, false);
        return new RequestAdapter.RequestViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        User user = items.get(position);
        holder.bind(user,position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void acceptBtnClickEvent(int position){
        new AlertDialog.Builder(mContext)
                .setMessage("가입 요청을 수락하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //가입요청한 유저를 그룹에 추가
                        viewModel.joinGroup(gName,items.get(position).getUserId(),items.get(position).getUserNickname());
                        //DB에서 요청목록 삭제
                        viewModel.deleteRequest(gName,items.get(position).getUserNickname());

                    }
                })
                .setNegativeButton("거절", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //DB에서 요청목록 삭제
                        viewModel.deleteRequest(gName,items.get(position).getUserNickname());
                        if(toast!=null){
                            toast.cancel();
                        }
                        toast = Toast.makeText(mContext,"가입 요청을 거절했습니다.",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }).show();
    }
    public void setgName(String gName) {
        this.gName = gName;
    }

    public String getgName() {
        return gName;
    }
}