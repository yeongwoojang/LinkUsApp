package com.example.linkusapp.view.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkusapp.R;
import com.example.linkusapp.databinding.ItemAddressBinding;
import com.example.linkusapp.databinding.ItemMyChatRoomBinding;
import com.example.linkusapp.model.vo.UserAddress;
import com.example.linkusapp.viewModel.MyPageViewModel;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> implements View.OnClickListener{
    private List<UserAddress> mDataset;
    private Activity getActivity;
    private MyPageViewModel viewModel;
    private String nickname;
    /**/
    public class AddressViewHolder extends RecyclerView.ViewHolder{
        private ItemAddressBinding binding;
        public AddressViewHolder(@NonNull ItemAddressBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public void updateItem(List<UserAddress> items){
        mDataset = items;
        notifyDataSetChanged();
    }
    public AddressAdapter(List<UserAddress> addressList, Activity getActivity,MyPageViewModel viewModel,String nickname) {
        mDataset = addressList;
        this.getActivity = getActivity;
        this.nickname = nickname;
        this.viewModel = viewModel;
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
        UserAddress address = mDataset.get(position);
        holder.binding.itemAddress.setText(address.getAddress());
        holder.binding.cardview.setTag(position);
        holder.binding.cardview.setOnClickListener(this);
        holder.binding.removeBtn.setTag(position);
        holder.binding.removeBtn.setOnClickListener(this);
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    @Override
    public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.cardview:
                        viewModel.updateAddress(nickname, mDataset.get((int) view.getTag()).getAddress());
                        break;
                    case R.id.remove_btn:
                        viewModel.removeAddress(mDataset.get((int) view.getTag()).getAddress());
                        mDataset.remove((int)view.getTag());
                        notifyItemRemoved((int)view.getTag());
                        notifyItemRangeChanged((int)view.getTag(), getItemCount());
                        break;
        }
    }
}