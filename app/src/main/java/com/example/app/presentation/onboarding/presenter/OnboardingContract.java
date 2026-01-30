package com.example.app.presentation.onboarding.presenter;

public interface OnboardingContract {
    interface View {
        void navigateToAuth();

        void setPage(int page);
    }

    interface Presenter {
        void onNextClicked(int currentItem);

        void onSkipClicked();
    }
}
