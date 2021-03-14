package com.example.linkusapp.view.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkusapp.R;
import com.example.linkusapp.databinding.ItemMyGroupBinding;
import com.example.linkusapp.databinding.ItemRequestBinding;
import com.example.linkusapp.model.vo.Board;
import com.example.linkusapp.model.vo.LeaderGroup;
import com.example.linkusapp.viewModel.ManageJoinViewModel;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

public class MyGroupAdapter extends RecyclerView.Adapter<MyGroupAdapter.MyGroupViewHolder> {

    private MyGroupAdapter thisObject = this;
    private List<LeaderGroup> items;
    private SlidingUpPanelLayout slidingView;
    private ManageJoinViewModel viewModel;
    private GroupNameListener mListener = null;
    public MyGroupAdapter(List<LeaderGroup> items, SlidingUpPanelLayout slidingView, ManageJoinViewModel viewModel) {
        this.items = items;
        this.viewModel = viewModel;
    }

    public class MyGroupViewHolder extends RecyclerView.ViewHolder{
        private ItemMyGroupBinding binding;

        public MyGroupViewHolder(@NonNull ItemMyGroupBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.setAdapter(thisObject);
        }
        void bind(LeaderGroup leaderGroup,int position){
            binding.setLeaderGroup(leaderGroup);
            binding.setPosition(position);
        }
    }

    public void updateItem(List<LeaderGroup> items){
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMyGroupBinding binding = ItemMyGroupBinding.inflate(inflater,parent,false);
        return new MyGroupAdapter.MyGroupViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyGroupViewHolder holder, int position) {
            LeaderGroup leaderGroup = items.get(position);
            holder.bind(leaderGroup,position);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public interface GroupNameListener{
        void returnGname(String gName);
    }
    public void setGnameListener(GroupNameListener listener){
        this.mListener = listener;
    }

    public void itemClickEvent(int position){
        String gName = items.get(position).getName();
        viewModel.getReqUser(gName);
        if(mListener!=null){
            mListener.returnGname(gName);
        }
    }
}
