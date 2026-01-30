package com.example.app.data.repository;

import com.example.app.Database.MealEntity;
import com.example.app.data.model.CategoryResponse;
import com.example.app.data.model.Meal;
import com.example.app.data.model.MealResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface MealRepository {
    Single<MealResponse> getRandomMeal();

    Single<CategoryResponse> getCategories();

    Single<MealResponse> getMealsByCategory(String category);

    Single<MealResponse> getMealById(String mealId);

    Single<com.example.app.data.model.AreaResponse> getAreas();

    Single<MealResponse> searchMeals(String query);

    Flowable<List<MealEntity>> getFavoriteMeals();

    Completable addToFavorites(Meal meal);

    Completable removeFromFavorites(String mealId);

    Flowable<List<MealEntity>> getPlannedMeals();

    Completable addToPlan(Meal meal, String day);

    Completable removeFromPlan(String mealId);
}
