package com.globalsovy.carserviceapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.globalsovy.carserviceapp.MainActivity;
import com.globalsovy.carserviceapp.MySharedPreferencies;
import com.globalsovy.carserviceapp.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class Edit_MyProfile_fragment extends Fragment {

    TextView login;
    EditText name;
    EditText surname;
    TextView email;
    EditText password;
    ImageView eye;
    CheckBox accountConfirmed;

    NavigationView navigationView;
    TextView toolbarTitle;
    ImageView toolbarBtn;

    boolean passVisible=false;
    String firstNameValue="";
    String lastNanemValue="";
    String passwordValue="";

    MySharedPreferencies mySharedPreferencies;
    RequestQueue myQueue;

    boolean makeChange;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent =  inflater.inflate(R.layout.fragment_my_profile,container,false);
        mySharedPreferencies = new MySharedPreferencies(getContext());
        myQueue = Volley.newRequestQueue(parent.getContext());

        login = parent.findViewById(R.id.loginProfile);
        name = parent.findViewById(R.id.nameProfile);
        surname = parent.findViewById(R.id.sureNameProfile);
        email = parent.findViewById(R.id.emailProfile);
        password = parent.findViewById(R.id.passwordProfile);
        eye = parent.findViewById(R.id.hideShowPass);
        accountConfirmed = parent.findViewById(R.id.accountConfirmedProfile);
        navigationView = parent.findViewById(R.id.nav_view);
        toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarBtn = getActivity().findViewById(R.id.toolbarTool);

        ((MainActivity)getActivity()).setNavigationButtonToDefault();

        toolbarTitle.setText("My Profile");
        toolbarBtn.setImageResource(R.drawable.done);
        toolbarBtn.setVisibility(View.VISIBLE);

        login.setText(mySharedPreferencies.getLogin());
        name.setText(mySharedPreferencies.getFnameLogin());
        password.setText(mySharedPreferencies.getPassword());
        surname.setText(mySharedPreferencies.getLnameLogin());
        email.setText(mySharedPreferencies.getEmailLogin());
        if (mySharedPreferencies.getConfirmedLogin()){
            accountConfirmed.setChecked(true);
        }else{
            accountConfirmed.setChecked(false);
        }
        accountConfirmed.setEnabled(false);
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passVisible){
                    eye.setImageResource(R.drawable.eye);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passVisible=false;
                }else{
                    eye.setImageResource(R.drawable.eye_hidden);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passVisible=true;
                }
            }
        });
        makeChange = false;
        toolbarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (makeChange) {
                    if (!passwordValue.equals("")){
                        changePassword();
                    }
                    if (!firstNameValue.equals("")){
                        changeFname();
                    }
                    if (!lastNanemValue.equals("")){
                        changeLname();
                    }
                    ((MainActivity)getActivity()).changeFragment(MyProfile_fragment.class);
                }else {
                    ((MainActivity)getActivity()).changeFragment(MyProfile_fragment.class);
                }
            }
        });

        setAddTextChangeListener(name);
        setAddTextChangeListener(surname);
        setAddTextChangeListener(password);

        setEditorAction(name);
        setEditorAction(surname);
        setEditorAction(password);

        return parent;
    }

    public void setAddTextChangeListener(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editText.getId() == password.getId()){
                    passwordValue = editable.toString();
                    makeChange = true;

                }
                else if (editText.getId() == name.getId()){
                    firstNameValue = editable.toString();
                    makeChange = true;
                }
                else{
                    lastNanemValue = editable.toString();
                    makeChange = true;
                }
            }
        });
    }

    public void setEditorAction(final EditText editText) {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {

                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    if (name.getText().toString().equals(mySharedPreferencies.getFnameLogin())){
                        firstNameValue = "";
                    }
                    if (surname.getText().toString().equals(mySharedPreferencies.getLnameLogin())){
                        lastNanemValue = "";
                    }
                    if (password.getText().toString().equals(mySharedPreferencies.getFnameLogin())){
                        passwordValue = "";
                    }

                    if (password.getText().toString().equals(mySharedPreferencies.getPassword()) &&
                            name.getText().toString().equals(mySharedPreferencies.getFnameLogin()) &&
                            surname.getText().toString().equals(mySharedPreferencies.getLnameLogin())
                    ) {
                        makeChange = false;
                    }

                    return true;
                }

                return  false;
            }
        });
    }

    public void changeFname() {
        String URL = mySharedPreferencies.getIp()+"/changefirstname";
        mySharedPreferencies.setFnameLogin(name.getText().toString());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
                    body.put("id",mySharedPreferencies.getIdLogin());
                    body.put("new_fname",name.getText().toString());

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
    public void changeLname() {
        String URL = mySharedPreferencies.getIp()+"/changelastname";
        mySharedPreferencies.setLnameLogin(surname.getText().toString());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
                    body.put("id",mySharedPreferencies.getIdLogin());
                    body.put("new_lname",surname.getText().toString());

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
    public void changePassword() {
        String URL = mySharedPreferencies.getIp()+"/changepassword";
        mySharedPreferencies.setPassword(password.getText().toString());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
                    body.put("email",mySharedPreferencies.getEmailLogin());
                    body.put("password",password.getText().toString());

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
