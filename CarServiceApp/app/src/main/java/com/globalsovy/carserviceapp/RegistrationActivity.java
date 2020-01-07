package com.globalsovy.carserviceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

public class RegistrationActivity extends AppCompatActivity {

    TextView backToLogin;
    TextView registrationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registrationText = findViewById(R.id.quickRegistration);
        backToLogin = findViewById(R.id.backToLogin);

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registration = new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(registration);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        setRegistration_toWhite();
    }

    public void setRegistration_toWhite(){
        String text = getString(R.string.quickRegistration);
        SpannableString spannable = new SpannableString(text);
        // here we set the color
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), text.length()-13, text.length(), 0);
        registrationText.setText(spannable);
    }
}
