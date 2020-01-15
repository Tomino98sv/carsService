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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.globalsovy.carserviceapp.LoginActivity;
import com.globalsovy.carserviceapp.MySharedPreferencies;
import com.globalsovy.carserviceapp.R;
import com.globalsovy.carserviceapp.UnconfirmedEmail;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ConfirmDigitCode extends AppCompatActivity {

    Button verifyCode;
    TextView text;
    TextView backToLogin;
    TextView errorMessage;

    String email="Example@gmail.com";
    String sixDigCode="";

    EditText firstD;
    EditText secondD;
    EditText thirdD;
    EditText fourthD;
    EditText fifthD;
    EditText sixthD;

    MySharedPreferencies mySharedPreferencies;
    RequestQueue myQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_digit_code);

        mySharedPreferencies = new MySharedPreferencies(this);
        myQueue = Volley.newRequestQueue(this);

        verifyCode = findViewById(R.id.verifyCode);
        text = findViewById(R.id.sentCodeToMailText);
        errorMessage = findViewById(R.id.errorMessage);

        firstD = findViewById(R.id.firstDigit);
        secondD = findViewById(R.id.secondDigit);
        thirdD = findViewById(R.id.thirdDigit);
        fourthD = findViewById(R.id.fourthDigit);
        fifthD = findViewById(R.id.fifthDigit);
        sixthD = findViewById(R.id.sixthDigit);
        backToLogin = findViewById(R.id.backToLogin);

        email = getIntent().getStringExtra("email");


        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(ConfirmDigitCode.this, LoginActivity.class);
                startActivity(login);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        verifyCode.setEnabled(false);
        verifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmCodeToChange();
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
                errorMessage.setText("");
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
                    sixDigCode = result;
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

    public void confirmCodeToChange() {
        String URL = mySharedPreferencies.getIp()+"/abletochangepassword";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Intent createNewPassword = new Intent(ConfirmDigitCode.this,CreateNewPassword.class);
                createNewPassword.putExtra("email",email);
                overridePendingTransition(0, 0);
                startActivity(createNewPassword);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error "+error);
                if(error.networkResponse.statusCode==401) {
                    errorMessage.setTextColor(getResources().getColor(R.color.red));
                    errorMessage.setText("Invalid code entered");
                }
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() {
                try {
                    JSONObject body = new JSONObject();
                    body.put("email",email);
                    body.put("code",sixDigCode);

                    String bodyString = body.toString();
                    return bodyString == null ? null : bodyString.getBytes("utf-8");
                } catch (UnsupportedEncodingException | JSONException uee) {
                    return null;
                }
            }


        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        myQueue.add(stringRequest);
    }


}
