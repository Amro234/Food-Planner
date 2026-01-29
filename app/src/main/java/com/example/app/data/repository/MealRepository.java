package com.example.logic_mvp.data.repository;

import com.example.logic_mvp.Database.MealEntity;
import com.example.logic_mvp.data.model.CategoryResponse;
import com.example.logic_mvp.data.model.Meal;
import com.example.logic_mvp.data.model.MealResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface MealRepository {
    Single<MealResponse> getRandomMeal();

    Single<CategoryResponse> getCategories();

    Single<MealResponse> getMealsByCategory(String category);

    Single<MealResponse> getMealById(String mealId);

    Flowable<List<MealEntity>> getFavoriteMeals();

    Completable addToFavorites(Meal meal);

    Completable removeFromFavorites(String mealId);
}
