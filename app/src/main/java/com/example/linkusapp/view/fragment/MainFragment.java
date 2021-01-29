package com.example.linkusapp.view.fragment;

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

import com.example.linkusapp.R;
import com.example.linkusapp.view.custom.RoundImageView;

public class MainFragment extends Fragment {

    private RoundImageView logoImage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        logoImage = view.findViewById(R.id.logo_image);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logoImage.setRectRadius(500f);
//        GradientDrawable drawable = (GradientDrawable)getActivity().getDrawable(R.drawable.form_round_img);
//        logoImage.setBackground(new ShapeDrawable(new OvalShape()));
//        logoImage.setClipToOutline(true);
    }

}
