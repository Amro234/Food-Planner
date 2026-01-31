package com.example.app.data.datasource.remote;

import com.example.app.data.model.CategoryResponse;
import com.example.app.data.model.MealResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealApi {
        @GET("random.php")
        Single<MealResponse> getRandomMeal();

        @GET("categories.php")
        Single<CategoryResponse> getCategories();

        @GET("list.php?a=list")
        Single<com.example.app.data.model.AreaResponse> getAreas();

        @GET("list.php?i=list")
        Single<com.example.app.data.model.IngredientResponse> getIngredients();

        @GET("filter.php")
        Single<MealResponse> getMealsByCategory(
                        @Query("c") String category);

        @GET("filter.php")
        Single<MealResponse> getMealsByIngredient(
                        @Query("i") String ingredient);

        @GET("filter.php")
        Single<MealResponse> getMealsByArea(
                        @Query("a") String area);

        @GET("lookup.php")
        Single<MealResponse> getMealById(
                        @Query("i") String mealId);

        @GET("search.php")
        Single<MealResponse> searchMeals(@Query("s") String name);
}
