package com.globalsovy.carserviceapp;

import android.content.Context;
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

public class IntroActivty extends AppCompatActivity implements View.OnClickListener, Animation.AnimationListener {

    ImageView carFa;
    ImageView describtion;

    TranslateAnimation moveToRightTop;
    Animation moveToLeft;
    AnimationSet animation;

    int width;
    int height;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_layout);

        carFa = findViewById(R.id.carFaIntro);
        describtion = findViewById(R.id.descLogo);

        carFa.setOnClickListener(this);
        describtion.setOnClickListener(this);

        getScreenDimension();
        setAnimation();

    }

    @Override
    public void onClick(View view) {
        carFa.setAnimation(animation);
        carFa.startAnimation(moveToRightTop);
        describtion.startAnimation(moveToLeft);
    }

    public void setAnimation(){
        moveToRightTop = new TranslateAnimation(0,(((0-width)/2)+200),0,(((0-height)/2)+100));//(xFrom,xTo, yFrom,yTo)
        moveToRightTop.setDuration(1100);
        moveToRightTop.setFillAfter(true);
        moveToRightTop.setAnimationListener(this);

        moveToLeft = AnimationUtils.loadAnimation(this,R.anim.fade_out);
        moveToLeft.setAnimationListener(this);

        animation = new AnimationSet(false);
        animation.addAnimation(moveToRightTop);
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
        if (animation == moveToRightTop) {
            Intent login = new Intent(IntroActivty.this,LoginActivity.class);
            startActivity(login);
            finish();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}