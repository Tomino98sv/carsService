package com.globalsovy.carserviceapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    TextView welcomeBack;

    EditText emailInp;
    TextView emailLabel;
    TextView validMail;

    EditText passwordInp;
    TextView passwordLabel;
    TextView validPassword;

    ImageView carSVG;
    Button loginBtn;
    TextView notRegistred;

    String email="";
    String password="";
    int onlyModifing=0;
    boolean emailValidate = false;
    boolean passwordValidate = false;

    Animation rotate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        welcomeBack = findViewById(R.id.welcomeBack);
        notRegistred = findViewById(R.id.notRegistred);
        setLogIn_toWhite();

        emailInp = findViewById(R.id.emailEditTextInp);
        emailLabel = findViewById(R.id.labelEmail);
        validMail = findViewById(R.id.validationEmail);
        passwordInp = findViewById(R.id.passwordEditTextInt);
        passwordLabel = findViewById(R.id.labelPassword);
        validPassword = findViewById(R.id.validationPassword);
        loginBtn = findViewById(R.id.logInButton);
        carSVG = findViewById(R.id.carSVGLogin);

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

        notRegistred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registration = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(registration);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        animationFadeIn();

        enterKeyListenerOnEmail();
        enterKeyListenerOnPassword();
        setPasswordValidate();
        setEmailValidate();
        setLoginButton();
    }

    public void animationFadeIn() {
        rotate = AnimationUtils.loadAnimation(this,R.anim.rotate);
        carSVG.startAnimation(rotate);
    }
    public void enterKeyListenerOnEmail() {
        emailInp.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    enable_disableLoginBTN();
                    if (onlyModifing>1){
                        hideKeyboardFrom(LoginActivity.this,view);
                        emailInp.clearFocus();
                    }
                    onlyModifing++;
                }
                return false;
            }
        });
    }
    public void enterKeyListenerOnPassword() {
        passwordInp.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    enable_disableLoginBTN();
                    passwordInp.clearFocus();
                    hideKeyboardFrom(LoginActivity.this,v);
                }
                return false;
            }
        });
    }
    public void setPasswordValidate() {
        passwordInp.addTextChangedListener(new TextWatcher() {
            private boolean isValid(CharSequence pass) {
                if (pass.length() > 5){
                    return true;
                }else {
                    return false;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordValidate = isValid(charSequence);
                if (!passwordValidate){
                    validPassword.setTextColor(getResources().getColor(R.color.red));
                    validPassword.setText("InValid password form");
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (passwordValidate){
                    validPassword.setTextColor(getResources().getColor(R.color.green));
                    validPassword.setText("Valid password form");
                }
            }
        });
    }
    public void setEmailValidate() {
        emailInp.addTextChangedListener(new TextWatcher() {
            private final Pattern sPattern
                    = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.{1}[A-Z]{1,6}$",Pattern.CASE_INSENSITIVE);

            private boolean isValid(CharSequence s) {
                return sPattern.matcher(s).matches();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after){
                emailValidate = isValid(s);
                if (!emailValidate){
                    validMail.setTextColor(getResources().getColor(R.color.red));
                    validMail.setText("InValid mail form");
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (emailValidate)
                {
                    validMail.setTextColor(getResources().getColor(R.color.green));
                    validMail.setText("Valid mail form");
                }
            }
        });
    }
    public void setLoginButton() {
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
        if (passwordValidate && emailValidate) {
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
    public void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}

