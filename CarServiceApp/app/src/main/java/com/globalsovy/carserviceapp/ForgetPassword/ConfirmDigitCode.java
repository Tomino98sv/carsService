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

import com.globalsovy.carserviceapp.R;

public class ConfirmDigitCode extends AppCompatActivity {

    Button verifyCode;
    TextView text;
    String email="Example@gmail.com";

    EditText firstD;
    EditText secondD;
    EditText thirdD;
    EditText fourthD;
    EditText fifthD;
    EditText sixthD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_digit_code);

        verifyCode = findViewById(R.id.verifyCode);
        text = findViewById(R.id.sentCodeToMailText);

        firstD = findViewById(R.id.firstDigit);
        secondD = findViewById(R.id.secondDigit);
        thirdD = findViewById(R.id.thirdDigit);
        fourthD = findViewById(R.id.fourthDigit);
        fifthD = findViewById(R.id.fifthDigit);
        sixthD = findViewById(R.id.sixthDigit);

        verifyCode.setEnabled(false);
        verifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createNewPassword = new Intent(ConfirmDigitCode.this,CreateNewPassword.class);
                startActivity(createNewPassword);
                overridePendingTransition(0, 0);
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
        String text = email+"\n"+getString(R.string.codeSent);
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)),0,email.length(),0);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)),email.length()+23,email.length()+35,0);
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
                    verifyCode.setBackgroundColor(getResources().getColor(R.color.buttonLoginColor));
                    verifyCode.setTextColor(getResources().getColor(R.color.buttonLoginColor));
                    verifyCode.setEnabled(false);
                }else {
                    verifyCode.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    verifyCode.setTextColor(getResources().getColor(R.color.white));
                    verifyCode.setEnabled(true);
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
