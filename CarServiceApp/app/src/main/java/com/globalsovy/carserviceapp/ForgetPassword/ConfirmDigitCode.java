package com.globalsovy.carserviceapp.ForgetPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.globalsovy.carserviceapp.R;

public class ConfirmDigitCode extends AppCompatActivity {

    Button verifyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_digit_code);

        verifyCode = findViewById(R.id.verifyCode);

        verifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createNewPassword = new Intent(ConfirmDigitCode.this,CreateNewPassword.class);
                startActivity(createNewPassword);
                overridePendingTransition(0, 0);
            }
        });
    }
}
