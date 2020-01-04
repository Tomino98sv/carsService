package com.globalsovy.carserviceapp;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class IntroActivty extends AppCompatActivity implements View.OnClickListener, Animation.AnimationListener {

    ImageView carFa;
    ImageView describtion;

    Animation fadeOut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_layout);

        carFa = findViewById(R.id.carFaIntro);
        describtion = findViewById(R.id.descLogo);

        fadeOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        fadeOut.setAnimationListener(this);

        carFa.setOnClickListener(this);
        describtion.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        carFa.startAnimation(fadeOut);
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == fadeOut) {
            Intent login = new Intent(IntroActivty.this,LoginActivity.class);
            startActivity(login);
            finish();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
