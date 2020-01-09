package com.globalsovy.carserviceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class RegistrationActivity extends AppCompatActivity {


    EditText nameInp;
    TextView nameLabel;
    TextView validName;

    EditText surnameInp;
    TextView surnameLabel;
    TextView validSurName;

    EditText emailInp;
    TextView emailLabel;
    TextView validMail;

    EditText passwordInp;
    TextView passwordLabel;
    TextView validPassword;

    EditText repeadPasswordInp;
    TextView repeadPasswordLabel;
    TextView validRepeadPassword;


    TextView backToLogin;
    TextView registrationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registrationText = findViewById(R.id.quickRegistration);
        backToLogin = findViewById(R.id.backToLogin);

        nameInp = findViewById(R.id.nameEditTextInp);
        nameLabel = findViewById(R.id.labelName);
        validName = findViewById(R.id.validationName);

        surnameInp = findViewById(R.id.surnameEditTextInp);
        surnameLabel = findViewById(R.id.labelSurname);
        validSurName = findViewById(R.id.validationSurname);

        emailInp = findViewById(R.id.emailEditTextInp);
        emailLabel = findViewById(R.id.labelEmail);
        validMail = findViewById(R.id.validationEmail);

        passwordInp = findViewById(R.id.passwordEditTextInt);
        passwordLabel = findViewById(R.id.labelPassword);
        validPassword = findViewById(R.id.validationPassword);

        repeadPasswordInp = findViewById(R.id.passwordRepeadEditTextInt);
        repeadPasswordLabel = findViewById(R.id.labelRepeadPassword);
        validRepeadPassword = findViewById(R.id.validationRepeadPassword);

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
