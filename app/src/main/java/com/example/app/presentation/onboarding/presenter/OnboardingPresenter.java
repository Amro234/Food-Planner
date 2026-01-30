package com.example.app.presentation.onboarding.presenter;

import android.content.Context;
import com.example.app.Helper.SaveState;

public class OnboardingPresenter implements OnboardingContract.Presenter {
    private final OnboardingContract.View view;
    private final SaveState saveState;

    public OnboardingPresenter(OnboardingContract.View view, Context context) {
        this.view = view;
        this.saveState = new SaveState(context, "on_boarding");
    }

    @Override
    public void onNextClicked(int currentItem) {
        if (currentItem < 2) {
            view.setPage(currentItem + 1);
        } else {
            completeOnboarding();
        }
    }

    @Override
    public void onSkipClicked() {
        completeOnboarding();
    }

    private void completeOnboarding() {
        saveState.setState(1);
        view.navigateToAuth();
    }
}
