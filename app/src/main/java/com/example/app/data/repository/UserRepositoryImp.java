package com.example.app.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.example.app.data.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

import io.reactivex.rxjava3.core.Completable;

public class UserRepositoryImp implements UserRepository {
    private static UserRepositoryImp instance;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_GUEST_MODE = "is_guest";

    private UserRepositoryImp(Context context) {
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static UserRepositoryImp getInstance(Context context) {
        if (instance == null) {
            instance = new UserRepositoryImp(context);
        }
        return instance;
    }

    @Override
    public Completable loginWithEmail(String email, String password) {
        return Completable.create(emitter -> {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        setGuestMode(false);
                        emitter.onComplete();
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }

    @Override
    public Completable firebaseAuthWithGoogle(String idToken) {
        return Completable.create(emitter -> {
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(credential)
                    .addOnSuccessListener(authResult -> {
                        setGuestMode(false);
                        FirebaseUser user = authResult.getUser();
                        if (user != null) {
                            User userModel = new User(user.getDisplayName(), user.getEmail());
                            FirebaseFirestore.getInstance().collection("users")
                                    .document(user.getUid())
                                    .set(userModel);
                        }
                        emitter.onComplete();
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }

    @Override
    public boolean isGuestMode() {
        return sharedPreferences.getBoolean(KEY_GUEST_MODE, false);
    }

    @Override
    public void setGuestMode(boolean isGuest) {
        sharedPreferences.edit().putBoolean(KEY_GUEST_MODE, isGuest).apply();
    }

    @Override
    public String getCurrentUserId() {
        return mAuth.getUid();
    }

    @Override
    public String getCurrentUserDisplayName() {
        FirebaseUser user = mAuth.getCurrentUser();
        return (user != null) ? user.getDisplayName() : null;
    }

    @Override
    public String getCurrentUserEmail() {
        FirebaseUser user = mAuth.getCurrentUser();
        return (user != null) ? user.getEmail() : null;
    }

    @Override
    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null && !isGuestMode();
    }

    @Override
    public void logout() {
        mAuth.signOut();
        setGuestMode(false);
    }

    @Override
    public Completable registerWithEmail(String email, String password) {
        return Completable.create(emitter -> {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        setGuestMode(false);
                        emitter.onComplete();
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }
}
