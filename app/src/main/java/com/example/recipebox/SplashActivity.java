package com.example.recipebox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recipebox.user_authentication.UserActivity;

public class SplashActivity extends Activity {

    private static int Splash_Screen = 2200;
    ImageView image;
    TextView logo;
    TextView tag;
    //ImageView splash;

    Animation topAnim;
    Animation bottomAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        image = findViewById(R.id.logo_img);
        logo = findViewById(R.id.logo_text);
        tag = findViewById(R.id.tagLine);
        //splash = findViewById(R.id.splash_bg);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        //Set animation to elements
        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        tag.setAnimation(bottomAnim);

        image.animate().translationY(2400).setDuration(1000).setStartDelay(2500);
        logo.animate().translationY(1400).setDuration(1000).setStartDelay(2500);
        tag.animate().translationY(1400).setDuration(1000).setStartDelay(2500);
        //splash.animate().translationY(6800).setDuration(1000).setStartDelay(2500);

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