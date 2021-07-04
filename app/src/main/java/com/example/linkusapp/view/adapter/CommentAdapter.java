package com.example.linkusapp.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkusapp.R;
import com.example.linkusapp.databinding.ItemBoardBinding;
import com.example.linkusapp.databinding.ItemCommentBinding;
import com.example.linkusapp.model.vo.Comment;
import com.example.linkusapp.viewModel.CommentViewModel;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private CommentAdapter thisObject = this;
    private OnreplyBtnClickListener mListener = null;
    private List<Comment> mDataset;
    private List<Comment> replyList = new ArrayList<>();
    private CommentViewModel viewModel = null;
    private Context mContext = null;
    private List<List<Comment>> items;
    ReplyAdapter adapter = null;

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        private ItemCommentBinding binding;

        public CommentViewHolder(ItemCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.setAdapter(thisObject);

        }

        void bind(Comment comment, int position) {
            binding.setComment(comment);
            binding.setPosition(position);
            adapter = new ReplyAdapter();
            Log.d("BOND", "bind: ");
            binding.subCommentRv.setAdapter(adapter);
            binding.setReplyList(items);
            Log.d("하하하", "bind: "+items);

            binding.openReplyRv.setOnClickListener(v -> {
                Log.d("PLUSCLICK", "!!!");
                if(binding.subCommentRv.getVisibility()==View.VISIBLE){
                    binding.subCommentRv.setVisibility(View.GONE);
                }else{
                    binding.subCommentRv.setVisibility(View.VISIBLE);
                }
            });

        }
    }

    public void updateItem(List<Comment> items) {
        mDataset = items;
        notifyDataSetChanged();
    }

    public void updateList(List<List<Comment>> items){
        this.items = items;
        Log.d("CommentADapter", "updateList: "+items);
        notifyDataSetChanged();
    }

    public CommentAdapter(List<Comment> commentList, CommentViewModel viewModel, Context context,List<List<Comment>> items) {
        mDataset = commentList;
        this.viewModel = viewModel;
        this.mContext = context;
        this.items = items;

    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCommentBinding binding = ItemCommentBinding.inflate(inflater, parent, false);
        return new CommentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment cmt = items.get(position).get(0);
        Log.d("코멘트포지션", "comment position : "+ position);
        holder.bind(cmt,position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void replyBtnClickEvent(int positon) {
        //답글달기 버튼 이벤트 내용
        if (mListener != null) {
            mListener.onClick(mDataset.get(positon).getWriter(), mDataset.get(positon).getComment());
        }
    }


    public interface OnreplyBtnClickListener {
        void onClick(String writer, String comment);
    }

    public void setReplyBtnClickListener(OnreplyBtnClickListener listener) {
        this.mListener = listener;
    }

}
