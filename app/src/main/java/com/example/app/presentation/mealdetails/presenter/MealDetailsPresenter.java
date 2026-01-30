package com.example.app.presentation.mealdetails.presenter;

import android.content.Context;

import com.example.app.data.model.Meal;
import com.example.app.data.model.MealResponse;
import com.example.app.data.repository.MealRepository;
import com.example.app.data.repository.MealRepositoryImp;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsPresenter implements MealDetailsContract.Presenter {

    private final MealDetailsContract.View view;
    private final MealRepository repository;
    private final CompositeDisposable disposables;

    public MealDetailsPresenter(MealDetailsContract.View view, Context context) {
        this.view = view;
        this.repository = MealRepositoryImp.getInstance(context);
        this.disposables = new CompositeDisposable();
    }

    @Override
    public void getMealById(String mealId) {
        view.showLoading();
        disposables.add(
                repository.getMealById(mealId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::handleMealSuccess,
                                this::handleError));
    }

    @Override
    public void addToFavorites(Meal meal) {
        disposables.add(
                repository.addToFavorites(meal)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> view.showSuccessMessage("Added to favorites"),
                                throwable -> view.showError(throwable.getMessage())));
    }

    @Override
    public void onDestroy() {
        disposables.clear();
    }

    private void handleMealSuccess(MealResponse response) {
        view.hideLoading();
        if (response != null && response.getMeals() != null && !response.getMeals().isEmpty()) {
            view.showMealDetails(response.getMeals().get(0));
        } else {
            view.showError("Meal not found");
        }
    }

    private void handleError(Throwable throwable) {
        view.hideLoading();
        view.showError(throwable.getMessage());
    }
}
