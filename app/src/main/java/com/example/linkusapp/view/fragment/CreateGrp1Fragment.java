package com.example.linkusapp.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.linkusapp.R;

public class CreateGrp1Fragment extends Fragment {

    ImageButton backBt;
    EditText edtGroupName, edtGroupExplanation, edtGroupPurpose;
    Button nextBt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_grp1, container, false);
        backBt = view.findViewById(R.id.btn_back);
        edtGroupName = view.findViewById(R.id.edt_group_name);
        edtGroupExplanation = view.findViewById(R.id.edt_group_explanation);
        edtGroupPurpose = view.findViewById(R.id.edt_group_purpose);
        nextBt = view.findViewById(R.id.btn_create_group);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nextBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_createGrp1Fragment_to_createGrp2Fragment);
            }
        });
    }
}