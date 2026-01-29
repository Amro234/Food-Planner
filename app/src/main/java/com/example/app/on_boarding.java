package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import com.example.app.Adapters.OnBoardingAdapter;
import com.example.app.Helper.SaveState;

import android.view.WindowManager;

public class on_boarding extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private OnBoardingAdapter adapter;
    private TextView[] dots;
    private CardView nextCard;
    private SaveState saveState;
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide status bar and make layout full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout._on_boarding);

        viewPager = findViewById(R.id.slider);
        dotsLayout = findViewById(R.id.dotsLayout);
        nextCard = findViewById(R.id.next_card);
        adapter = new OnBoardingAdapter(this);
        viewPager.setAdapter(adapter);
        addDots(0);

        saveState = new SaveState(this, "on_boarding");

        viewPager.addOnPageChangeListener(changeListener);

        nextCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = viewPager.getCurrentItem();
                if (current < 2) {
                    viewPager.setCurrentItem(current + 1);
                } else {
                    saveOneTimeState();
                }
            }
        });
    }

    private void saveOneTimeState() {
        saveState.setState(1);
        Intent intent = new Intent(on_boarding.this, com.example.app.auth.AuthActivity.class);
        startActivity(intent);
        finish();
    }

    private void addDots(int position) {
        dots = new TextView[3];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;")); // Bullet character
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.white3)); // Inactive dot color

            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.blue3)); // Active dot color
        }
    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            currentPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };
}