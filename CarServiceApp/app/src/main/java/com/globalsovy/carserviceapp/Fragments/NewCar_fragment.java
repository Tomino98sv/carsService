package com.globalsovy.carserviceapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.globalsovy.carserviceapp.MainActivity;
import com.globalsovy.carserviceapp.R;
import com.google.android.material.navigation.NavigationView;

public class NewCar_fragment extends Fragment {

    Toolbar toolbar;
    NavigationView navigationView;
    TextView toolbarTitle;
    ImageView toolbarBtn;

    ConstraintLayout addCarPhoto;
    ImageView carPhoto;

    Spinner dropList;
    String[] brands = new String[]{"Ferrari","BMW","Mazda"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_new_car, container, false);
        toolbar = getActivity().findViewById(R.id.toolbar);
        navigationView = parent.findViewById(R.id.nav_view);
        toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarBtn = getActivity().findViewById(R.id.toolbarTool);
        dropList = parent.findViewById(R.id.brandList);
        addCarPhoto = parent.findViewById(R.id.addPhotoCarLayout);
        carPhoto = parent.findViewById(R.id.currentCarPhoto);

        ((MainActivity)getActivity()).setNavigationButtonToDefault();

        toolbar.setNavigationIcon(R.drawable.close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).changeFragment(MyCars_fragment.class);
            }
        });
        toolbarTitle.setText("New car");
        toolbarBtn.setVisibility(View.GONE);

        addCarPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carPhoto.setImageResource(R.drawable.ic_car);
            }
        });

        ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_item,R.id.brandItem,brands);
        dropList.setAdapter(brandAdapter);

        return parent;
    }
}


