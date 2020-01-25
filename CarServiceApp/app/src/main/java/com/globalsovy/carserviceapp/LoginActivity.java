package com.globalsovy.carserviceapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.globalsovy.carserviceapp.ForgetPassword.EmailForResetPassword;
import com.globalsovy.carserviceapp.POJO.Credencials;
import com.globalsovy.carserviceapp.POJO.UserInfo;
import com.globalsovy.carserviceapp.alertDialogs.ExitAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity implements Animation.AnimationListener {

    TextView welcomeBack;

    RelativeLayout loginContainer;
    EditText loginInp;
    TextView loginLabel;
    TextView validLogin;

    RelativeLayout passwordContainer;
    EditText passwordInp;
    TextView passwordLabel;
    TextView validPassword;
    ImageView eye;

    boolean passVisible = false;

    ImageView carSVG;
    Button loginBtn;
    TextView notRegistred;
    TextView forgotPassword;

    String login="";
    String password="";
    int onlyModifing=0;
    boolean loginValidate = false;
    boolean passwordValidate = false;
    ImageView carFaLogin;

    int width = 0;
    int height = 0;

    Animation rotate;
    TranslateAnimation moveToRightTop;
    Animation fadeIn;

    MySharedPreferencies mySharedPreferencies;
    RequestQueue myQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        mySharedPreferencies = new MySharedPreferencies(this);
        myQueue = Volley.newRequestQueue(this);

        welcomeBack = findViewById(R.id.welcomeBack);
        notRegistred = findViewById(R.id.notRegistred);
        setLogIn_toWhite();

        loginContainer = findViewById(R.id.loginInp);
        loginInp = findViewById(R.id.signInLoginTextInp);
        loginLabel = findViewById(R.id.labelSignInLogin);
        validLogin = findViewById(R.id.validationSignInLogin);
        passwordContainer = findViewById(R.id.passwordInt);
        passwordInp = findViewById(R.id.passwordEditTextInt);
        passwordLabel = findViewById(R.id.labelPassword);
        validPassword = findViewById(R.id.validationPassword);
        loginBtn = findViewById(R.id.logInButton);
        carSVG = findViewById(R.id.carSVGLogin);
        carFaLogin = findViewById(R.id.carFaLogin);
        forgotPassword = findViewById(R.id.forgetPass);
        eye = findViewById(R.id.eyeLogin);

        loginContainer.setVisibility(View.INVISIBLE);
        passwordContainer.setVisibility(View.INVISIBLE);
        welcomeBack.setVisibility(View.INVISIBLE);
        loginBtn.setVisibility(View.INVISIBLE);
        notRegistred.setVisibility(View.INVISIBLE);
        forgotPassword.setVisibility(View.INVISIBLE);

        if (!mySharedPreferencies.getToken().equals("logouted")){
            Intent main = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(main);
            overridePendingTransition(0, 0);
            finish();
        }

        loginBtn.setEnabled(false);

        loginInp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){
                    loginInp.setBackgroundColor(getResources().getColor(R.color.white));
                    loginInp.setTextColor(getResources().getColor(R.color.black));
                    loginLabel.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    loginLabel.setTextColor(getResources().getColor(R.color.white));
                }else{
                    loginInp.setBackground(getResources().getDrawable(R.drawable.fill_hard));
                    loginInp.setTextColor(getResources().getColor(R.color.white));
                    loginLabel.setBackgroundColor(getResources().getColor(R.color.white));
                    loginLabel.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });

        passwordInp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){
                    passwordInp.setBackgroundColor(getResources().getColor(R.color.white));
                    passwordInp.setTextColor(getResources().getColor(R.color.black));
                    passwordLabel.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    passwordLabel.setTextColor(getResources().getColor(R.color.white));
                    eye.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                }
                else{
                    passwordInp.setBackground(getResources().getDrawable(R.drawable.fill_hard));
                    passwordInp.setTextColor(getResources().getColor(R.color.white));
                    passwordLabel.setBackgroundColor(getResources().getColor(R.color.white));
                    passwordLabel.setTextColor(getResources().getColor(R.color.black));
                    eye.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                }
            }
        });

        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passVisible){
                    passwordInp.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eye.setImageResource(R.drawable.eye);
                    passVisible = false;
                }else {
                    passwordInp.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    eye.setImageResource(R.drawable.eye_hidden);
                    passVisible = true;
                }
            }
        });

        notRegistred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registration = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(registration);
                overridePendingTransition(0, 0);
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotPassword = new Intent(LoginActivity.this, EmailForResetPassword.class);
                startActivity(forgotPassword);
                overridePendingTransition(0,0);
            }
        });

        getScreenDimension();
        introAnimation();
        enterKeyListenerOnLogin();
        enterKeyListenerOnPassword();
        setPasswordValidate();
        setEmailValidate();
        setLoginButton();

        try {
            login = getIntent().getStringExtra("login");
            password = getIntent().getStringExtra("password");
            if (!login.equals("") && !password.equals("")){
                loginInp.setText(login);
                passwordInp.setText(password);

                loginInp.requestFocus();
                loginInp.clearFocus();
                passwordInp.requestFocus();
                passwordInp.clearFocus();

                loginValidate = true;
                passwordValidate = true;
                enable_disableLoginBTN();
            }
        }catch (NullPointerException e){

        }
    }

    public void enterKeyListenerOnLogin() {
        loginInp.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    enable_disableLoginBTN();
                    if (onlyModifing>1){
                        hideKeyboardFrom(LoginActivity.this,view);
                        loginInp.clearFocus();
                    }
                    onlyModifing++;
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
                    passwordInp.clearFocus();
                    hideKeyboardFrom(LoginActivity.this,v);
                }
                return false;
            }
        });
    }
    public void setPasswordValidate() {
        passwordInp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                passwordValidate = editable.length() >= 6;
                if (!passwordValidate){
                    validPassword.setTextColor(getResources().getColor(R.color.red));
                    validPassword.setText("password must be at least 6 length short");
                }
                if (passwordValidate){
                    validPassword.setTextColor(getResources().getColor(R.color.green));
                    validPassword.setText("Correct");
                }
            }
        });
    }
    public void setEmailValidate() {
        loginInp.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                loginValidate = s.length() >= 3;
                if (!loginValidate){
                    validLogin.setTextColor(getResources().getColor(R.color.red));
                    validLogin.setText("login must be at least 3 length short");
                }
                if (loginValidate)
                {
                    validLogin.setTextColor(getResources().getColor(R.color.green));
                    validLogin.setText("Correct");
                }
            }
        });
    }
    public void setLoginButton() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginRequest();
            }
        });
    }

    public void loginRequest() {
        String URL = mySharedPreferencies.getIp()+"/login";

        login = loginInp.getText().toString();
        password = passwordInp.getText().toString();

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                Intent main = new Intent(LoginActivity.this,MainActivity.class);
                try {
                    JSONObject token = response.getJSONObject("token");
                    Credencials credencials = new Credencials(token.getString("login"),token.getString("token"));
                    JSONObject userinfo = response.getJSONObject("userinfo");
                    UserInfo userInfo = new UserInfo(
                            userinfo.getInt("id"),
                            userinfo.getString("first_name"),
                            userinfo.getString("last_name"),
                            userinfo.getString("email"),
                            password,
                            userinfo.getInt("confirmed")==1
                    );
                    mySharedPreferencies.fillLoginData(credencials,userInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(main);
                loginInp.setText("");
                validLogin.setText("");
                passwordInp.setText("");
                validPassword.setText("");
                overridePendingTransition(0, 0);
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(com.android.volley.error.VolleyError error) {
                Log.i("VOLLEY","Error "+error.networkResponse.statusCode);
                if(error.networkResponse.statusCode==404) {
                    Toast.makeText(LoginActivity.this,"Wrong login or password",Toast.LENGTH_LONG).show();
                }
                if(error.networkResponse.statusCode==401) {
                    String email = "Ecample";
                    try {
                        email = new String(error.networkResponse.data,"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Intent unconfirmedEmail = new Intent(LoginActivity.this,UnconfirmedEmail.class);
                    unconfirmedEmail.putExtra("email",email);
                    unconfirmedEmail.putExtra("login",login);
                    unconfirmedEmail.putExtra("password",password);
                    startActivity(unconfirmedEmail);
                    overridePendingTransition(0, 0);
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

    public void setLogIn_toWhite(){
        String text = getString(R.string.welcomeBack);
        SpannableString spannable = new SpannableString(text);
        // here we set the color
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), text.length()-6, text.length(), 0);
        welcomeBack.setText(spannable);
    }
    public void enable_disableLoginBTN(){
        if (passwordValidate && loginValidate) {
            loginBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            loginBtn.setTextColor(getResources().getColor(R.color.white));
            loginBtn.setEnabled(true);
        }else {
            loginBtn.setBackgroundColor(getResources().getColor(R.color.buttonLoginColor));
            loginBtn.setTextColor(getResources().getColor(R.color.buttonLoginColor));
            loginBtn.setEnabled(false);
        }
    }
    public void showToast(String text){
        Toast.makeText(LoginActivity.this,text,Toast.LENGTH_SHORT).show();

    }
    public void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void getScreenDimension(){
        WindowManager wm = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
    }
    public void introAnimation(){
        width = (width/2)- (width/100*30);
        height = (height/2)- (height/100*10);
        moveToRightTop = new TranslateAnimation(width,0,height,0);//(xFrom,xTo, yFrom,yTo)
        moveToRightTop.setDuration(900);
        moveToRightTop.setFillAfter(true);

        moveToRightTop.setAnimationListener(this);

        rotate = AnimationUtils.loadAnimation(this,R.anim.rotate);
        fadeIn = AnimationUtils.loadAnimation(this,R.anim.fade_in);

        carFaLogin.setAnimation(moveToRightTop);
        carFaLogin.startAnimation(moveToRightTop);
        carSVG.startAnimation(rotate);

    }

    @Override
    public void onBackPressed() {
        // Here you want to show the user a dialog box
        ExitAlertDialog dialog = new ExitAlertDialog();
        dialog.showDialog(LoginActivity.this,"Exit application","Are you sure?");
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation.equals(moveToRightTop)){
            loginContainer.setVisibility(View.INVISIBLE);
            passwordContainer.setVisibility(View.INVISIBLE);
            welcomeBack.setVisibility(View.INVISIBLE);
            loginBtn.setVisibility(View.INVISIBLE);
            notRegistred.setVisibility(View.INVISIBLE);
            forgotPassword.setVisibility(View.INVISIBLE);


            loginContainer.startAnimation(fadeIn);
            passwordContainer.startAnimation(fadeIn);
            welcomeBack.startAnimation(fadeIn);
            loginBtn.startAnimation(fadeIn);
            notRegistred.startAnimation(fadeIn);
            forgotPassword.startAnimation(fadeIn);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}

