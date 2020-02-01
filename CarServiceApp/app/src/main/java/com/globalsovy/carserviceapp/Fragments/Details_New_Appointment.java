package com.globalsovy.carserviceapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.globalsovy.carserviceapp.MainActivity;
import com.globalsovy.carserviceapp.POJO.CarItem;
import com.globalsovy.carserviceapp.R;
import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Details_New_Appointment extends Fragment {

    Toolbar toolbar;
    NavigationView navigationView;
    TextView toolbarTitle;
    ImageView toolbarBtn;

    Spinner myCarsSpinner;
    ArrayAdapter<String> adapterMyCars;
    ArrayList<String> listForSpinner;
    ArrayList<CarItem> myCars;

    EditText notes;
    ConstraintLayout addPic;
    Button saveNewAppointment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_details_new_appointment, container, false);

        navigationView = parent.findViewById(R.id.nav_view);
        toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarBtn = getActivity().findViewById(R.id.toolbarTool);
        toolbar = getActivity().findViewById(R.id.toolbar);
        myCarsSpinner = parent.findViewById(R.id.spinnerMyCars);
        notes = parent.findViewById(R.id.note);
        addPic = parent.findViewById(R.id.addPhotoAppointmentLayout);
        saveNewAppointment = parent.findViewById(R.id.saveAppointment);

        myCars = ((MainActivity)getActivity()).getMyCars();
        ((MainActivity)getActivity()).setNavigationButtonToDefault();


        toolbarTitle.setText("New Appointment");
        toolbarBtn.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).changeFragment(New_Appointment.class);
            }
        });

        setSpinner();
        return parent;
    }

    public void setSpinner(){
        listForSpinner = new ArrayList<>();
        for (CarItem carItem:myCars){
            listForSpinner.add(carItem.getBrand()+" "+carItem.getModel());
        }
        adapterMyCars = new ArrayAdapter<String>(getContext(),R.layout.spinner_item,R.id.brandItem,listForSpinner);
        myCarsSpinner.setAdapter(adapterMyCars);

        myCarsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date(((MainActivity)getActivity()).getNewAppointmentDate()));
                System.out.println("Time "+((MainActivity)getActivity()).getNewAppointmentTime());
                System.out.println("Date"+date);
                System.out.println("position"+position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}

