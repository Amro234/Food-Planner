package com.example.app.presentation.home.presenter;

import com.example.app.data.model.Category;
import com.example.app.data.model.Meal;

import java.util.List;

public interface HomeContract {

    interface View {
        void showRandomMeal(Meal meal);

        void showCategories(List<Category> categories);

        void showRecommendedMeals(List<Meal> meals);

        void showLoading();

        void hideLoading();

        void showError(String message);
    }

    interface Presenter {
        void loadRandomMeal();

        void loadCategories();

        void loadRecommendedMeals();

        void onCategoryClicked(String categoryName);

        void onDestroy();
    }
}
