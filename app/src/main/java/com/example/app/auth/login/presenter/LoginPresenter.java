package com.example.app.auth.login.presenter;

import android.content.Context;
import com.example.app.Helper.SaveState;
import com.example.app.data.repository.UserRepository;
import com.example.app.data.repository.UserRepositoryImp;
import com.example.app.data.repository.MealRepository;
import com.example.app.data.repository.MealRepositoryImp;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View view;
    private final SaveState saveState;
    private final UserRepository userRepository;
    private final MealRepository mealRepository;

    public LoginPresenter(LoginContract.View view, Context context) {
        this.view = view;
        this.saveState = new SaveState(context, "on_boarding");
        this.userRepository = UserRepositoryImp.getInstance(context);
        this.mealRepository = MealRepositoryImp.getInstance(context);
    }

    @Override
    public void login(String email, String password) {
        view.showLoading();
        userRepository.loginWithEmail(email, password)
                .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                .observeOn(io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread())
                .andThen(mealRepository.syncDataFromFirestore())
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
    public void loginWithGoogle(String idToken) {
        view.showLoading();
        userRepository.firebaseAuthWithGoogle(idToken)
                .andThen(mealRepository.syncDataFromFirestore()) // الـ Sync المهم جداً
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            view.hideLoading();
                            view.navigateToHome();
                        },
                        throwable -> {
                            view.hideLoading();
                            view.showError(throwable.getMessage());
                        });
    }

    @Override
    public void loginAsGuest() {
        userRepository.setGuestMode(true);
        saveState.setGuest(true);
        view.navigateToHome();
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
