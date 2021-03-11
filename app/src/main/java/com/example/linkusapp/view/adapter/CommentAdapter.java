package com.example.linkusapp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkusapp.R;
import com.example.linkusapp.databinding.ItemBoardBinding;
import com.example.linkusapp.databinding.ItemCommentBinding;
import com.example.linkusapp.model.vo.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private CommentAdapter thisObject = this;

    private List<Comment> mDataset;
    public class CommentViewHolder extends RecyclerView.ViewHolder {
        private ItemCommentBinding binding;


        public CommentViewHolder(ItemCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.setAdapter(thisObject);
        }

        void bind(Comment comment,int position) {
            binding.setComment(comment);
            binding.setPosition(position);
        }
    }

    public void updateItem(List<Comment> items) {
        mDataset = items;
        notifyDataSetChanged();
    }

    public CommentAdapter(List<Comment> commentList) {
        mDataset = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCommentBinding binding = ItemCommentBinding.inflate(inflater, parent, false);
        return new CommentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder holder, int position) {
        Comment comment = mDataset.get(position);
        comment.setWriteTime(comment.getWriteTime().substring(2, 10) + "  " + comment.getWriteTime().substring(11, 16));
        holder.bind(comment,position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void replyBtnClickEvent(int position){
        //답글달기 버튼 이벤트 내용
    }
}
