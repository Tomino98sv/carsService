package com.globalsovy.carserviceapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.globalsovy.carserviceapp.R;
import com.google.android.material.navigation.NavigationView;

public class Notifications_fragment extends Fragment {

    NavigationView navigationView;
    TextView toolbarTitle;
    ImageView toolbarBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_notifications,container,false);

        navigationView = parent.findViewById(R.id.nav_view);
        toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarBtn = getActivity().findViewById(R.id.toolbarTool);

        toolbarTitle.setText("Notifications");
        toolbarBtn.setVisibility(View.GONE);

        return parent;
    }
}
