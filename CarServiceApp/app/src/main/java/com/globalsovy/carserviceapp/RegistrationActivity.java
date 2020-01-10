package com.globalsovy.carserviceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity implements View.OnFocusChangeListener {


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
    Button registerButton;

    boolean nameValidation = false;
    boolean surnameValidation = false;
    boolean emailValidation = false;
    boolean passwordValidation = false;
    boolean repeadPasswordValidation = false;

    int onlyModifingName = 0;
    int onlyModifingSurname = 0;
    int onlyModifindEmail = 0;
    int onlyModifindPassword = 0;
    int onlyModifindRepeadPassword = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registerButton = findViewById(R.id.registerButton);
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

        registerButton.setEnabled(false);

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
        setOnChangeListeners();
        setRegistrationButton();

        setAddTextChangeListener(nameInp);
        setAddTextChangeListener(surnameInp);
        setAddTextChangeListener(emailInp);
        setAddTextChangeListener(passwordInp);
        setAddTextChangeListener(repeadPasswordInp);

        enterKeyListenerOnSurname();
        enterKeyListenerOnEmail();
        enterKeyListenerOnName();
        enterKeyListenerOnPassword();
        enterKeyListenerOnRepeadPassword();
    }

    public void setRegistration_toWhite(){
        String text = getString(R.string.quickRegistration);
        SpannableString spannable = new SpannableString(text);
        // here we set the color
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), text.length()-13, text.length(), 0);
        registrationText.setText(spannable);
    }
    public void setRegistrationButton(){
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("Registration Reqeust sent");
            }
        });
    }
    public void setOnChangeListeners() {
        nameInp.setOnFocusChangeListener(this);
        surnameInp.setOnFocusChangeListener(this);
        emailInp.setOnFocusChangeListener(this);
        passwordInp.setOnFocusChangeListener(this);
        repeadPasswordInp.setOnFocusChangeListener(this);
    }
    public void showToast(String text){
        Toast.makeText(RegistrationActivity.this,text,Toast.LENGTH_SHORT).show();
    }
    public void enable_disableLoginBTN(){
        if (nameValidation && emailValidation && passwordValidation && repeadPasswordValidation && surnameValidation) {
            registerButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            registerButton.setTextColor(getResources().getColor(R.color.white));
            registerButton.setEnabled(true);
        }else {
            registerButton.setBackgroundColor(getResources().getColor(R.color.buttonLoginColor));
            registerButton.setTextColor(getResources().getColor(R.color.buttonLoginColor));
            registerButton.setEnabled(false);
        }
    }
    public void setAddTextChangeListener(final EditText current){
        current.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (current.getId() == nameInp.getId()){
                    if (!nameValidation) {
                        validName.setTextColor(getResources().getColor(R.color.red));
                        if (charSequence.length() <3){
                            validName.setText("surname must at least 3 length short");
                        }else {
                            validName.setText("surname can't be longer then 9 chars");
                        }
                    }
                    return;
                }
                if (current.getId() == surnameInp.getId()){
                    if (!surnameValidation){
                        validSurName.setTextColor(getResources().getColor(R.color.red));
                        if (charSequence.length() <3){
                            validSurName.setText("surname must at least 3 length short");
                        }else {
                            validSurName.setText("surname can't be longer then 9 chars");
                        }
                    }
                    return;
                }
                if (current.getId() == emailInp.getId()){
                    if (!emailValidation){
                        validMail.setTextColor(getResources().getColor(R.color.red));
                        validMail.setText("InValid email form");
                    }
                    return;
                }
                if (current.getId() == passwordInp.getId()){
                    if (!passwordValidation){
                        validPassword.setTextColor(getResources().getColor(R.color.red));
                        validPassword.setText("name must at least 6 length short");
                    }
                    return;
                }
                if (current.getId() == repeadPasswordInp.getId()){
                    if (!repeadPasswordValidation){
                        validRepeadPassword.setTextColor(getResources().getColor(R.color.red));
                        validRepeadPassword.setText("Repeadet password and password must be same");
                    }
                    return;
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            if (current.getId() == nameInp.getId()){
                nameValidation = (editable.length() >= 3 && editable.length() < 10);
                if (nameValidation) {
                    validName.setTextColor(getResources().getColor(R.color.green));
                    validName.setText("Correct");
                }
                return;
            }
            if (current.getId() == surnameInp.getId()){
                surnameValidation = (editable.length() >= 3 && editable.length() < 10);
                if (surnameValidation){
                    validSurName.setTextColor(getResources().getColor(R.color.green));
                    validSurName.setText("Correct");
                }
                return;
            }
            if (current.getId() == emailInp.getId()){
                emailValidation = isEmailValid(editable);
                if (emailValidation){
                    validMail.setTextColor(getResources().getColor(R.color.green));
                    validMail.setText("Correct");
                }
                return;
            }
            if (current.getId() == passwordInp.getId()){
                passwordValidation = editable.length() >= 6;
                if (passwordValidation){
                    validPassword.setTextColor(getResources().getColor(R.color.green));
                    validPassword.setText("Correct");
                }
                return;
            }
            if (current.getId() == repeadPasswordInp.getId()){
                repeadPasswordValidation = editable.toString().equals(passwordInp.getText().toString());
                if (repeadPasswordValidation){
                    validRepeadPassword.setTextColor(getResources().getColor(R.color.green));
                    validRepeadPassword.setText("Correct");
                }
                return;
            }
                enable_disableLoginBTN();
            }
        });
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {

        if (view.getId() == nameInp.getId()){
            if (hasFocus){
                nameInp.setBackgroundColor(getResources().getColor(R.color.white));
                nameInp.setTextColor(getResources().getColor(R.color.black));
                nameLabel.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                nameLabel.setTextColor(getResources().getColor(R.color.white));
            }else{
                nameInp.setBackground(getResources().getDrawable(R.drawable.fill_hard));
                nameInp.setTextColor(getResources().getColor(R.color.white));
                nameLabel.setBackgroundColor(getResources().getColor(R.color.white));
                nameLabel.setTextColor(getResources().getColor(R.color.black));
            }
        }
        if (view.getId() == surnameInp.getId()){
            if (hasFocus){
                surnameInp.setBackgroundColor(getResources().getColor(R.color.white));
                surnameInp.setTextColor(getResources().getColor(R.color.black));
                surnameLabel.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                surnameLabel.setTextColor(getResources().getColor(R.color.white));
            }else{
                surnameInp.setBackground(getResources().getDrawable(R.drawable.fill_hard));
                surnameInp.setTextColor(getResources().getColor(R.color.white));
                surnameLabel.setBackgroundColor(getResources().getColor(R.color.white));
                surnameLabel.setTextColor(getResources().getColor(R.color.black));
            }
        }
        if (view.getId() == emailInp.getId()){
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
        if (view.getId() == passwordInp.getId()){
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
        if (view.getId() == repeadPasswordInp.getId()){
            if (hasFocus){
                repeadPasswordInp.setBackgroundColor(getResources().getColor(R.color.white));
                repeadPasswordInp.setTextColor(getResources().getColor(R.color.black));
                repeadPasswordLabel.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                repeadPasswordLabel.setTextColor(getResources().getColor(R.color.white));
            }
            else{
                repeadPasswordInp.setBackground(getResources().getDrawable(R.drawable.fill_hard));
                repeadPasswordInp.setTextColor(getResources().getColor(R.color.white));
                repeadPasswordLabel.setBackgroundColor(getResources().getColor(R.color.white));
                repeadPasswordLabel.setTextColor(getResources().getColor(R.color.black));
            }
        }

    }

    private boolean isEmailValid(CharSequence s) {
        Pattern sPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]{3,10}\\.{1}[A-Z]{2,6}$",Pattern.CASE_INSENSITIVE);
        return sPattern.matcher(s).matches();
    }

    public void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void enterKeyListenerOnEmail() {
        emailInp.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    enable_disableLoginBTN();
                    if (onlyModifindEmail>1){
                        hideKeyboardFrom(RegistrationActivity.this,view);
                        emailInp.clearFocus();
                    }
                    onlyModifindEmail++;
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
                    if (onlyModifindPassword>1){
                        hideKeyboardFrom(RegistrationActivity.this,v);
                        passwordInp.clearFocus();
                    }
                    onlyModifindPassword++;

                }
                return false;
            }
        });
    }
    public void enterKeyListenerOnName() {
        nameInp.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    enable_disableLoginBTN();
                    if (onlyModifingName>1){
                        hideKeyboardFrom(RegistrationActivity.this,view);
                        nameInp.clearFocus();
                    }
                    onlyModifingName++;
                }
                return false;
            }
        });
    }
    public void enterKeyListenerOnSurname() {
        surnameInp.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    enable_disableLoginBTN();
                    if (onlyModifingSurname>1){
                        hideKeyboardFrom(RegistrationActivity.this,v);
                        surnameInp.clearFocus();
                    }
                    onlyModifingSurname++;

                }
                return false;
            }
        });
    }
    public void enterKeyListenerOnRepeadPassword() {
        repeadPasswordInp.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    enable_disableLoginBTN();
                    if (onlyModifindRepeadPassword>1){
                        hideKeyboardFrom(RegistrationActivity.this,view);
                        repeadPasswordInp.clearFocus();
                    }
                    onlyModifindRepeadPassword++;
                }
                return false;
            }
        });
    }

}
