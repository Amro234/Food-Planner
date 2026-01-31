package com.example.app.data.datasource.remote;

import com.example.app.Network.RetrofitClient;
import com.example.app.data.model.CategoryResponse;
import com.example.app.data.model.MealResponse;

import io.reactivex.rxjava3.core.Single;

public class MealRemoteDataSourceImp implements MealRemoteDataSource {

    private final MealApi mealApi;

    public MealRemoteDataSourceImp() {
        this.mealApi = RetrofitClient.getMealApi();
    }

    @Override
    public Single<MealResponse> getRandomMeal() {
        return mealApi.getRandomMeal();
    }

    @Override
    public Single<CategoryResponse> getCategories() {
        return mealApi.getCategories();
    }

    @Override
    public Single<MealResponse> getMealsByCategory(String category) {
        return mealApi.getMealsByCategory(category);
    }

    @Override
    public Single<MealResponse> getMealsByIngredient(String ingredient) {
        return mealApi.getMealsByIngredient(ingredient);
    }

    @Override
    public Single<MealResponse> getMealsByArea(String area) {
        return mealApi.getMealsByArea(area);
    }

    @Override
    public Single<MealResponse> getMealById(String mealId) {
        return mealApi.getMealById(mealId);
    }

    @Override
    public Single<com.example.app.data.model.AreaResponse> getAreas() {
        return mealApi.getAreas();
    }

    @Override
    public Single<com.example.app.data.model.IngredientResponse> getIngredients() {
        return mealApi.getIngredients();
    }

    @Override
    public Single<MealResponse> searchMeals(String query) {
        return mealApi.searchMeals(query);
    }
}
