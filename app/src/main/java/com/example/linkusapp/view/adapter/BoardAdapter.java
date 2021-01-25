package com.example.linkusapp.view.adapter;

import android.app.Activity;
import android.content.Intent;
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

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardViewHolder> implements View.OnClickListener {

    private List<Board> boardList;
    private Activity getActivity;

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
    //ViewHolder 끝

    public void updateItem(List<Board> items){
        boardList = items;
        notifyDataSetChanged();
    }

    public BoardAdapter(List<Board> boardList, Activity getActivity){
        this.boardList = boardList;
        this.getActivity = getActivity;
    }

    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board,parent,false);
        return new BoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        Board board = boardList.get(position);
        holder.gPart.setText(board.getgPart());
        holder.gArea.setText(board.getgArea());
        holder.gReader.setText(board.getgReader());
        holder.gName.setText(board.getgName());
        holder.gPurpose.setText(board.getgPurpose());
        holder.gJoinMethod.setText(board.getgJoinMethod());
        holder.cardView.setTag(position);
        holder.cardView.setOnClickListener(this);
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
        }else{
            Intent intent = new Intent(getActivity, EnterMainGroupActivity.class);
            intent.putExtra("board",boardList.get((int)v.getTag()));
            getActivity.startActivity(intent);
            getActivity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }
}
