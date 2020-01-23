package com.globalsovy.carserviceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.globalsovy.carserviceapp.Fragments.MyAppointments_fragment;
import com.globalsovy.carserviceapp.Fragments.MyCars_fragment;
import com.globalsovy.carserviceapp.Fragments.MyProfile_fragment;
import com.globalsovy.carserviceapp.Fragments.Notifications_fragment;
import com.globalsovy.carserviceapp.alertDialogs.BackToLoginAlertDialog;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    MySharedPreferencies mySharedPreferencies;
    RequestQueue myQueue;


    TextView navFirstName;
    TextView navEmail;
    TextView MyProfile;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mySharedPreferencies = new MySharedPreferencies(this);
        myQueue = Volley.newRequestQueue(this);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.mainActivityDrawable);
        navigationView = findViewById(R.id.nav_view);

        navFirstName = findViewById(R.id.firstName);
        navEmail = findViewById(R.id.email);
        MyProfile = findViewById(R.id.btnMyProfile);
        logout = findViewById(R.id.logOut);

        navFirstName.setSelected(true);
        navEmail.setSelected(true);

        navFirstName.setText(mySharedPreferencies.getFnameLogin()+" "+mySharedPreferencies.getLnameLogin());
        navEmail.setText(mySharedPreferencies.getEmailLogin());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

//        int: navigation_drawer_open String resource to describe the "open drawer" action for accessibility
//        int: navigation_drawer_close String resource to describe the "close drawer" action for accessibility

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
//        Synchronize the state of the drawer indicator/affordance with the linked DrawerLayout.


        View closeNav = navigationView.getHeaderView(0);
        closeNav = closeNav.findViewById(R.id.closeImage);
        closeNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        MyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(MyProfile_fragment.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                for (int i=0; i< navigationView.getMenu().size();i++) {
                    navigationView.getMenu().getItem(i).setChecked(false);
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MyCars_fragment()).commit();
            navigationView.setCheckedItem(R.id.my_cars);
        }

    }

    public void logOutRequest() {
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            BackToLoginAlertDialog dialog = new BackToLoginAlertDialog();
            dialog.showDialog(MainActivity.this,"Log Out","You will be routed back to login screen");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.my_cars:
                if (!menuItem.isChecked()){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new MyCars_fragment()).commit();
                    navigationView.setCheckedItem(R.id.my_cars);
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
            case R.id.appointments:
                if (!menuItem.isChecked()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new MyAppointments_fragment()).commit();
                    navigationView.setCheckedItem(R.id.appointments);
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
            case R.id.notifications:
                if (!menuItem.isChecked()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new Notifications_fragment()).commit();
                    navigationView.setCheckedItem(R.id.notifications);
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
        }
        return false;
    }

    public void logout() {
        String URL = mySharedPreferencies.getIp()+"/logout";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                mySharedPreferencies.setToken("logouted");
                startActivity(login);
                finish();
                Toast.makeText(getBaseContext(),"See you soon",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(),"Error "+error.getMessage(),Toast.LENGTH_SHORT).show();
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
                    body.put("login",mySharedPreferencies.getLogin());
                    body.put("token",mySharedPreferencies.getToken());

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

    public void changeFragment(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragment).commit();
    }

    public void setNavigationButtonToDefault(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        navigationView.setNavigationItemSelectedListener(this);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
//        Synchronize the stat
    }
}
