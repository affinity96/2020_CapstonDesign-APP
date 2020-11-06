package com.example.homekippa;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

public class SplashScreen extends Activity {
    Animation anim;
    ConstraintLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 화면 가로 고정
        setContentView(R.layout.activity_splash_screen);
        layout=(ConstraintLayout)findViewById(R.id.layout_splashscreen); // Declare an imageView to show the animation.
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha); // Create the animation.
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplashScreen.this,StartActivity.class));
                // HomeActivity.class is the activity to go after showing the splash screen.
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        layout.startAnimation(anim);
    }


}
