package com.globalsovy.carserviceapp;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    TextView welcomeBack;

    EditText emailInp;
    EditText passwordInp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        welcomeBack = findViewById(R.id.welcomeBack);
        setLogIn_toWhite();

        emailInp = findViewById(R.id.emailInp);
        passwordInp = findViewById(R.id.passwordInt);


//        emailInp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                emailInp.setBackgroundColor(Color.argb(120,0,0,0));
//            }
//        });
//
//        passwordInp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                emailInp.setBackgroundColor(Color.argb(120,0,0,0));
//            }
//        });
    }

    public void setLogIn_toWhite(){
        String text = getString(R.string.welcomeBack);
        SpannableString spannable = new SpannableString(text);
        // here we set the color
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), text.length()-6, text.length(), 0);
        welcomeBack.setText(spannable);
    }
}
