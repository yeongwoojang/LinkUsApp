package com.example.linkusapp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkusapp.R;
import com.example.linkusapp.model.vo.Board;

import java.util.ArrayList;
import java.util.List;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardViewHolder> {

    private List<Board> boardList;

    public BoardAdapter(List<Board> boardList){
        this.boardList = boardList;
    }

    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board,parent,false);
        return new BoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        if (holder instanceof BoardAdapter.BoardViewHolder) {
            holder.onBind(position);
        }
    }

    @Override
    public int getItemCount() {
        return boardList.size();
    }


    public class BoardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private int position;
        private CardView cardView;
        private TextView gPart;
        private TextView gArea;
        private TextView gReader;
        private TextView gName;
        private TextView gGoal;
        private TextView gJoinMethod;

        public BoardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.board_item);
            gPart = itemView.findViewById(R.id.g_part);
            gArea = itemView.findViewById(R.id.g_area);
            gReader = itemView.findViewById(R.id.g_reader);
            gName = itemView.findViewById(R.id.g_name);
            gGoal = itemView.findViewById(R.id.g_goal);
            gJoinMethod = itemView.findViewById(R.id.g_join_method);

        }

        public void onBind(int position) {
            this.position = position;
            gPart.setText(boardList.get(position).getgPart());
            gArea.setText(boardList.get(position).getgArea());
            gReader.setText(boardList.get(position).getgReader());
            gName.setText(boardList.get(position).getgName());
            gGoal.setText(boardList.get(position).getgGoal());
            gJoinMethod.setText(boardList.get(position).getgJoinMethod());
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}