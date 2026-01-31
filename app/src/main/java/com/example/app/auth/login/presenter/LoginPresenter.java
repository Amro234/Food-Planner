package com.example.app.auth.login.presenter;

import android.content.Context;
import com.example.app.Helper.SaveState;
import com.example.app.data.repository.UserRepository;
import com.example.app.data.repository.UserRepositoryImp;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View view;
    private final SaveState saveState;
    private final UserRepository userRepository;

    public LoginPresenter(LoginContract.View view, Context context) {
        this.view = view;
        this.saveState = new SaveState(context, "on_boarding");
        this.userRepository = UserRepositoryImp.getInstance(context);
    }

    @Override
    public void login(String email, String password) {
        view.showLoading();
        userRepository.loginWithEmail(email, password)
                .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                .observeOn(io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            view.hideLoading();
                            view.onLoginSuccess();
                            view.navigateToHome();
                        },
                        throwable -> {
                            view.hideLoading();
                            view.showError(throwable.getMessage());
                        });
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
