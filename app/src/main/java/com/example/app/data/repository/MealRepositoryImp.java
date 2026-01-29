package com.example.logic_mvp.data.repository;

import android.content.Context;

import com.example.logic_mvp.Database.MealEntity;
import com.example.logic_mvp.data.datasource.local.MealLocalDataSource;
import com.example.logic_mvp.data.datasource.local.MealLocalDataSourceImp;
import com.example.logic_mvp.data.datasource.remote.MealRemoteDataSource;
import com.example.logic_mvp.data.datasource.remote.MealRemoteDataSourceImp;
import com.example.logic_mvp.data.model.CategoryResponse;
import com.example.logic_mvp.data.model.Meal;
import com.example.logic_mvp.data.model.MealResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class MealRepositoryImp implements MealRepository {

    private static MealRepositoryImp instance = null;
    private final MealRemoteDataSource remoteDataSource;
    private final MealLocalDataSource localDataSource;

    private MealRepositoryImp(Context context) {
        this.remoteDataSource = new MealRemoteDataSourceImp();
        this.localDataSource = new MealLocalDataSourceImp(context);
    }

    public static MealRepositoryImp getInstance(Context context) {
        if (instance == null) {
            instance = new MealRepositoryImp(context);
        }
        return instance;
    }

    @Override
    public Single<MealResponse> getRandomMeal() {
        return remoteDataSource.getRandomMeal();
    }

    @Override
    public Single<CategoryResponse> getCategories() {
        return remoteDataSource.getCategories();
    }

    @Override
    public Single<MealResponse> getMealsByCategory(String category) {
        return remoteDataSource.getMealsByCategory(category);
    }

    @Override
    public Single<MealResponse> getMealById(String mealId) {
        return remoteDataSource.getMealById(mealId);
    }

    @Override
    public Flowable<List<MealEntity>> getFavoriteMeals() {
        return localDataSource.getAllFavoriteMeals();
    }

    @Override
    public Completable addToFavorites(Meal meal) {
        MealEntity entity = convertMealToEntity(meal);
        entity.setFavorite(true);
        return localDataSource.insertMeal(entity);
    }

    @Override
    public Completable removeFromFavorites(String mealId) {
        return localDataSource.deleteMealById(mealId);
    }

    private MealEntity convertMealToEntity(Meal meal) {
        MealEntity entity = new MealEntity();
        entity.setIdMeal(meal.getIdMeal());
        entity.setStrMeal(meal.getStrMeal());
        entity.setStrCategory(meal.getStrCategory());
        entity.setStrArea(meal.getStrArea());
        entity.setStrInstructions(meal.getStrInstructions());
        entity.setStrMealThumb(meal.getStrMealThumb());
        entity.setStrTags(meal.getStrTags());
        entity.setStrYoutube(meal.getStrYoutube());

        entity.setStrIngredient1(meal.getStrIngredient1());
        entity.setStrIngredient2(meal.getStrIngredient2());
        entity.setStrIngredient3(meal.getStrIngredient3());
        entity.setStrIngredient4(meal.getStrIngredient4());
        entity.setStrIngredient5(meal.getStrIngredient5());
        entity.setStrIngredient6(meal.getStrIngredient6());
        entity.setStrIngredient7(meal.getStrIngredient7());
        entity.setStrIngredient8(meal.getStrIngredient8());
        entity.setStrIngredient9(meal.getStrIngredient9());
        entity.setStrIngredient10(meal.getStrIngredient10());
        entity.setStrIngredient11(meal.getStrIngredient11());
        entity.setStrIngredient12(meal.getStrIngredient12());
        entity.setStrIngredient13(meal.getStrIngredient13());
        entity.setStrIngredient14(meal.getStrIngredient14());
        entity.setStrIngredient15(meal.getStrIngredient15());
        entity.setStrIngredient16(meal.getStrIngredient16());
        entity.setStrIngredient17(meal.getStrIngredient17());
        entity.setStrIngredient18(meal.getStrIngredient18());
        entity.setStrIngredient19(meal.getStrIngredient19());
        entity.setStrIngredient20(meal.getStrIngredient20());

        entity.setStrMeasure1(meal.getStrMeasure1());
        entity.setStrMeasure2(meal.getStrMeasure2());
        entity.setStrMeasure3(meal.getStrMeasure3());
        entity.setStrMeasure4(meal.getStrMeasure4());
        entity.setStrMeasure5(meal.getStrMeasure5());
        entity.setStrMeasure6(meal.getStrMeasure6());
        entity.setStrMeasure7(meal.getStrMeasure7());
        entity.setStrMeasure8(meal.getStrMeasure8());
        entity.setStrMeasure9(meal.getStrMeasure9());
        entity.setStrMeasure10(meal.getStrMeasure10());
        entity.setStrMeasure11(meal.getStrMeasure11());
        entity.setStrMeasure12(meal.getStrMeasure12());
        entity.setStrMeasure13(meal.getStrMeasure13());
        entity.setStrMeasure14(meal.getStrMeasure14());
        entity.setStrMeasure15(meal.getStrMeasure15());
        entity.setStrMeasure16(meal.getStrMeasure16());
        entity.setStrMeasure17(meal.getStrMeasure17());
        entity.setStrMeasure18(meal.getStrMeasure18());
        entity.setStrMeasure19(meal.getStrMeasure19());
        entity.setStrMeasure20(meal.getStrMeasure20());

        entity.setStrSource(meal.getStrSource());
        entity.setStrImageSource(meal.getStrImageSource());

        return entity;
    }
}
