package com.example.a24a_10357_finalproject.UI_Controllers;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a24a_10357_finalproject.R;
import com.example.a24a_10357_finalproject.UI_Controllers.StartActivity;
import com.google.android.material.imageview.ShapeableImageView;

public class SplashActivity extends AppCompatActivity {

    private ShapeableImageView splash_IMG_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splash_IMG_logo = findViewById(R.id.splash_IMG_logo);
        startAnimation(splash_IMG_logo);
    }

    private void startAnimation(View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels; //screen height

        //start state:
        view.setY(-height / 2.0f);
        view.setScaleX(0.0f);
        view.setScaleY(0.0f);
        view.setAlpha(0.0f);

        view.animate()
                .alpha(1.0f)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .rotation(360)
                .translationY(0)
                .setDuration(4000)
                .setInterpolator(new OvershootInterpolator())
                .setListener(
                        new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(@NonNull Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(@NonNull Animator animation) {
                                transactToStartActivity();
                            }

                            @Override
                            public void onAnimationCancel(@NonNull Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(@NonNull Animator animation) {
                            }
                        }
                );
    }

    private void transactToStartActivity() {
        startActivity(new Intent(this, StartActivity.class));
        finish();
    }
}