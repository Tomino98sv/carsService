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
    TextView emailLabel;
    EditText passwordInp;
    TextView passwordLabel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        welcomeBack = findViewById(R.id.welcomeBack);
        setLogIn_toWhite();

        emailInp = findViewById(R.id.emailEditTextInp);
        emailLabel = findViewById(R.id.labelEmail);
        passwordInp = findViewById(R.id.passwordEditTextInt);
        passwordLabel = findViewById(R.id.labelPassword);

        emailInp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){
                    emailInp.setBackgroundColor(getResources().getColor(R.color.white));
                    emailInp.setTextColor(getResources().getColor(R.color.black));
                    emailLabel.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    emailLabel.setTextColor(getResources().getColor(R.color.white));
                }else{
                    emailInp.setBackground(getResources().getDrawable(R.drawable.fill_hard));
                    emailInp.setTextColor(getResources().getColor(R.color.white));
                    emailLabel.setBackgroundColor(getResources().getColor(R.color.white));
                    emailLabel.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });

        passwordInp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){
                    passwordInp.setBackgroundColor(getResources().getColor(R.color.white));
                    passwordInp.setTextColor(getResources().getColor(R.color.black));
                    passwordLabel.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    passwordLabel.setTextColor(getResources().getColor(R.color.white));
                }else{
                    passwordInp.setBackground(getResources().getDrawable(R.drawable.fill_hard));
                    passwordInp.setTextColor(getResources().getColor(R.color.white));
                    passwordLabel.setBackgroundColor(getResources().getColor(R.color.white));
                    passwordLabel.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });

    }

    public void setLogIn_toWhite(){
        String text = getString(R.string.welcomeBack);
        SpannableString spannable = new SpannableString(text);
        // here we set the color
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), text.length()-6, text.length(), 0);
        welcomeBack.setText(spannable);
    }
}
