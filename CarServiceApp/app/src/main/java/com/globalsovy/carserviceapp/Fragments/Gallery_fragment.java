package com.globalsovy.carserviceapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.toolbox.Volley;
import com.globalsovy.carserviceapp.Adapters.PageAdapter;
import com.globalsovy.carserviceapp.Adapters.PageAdapterGallery;
import com.globalsovy.carserviceapp.MainActivity;
import com.globalsovy.carserviceapp.MySharedPreferencies;
import com.globalsovy.carserviceapp.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class Gallery_fragment extends Fragment {

    Toolbar toolbar;
    NavigationView navigationView;
    TextView toolbarTitle;
    ImageView toolbarBtn;

    ViewPager viewPager;
    PageAdapterGallery pageAdapterGallery;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_gallery, container, false);

        viewPager = parent.findViewById(R.id.carPhoto);
        toolbar = getActivity().findViewById(R.id.toolbar);
        navigationView = parent.findViewById(R.id.nav_view);
        toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarBtn = getActivity().findViewById(R.id.toolbarTool);

        ((MainActivity)getActivity()).setNavigationButtonToDefault();

        toolbar.setNavigationIcon(R.drawable.close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).changeFragment(MyAppointments_fragment.class);
            }
        });

        toolbarTitle.setText("Appointments Photos");
        toolbarBtn.setVisibility(View.GONE);


        ArrayList<String> urlPhotos = ((MainActivity)getActivity()).getCurrentApp().getUrlImages();

        pageAdapterGallery = new PageAdapterGallery(
                urlPhotos,
                getContext(),
                getActivity());
        viewPager.setAdapter(pageAdapterGallery);
        return parent;
    }
}
