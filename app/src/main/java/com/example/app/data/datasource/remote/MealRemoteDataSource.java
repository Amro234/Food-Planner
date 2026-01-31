package com.example.app.data.datasource.remote;

import com.example.app.data.model.CategoryResponse;
import com.example.app.data.model.MealResponse;

import io.reactivex.rxjava3.core.Single;

public interface MealRemoteDataSource {

    Single<MealResponse> getRandomMeal();

    Single<CategoryResponse> getCategories();

    Single<MealResponse> getMealsByCategory(String category);

    Single<MealResponse> getMealsByIngredient(String ingredient);

    Single<MealResponse> getMealsByArea(String area);

    Single<MealResponse> getMealById(String mealId);

    Single<com.example.app.data.model.AreaResponse> getAreas();

    Single<com.example.app.data.model.IngredientResponse> getIngredients();

    Single<MealResponse> searchMeals(String query);
}
