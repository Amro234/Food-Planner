package com.example.app.auth.login.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.MainActivity;
import com.example.app.R;
import com.example.app.auth.AuthActivity;
import com.example.app.auth.login.presenter.LoginContract;
import com.example.app.auth.login.presenter.LoginPresenter;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment implements LoginContract.View {

    private TextInputLayout emailInputLayout;
    private TextInputLayout passwordInputLayout;
    private Button loginButton;
    private TextView createAccountText;
    private TextView guestButton;
    private LoginContract.Presenter presenter;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new LoginPresenter(this, requireContext());

        emailInputLayout = view.findViewById(R.id.Outlined_mail);
        passwordInputLayout = view.findViewById(R.id.Outlined_password);
        loginButton = view.findViewById(R.id.login_button);
        createAccountText = view.findViewById(R.id.create_account_text);
        guestButton = view.findViewById(R.id.guest_button);

        loginButton.setOnClickListener(v -> validateAndLogin());

        createAccountText.setOnClickListener(v -> {
            if (getActivity() instanceof AuthActivity) {
                ((AuthActivity) getActivity()).showSignup();
            }
        });

        guestButton.setOnClickListener(v -> presenter.loginAsGuest());
    }

    private void validateAndLogin() {
        String email = emailInputLayout.getEditText().getText().toString().trim();
        String password = passwordInputLayout.getEditText().getText().toString().trim();

        boolean isValid = true;

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
            presenter.login(email, password);
        }
    }

    @Override
    public void showLoading() {
        // Implement loading state if UI exists
    }

    @Override
    public void hideLoading() {
        // Implement hide loading state
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(getActivity(), com.example.app.MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onLoginSuccess() {
        Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }
}
