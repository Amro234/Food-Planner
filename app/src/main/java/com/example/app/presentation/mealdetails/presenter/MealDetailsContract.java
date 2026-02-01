package com.example.app.presentation.mealdetails.presenter;

import com.example.app.data.model.Ingredient;
import com.example.app.data.model.Meal;

import java.util.List;

public interface MealDetailsContract {
    interface View {
        void showMealDetails(Meal meal);

        void showIngredients(java.util.List<Ingredient> ingredients);

        void showLoading();

        void hideLoading();

        void showError(String message);

        void showSuccessMessage(String message);
    }

    interface Presenter {
        void getMealById(String mealId);

        void addToFavorites(Meal meal);

        void addToPlan(Meal meal, String day);

        void onDestroy();
    }
}
