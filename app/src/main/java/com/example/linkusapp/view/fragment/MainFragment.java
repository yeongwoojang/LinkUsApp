package com.example.linkusapp.view.fragment;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.linkusapp.R;
import com.example.linkusapp.view.activity.ChatActivity;
import com.example.linkusapp.view.custom.RoundImageView;

public class MainFragment extends Fragment {

    private RoundImageView logoImage;
    TextView testChat;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        testChat = view.findViewById(R.id.test_chat);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        testChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChatActivity.class));
            }
        });
//        GradientDrawable drawable = (GradientDrawable)getActivity().getDrawable(R.drawable.form_round_img);
//        logoImage.setBackground(new ShapeDrawable(new OvalShape()));
//        logoImage.setClipToOutline(true);
    }

}
