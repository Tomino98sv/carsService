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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class New_Appointment extends Fragment {

    Toolbar toolbar;
    NavigationView navigationView;
    TextView toolbarTitle;
    ImageView toolbarBtn;

    ScrollView scrollView;
    TextView pickDate;
    CalendarView calendarView;

    Animation hide;
    Animation show;

    String monthYear="";
    String dayMonthYear="";

    String resultDateReq="";

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
        initializeTimes(parent);

        hide = AnimationUtils.loadAnimation(getContext(),R.anim.hide);
        show = AnimationUtils.loadAnimation(getContext(),R.anim.show);
        ((MainActivity)getActivity()).setNavigationButtonToDefault();


        toolbarTitle.setText("Select Date & Time");
        toolbarBtn.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.drawable.close);
        calendarView.setVisibility(View.VISIBLE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).changeFragment(MyAppointments_fragment.class);
            }
        });

        show.setAnimationListener(new Animation.AnimationListener() {
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

        hide.setAnimationListener(new Animation.AnimationListener() {
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
                    calendarView.startAnimation(show);
                    pickDate.setText(monthYear);
                }else {
                    calendarView.startAnimation(hide);
                    dayMonthYear = new SimpleDateFormat("dd/MM/yyyy").format(new Date(calendarView.getDate()));
                    String[] array = dayMonthYear.split("/",-1);
                    dayMonthYear = formatDay(Integer.valueOf(array[0]))+" "+formatMonth(Integer.valueOf(array[1]))+" "+array[2];
                    pickDate.setText(dayMonthYear);
                }

                resultDateReq = new SimpleDateFormat("dd.MM.yyyy").format(new Date(calendarView.getDate()));

            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                monthYear = formatMonth(month)+" "+year;
                dayMonthYear = formatDay(day)+" "+formatMonth(month)+" "+year;
                pickDate.setText(dayMonthYear);
                calendarView.startAnimation(hide);

                String dayString = day<10 ? "0"+day : day+"";
                String monthString = month<10 ? "0"+month : month+"";
                resultDateReq = dayString+"."+monthString+"."+year;
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

    public void initializeTimes(View parent) {
        for (int i=8;i<17;i++){
            String name="t"+i;
            name+="00";
            int resourceId = this.getResources().
                    getIdentifier(name,"id",getContext().getPackageName());
            final TextView textViewnul = parent.findViewById(resourceId);
            if (textViewnul!=null){
                textViewnul.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String test = new SimpleDateFormat("dd.MM.yyyy").format(new Date(calendarView.getDate()));
                        System.out.println("TEST "+ test);
                        ((MainActivity)getActivity()).setNewAppointmentDate(resultDateReq);
                        ((MainActivity)getActivity()).setNewAppointmentTime(textViewnul.getText().toString());
                        ((MainActivity)getActivity()).changeFragment(Details_New_Appointment.class);
                    }
                });
            }


            name="t"+i;
            name+="30";
            resourceId = this.getResources().
                    getIdentifier(name,"id",getContext().getPackageName());
            final TextView textViewhalf = parent.findViewById(resourceId);
            if (textViewhalf!=null){
                textViewhalf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String test = new SimpleDateFormat("dd.MM.yyyy").format(new Date(calendarView.getDate()));
                        System.out.println("TEST "+ test);
                        ((MainActivity)getActivity()).setNewAppointmentDate(resultDateReq);
                        ((MainActivity)getActivity()).setNewAppointmentTime(textViewhalf.getText().toString());
                        ((MainActivity)getActivity()).changeFragment(Details_New_Appointment.class);
                    }
                });
            }
        }
    }
}
