package com.globalsovy.carserviceapp.ForgetPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_password);

        
    }
}
