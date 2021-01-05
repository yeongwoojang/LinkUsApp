package com.example.linkusapp.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.linkusapp.R;

import org.w3c.dom.Text;

public class MyPageFragment extends Fragment {

    private TextView nickNameTV,addressTV,idTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);
        nickNameTV = (TextView) view.findViewById(R.id.nickname_tv);
        addressTV = (TextView) view.findViewById(R.id.address_tv);
        idTV = (TextView) view.findViewById(R.id.id_tv);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}