package com.example.app.presentation.favorites.presenter;

import com.example.app.Database.MealEntity;
import java.util.List;

public interface FavoritesContract {
    interface View {
        void showFavoriteMeals(List<MealEntity> meals);

        void showEmptyState();

        void showError(String message);
    }

    interface Presenter {
        void getFavoriteMeals();

        void removeFromFavorites(String mealId);

        void onDestroy();
    }
}
