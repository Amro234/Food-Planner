package com.example.app.presentation.splash.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.Helper.SaveState;
import com.example.app.R;

public class SplashActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private Runnable runnable;
    private SaveState saveState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        saveState = new SaveState(this, "on_boarding");

        runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (saveState.getState() != 1) {
                    // Onboarding not finished, go to Onboarding
                    intent = new Intent(SplashActivity.this,
                            com.example.app.presentation.onboarding.view.OnboardingActivity.class);
                } else {
                    // Onboarding finished, check Auth or Guest
                    boolean isGuest = saveState.isGuest();
                    boolean isLoggedIn = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser() != null;

                    if (isLoggedIn || isGuest) {
                        intent = new Intent(SplashActivity.this, com.example.app.MainActivity.class);
                    } else {
                        intent = new Intent(SplashActivity.this, com.example.app.auth.AuthActivity.class);
                    }
                }
                startActivity(intent);
                finish();
            }
        };
        handler.postDelayed(runnable, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }
}
