package com.example.app.presentation.favorites.presenter;

import android.content.Context;

import com.example.app.data.repository.MealRepository;
import com.example.app.data.repository.MealRepositoryImp;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritesPresenter implements FavoritesContract.Presenter {

    private final FavoritesContract.View view;
    private final MealRepository repository;
    private final CompositeDisposable disposables;

    public FavoritesPresenter(FavoritesContract.View view, Context context) {
        this.view = view;
        this.repository = MealRepositoryImp.getInstance(context);
        this.disposables = new CompositeDisposable();
    }

    @Override
    public void getFavoriteMeals() {
        disposables.add(
                repository.getFavoriteMeals()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                meals -> {
                                    if (meals == null || meals.isEmpty()) {
                                        view.showEmptyState();
                                    } else {
                                        view.showFavoriteMeals(meals);
                                    }
                                },
                                throwable -> view.showError(throwable.getMessage())));
    }

    @Override
    public void removeFromFavorites(String mealId) {
        disposables.add(
                repository.removeFromFavorites(mealId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                }, // View will naturally update via Flowable
                                throwable -> view.showError(throwable.getMessage())));
    }

    @Override
    public void onDestroy() {
        disposables.clear();
    }
}
