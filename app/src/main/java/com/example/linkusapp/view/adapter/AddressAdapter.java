package com.example.linkusapp.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.example.linkusapp.viewModel.MyPageViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> implements View.OnClickListener{
    private List<UserAddress> mDataset;
    private Activity getActivity;
    private Context mContext;
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
    public AddressAdapter(List<UserAddress> addressList, Activity getActivity,MyPageViewModel viewModel,Context mContext,String nickname) {
        mDataset = addressList;
        this.getActivity = getActivity;
        this.nickname = nickname;
        this.mContext = mContext;
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
                        getActivity.finish();
                        Toast.makeText(mContext,"주소가 "+ mDataset.get((int) view.getTag()).getAddress()+"로 변경되었습니다.",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.remove_btn:
                        viewModel.removeAddress(mDataset.get((int) view.getTag()).getAddress());
                        mDataset.remove((int)view.getTag());
                        notifyItemRemoved((int)view.getTag());
                        notifyItemRangeChanged((int)view.getTag(), getItemCount());
                        Toast.makeText(mContext,"해당 주소가 리스트에서 삭제되었습니다.",Toast.LENGTH_SHORT).show();
                        break;
        }
    }
}