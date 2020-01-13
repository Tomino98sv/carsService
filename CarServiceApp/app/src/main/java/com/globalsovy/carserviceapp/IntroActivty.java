package com.globalsovy.carserviceapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class IntroActivty extends AppCompatActivity implements View.OnClickListener, Animation.AnimationListener {

    ImageView carFa;
    ImageView describtion;
    ConstraintLayout parent;

    ImageView carSVG;

    Animation moveSlowlyDown;
    Animation moveFastForward;

    int width;
    int height;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_layout);

        carFa = findViewById(R.id.carFaIntro);
        describtion = findViewById(R.id.descLogo);
        carSVG = findViewById(R.id.carSVG);
        parent = findViewById(R.id.parentIntro);

        parent.setOnClickListener(this);

        getScreenDimension();
        setAnimation();

    }

    @Override
    public void onClick(View view) {
        describtion.setVisibility(View.GONE);
        carSVG.startAnimation(moveSlowlyDown);
    }

    public void setAnimation(){
        moveSlowlyDown = AnimationUtils.loadAnimation(this,R.anim.car_back);
        moveFastForward = AnimationUtils.loadAnimation(this,R.anim.car_forward);
        moveFastForward.setAnimationListener(this);

        moveSlowlyDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                carSVG.startAnimation(moveFastForward);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

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

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == moveFastForward) {
            Intent login = new Intent(IntroActivty.this,LoginActivity.class);
            startActivity(login);
            overridePendingTransition(0, 0);
            finish();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onBackPressed() {
        // Here you want to show the user a dialog box
        ExitAlertDialog dialog = new ExitAlertDialog();
        dialog.showDialog(IntroActivty.this,"Exit application","Are you sure?");
    }
}