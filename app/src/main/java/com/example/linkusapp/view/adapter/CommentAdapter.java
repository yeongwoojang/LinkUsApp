package com.example.linkusapp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkusapp.R;
import com.example.linkusapp.model.vo.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> mDataset;

    public class CommentViewHolder extends RecyclerView.ViewHolder{

        private TextView writerTv,writingTimeTv,commentTv;
        private Button recommentBtn;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            writerTv= itemView.findViewById(R.id.item_id_tv);
            writingTimeTv= itemView.findViewById(R.id.item_time_tv);
            commentTv = itemView.findViewById(R.id.item_comment_tv);
            recommentBtn = itemView.findViewById(R.id.item_reply_btn);
        }
    }
    public void updateItem(List<Comment> items){
        mDataset = items;
        notifyDataSetChanged();
    }
    public CommentAdapter(List<Comment> commentList){
        mDataset = commentList;
    }
    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder holder, int position) {
        Comment comment = mDataset.get(position);
        holder.writerTv.setText(comment.getbWriter());
        holder.commentTv.setText(comment.getbComment());
        holder.writingTimeTv.setText(comment.getbWriteTime());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
