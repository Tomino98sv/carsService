package com.globalsovy.carserviceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    TextView firstName;
    TextView lastName;
    TextView email;

    Button logOut;

    MySharedPreferencies mySharedPreferencies;
    RequestQueue myQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mySharedPreferencies = new MySharedPreferencies(this);

        firstName = findViewById(R.id.firstNameTest);
        lastName = findViewById(R.id.lastNameTest);
        email = findViewById(R.id.emailTest);
        logOut = findViewById(R.id.logOut);

        firstName.setText(mySharedPreferencies.getFnameLogin());
        lastName.setText(mySharedPreferencies.getLnameLogin());
        email.setText(mySharedPreferencies.getEmailLogin());

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutRequest();
            }
        });
    }

    public void logOutRequest() {
        finish();
    }


}
