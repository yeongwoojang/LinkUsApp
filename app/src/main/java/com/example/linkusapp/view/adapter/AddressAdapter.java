package com.example.linkusapp.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkusapp.R;
import com.example.linkusapp.databinding.ItemAddressBinding;
import com.example.linkusapp.databinding.ItemMyChatRoomBinding;
import com.example.linkusapp.model.vo.UserAddress;
import com.example.linkusapp.view.activity.MainActivity;
import com.example.linkusapp.view.activity.MyStudyGroupActivity;
import com.example.linkusapp.viewModel.MyPageViewModel;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder>{
    private List<UserAddress> mDataset;
    private List<String> items;
    private Activity getActivity;
    private Context mContext;
    private MyPageViewModel viewModel;
    private String nickname;
    private AddressAdapter thisObject = this;

    /**/
    public class AddressViewHolder extends RecyclerView.ViewHolder {
        private ItemAddressBinding binding;

        public AddressViewHolder(@NonNull ItemAddressBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.setAdapter(thisObject);
        }

        void bind(String address, int position) {
            binding.setAddress(address);
            binding.setPosition(position);
        }
    }

    public void updateItem(List<String> items) {
//        mDataset = items;
        this.items = items;
        notifyDataSetChanged();
    }

    public AddressAdapter(List<String> addressList, Activity getActivity, MyPageViewModel viewModel,Context mContext,String nickname) {
        this.items = addressList;
        this.getActivity = getActivity;
        this.nickname = nickname;
        this.viewModel = viewModel;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemAddressBinding binding = ItemAddressBinding.inflate(inflater, parent, false);
        return new AddressViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        String address = items.get(position);
        holder.bind(address, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void itemClickEvent(int position) {
        viewModel.updateAddress(nickname, items.get(position));
//        getActivity.startActivity(new Intent(getActivity, MyStudyGroupActivity.class));
//        getActivity.finish();
    }

    public void removeClickEvent(int position) {
        if(getItemCount()!=0){
            viewModel.removeAddress(items.get(position));
            items.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }

    }
}