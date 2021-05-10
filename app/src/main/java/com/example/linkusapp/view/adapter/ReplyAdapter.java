package com.example.linkusapp.view.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkusapp.databinding.ItemCommentBinding;
import com.example.linkusapp.databinding.ItemReplyBinding;
import com.example.linkusapp.model.vo.Comment;

import java.util.ArrayList;
import java.util.List;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {

    private List<Comment> items = new ArrayList<>();

    private ReplyAdapter thisObj = this;


    public class ReplyViewHolder extends RecyclerView.ViewHolder{
        private ItemReplyBinding binding;

        public ReplyViewHolder(ItemReplyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Comment comment, int position){
            binding.setComment(comment);
            binding.setPosition(position);
        }


    }
    public void updateItem(List<Comment> items){
        if(items.size()!=1){
            this.items = items;
            notifyDataSetChanged();
        }

    }


    @NonNull
    @Override
    public ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemReplyBinding binding = ItemReplyBinding.inflate(inflater, parent, false);
        return new ReplyAdapter.ReplyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyViewHolder holder, int position) {
        Comment comment = items.get(position);
        Log.d("포지션", "reply position : "+ position);
        holder.bind(comment,position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
