package com.example.app.presentation.onboarding.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import com.example.app.R;
import com.example.app.auth.AuthActivity;
import com.example.app.presentation.onboarding.presenter.OnboardingContract;
import com.example.app.presentation.onboarding.presenter.OnboardingPresenter;

public class OnboardingActivity extends AppCompatActivity implements OnboardingContract.View {

    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private OnBoardingAdapter adapter;
    private TextView[] dots;
    private CardView nextCard;
    private Button skipButton;
    private OnboardingContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout._on_boarding);

        presenter = new OnboardingPresenter(this, this);

        viewPager = findViewById(R.id.slider);
        dotsLayout = findViewById(R.id.dotsLayout);
        nextCard = findViewById(R.id.next_card);
        skipButton = findViewById(R.id.button);

        adapter = new OnBoardingAdapter(this);
        viewPager.setAdapter(adapter);
        addDots(0);

        viewPager.addOnPageChangeListener(changeListener);

        nextCard.setOnClickListener(v -> presenter.onNextClicked(viewPager.getCurrentItem()));
        skipButton.setOnClickListener(v -> presenter.onSkipClicked());
    }

    @Override
    public void navigateToAuth() {
        Intent intent = new Intent(OnboardingActivity.this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void setPage(int page) {
        viewPager.setCurrentItem(page);
    }

    private void addDots(int position) {
        dots = new TextView[3];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;", Html.FROM_HTML_MODE_LEGACY));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.white3));
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.blue3));
        }
    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };
}
