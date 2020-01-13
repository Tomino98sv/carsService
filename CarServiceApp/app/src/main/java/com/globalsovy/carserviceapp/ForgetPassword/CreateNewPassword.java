package com.globalsovy.carserviceapp.ForgetPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.globalsovy.carserviceapp.LoginActivity;
import com.globalsovy.carserviceapp.R;

public class CreateNewPassword extends AppCompatActivity {

    EditText newPassword;
    TextView labelNewPassword;
    TextView validationNewPassword;

    EditText repeadPassword;
    TextView labelRepeadPassword;
    TextView validationRepeadPassword;

    TextView text;
    Button createPassword;
    TextView backToLogin;

    String email = "example@gmail.com";
    boolean newPasswordValid = false;
    boolean repeadPasswordValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_password);

        newPassword = findViewById(R.id.newPasswordEditText);
        labelNewPassword = findViewById(R.id.labelNewPassword);
        validationNewPassword = findViewById(R.id.validationNewPassword);

        repeadPassword = findViewById(R.id.Repead_newPasswordEditText);
        labelRepeadPassword = findViewById(R.id.labelRepead_newPassword);
        validationRepeadPassword = findViewById(R.id.validationRepead_NewPassword);

        text = findViewById(R.id.createPasswordText);
        createPassword = findViewById(R.id.createPassword);
        backToLogin = findViewById(R.id.backToLogin);

        email = getIntent().getStringExtra("email");

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(CreateNewPassword.this, LoginActivity.class);
                startActivity(login);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        createPassword.setEnabled(false);
        createPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createPass = new Intent(CreateNewPassword.this,LoginActivity.class);
                startActivity(createPass);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        setNewPassword_toWhite();
        setFocusOnNewPass();
        setFocusOnRepeadNewPass();
        setTextChangeListenerOnNewPassword();
        setTextChangeListenerOnNewRepeadPassword();
    }

    public void setNewPassword_toWhite() {
        String text = email+"\n" + getString(R.string.createNewPassword);
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)),0,email.length(),0);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)),email.length()+13,email.length()+25,0);
        this.text.setText(spannableString);
    }
    public void setFocusOnNewPass() {
        newPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){
                    newPassword.setBackgroundColor(getResources().getColor(R.color.white));
                    newPassword.setTextColor(getResources().getColor(R.color.black));
                    labelNewPassword.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    labelNewPassword.setTextColor(getResources().getColor(R.color.white));
                }else{
                    newPassword.setBackground(getResources().getDrawable(R.drawable.fill_hard));
                    newPassword.setTextColor(getResources().getColor(R.color.white));
                    labelNewPassword.setBackgroundColor(getResources().getColor(R.color.white));
                    labelNewPassword.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });
    }
    public void setFocusOnRepeadNewPass() {
        repeadPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){
                    repeadPassword.setBackgroundColor(getResources().getColor(R.color.white));
                    repeadPassword.setTextColor(getResources().getColor(R.color.black));
                    labelRepeadPassword.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    labelRepeadPassword.setTextColor(getResources().getColor(R.color.white));
                }else{
                    repeadPassword.setBackground(getResources().getDrawable(R.drawable.fill_hard));
                    repeadPassword.setTextColor(getResources().getColor(R.color.white));
                    labelRepeadPassword.setBackgroundColor(getResources().getColor(R.color.white));
                    labelRepeadPassword.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });
    }
    public void setTextChangeListenerOnNewPassword() {
        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                newPasswordValid = editable.length()>=6;
                if(newPasswordValid) {
                    validationNewPassword.setTextColor(getResources().getColor(R.color.green));
                    validationNewPassword.setText("Correct");
                }else {
                    validationNewPassword.setTextColor(getResources().getColor(R.color.red));
                    validationNewPassword.setText("New password must be at least 6 char long");
                }
                enable_disableBTN();
            }
        });
    }
    public void setTextChangeListenerOnNewRepeadPassword() {
        repeadPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                repeadPasswordValid = editable.toString().equals(newPassword.getText().toString());
                if(repeadPasswordValid) {
                    validationRepeadPassword.setTextColor(getResources().getColor(R.color.green));
                    validationRepeadPassword.setText("Correct");
                }else {
                    validationRepeadPassword.setTextColor(getResources().getColor(R.color.red));
                    validationRepeadPassword.setText("Passwords must match.");
                }
                enable_disableBTN();
            }
        });
    }

    public void enable_disableBTN() {
        if (newPasswordValid && repeadPasswordValid) {
            createPassword.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            createPassword.setTextColor(getResources().getColor(R.color.white));
            createPassword.setEnabled(true);
        }else {
            createPassword.setBackgroundColor(getResources().getColor(R.color.buttonLoginColor));
            createPassword.setTextColor(getResources().getColor(R.color.buttonLoginColor));
            createPassword.setEnabled(false);
        }
    }
}
