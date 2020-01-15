package com.globalsovy.carserviceapp.ForgetPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.globalsovy.carserviceapp.LoginActivity;
import com.globalsovy.carserviceapp.R;

public class NotRegisterEmail extends AppCompatActivity {

    Button TryAnotherEmail;
    TextView backToLogin;
    TextView text;

    String email = "example@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_register_email);

        backToLogin = findViewById(R.id.backToLogin);
        TryAnotherEmail = findViewById(R.id.tryAnotherMail);
        text = findViewById(R.id.wrongEmailAddress);

        email = getIntent().getStringExtra("email");

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(NotRegisterEmail.this, LoginActivity.class);
                startActivity(login);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        TryAnotherEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overridePendingTransition(0, 0);
                finish();
            }
        });
        setEmail_toWhite();
    }

    public void setEmail_toWhite() {
        String text = email+"\n" + getString(R.string.emailNotRegister);
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)),0,email.length(),0);
        this.text.setText(spannableString);
    }

    @Override
    public void onBackPressed() {
        // Here you want to show the user a dialog box
        Intent emailForReset = new Intent(NotRegisterEmail.this,EmailForResetPassword.class);
        startActivity(emailForReset);
        overridePendingTransition(0, 0);
        finish();
    }
}
