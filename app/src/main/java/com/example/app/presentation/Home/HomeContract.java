package com.example.logic_mvp.presentation.Home;

import com.example.logic_mvp.data.model.Category;
import com.example.logic_mvp.data.model.Meal;

import java.util.List;

public interface HomeContract {

    interface View {
        void showRandomMeal(Meal meal);

        void showCategories(List<Category> categories);

        void showLoading();

        void hideLoading();

        void showError(String message);
    }

    interface Presenter {
        void loadRandomMeal();

        void loadCategories();

        void onCategoryClicked(String categoryName);

        void onDestroy();
    }
}
