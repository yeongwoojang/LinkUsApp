package com.example.linkusapp.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkusapp.databinding.ItemAddressBinding;
import com.example.linkusapp.databinding.ItemBoardBinding;
import com.example.linkusapp.databinding.ItemSelectStudyBinding;
import com.example.linkusapp.view.activity.EnterMainGroupActivity;
import com.example.linkusapp.R;
import com.example.linkusapp.model.vo.Board;
import com.example.linkusapp.view.activity.GroupMainActivity;
import com.example.linkusapp.viewModel.BoardViewModel;

import java.util.List;

public class BoardAdapter extends RecyclerView.Adapter {

    private List<Board> boardList;
    private Activity getActivity;
    private BoardViewModel viewModel;
    private int mType;
    private BoardAdapter thisObject = this;
    //ViewHolder 시작
    public class BoardViewHolder extends RecyclerView.ViewHolder{
        private ItemBoardBinding binding;

        public BoardViewHolder(ItemBoardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.setAdapter(thisObject);
        }
        void bind(Board board,int position){
            binding.setBoard(board);
            binding.setPosition(position);
        }
    }

    public class SelectViewHolder extends RecyclerView.ViewHolder{
        private ItemSelectStudyBinding binding;

        public SelectViewHolder(ItemSelectStudyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        void bind(Board board, int position){
            binding.setBoard(board);
            binding.setPosition(position);
        }
    }
    //ViewHolder 끝

    public void updateItem(List<Board> items){
        boardList = items;
        notifyDataSetChanged();
    }

    public BoardAdapter(List<Board> boardList, Activity getActivity, BoardViewModel viewModel, int type){
        this.boardList = boardList;
        this.getActivity = getActivity;
        this.viewModel = viewModel;
        this.mType = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==1){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ItemBoardBinding binding = ItemBoardBinding.inflate(inflater, parent, false);
            return new BoardViewHolder(binding);
        }else{
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ItemSelectStudyBinding binding = ItemSelectStudyBinding.inflate(inflater, parent, false);
            return new SelectViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof BoardViewHolder){
            Board board = boardList.get(position);
            ((BoardViewHolder)(holder)).bind(board,position);
        }else{
            Board board = boardList.get(position);
            ((SelectViewHolder)(holder)).bind(board,position);
        }
    }

    @Override
    public int getItemCount() {
        return boardList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
       if(mType==1){
           viewType = 1;
       }else{
           viewType = 2;
       }
       return viewType;
    }

    public void itemClickEvent(int position){
        if(getActivity.toString().contains("MainActivity")&& mType==1)
        {
            Intent intent = new Intent(getActivity, GroupMainActivity.class);
            intent.putExtra("board",boardList.get(position));
            getActivity.startActivity(intent);
            getActivity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }else if(getActivity.toString().contains("MainActivity")&& mType==2){
            String userNick = viewModel.getUserInfoFromShared().getUserNickname();
            viewModel.updateSelected(userNick,boardList.get(position).getTitle());
        }else if(getActivity.toString().contains("MyStudyGroupActivity")){
            Intent intent = new Intent(getActivity, EnterMainGroupActivity.class);
            intent.putExtra("board",boardList.get(position));
            Log.d("AdapterBorad", "itemClickEvent: "+boardList.get(position));
            getActivity.startActivity(intent);
            getActivity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }
}
