package com.example.app.data.datasource.local;

import com.example.app.Database.MealDAO;
import com.example.app.Database.MealEntity;

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

    Flowable<List<MealEntity>> getPlannedMeals();

    Completable updateFavoriteStatus(String mealId, boolean isFavorite);

    Completable updatePlannedStatus(String mealId, boolean isPlanned, String day);
}
