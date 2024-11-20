package com.zebra.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.os.Handler;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView splashImage = findViewById(R.id.splash_image);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        splashImage.startAnimation(fadeIn);



        // Hide both the status bar and navigation bar for full-screen immersive mode
        WindowInsetsController controller = getWindow().getInsetsController();
        if (controller != null) {
            controller.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            controller.setSystemBarsBehavior(
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            );
        }

        // Delay for 3 seconds and then launch the MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, DataProcessActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000); // 3000 milliseconds = 3 seconds
    }
}
