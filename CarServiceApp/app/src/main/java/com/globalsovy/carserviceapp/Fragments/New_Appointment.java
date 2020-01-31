package com.globalsovy.carserviceapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.globalsovy.carserviceapp.MainActivity;
import com.globalsovy.carserviceapp.R;
import com.google.android.material.navigation.NavigationView;


public class New_Appointment extends Fragment {

    Toolbar toolbar;
    NavigationView navigationView;
    TextView toolbarTitle;
    ImageView toolbarBtn;

    ScrollView scrollView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_new_appointment, container, false);

        navigationView = parent.findViewById(R.id.nav_view);
        toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarBtn = getActivity().findViewById(R.id.toolbarTool);
        toolbar = getActivity().findViewById(R.id.toolbar);
        scrollView = parent.findViewById(R.id.scrollOnNewAppoint);

        ((MainActivity)getActivity()).setNavigationButtonToDefault();


        toolbarTitle.setText("Select Date & Time");
        toolbarBtn.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.drawable.close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).changeFragment(MyAppointments_fragment.class);
            }
        });

        return parent;
    }
}
