package com.globalsovy.carserviceapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.globalsovy.carserviceapp.MainActivity;
import com.globalsovy.carserviceapp.MySharedPreferencies;
import com.globalsovy.carserviceapp.R;
import com.google.android.material.navigation.NavigationView;


public class MyProfile_fragment extends Fragment {

    TextView login;
    TextView name;
    TextView surname;
    TextView email;
    EditText password;
    ImageView eye;
    CheckBox accountConfirmed;

    NavigationView navigationView;
    TextView toolbarTitle;
    ImageView toolbarBtn;

    boolean passVisible=false;

    MySharedPreferencies mySharedPreferencies;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View parent =  inflater.inflate(R.layout.fragment_my_profile_read,container,false);
        mySharedPreferencies = new MySharedPreferencies(getContext());

        login = parent.findViewById(R.id.loginProfile);
        name = parent.findViewById(R.id.nameProfile);
        surname = parent.findViewById(R.id.sureNameProfile);
        email = parent.findViewById(R.id.emailProfile);
        password = parent.findViewById(R.id.passwordProfile);
        eye = parent.findViewById(R.id.hideShowPass);
        accountConfirmed = parent.findViewById(R.id.accountConfirmedProfile);
        navigationView = parent.findViewById(R.id.nav_view);
        toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarBtn = getActivity().findViewById(R.id.toolbarTool);

        ((MainActivity)getActivity()).setNavigationButtonToDefault();

        password.setEnabled(false);

        toolbarTitle.setText("My Profile");
        toolbarBtn.setImageResource(R.drawable.edit_white);
        toolbarBtn.setVisibility(View.VISIBLE);

        toolbarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).changeFragment(Edit_MyProfile_fragment.class);
            }
        });

        System.out.println("Calling MyProfile_fragment oncreate again");

        System.out.println(mySharedPreferencies.getFnameLogin());
        System.out.println(mySharedPreferencies.getLnameLogin());
        System.out.println(mySharedPreferencies.getPassword());

        login.setText(mySharedPreferencies.getLogin());
        name.setText(mySharedPreferencies.getFnameLogin());
        password.setText(mySharedPreferencies.getPassword());
        surname.setText(mySharedPreferencies.getLnameLogin());
        email.setText(mySharedPreferencies.getEmailLogin());
        if (mySharedPreferencies.getConfirmedLogin()){
            accountConfirmed.setChecked(true);
        }else{
            accountConfirmed.setChecked(false);
        }
        accountConfirmed.setEnabled(false);
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passVisible){
                    eye.setImageResource(R.drawable.eye);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passVisible=false;
                }else{
                    eye.setImageResource(R.drawable.eye_hidden);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passVisible=true;
                }
            }
        });
        return parent;
    }
}
