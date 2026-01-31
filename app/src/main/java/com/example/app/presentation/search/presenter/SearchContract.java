package com.example.app.presentation.search.presenter;

import com.example.app.data.model.Area;
import com.example.app.data.model.Category;
import com.example.app.data.model.Ingredient;
import com.example.app.data.model.Meal;
import java.util.List;

public interface SearchContract {
    interface View {
        void showSearchResults(List<Meal> meals);

        void showCategories(List<Category> categories);

        void showIngredients(List<Ingredient> ingredients);

        void showAreas(List<Area> areas);

        void showLoading();

        void hideLoading();

        void showError(String message);

        void showEmptyState();
    }

    interface Presenter {
        void searchMealsByName(String query);

        void searchMealsByCategory(String category);

        void searchMealsByIngredient(String ingredient);

        void searchMealsByArea(String area);

        void getMealsByCategory(String category);

        void getMealsByIngredient(String ingredient);

        void getMealsByArea(String area);

        void addToFavorites(Meal meal);

        void onDestroy();
    }
}
