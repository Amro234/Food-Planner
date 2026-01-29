package com.example.logic_mvp.data.datasource.remote;

import com.example.logic_mvp.Network.api.RetrofitClient;
import com.example.logic_mvp.data.model.CategoryResponse;
import com.example.logic_mvp.data.model.MealResponse;

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
    public Single<MealResponse> getMealById(String mealId) {
        return mealApi.getMealById(mealId);
    }
}
