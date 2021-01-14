package com.example.linkusapp.view.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkusapp.R;
import com.example.linkusapp.model.vo.Board;
import com.example.linkusapp.model.vo.UserAddress;
import com.example.linkusapp.viewModel.MyPageViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> implements View.OnClickListener{


    private List<UserAddress> mDataset;
    private Activity getActivity;
    private MyPageViewModel viewModel;
    private String nickname;
    /**/
    public class AddressViewHolder extends RecyclerView.ViewHolder{

        private TextView addressTV;
        private CardView cardView;
        private ImageButton remove;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview);
            addressTV = itemView.findViewById(R.id.item_address);
            remove = itemView.findViewById(R.id.remove_btn);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address,parent,false);
        return new AddressViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        UserAddress address = mDataset.get(position);
        holder.addressTV.setText(address.getAddress());
        holder.cardView.setTag(position);
        holder.cardView.setOnClickListener(this);
        holder.remove.setTag(position);
        holder.remove.setOnClickListener(this);
       /* holder.remove.setTag(position);
        holder.remove.setOnClickListener(this);*/
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