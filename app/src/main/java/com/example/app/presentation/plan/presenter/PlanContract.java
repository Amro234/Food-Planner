package com.example.app.presentation.plan.presenter;

import com.example.app.Database.MealEntity;
import java.util.List;

public interface PlanContract {
    interface View {
        void showPlannedMeals(List<MealEntity> meals);

        void showEmptyState();

        void showError(String message);
    }

    interface Presenter {
        void getPlannedMeals();

        void removeFromPlan(String mealId);

        void onDestroy();
    }
}
