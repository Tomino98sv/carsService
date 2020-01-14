package com.globalsovy.carserviceapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.globalsovy.carserviceapp.ForgetPassword.ConfirmDigitCode;
import com.globalsovy.carserviceapp.ForgetPassword.CreateNewPassword;
import com.globalsovy.carserviceapp.POJO.Credencials;
import com.globalsovy.carserviceapp.POJO.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class UnconfirmedEmail extends AppCompatActivity {

    Button confirmMail;
    TextView text;
    TextView backToLogin;
    TextView errorMessage;

    String email="Example@gmail.com";
    String sixDigCode = "";
    String login = "";
    String password = "";

    EditText firstD;
    EditText secondD;
    EditText thirdD;
    EditText fourthD;
    EditText fifthD;
    EditText sixthD;

    MySharedPreferencies mySharedPreferencies;
    RequestQueue myQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_digit_code);

        mySharedPreferencies = new MySharedPreferencies(this);
        myQueue = Volley.newRequestQueue(this);

        confirmMail = findViewById(R.id.verifyCode);
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
        login = getIntent().getStringExtra("login");
        password = getIntent().getStringExtra("password");

        confirmMail.setEnabled(false);
        confirmMail.setText("Confirm Email Adress");
        confirmMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmAccountRequest();
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
                errorMessage.setText("");
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

    public void confirmAccountRequest() {
        String URL = mySharedPreferencies.getIp()+"/confirmuser";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loginRequest();
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
    public void loginRequest() {
        String URL = mySharedPreferencies.getIp()+"/login";

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                Intent main = new Intent(UnconfirmedEmail.this,MainActivity.class);

                try {
                    JSONObject token = response.getJSONObject("token");
                    Credencials credencials = new Credencials(token.getString("login"),token.getString("token"));
                    JSONObject userinfo = response.getJSONObject("userinfo");
                    UserInfo userInfo = new UserInfo(
                            userinfo.getInt("id"),
                            userinfo.getString("first_name"),
                            userinfo.getString("last_name"),
                            userinfo.getString("email"),
                            userinfo.getInt("confirmed")==1
                    );
                    mySharedPreferencies.fillLoginData(credencials,userInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(main);
                overridePendingTransition(0, 0);
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("VOLLEY","Error "+error.networkResponse.statusCode);
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
                    body.put("login",login);
                    body.put("password",password);
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
