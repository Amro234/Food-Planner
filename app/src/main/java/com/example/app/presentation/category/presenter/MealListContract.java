package com.example.app.presentation.category.presenter;

import com.example.app.data.model.Meal;
import java.util.List;

public interface MealListContract {
    interface View {
        void showMeals(List<Meal> meals);

        void showLoading();

        void hideLoading();

        void showError(String message);
    }

    interface Presenter {
        void getMealsByCategory(String category);

        void addToFavorites(Meal meal);

        void onDestroy();
    }
}
