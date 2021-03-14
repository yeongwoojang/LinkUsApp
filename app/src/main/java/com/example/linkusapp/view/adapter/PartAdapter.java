//package com.example.linkusapp.view.adapter;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.LifecycleOwner;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.linkusapp.R;
//import com.example.linkusapp.model.vo.Board;
//import com.example.linkusapp.view.activity.GroupMainActivity;
//import com.example.linkusapp.viewModel.BoardViewModel;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class PartAdapter extends RecyclerView.Adapter<PartAdapter.PartViewHolder> implements View.OnClickListener{
//
//    private ArrayList<String> partList = null;
//    private Activity getActivity;
//    private int position;
//    private BoardViewModel viewModel;
//
//    @Override
//    public void onClick(View v) {
//        Log.d("TAG", "onClick: " + v.getTag());
//    }
//
//    public void updateItem(ArrayList<String> items){
//        partList = items;
//        notifyDataSetChanged();
//    }
//
//    // 아이템 뷰를 저장하는 뷰홀더 클래스.
//    public class PartViewHolder extends RecyclerView.ViewHolder{
//        private TextView textView1;
//
//        public PartViewHolder(@NonNull View itemView) {
//            super(itemView);
//            textView1 = itemView.findViewById(R.id.part_name);
//        }
//    }
//
//    public PartAdapter(ArrayList<String> partList, Activity getActivity) {
//        this.partList = partList ;
//        this.getActivity = getActivity;
//    }
//
//    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
//    @NonNull
//    @Override
//    public PartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_part, parent, false);
//        return new PartAdapter.PartViewHolder(view);
//    }
//
//    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
//    @Override
//    public void onBindViewHolder(@NonNull PartViewHolder holder, int position) {
//        this.position = position;
//        String text = partList.get(position);
//        holder.textView1.setText(text);
//        holder.textView1.setTag(position);
//        holder.textView1.setOnClickListener(this::onClick);
//    }
//
//    // getItemCount() - 전체 데이터 갯수 리턴.
//    @Override
//    public int getItemCount() {
//        return partList.size();
//    }
//}
package com.example.linkusapp.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkusapp.R;
import com.example.linkusapp.databinding.ItemPartBinding;
import com.example.linkusapp.databinding.ItemRequestBinding;
import com.example.linkusapp.model.vo.Board;
import com.example.linkusapp.view.activity.GroupMainActivity;
import com.example.linkusapp.viewModel.BoardViewModel;

import java.util.ArrayList;
import java.util.List;

public class PartAdapter extends RecyclerView.Adapter<PartAdapter.PartViewHolder>{

    private ArrayList<String> partList = null;
    private PartAdapter thisobject = this;
    private OnItemClickListener mListener = null ;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class PartViewHolder extends RecyclerView.ViewHolder{
        private ItemPartBinding binding;

        public PartViewHolder(ItemPartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.setAdapter(thisobject);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener.onItemClick(v, pos) ;
                        }
                    }
                }
            });
        }

        void bind(String part, int position){
            binding.setPart(part);
            binding.setPosition(position);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

    public PartAdapter(ArrayList<String> partList) {
        this.partList = partList ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public PartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPartBinding binding = ItemPartBinding.inflate(inflater, parent, false);
        return new PartAdapter.PartViewHolder(binding);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(@NonNull PartViewHolder holder, int position) {
        String part = partList.get(position);
        holder.bind(part,position);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return partList.size();
    }
}