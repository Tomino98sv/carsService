package com.globalsovy.carserviceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    TextView firstName;
    TextView lastName;
    TextView email;

    MySharedPreferencies mySharedPreferencies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mySharedPreferencies = new MySharedPreferencies(this);

        firstName = findViewById(R.id.firstNameTest);
        lastName = findViewById(R.id.lastNameTest);
        email = findViewById(R.id.emailTest);

        firstName.setText(mySharedPreferencies.getFnameLogin());
        lastName.setText(mySharedPreferencies.getLnameLogin());
        email.setText(mySharedPreferencies.getEmailLogin());
    }


}
