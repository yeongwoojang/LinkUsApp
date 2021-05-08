package com.example.linkusapp.view.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkusapp.R;
import com.example.linkusapp.databinding.ItemBoardBinding;
import com.example.linkusapp.databinding.ItemCommentBinding;
import com.example.linkusapp.model.vo.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private CommentAdapter thisObject = this;
    private OnreplyBtnClickListener mListener = null;
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
        holder.bind(comment,position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void replyBtnClickEvent(int positon){
        //답글달기 버튼 이벤트 내용
        if(mListener!=null){
            mListener.onClick(mDataset.get(positon).getWriter(),mDataset.get(positon).getComment());
        }


    }
    public interface OnreplyBtnClickListener{
        void onClick(String writer,String comment);
    }
    public void setReplyBtnClickListener(OnreplyBtnClickListener listener){this.mListener = listener;}
}
