package com.example.app.presentation.category.presenter;

import android.content.Context;

import com.example.app.data.model.Meal;
import com.example.app.data.model.MealResponse;
import com.example.app.data.repository.MealRepository;
import com.example.app.data.repository.MealRepositoryImp;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealListPresenter implements MealListContract.Presenter {

    private final MealListContract.View view;
    private final MealRepository repository;
    private final CompositeDisposable disposables;

    public MealListPresenter(MealListContract.View view, Context context) {
        this.view = view;
        this.repository = MealRepositoryImp.getInstance(context);
        this.disposables = new CompositeDisposable();
    }

    @Override
    public void getMealsByCategory(String category) {
        view.showLoading();
        disposables.add(
                repository.getMealsByCategory(category)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    view.hideLoading();
                                    if (response != null && response.getMeals() != null) {
                                        view.showMeals(response.getMeals());
                                    }
                                },
                                throwable -> {
                                    view.hideLoading();
                                    view.showError(throwable.getMessage());
                                }));
    }

    @Override
    public void addToFavorites(Meal meal) {
        disposables.add(
                repository.addToFavorites(meal)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    // Success
                                },
                                throwable -> {
                                    view.showError(throwable.getMessage());
                                }));
    }

    @Override
    public void onDestroy() {
        disposables.clear();
    }
}
