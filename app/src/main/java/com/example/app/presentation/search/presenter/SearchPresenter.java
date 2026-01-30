package com.example.app.presentation.search.presenter;

import android.content.Context;
import com.example.app.data.model.Meal;
import com.example.app.data.repository.MealRepository;
import com.example.app.data.repository.MealRepositoryImp;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchPresenter implements SearchContract.Presenter {

    private final SearchContract.View view;
    private final MealRepository repository;
    private final CompositeDisposable disposables;

    public SearchPresenter(SearchContract.View view, Context context) {
        this.view = view;
        this.repository = MealRepositoryImp.getInstance(context);
        this.disposables = new CompositeDisposable();
    }

    @Override
    public void searchMeals(String query, String area) {
        if (query == null || query.trim().isEmpty()) {
            view.showEmptyState();
            return;
        }

        view.showLoading();
        disposables.add(
                repository.searchMeals(query)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    view.hideLoading();
                                    List<Meal> meals = response.getMeals();
                                    if (meals != null && !meals.isEmpty()) {
                                        if (area != null && !area.equalsIgnoreCase("Unknown")) {
                                            // Local filtering by area as requested
                                            List<Meal> filteredMeals = meals.stream()
                                                    .filter(meal -> area.equalsIgnoreCase(meal.getStrArea()))
                                                    .collect(Collectors.toList());

                                            if (filteredMeals.isEmpty()) {
                                                view.showEmptyState();
                                            } else {
                                                view.showSearchResults(filteredMeals);
                                            }
                                        } else {
                                            view.showSearchResults(meals);
                                        }
                                    } else {
                                        view.showEmptyState();
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
                                },
                                throwable -> view.showError(throwable.getMessage())));
    }

    @Override
    public void onDestroy() {
        disposables.clear();
    }
}
