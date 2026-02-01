package com.example.app.data.datasource.local;

import android.content.Context;

import com.example.app.Database.AppDataBase;
import com.example.app.Database.MealDAO;
import com.example.app.Database.MealEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class MealLocalDataSourceImp implements MealLocalDataSource {

    private final MealDAO mealDAO;

    public MealLocalDataSourceImp(Context context) {
        AppDataBase database = AppDataBase.getInstance(context);
        this.mealDAO = database.mealDAO();
    }

    @Override
    public Flowable<List<MealEntity>> getAllFavoriteMeals(String userId) {
        return mealDAO.getFavoriteMeals(userId);
    }

    @Override
    public Single<MealEntity> getMealById(String mealId) {
        return mealDAO.getMealById(mealId);
    }

    @Override
    public Completable insertMeal(MealEntity meal) {
        return mealDAO.insertMeal(meal);
    }

    @Override
    public Completable insertMeals(List<MealEntity> meals) {
        return mealDAO.insertMeals(meals);
    }

    @Override
    public Completable deleteMealsByUser(String userId) {
        return mealDAO.deleteMealsByUser(userId);
    }

    @Override
    public Completable deleteMeal(MealEntity meal) {
        return mealDAO.deleteMeal(meal);
    }

    @Override
    public Completable deleteMealById(String mealId) {
        return mealDAO.deleteMealById(mealId);
    }

    @Override
    public Flowable<List<MealEntity>> getPlannedMeals(String userId) {
        return mealDAO.getPlannedMeals(userId);
    }

    @Override
    public Flowable<List<MealEntity>> getPlannedMealsByDate(String date, String userId) {
        return mealDAO.getPlannedMealsByDay(date, userId);
    }

    @Override
    public Completable updateFavoriteStatus(String mealId, boolean isFavorite, String userId) {
        return mealDAO.updateFavoriteStatus(mealId, isFavorite, userId);
    }

    @Override
    public Completable updatePlannedStatus(String mealId, boolean isPlanned, String day, String userId) {
        return mealDAO.updatePlannedStatus(mealId, isPlanned, day, userId);
    }
}
