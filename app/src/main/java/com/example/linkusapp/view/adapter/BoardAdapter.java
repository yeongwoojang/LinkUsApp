package com.example.linkusapp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkusapp.R;
import com.example.linkusapp.model.vo.Board;

import java.util.ArrayList;
import java.util.List;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardViewHolder> {

    private List<Board> mItems = new ArrayList<>();

    public class BoardViewHolder extends RecyclerView.ViewHolder {
        TextView partTextView;
        TextView areaTextView;
        TextView nicknameTextView;
        TextView titleTextView;
        TextView dateTextView;

        public BoardViewHolder(@NonNull View itemView) {
            super(itemView);

            partTextView = itemView.findViewById(R.id.part);
            areaTextView = itemView.findViewById(R.id.area);
            nicknameTextView = itemView.findViewById(R.id.nickname);
            titleTextView = itemView.findViewById(R.id.title);
            dateTextView = itemView.findViewById(R.id.writing_day);
        }
    }

    public BoardAdapter(List<Board> list){
        mItems = list;
    }

    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board,parent,false);
        return new BoardViewHolder(view);

//        Context context = parent.getContext() ;
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
//
//        View view = inflater.inflate(R.layout.item_board, parent, false) ;
//        BoardViewHolder vh = new BoardViewHolder(view) ;
//        return vh ;
    }

    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        Board board = mItems.get(position);

        holder.partTextView.setText(board.getPart());
        holder.areaTextView.setText(board.getArea());
        holder.nicknameTextView.setText(board.getUserNickname());
        holder.titleTextView.setText(board.getTitle());
        holder.dateTextView.setText(board.getWriteTime());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
