package com.example.logic_mvp.data.datasource.local;

import com.example.logic_mvp.Database.MealEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface MealLocalDataSource {

    Flowable<List<MealEntity>> getAllFavoriteMeals();

    Single<MealEntity> getMealById(String mealId);

    Completable insertMeal(MealEntity meal);

    Completable insertMeals(List<MealEntity> meals);

    Completable deleteMeal(MealEntity meal);

    Completable deleteMealById(String mealId);

    Completable updateFavoriteStatus(String mealId, boolean isFavorite);
}
