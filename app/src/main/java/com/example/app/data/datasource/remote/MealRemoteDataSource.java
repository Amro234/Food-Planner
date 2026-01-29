package com.example.logic_mvp.data.datasource.remote;

import com.example.logic_mvp.data.model.CategoryResponse;
import com.example.logic_mvp.data.model.MealResponse;

import io.reactivex.rxjava3.core.Single;

public interface MealRemoteDataSource {

    Single<MealResponse> getRandomMeal();

    Single<CategoryResponse> getCategories();

    Single<MealResponse> getMealsByCategory(String category);

    Single<MealResponse> getMealById(String mealId);
}
