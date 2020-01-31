package com.globalsovy.carserviceapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CalendarView;
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
    TextView pickDate;
    CalendarView calendarView;

    Animation scaleDown;
    Animation scaleUp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_new_appointment, container, false);

        navigationView = parent.findViewById(R.id.nav_view);
        toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarBtn = getActivity().findViewById(R.id.toolbarTool);
        toolbar = getActivity().findViewById(R.id.toolbar);
        scrollView = parent.findViewById(R.id.scrollOnNewAppoint);
        pickDate = parent.findViewById(R.id.pickDisplayDate);
        calendarView = parent.findViewById(R.id.calendarView);

        scaleDown = AnimationUtils.loadAnimation(getContext(),R.anim.scale_up_down);
        scaleUp = AnimationUtils.loadAnimation(getContext(),R.anim.scale_down_toup);

        ((MainActivity)getActivity()).setNavigationButtonToDefault();


        toolbarTitle.setText("Select Date & Time");
        toolbarBtn.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.drawable.close);
        calendarView.setVisibility(View.GONE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).changeFragment(MyAppointments_fragment.class);
            }
        });

        scaleUp.setAnimationListener(new Animation.AnimationListener() {
            //z hora na dol
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                calendarView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        scaleDown.setAnimationListener(new Animation.AnimationListener() {
            //z dola na hor
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                calendarView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (calendarView.getVisibility() == View.GONE){
                    calendarView.startAnimation(scaleUp);
                }else {
                    calendarView.startAnimation(scaleDown);
                }
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                String date = formatDay(day)+" "+formatMonth(month)+" "+year;
                pickDate.setText(date);
                calendarView.setVisibility(View.GONE);
            }
        });

        return parent;
    }

    public String formatDay(int day){
        if (day<10){
            return "0"+day+".";
        }else {
            return ""+day+".";
        }
    }

    public String formatMonth(int month){
        String m="";
        switch (month) {
            case 1:
                m="January";
                break;
            case 2:
                m="February";
                break;
            case 3:
                m="March";
                break;
            case 4:
                m="April";
                break;
            case 5:
                m="May";
                break;
            case 6:
                m="June";
                break;
            case 7:
                m="July";
                break;
            case 8:
                m="August";
                break;
            case 9:
                m="September";
                break;
            case 10:
                m="October";
                break;
            case 11:
                m="November";
                break;
            case 12:
                m="December";
                break;

        }
        return m;
    }
}
