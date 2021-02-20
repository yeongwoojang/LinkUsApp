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
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkusapp.view.activity.EnterMainGroupActivity;
import com.example.linkusapp.R;
import com.example.linkusapp.model.vo.Board;
import com.example.linkusapp.view.activity.GroupMainActivity;

import java.util.List;

public class BoardAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private List<Board> boardList;
    private Activity getActivity;
    private int mType;

    //ViewHolder 시작
    public class BoardViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        private TextView gPart;
        private TextView gArea;
        private TextView gReader;
        private TextView gName;
        private TextView gPurpose;
        private TextView gJoinMethod;

        public BoardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.board_item);
            gPart = itemView.findViewById(R.id.g_part);
            gArea = itemView.findViewById(R.id.g_area);
            gReader = itemView.findViewById(R.id.g_reader);
            gName = itemView.findViewById(R.id.g_name);
            gPurpose = itemView.findViewById(R.id.g_purpose);
            gJoinMethod = itemView.findViewById(R.id.g_join_method);
        }


    }

    public class SelectViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        private TextView gName;
        private TextView gPurpose;
        public SelectViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.board_item);
            gName = itemView.findViewById(R.id.g_name);
            gPurpose = itemView.findViewById(R.id.g_purpose);
        }
    }

    //ViewHolder 끝

    public void updateItem(List<Board> items){
        boardList = items;
        notifyDataSetChanged();
    }

    public BoardAdapter(List<Board> boardList, Activity getActivity,int type){
        this.boardList = boardList;
        this.getActivity = getActivity;
        this.mType = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board,parent,false);
            return new BoardViewHolder(view);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_study,parent,false);
            return new SelectViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d("Type", holder.getClass().getName());
        if(holder instanceof BoardViewHolder){
            Board board = boardList.get(position);
            ((BoardViewHolder)(holder)).gPart.setText(board.getgPart());
            ((BoardViewHolder)(holder)).gArea.setText(board.getgArea());
            ((BoardViewHolder)(holder)).gReader.setText(board.getgReader());
            ((BoardViewHolder)(holder)).gName.setText(board.getgName());
            ((BoardViewHolder)(holder)).gPurpose.setText(board.getgPurpose());
            ((BoardViewHolder)(holder)).gJoinMethod.setText(board.getgJoinMethod());
            ((BoardViewHolder)(holder)).cardView.setTag(position);
            ((BoardViewHolder)(holder)).cardView.setOnClickListener(this);
        }else{
            Board board = boardList.get(position);
            ((SelectViewHolder)(holder)).gName.setText(board.getgName());
            ((SelectViewHolder)(holder)).gPurpose.setText(board.getgPurpose());
            ((SelectViewHolder)(holder)).cardView.setTag(position);
        }
    }



    @Override
    public int getItemCount() {
        return boardList.size();
    }

    @Override
    public void onClick(View v) {
        if(getActivity.toString().contains("MainActivity"))
        {
            Intent intent = new Intent(getActivity, GroupMainActivity.class);
            intent.putExtra("board",boardList.get((int)v.getTag()));
            getActivity.startActivity(intent);
            getActivity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }else if(getActivity.toString().contains("MyStudyGroupActivity")){
            Intent intent = new Intent(getActivity, EnterMainGroupActivity.class);
            intent.putExtra("board",boardList.get((int)v.getTag()));
            getActivity.startActivity(intent);
            getActivity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        Log.d("viewType", mType+"");
        if(mType==1){
            viewType = 1;
        }else{
            viewType = 2;
        }
        return viewType;
    }
}