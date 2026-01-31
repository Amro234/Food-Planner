package com.example.app.data.repository;

import io.reactivex.rxjava3.core.Completable;

public interface UserRepository {
    Completable loginWithEmail(String email, String password);
    Completable registerWithEmail(String email, String password);
    void logout();

    boolean isGuestMode();
    void setGuestMode(boolean isGuest);
    String getCurrentUserId();
    boolean isUserLoggedIn();
}