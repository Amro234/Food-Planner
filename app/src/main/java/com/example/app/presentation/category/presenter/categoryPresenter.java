package com.example.app.presentation.category.presenter;

import android.content.Context;

import com.example.logic_mvp.data.model.CategoryResponse;
import com.example.logic_mvp.data.model.MealResponse;
import com.example.logic_mvp.data.repository.MealRepository;
import com.example.logic_mvp.data.repository.MealRepositoryImp;
import com.example.logic_mvp.presentation.Home.HomeContract;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class categoryPresenter implements HomeContract.Presenter {

    private final HomeContract.View view;
    private final MealRepository repository;
    private final CompositeDisposable disposables;

    public categoryPresenter(HomeContract.View view, Context context) {
        this.view = view;
        this.repository = MealRepositoryImp.getInstance(context);
        this.disposables = new CompositeDisposable();
    }

    @Override
    public void loadRandomMeal() {
        view.showLoading();
        disposables.add(
                repository.getRandomMeal()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::handleRandomMealSuccess,
                                this::handleError));
    }

    @Override
    public void loadCategories() {
        disposables.add(
                repository.getCategories()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::handleCategoriesSuccess,
                                this::handleError));
    }

    @Override
    public void onCategoryClicked(String categoryName) {
        // Navigation will be handled in the activity
    }

    @Override
    public void onDestroy() {
        disposables.clear();
    }

    private void handleRandomMealSuccess(MealResponse response) {
        view.hideLoading();
        if (response != null && response.getMeals() != null && !response.getMeals().isEmpty()) {
            view.showRandomMeal(response.getMeals().get(0));
        } else {
            view.showError("No meal found");
        }
    }

    private void handleCategoriesSuccess(CategoryResponse response) {
        if (response != null && response.getCategories() != null) {
            view.showCategories(response.getCategories());
        }
    }

    private void handleError(Throwable throwable) {
        view.hideLoading();
        view.showError(throwable.getMessage());
    }
}
