package com.example.app.auth;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app.R;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize ViewPager2
        androidx.viewpager2.widget.ViewPager2 viewPager = findViewById(R.id.viewPager);
        AuthPagerAdapter adapter = new AuthPagerAdapter(this);
        viewPager.setAdapter(adapter);
    }

    public void showLogin() {
        androidx.viewpager2.widget.ViewPager2 viewPager = findViewById(R.id.viewPager);
        viewPager.setCurrentItem(0, true);
    }

    public void showSignup() {
        androidx.viewpager2.widget.ViewPager2 viewPager = findViewById(R.id.viewPager);
        viewPager.setCurrentItem(1, true);
    }
}