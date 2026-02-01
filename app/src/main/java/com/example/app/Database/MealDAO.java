package com.example.app.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.app.Database.MealEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface MealDAO {

    @Query("SELECT * FROM meals WHERE isFavorite = 1 AND userId = :userId")
    Flowable<List<MealEntity>> getFavoriteMeals(String userId);

    @Query("SELECT * FROM meals WHERE isPlanned = 1 AND userId = :userId")
    Flowable<List<MealEntity>> getPlannedMeals(String userId);

    @Query("SELECT * FROM meals WHERE isPlanned = 1 AND plannedDay = :day AND userId = :userId")
    Flowable<List<MealEntity>> getPlannedMealsByDay(String day, String userId);

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

    @Query("DELETE FROM meals WHERE userId = :userId")
    Completable deleteMealsByUser(String userId);

    @Update
    Completable updateMeal(MealEntity meal);

    @Query("UPDATE meals SET isFavorite = :isFavorite WHERE idMeal = :mealId AND userId = :userId")
    Completable updateFavoriteStatus(String mealId, boolean isFavorite, String userId);

    @Query("UPDATE meals SET isPlanned = :isPlanned, plannedDay = :day WHERE idMeal = :mealId AND userId = :userId")
    Completable updatePlannedStatus(String mealId, boolean isPlanned, String day, String userId);
}
