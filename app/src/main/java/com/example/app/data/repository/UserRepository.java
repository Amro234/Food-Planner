package com.example.app.data.repository;

import io.reactivex.rxjava3.core.Completable;

public interface UserRepository {
    Completable loginWithEmail(String email, String password);

    Completable registerWithEmail(String email, String password);

    Completable firebaseAuthWithGoogle(String idToken);

    void logout();

    boolean isGuestMode();

    void setGuestMode(boolean isGuest);

    String getCurrentUserId();

    String getCurrentUserDisplayName();

    String getCurrentUserEmail();

    boolean isUserLoggedIn();
}