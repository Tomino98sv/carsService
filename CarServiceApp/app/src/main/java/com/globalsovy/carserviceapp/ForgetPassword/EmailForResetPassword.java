package com.globalsovy.carserviceapp.ForgetPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.globalsovy.carserviceapp.ExitAlertDialog;
import com.globalsovy.carserviceapp.LoginActivity;
import com.globalsovy.carserviceapp.MySharedPreferencies;
import com.globalsovy.carserviceapp.R;
import com.globalsovy.carserviceapp.RegistrationActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

public class EmailForResetPassword extends AppCompatActivity {

    TextView text;

    RelativeLayout emailContainer;
    EditText emailInp;
    TextView emailLabel;
    TextView validEmail;

    Button sendRequestForCode;
    TextView backToLogin;

    boolean emailValid = false;
    int width = 0;
    int height = 0;

    MySharedPreferencies mySharedPreferencies;
    RequestQueue myQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_for_reset_password);

        mySharedPreferencies = new MySharedPreferencies(this);
        myQueue = Volley.newRequestQueue(this);

        emailContainer = findViewById(R.id.sendEmailInp);
        emailInp = findViewById(R.id.email_resertPass_EditTextInp);
        emailLabel = findViewById(R.id.labelEmail);
        validEmail = findViewById(R.id.validationEmail);
        text = findViewById(R.id.forgotPasswordText);
        sendRequestForCode = findViewById(R.id.sendResetCode);
        backToLogin = findViewById(R.id.backToLogin);

        sendRequestForCode.setEnabled(false);
        sendRequestForCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registration = new Intent(EmailForResetPassword.this, ConfirmDigitCode.class);
                registration.putExtra("email",emailInp.getText().toString());
                startActivity(registration);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(EmailForResetPassword.this,LoginActivity.class);
                startActivity(login);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        getScreenDimension();
        setPassword_toWhite();
        enterKeyListenerOnEmail();
        setFocus();
        setTextChangeListener();
    }

    public void setPassword_toWhite() {
        String text = getString(R.string.resetPassword);
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)),7,16,0);
        this.text.setText(spannableString);
    }
    public void enterKeyListenerOnEmail() {
        emailInp.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER){
                    enable_disableBTN();
                }
                return false;
            }
        });
    }
    public void enable_disableBTN() {
        if (emailValid) {
            sendRequestForCode.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            sendRequestForCode.setTextColor(getResources().getColor(R.color.white));
            sendRequestForCode.setEnabled(true);
        }else {
            sendRequestForCode.setBackgroundColor(getResources().getColor(R.color.buttonLoginColor));
            sendRequestForCode.setTextColor(getResources().getColor(R.color.buttonLoginColor));
            sendRequestForCode.setEnabled(false);
        }
    }
    public void setFocus() {
        emailInp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
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
        });
    }
    private boolean isEmailValid(CharSequence s) {
    Pattern sPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]{3,10}\\.{1}[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    return sPattern.matcher(s).matches();
    }
    public void setTextChangeListener() {
        emailInp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                    emailValid = isEmailValid(editable);
                    if (emailValid) {
                        validEmail.setTextColor(getResources().getColor(R.color.green));
                        validEmail.setText("Correct");
                    }
                    if (!emailValid) {
                        validEmail.setTextColor(getResources().getColor(R.color.red));
                        validEmail.setText("Invalid email form");
                    }
            }
        });
    }
    public void getScreenDimension(){
        WindowManager wm = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
    }

    public void sendCodeOnMailRequest() {
        String URL = mySharedPreferencies.getIp()+"/sendcodeforchangepassword";

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
}
