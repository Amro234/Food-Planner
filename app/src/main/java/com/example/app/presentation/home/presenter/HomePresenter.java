package com.example.app.presentation.home.presenter;

import android.content.Context;

import com.example.app.data.model.CategoryResponse;
import com.example.app.data.model.MealResponse;
import com.example.app.data.repository.MealRepository;
import com.example.app.data.repository.MealRepositoryImp;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenter implements HomeContract.Presenter {

    private final HomeContract.View view;
    private final MealRepository repository;
    private final CompositeDisposable disposables;

    public HomePresenter(HomeContract.View view, Context context) {
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
        // Navigation will be handled in the view or via a router if added
    }

    @Override
    public void loadRecommendedMeals() {
        disposables.add(
                repository.getMealsByCategory("Beef") // Default recommendation for now
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response != null && response.getMeals() != null) {
                                        view.showRecommendedMeals(response.getMeals());
                                    }
                                },
                                this::handleError));
    }

    @Override
    public void loadAreas() {
        disposables.add(
                repository.getAreas()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response != null && response.getAreas() != null) {
                                        view.showAreas(response.getAreas());
                                    }
                                },
                                this::handleError));
    }

    @Override
    public void searchMeals(String query) {
        disposables.add(
                repository.searchMeals(query)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    if (response != null && response.getMeals() != null) {
                                        view.showSearchResults(response.getMeals());
                                    } else {
                                        // Handle empty results if needed, e.g. clear list
                                        view.showSearchResults(new java.util.ArrayList<>());
                                    }
                                },
                                this::handleError));
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
