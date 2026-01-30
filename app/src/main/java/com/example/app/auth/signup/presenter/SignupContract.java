package com.example.app.auth.signup.presenter;

public interface SignupContract {
    interface View {
        void showSignupSuccess();

        void showSignupError(String message);

        void showLoading();

        void hideLoading();
    }

    interface Presenter {
        void signup(String username, String email, String password);
    }
}
