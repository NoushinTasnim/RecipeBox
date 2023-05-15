package com.example.quotepad.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.quotepad.R;
import com.example.quotepad.main.user.UserActivity;

public class SplashActivity extends Activity {

    private static int Splash_Screen = 3200;
    ImageView image;
    ImageView splash;
    LottieAnimationView lottieAnimationView;

    Animation topAnim;
    Animation bottomAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        image = findViewById(R.id.logo_img);
        lottieAnimationView = findViewById(R.id.recipe_logo_json);
        splash = findViewById(R.id.splash_bg);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        //Set animation to elements
        image.setAnimation(topAnim);
        lottieAnimationView.setAnimation(bottomAnim);

        image.animate().translationY(2400).setDuration(1000).setStartDelay(2500);
        lottieAnimationView.animate().translationY(3000).setDuration(1000).setStartDelay(2500);
        splash.animate().translationY(6800).setDuration(1000).setStartDelay(2500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, UserActivity.class);
                startActivity(intent);
                finish();
            }
        },Splash_Screen);
    }
}