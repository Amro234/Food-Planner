package com.example.app.auth.login.presenter;

public interface LoginContract {
    interface View {
        void showLoading();

        void hideLoading();

        void showError(String message);

        void navigateToHome();

        void onLoginSuccess();
    }

    interface Presenter {
        void login(String email, String password);

        void loginAsGuest();

        void onDestroy();
    }
}
