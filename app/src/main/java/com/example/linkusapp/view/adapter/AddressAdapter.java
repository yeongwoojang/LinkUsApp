package com.example.linkusapp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkusapp.R;
import com.example.linkusapp.model.vo.UserAddress;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {


    private List<UserAddress> mDataset;

    public AddressAdapter(List<UserAddress> addressList) {
        this.mDataset = addressList;
        notifyDataSetChanged();
    }

    /**/
    public class AddressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private int position;
        private TextView addressTV;
        private CardView cardView;
        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview);
            addressTV = itemView.findViewById(R.id.item_address);
        }
        public void onBind(int position){
            this.position = position;
            addressTV.setText(mDataset.get(position).getAddress());
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address,parent,false);
        return new AddressViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        if (holder instanceof AddressAdapter.AddressViewHolder) {
            holder.onBind(position);
        }
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}