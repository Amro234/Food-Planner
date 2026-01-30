package com.example.app.auth.signup.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app.R;
import com.example.app.auth.AuthActivity;
import com.example.app.auth.signup.presenter.SignupContract;
import com.example.app.auth.signup.presenter.SignupPresenter;
import com.google.android.material.textfield.TextInputLayout;

public class SignupFragment extends Fragment implements SignupContract.View {

    private TextInputLayout usernameInputLayout;
    private TextInputLayout emailInputLayout;
    private TextInputLayout passwordInputLayout;
    private Button signupButton;
    private TextView loginText;
    private SignupContract.Presenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new SignupPresenter(this);

        usernameInputLayout = view.findViewById(R.id.Outlined_username);
        emailInputLayout = view.findViewById(R.id.Outlined_mail);
        passwordInputLayout = view.findViewById(R.id.Outlined_password);
        signupButton = view.findViewById(R.id.sign_in_btn);
        loginText = view.findViewById(R.id.login_text);

        signupButton.setOnClickListener(v -> validateAndSignup());

        view.findViewById(R.id.back_arrow).setOnClickListener(v -> {
            if (getActivity() instanceof AuthActivity) {
                ((AuthActivity) getActivity()).showLogin();
            }
        });

        loginText.setOnClickListener(v -> {
            if (getActivity() instanceof AuthActivity) {
                ((AuthActivity) getActivity()).showLogin();
            }
        });
    }

    private void validateAndSignup() {
        String username = usernameInputLayout.getEditText().getText().toString().trim();
        String email = emailInputLayout.getEditText().getText().toString().trim();
        String password = passwordInputLayout.getEditText().getText().toString().trim();

        boolean isValid = true;

        if (TextUtils.isEmpty(username)) {
            usernameInputLayout.setError("Username is required");
            isValid = false;
        } else {
            usernameInputLayout.setError(null);
        }

        if (TextUtils.isEmpty(email)) {
            emailInputLayout.setError("Email is required");
            isValid = false;
        } else {
            emailInputLayout.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            passwordInputLayout.setError("Password is required");
            isValid = false;
        } else {
            passwordInputLayout.setError(null);
        }

        if (isValid) {
            presenter.signup(username, email, password);
        }
    }

    @Override
    public void showSignupSuccess() {
        Toast.makeText(getContext(), "Signup Successful", Toast.LENGTH_SHORT).show();
        if (getActivity() instanceof AuthActivity) {
            ((AuthActivity) getActivity()).showLogin();
        }
    }

    @Override
    public void showSignupError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        // Show progress bar if available
    }

    @Override
    public void hideLoading() {
        // Hide progress bar
    }
}
