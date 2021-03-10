package com.example.linkusapp.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkusapp.R;
import com.example.linkusapp.databinding.ItemMyChatRoomBinding;
import com.example.linkusapp.databinding.ItemYourChatRoomBinding;
import com.example.linkusapp.model.vo.Chat;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {

    private List<Chat> items;
    private String myNickName;

    public ChatAdapter(List<Chat> items, String myNickName) {
        this.items = items;
        this.myNickName = myNickName;
    }

    public void updateItem(List<Chat> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public class MyChatViewHolder extends RecyclerView.ViewHolder {
        private ItemMyChatRoomBinding binding;
        public MyChatViewHolder(@NonNull ItemMyChatRoomBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Chat chat) {
            binding.setChat(chat);
        }


    }

    public class YourChatViewHolder extends RecyclerView.ViewHolder {
        private ItemYourChatRoomBinding binding;
        public YourChatViewHolder(ItemYourChatRoomBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Chat chat) {
            binding.setChat(chat);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ItemMyChatRoomBinding binding = ItemMyChatRoomBinding.inflate(inflater, parent, false);
            return new MyChatViewHolder(binding);
        } else {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ItemYourChatRoomBinding binding = ItemYourChatRoomBinding.inflate(inflater, parent, false);
            return new YourChatViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyChatViewHolder) {
            Chat chat = items.get(position);
            ((MyChatViewHolder) (holder)).binding.setChat(chat);
            ((MyChatViewHolder) (holder)).binding.executePendingBindings();

        } else {
            Chat chat = items.get(position);
            ((YourChatViewHolder) (holder)).binding.setChat(chat);
            ((YourChatViewHolder) (holder)).binding.executePendingBindings();
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getMsgFrom().equals(myNickName)) {
            return 1;
        } else {
            return 2;
        }
    }
}
