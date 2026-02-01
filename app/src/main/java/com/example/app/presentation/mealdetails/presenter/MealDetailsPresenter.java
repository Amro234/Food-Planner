package com.example.app.presentation.mealdetails.presenter;

import android.content.Context;

import com.example.app.data.model.Meal;
import com.example.app.data.model.MealResponse;
import com.example.app.data.repository.MealRepository;
import com.example.app.data.repository.MealRepositoryImp;

import com.example.app.data.model.Ingredient;
import com.example.app.data.repository.UserRepositoryImp;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsPresenter implements MealDetailsContract.Presenter {

    private final MealDetailsContract.View view;
    private final MealRepository repository;
    private final CompositeDisposable disposables;

    private void handleMealSuccess(MealResponse response) {
        view.hideLoading();
        if (response != null && response.getMeals() != null && !response.getMeals().isEmpty()) {
            Meal meal = response.getMeals().get(0);
            view.showMealDetails(meal);

            view.showIngredients(extractIngredients(meal));
        } else {
            view.showError("Meal not found");
        }
    }

    private List<Ingredient> extractIngredients(Meal meal) {
        List<Ingredient> list = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            try {
                Method getIng = Meal.class.getMethod("getStrIngredient" + i);
                Method getMeas = Meal.class.getMethod("getStrMeasure" + i);

                String name = (String) getIng.invoke(meal);
                String measure = (String) getMeas.invoke(meal);

                if (name != null && !name.trim().isEmpty()) {
                    list.add(new Ingredient(name, measure));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

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
        if (!UserRepositoryImp.getInstance(null).isUserLoggedIn()) {
            view.showError("Must be Signed-in to add favorites!");
            return;
        }

        view.showLoading();
        disposables.add(
                repository.addToFavorites(meal)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    view.hideLoading();
                                    view.showSuccessMessage("Added to favorites & Synced!");
                                },
                                throwable -> {
                                    view.hideLoading();
                                    view.showError(throwable.getMessage());
                                }));
    }

    @Override
    public void addToPlan(Meal meal, String day) {
        view.showLoading();
        disposables.add(
                repository.addToPlan(meal, day)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    view.hideLoading();
                                    view.showSuccessMessage("Added to Plan!");
                                },
                                throwable -> {
                                    view.hideLoading();
                                    view.showError(throwable.getMessage());
                                }));
    }

    @Override
    public void onDestroy() {
        disposables.clear();
    }

    private void handleError(Throwable throwable) {
        view.hideLoading();
        view.showError(throwable.getMessage());
    }
}
