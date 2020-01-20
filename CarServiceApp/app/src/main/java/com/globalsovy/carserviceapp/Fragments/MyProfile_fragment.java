package com.globalsovy.carserviceapp.Fragments;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.globalsovy.carserviceapp.MySharedPreferencies;
import com.globalsovy.carserviceapp.R;

public class MyProfile_fragment extends Fragment {

    TextView login;
    TextView name;
    TextView surname;
    TextView email;
    TextView password;
    ImageView eye;
    CheckBox accountConfirmed;

    boolean passVisible=false;

    MySharedPreferencies mySharedPreferencies;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent =  inflater.inflate(R.layout.fragment_my_profile,container,false);
        mySharedPreferencies = new MySharedPreferencies(getContext());
        login = parent.findViewById(R.id.loginProfile);
        name = parent.findViewById(R.id.nameProfile);
        surname = parent.findViewById(R.id.sureNameProfile);
        email = parent.findViewById(R.id.emailProfile);
        password = parent.findViewById(R.id.passwordProfile);
        eye = parent.findViewById(R.id.hideShowPass);
        accountConfirmed = parent.findViewById(R.id.accountConfirmedProfile);

        login.setText(mySharedPreferencies.getLogin());
        name.setText(mySharedPreferencies.getFnameLogin());
        surname.setText(mySharedPreferencies.getLnameLogin());
        email.setText(mySharedPreferencies.getEmailLogin());
        password.setText("******");
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
                    password.setText("******");
                    passVisible=false;
                }else{
                    password.setText(mySharedPreferencies.getPassword());
                    passVisible=true;
                }
            }
        });

        return parent;
    }
}
