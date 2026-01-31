package com.example.app.auth.signup.presenter;

import com.google.firebase.auth.FirebaseAuth;
import com.example.app.data.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupPresenter implements SignupContract.Presenter {

    private final SignupContract.View view;
    private final FirebaseAuth auth;
    private final FirebaseFirestore firestore;

    public SignupPresenter(SignupContract.View view) {
        this.view = view;
        this.auth = FirebaseAuth.getInstance();
        this.firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void signup(String username, String email, String password) {
        view.showLoading();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = auth.getUid();
                        User user = new User(username, email);

                        firestore.collection("users").document(userId)
                                .set(user)
                                .addOnSuccessListener(aVoid -> {
                                    view.hideLoading();
                                    view.showSignupSuccess();
                                })
                                .addOnFailureListener(e -> {
                                    view.hideLoading();
                                    view.showSignupError(e.getMessage());
                                });
                    } else {
                        view.hideLoading();
                        view.showSignupError(
                                task.getException() != null ? task.getException().getMessage() : "Signup failed");
                    }
                });
    }
}
