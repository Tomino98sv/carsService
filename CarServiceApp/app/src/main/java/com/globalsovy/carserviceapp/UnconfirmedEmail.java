package com.globalsovy.carserviceapp;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.globalsovy.carserviceapp.ForgetPassword.ConfirmDigitCode;
import com.globalsovy.carserviceapp.ForgetPassword.CreateNewPassword;

public class UnconfirmedEmail extends AppCompatActivity {

    Button confirmMail;
    TextView text;
    TextView backToLogin;

    String email="Example@gmail.com";

    EditText firstD;
    EditText secondD;
    EditText thirdD;
    EditText fourthD;
    EditText fifthD;
    EditText sixthD;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_digit_code);

        confirmMail = findViewById(R.id.verifyCode);
        text = findViewById(R.id.sentCodeToMailText);

        firstD = findViewById(R.id.firstDigit);
        secondD = findViewById(R.id.secondDigit);
        thirdD = findViewById(R.id.thirdDigit);
        fourthD = findViewById(R.id.fourthDigit);
        fifthD = findViewById(R.id.fifthDigit);
        sixthD = findViewById(R.id.sixthDigit);
        backToLogin = findViewById(R.id.backToLogin);

        email = getIntent().getStringExtra("email");

        confirmMail.setEnabled(false);
        confirmMail.setText("Confirm Email Adress");
        confirmMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createNewPassword = new Intent(UnconfirmedEmail.this, MainActivity.class);
                createNewPassword.putExtra("email",email);
                startActivity(createNewPassword);
                overridePendingTransition(0, 0);
            }
        });

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(UnconfirmedEmail.this, LoginActivity.class);
                startActivity(login);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        setPassword_toWhite();
        setFocus(firstD);
        setFocus(secondD);
        setFocus(thirdD);
        setFocus(fourthD);
        setFocus(fifthD);
        setFocus(sixthD);

        setTextChangeListener(firstD);
        setTextChangeListener(secondD);
        setTextChangeListener(thirdD);
        setTextChangeListener(fourthD);
        setTextChangeListener(fifthD);
        setTextChangeListener(sixthD);
    }

    public void setPassword_toWhite() {
        String text = email+"\n"+getString(R.string.uncomfirmMail);
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)),0,email.length(),0);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)),email.length()+13,email.length()+22,0);
        this.text.setText(spannableString);
    }

    public void setFocus(final EditText charInp) {
        charInp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){
                    charInp.setBackgroundColor(getResources().getColor(R.color.white));
                    charInp.setTextColor(getResources().getColor(R.color.black));
                }else{
                    charInp.setBackground(getResources().getDrawable(R.drawable.fill_hard));
                    charInp.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });
    }

    public void setTextChangeListener(final EditText charInp) {
        charInp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String result = firstD.getText().toString()
                        +secondD.getText().toString()
                        +thirdD.getText().toString()
                        +fourthD.getText().toString()
                        +fifthD.getText().toString()
                        +sixthD.getText().toString();
                if (result.length()<6){
                    confirmMail.setBackgroundColor(getResources().getColor(R.color.buttonLoginColor));
                    confirmMail.setTextColor(getResources().getColor(R.color.buttonLoginColor));
                    confirmMail.setEnabled(false);
                }else {
                    confirmMail.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    confirmMail.setTextColor(getResources().getColor(R.color.white));
                    confirmMail.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(charInp.getId() == firstD.getId()) { secondD.requestFocus();}
                if(charInp.getId() == secondD.getId()) { thirdD.requestFocus();}
                if(charInp.getId() == thirdD.getId()) { fourthD.requestFocus();}
                if(charInp.getId() == fourthD.getId()) { fifthD.requestFocus();}
                if(charInp.getId() == fifthD.getId()) { sixthD.requestFocus();}
            }
        });
    }
}
