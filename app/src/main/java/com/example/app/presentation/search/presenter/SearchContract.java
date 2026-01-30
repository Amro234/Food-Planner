package com.example.app.presentation.search.presenter;

import com.example.app.data.model.Meal;
import java.util.List;

public interface SearchContract {
    interface View {
        void showSearchResults(List<Meal> meals);

        void showLoading();

        void hideLoading();

        void showError(String message);

        void showEmptyState();
    }

    interface Presenter {
        void searchMeals(String query, String area);

        void addToFavorites(Meal meal);

        void onDestroy();
    }
}
