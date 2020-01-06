package com.globalsovy.carserviceapp;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    TextView welcomeBack;

    EditText emailInp;
    TextView emailLabel;
    EditText passwordInp;
    TextView passwordLabel;
    Button loginBtn;

    String email="";
    String password="";

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
        loginBtn = findViewById(R.id.logInButton);

        loginBtn.setEnabled(false);

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
                }
                else{
                    passwordInp.setBackground(getResources().getDrawable(R.drawable.fill_hard));
                    passwordInp.setTextColor(getResources().getColor(R.color.white));
                    passwordLabel.setBackgroundColor(getResources().getColor(R.color.white));
                    passwordLabel.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("Login Reqeust sent");
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

    public void enable_disableLoginBTN(){
        email = emailInp.getText().toString();
        password = passwordInp.getText().toString();
        if (password.length() >0 && email.length()>0) {
            loginBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            loginBtn.setTextColor(getResources().getColor(R.color.white));
            loginBtn.setEnabled(true);
        }else {
            loginBtn.setBackgroundColor(getResources().getColor(R.color.buttonLoginColor));
            loginBtn.setTextColor(getResources().getColor(R.color.buttonLoginColor));
            loginBtn.setEnabled(false);
        }
    }
    public void showToast(String text){
        Toast.makeText(LoginActivity.this,text,Toast.LENGTH_SHORT).show();

    }

}


//passwordInp.addTextChangedListener(new TextWatcher() {
//@Override
//public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//        }
//
//@Override
//public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//        }
//
//@Override
//public void afterTextChanged(Editable editable) {
//
//        }
//        });
