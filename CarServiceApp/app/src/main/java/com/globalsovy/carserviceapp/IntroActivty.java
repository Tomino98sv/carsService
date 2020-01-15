package com.globalsovy.carserviceapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class IntroActivty extends AppCompatActivity implements View.OnClickListener {

    ImageView carFa;
    ImageView describtion;
    ConstraintLayout parent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_layout);

        carFa = findViewById(R.id.carFaIntro);
        describtion = findViewById(R.id.descLogo);
        parent = findViewById(R.id.parentIntro);

        parent.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        describtion.setVisibility(View.GONE);
        Intent login = new Intent(IntroActivty.this,LoginActivity.class);
        startActivity(login);
        overridePendingTransition(0, 0);
        finish();
    }



    @Override
    public void onBackPressed() {
        // Here you want to show the user a dialog box
        ExitAlertDialog dialog = new ExitAlertDialog();
        dialog.showDialog(IntroActivty.this,"Exit application","Are you sure?");
    }
}