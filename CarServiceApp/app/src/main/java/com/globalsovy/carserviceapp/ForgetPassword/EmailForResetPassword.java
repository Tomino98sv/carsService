package com.globalsovy.carserviceapp.ForgetPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.globalsovy.carserviceapp.LoginActivity;
import com.globalsovy.carserviceapp.R;
import com.globalsovy.carserviceapp.RegistrationActivity;

public class EmailForResetPassword extends AppCompatActivity {

    Button sendRequestForCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_for_reset_password);

        sendRequestForCode = findViewById(R.id.sendResetCode);

        sendRequestForCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registration = new Intent(EmailForResetPassword.this, ConfirmDigitCode.class);
                startActivity(registration);
                overridePendingTransition(0, 0);
            }
        });
    }
}
