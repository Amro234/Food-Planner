package com.example.logic_mvp.data.datasource.remote;

import com.example.logic_mvp.data.model.CategoryResponse;
import com.example.logic_mvp.data.model.MealResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealApi {
    @GET("random.php")
    Single<MealResponse> getRandomMeal();

    @GET("categories.php")
    Single<CategoryResponse> getCategories();

    @GET("filter.php")
    Single<MealResponse> getMealsByCategory(
            @Query("c") String category);

    @GET("lookup.php")
    Single<MealResponse> getMealById(
            @Query("i") String mealId);
}
