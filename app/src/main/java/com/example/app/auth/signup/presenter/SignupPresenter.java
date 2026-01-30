package com.example.app.auth.signup.presenter;

import com.google.firebase.auth.FirebaseAuth;

public class SignupPresenter implements SignupContract.Presenter {

    private final SignupContract.View view;
    private final FirebaseAuth auth;

    public SignupPresenter(SignupContract.View view) {
        this.view = view;
        this.auth = FirebaseAuth.getInstance();
    }

    @Override
    public void signup(String username, String email, String password) {
        view.showLoading();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    view.hideLoading();
                    if (task.isSuccessful()) {
                        view.showSignupSuccess();
                    } else {
                        view.showSignupError(
                                task.getException() != null ? task.getException().getMessage() : "Signup failed");
                    }
                });
    }
}
