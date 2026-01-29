package com.example.logic_mvp.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface MealDAO {

    @Query("SELECT * FROM meals WHERE isFavorite = 1")
    Flowable<List<MealEntity>> getAllFavoriteMeals();

    @Query("SELECT * FROM meals WHERE idMeal = :mealId")
    Single<MealEntity> getMealById(String mealId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertMeal(MealEntity meal);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertMeals(List<MealEntity> meals);

    @Delete
    Completable deleteMeal(MealEntity meal);

    @Query("DELETE FROM meals WHERE idMeal = :mealId")
    Completable deleteMealById(String mealId);

    @Query("DELETE FROM meals")
    Completable clearAllMeals();

    @Update
    Completable updateMeal(MealEntity meal);

    @Query("UPDATE meals SET isFavorite = :isFavorite WHERE idMeal = :mealId")
    Completable updateFavoriteStatus(String mealId, boolean isFavorite);
}
