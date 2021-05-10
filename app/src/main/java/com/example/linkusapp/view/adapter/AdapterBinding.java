package com.example.linkusapp.view.adapter;

import android.util.Log;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkusapp.model.vo.Comment;

import java.util.List;

public class AdapterBinding {

    @BindingAdapter("bind:item")
    public static void bindItem(RecyclerView recyclerView, List<Comment> replyList) {
        ReplyAdapter replyAdapter = (ReplyAdapter) recyclerView.getAdapter();
        if (replyAdapter != null) {
            if(replyList.size()!=1){
                replyList.remove(0);
                replyAdapter.updateItem(replyList);
            }
        }
    }
}
