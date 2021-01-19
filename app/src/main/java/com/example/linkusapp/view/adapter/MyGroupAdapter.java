package com.example.linkusapp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkusapp.R;
import com.example.linkusapp.model.vo.Board;

import java.util.List;

public class MyGroupAdapter extends RecyclerView.Adapter<MyGroupAdapter.MyGroupViewHolder> {

    public class MyGroupViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout groupLayout;
        private TextView groupName,joinReqCount;
        public MyGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            groupLayout = itemView.findViewById(R.id.group_layout);
            groupName = itemView.findViewById(R.id.my_group_name);
            joinReqCount = itemView.findViewById(R.id.request_cnt);
        }
    }

    public void updateItem(List<Board> items){
//        boardList = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_group,parent,false);
        return new MyGroupAdapter.MyGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyGroupViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
