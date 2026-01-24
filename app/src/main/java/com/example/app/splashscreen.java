package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.Helper.SaveState;

public class splashscreen extends AppCompatActivity {

    // Handler is used for scheduling the transition from splash activity to main
    // activity
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
                if (saveState.getState() == 1) {
                    intent = new Intent(splashscreen.this, MainActivity.class);
                } else {
                    intent = new Intent(splashscreen.this, on_boarding.class);
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
