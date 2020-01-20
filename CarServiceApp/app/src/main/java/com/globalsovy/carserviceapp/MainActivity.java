package com.globalsovy.carserviceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.globalsovy.carserviceapp.Fragments.MyAppointments_fragment;
import com.globalsovy.carserviceapp.Fragments.MyCars_fragment;
import com.globalsovy.carserviceapp.Fragments.MyProfile_fragment;
import com.globalsovy.carserviceapp.alertDialogs.BackToLoginAlertDialog;
import com.globalsovy.carserviceapp.alertDialogs.ExitAlertDialog;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    MySharedPreferencies mySharedPreferencies;
    RequestQueue myQueue;


    TextView navFirstName;
    TextView navSureName;
    TextView navEmail;
    TextView MyProfile;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mySharedPreferencies = new MySharedPreferencies(this);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.mainActivityDrawable);
        navigationView = findViewById(R.id.nav_view);

        navFirstName = findViewById(R.id.firstName);
        navSureName = findViewById(R.id.sureName);
        navEmail = findViewById(R.id.email);
        MyProfile = findViewById(R.id.btnMyProfile);
        logout = findViewById(R.id.logOut);

        navFirstName.setText(mySharedPreferencies.getFnameLogin());
        navSureName.setText(mySharedPreferencies.getLnameLogin());
        navEmail.setText(mySharedPreferencies.getEmailLogin());

        setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MyProfile_fragment()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
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
        }
        return false;
    }
}
