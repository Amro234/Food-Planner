package com.example.app.presentation.search.presenter;

import android.content.Context;
import com.example.app.data.model.Area;
import com.example.app.data.model.Category;
import com.example.app.data.model.Ingredient;
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
    public void searchMealsByName(String query) {
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
                                        view.showSearchResults(meals);
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
    public void searchMealsByCategory(String query) {
        if (query == null || query.trim().isEmpty()) {
            view.showEmptyState();
            return;
        }
        view.showLoading();
        disposables.add(
                repository.getCategories()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    view.hideLoading();
                                    List<Category> filtered = response.getCategories().stream()
                                            .filter(c -> c.getStrCategory().toLowerCase().contains(query.toLowerCase()))
                                            .collect(Collectors.toList());
                                    if (!filtered.isEmpty()) {
                                        view.showCategories(filtered);
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
    public void searchMealsByIngredient(String query) {
        if (query == null || query.trim().isEmpty()) {
            view.showEmptyState();
            return;
        }
        view.showLoading();
        disposables.add(
                repository.getIngredients()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    view.hideLoading();
                                    List<Ingredient> filtered = response.getIngredients().stream()
                                            .filter(i -> i.getStrIngredient().toLowerCase()
                                                    .contains(query.toLowerCase()))
                                            .collect(Collectors.toList());
                                    if (!filtered.isEmpty()) {
                                        view.showIngredients(filtered);
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
    public void searchMealsByArea(String query) {
        if (query == null || query.trim().isEmpty()) {
            view.showEmptyState();
            return;
        }
        view.showLoading();
        disposables.add(
                repository.getAreas()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    view.hideLoading();
                                    List<Area> filtered = response.getAreas().stream()
                                            .filter(a -> a.getStrArea().toLowerCase().contains(query.toLowerCase()))
                                            .collect(Collectors.toList());
                                    if (!filtered.isEmpty()) {
                                        view.showAreas(filtered);
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
    public void getMealsByCategory(String category) {
        view.showLoading();
        disposables.add(
                repository.getMealsByCategory(category)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    view.hideLoading();
                                    view.showSearchResults(response.getMeals());
                                },
                                throwable -> {
                                    view.hideLoading();
                                    view.showError(throwable.getMessage());
                                }));
    }

    @Override
    public void getMealsByIngredient(String ingredient) {
        view.showLoading();
        disposables.add(
                repository.getMealsByIngredient(ingredient)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    view.hideLoading();
                                    view.showSearchResults(response.getMeals());
                                },
                                throwable -> {
                                    view.hideLoading();
                                    view.showError(throwable.getMessage());
                                }));
    }

    @Override
    public void getMealsByArea(String area) {
        view.showLoading();
        disposables.add(
                repository.getMealsByArea(area)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                response -> {
                                    view.hideLoading();
                                    view.showSearchResults(response.getMeals());
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
