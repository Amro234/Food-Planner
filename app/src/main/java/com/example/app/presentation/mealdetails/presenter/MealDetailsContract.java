package com.example.app.presentation.mealdetails.presenter;

import com.example.app.data.model.Meal;

public interface MealDetailsContract {
    interface View {
        void showMealDetails(Meal meal);

        void showLoading();

        void hideLoading();

        void showError(String message);

        void showSuccessMessage(String message);
    }

    interface Presenter {
        void getMealById(String mealId);

        void addToFavorites(Meal meal);

        void onDestroy();
    }
}
