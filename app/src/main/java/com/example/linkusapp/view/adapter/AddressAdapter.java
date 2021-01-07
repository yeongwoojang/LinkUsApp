package com.example.linkusapp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkusapp.R;
import com.example.linkusapp.model.vo.Address;
import com.example.linkusapp.model.vo.Board;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {


    private ArrayList<Address> mDataset;

    public class AddressViewHolder extends RecyclerView.ViewHolder {

        TextView addressTV;
        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);

            addressTV = itemView.findViewById(R.id.item_address);
        }
    }
    public AddressAdapter(ArrayList<Address> myDataset){
        myDataset = myDataset;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address,parent,false);
        return new AddressViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Address address = mDataset.get(position);

        holder.addressTV.setText(address.getAddress());
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}