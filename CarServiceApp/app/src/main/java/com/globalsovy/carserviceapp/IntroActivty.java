package com.globalsovy.carserviceapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.globalsovy.carserviceapp.alertDialogs.ExitAlertDialog;

public class IntroActivty extends AppCompatActivity {

    ImageView carFa;
    ImageView describtion;
    ConstraintLayout parent;

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_layout);

        carFa = findViewById(R.id.carFaIntro);
        describtion = findViewById(R.id.descLogo);
        parent = findViewById(R.id.parentIntro);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                describtion.setVisibility(View.GONE);
                Intent login = new Intent(IntroActivty.this,LoginActivity.class);
                startActivity(login);
                overridePendingTransition(0, 0);
                finish();
            }
        };

        handler.postDelayed(runnable,800);

    }
    @Override
    public void onBackPressed() {
        // Here you want to show the user a dialog box
        ExitAlertDialog dialog = new ExitAlertDialog();
        dialog.showDialog(IntroActivty.this,"Exit application","Are you sure?");
    }
}