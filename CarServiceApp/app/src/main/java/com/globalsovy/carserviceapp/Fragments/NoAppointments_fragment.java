package com.globalsovy.carserviceapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.globalsovy.carserviceapp.MainActivity;
import com.globalsovy.carserviceapp.R;
import com.google.android.material.navigation.NavigationView;

public class NoAppointments_fragment extends Fragment {

    NavigationView navigationView;
    TextView toolbarTitle;
    ImageView toolbarBtn;

    RelativeLayout noAppointment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_no_appointments,container,false);

        noAppointment = parent.findViewById(R.id.noAppointment);
        navigationView = parent.findViewById(R.id.nav_view);
        toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarBtn = getActivity().findViewById(R.id.toolbarTool);

        ((MainActivity)getActivity()).setNavigationButtonToDefault();

        toolbarTitle.setText("My Appointments");
        toolbarBtn.setImageResource(R.drawable.add);
        toolbarBtn.setVisibility(View.VISIBLE);
        toolbarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).changeFragment(New_Appointment.class);
            }
        });

        noAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).changeFragment(New_Appointment.class);
            }
        });

        return parent;
    }
}
