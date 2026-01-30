package com.example.app.auth.login.presenter;

import android.content.Context;
import com.example.app.Helper.SaveState;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View view;
    private final SaveState saveState;

    public LoginPresenter(LoginContract.View view, Context context) {
        this.view = view;
        this.saveState = new SaveState(context, "on_boarding");
    }

    @Override
    public void login(String email, String password) {
        // Future: Implement Firebase login logic
        view.showLoading();
        // Placeholder success
        view.hideLoading();
        view.onLoginSuccess();
        view.navigateToHome();
    }

    @Override
    public void loginAsGuest() {
        saveState.setGuest(true);
        view.navigateToHome();
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
