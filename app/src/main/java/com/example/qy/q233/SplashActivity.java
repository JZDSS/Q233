package com.example.qy.q233;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * Created by Xu Yining on 2017/3/27.
 */

public class SplashActivity extends Activity {
    static int apiVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        apiVersion = android.os.Build.VERSION.SDK_INT;

        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, 0);
        animation.setDuration(100);
        animation.setFillAfter(true);
        ImageView imageView = (ImageView) findViewById(R.id.loading);
        ObjectAnimator.ofFloat(imageView, "rotation", 0F, 6000F).setDuration(100).start();

        int SPLASH_DISPLAY_LENGHT = 1000;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this,
                        Start.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);

    }

}
