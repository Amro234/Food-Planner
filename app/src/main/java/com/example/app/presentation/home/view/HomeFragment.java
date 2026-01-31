package com.example.app.presentation.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.app.R;
import com.example.app.databinding.FragmentHomeBinding;
import com.example.app.data.model.Category;
import com.example.app.data.model.Meal;
import com.example.app.presentation.home.presenter.HomeContract;
import com.example.app.presentation.home.presenter.HomePresenter;
import com.example.app.presentation.category.view.CategoryAdapter;
import com.example.app.presentation.category.view.MealListAdapter;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeContract.View {

    private FragmentHomeBinding binding;
    private HomeContract.Presenter presenter;
    private CategoryAdapter categoryAdapter;
    private MealListAdapter recommendedAdapter;
    private Meal currentRandomMeal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new HomePresenter(this, requireContext());
        setupRecyclerView();
        presenter.loadRandomMeal();
        presenter.loadCategories();
        presenter.loadRecommendedMeals();

        binding.mealOfDayCard.setOnClickListener(v -> {
            if (currentRandomMeal != null) {
                com.example.app.presentation.home.view.HomeFragmentDirections.ActionHomeToMealDetails action = com.example.app.presentation.home.view.HomeFragmentDirections
                        .actionHomeToMealDetails(currentRandomMeal.getIdMeal());
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }
        });
    }

    private void setupRecyclerView() {
        categoryAdapter = new CategoryAdapter(new ArrayList<>(), category -> {
            com.example.app.presentation.home.view.HomeFragmentDirections.ActionHomeToMealList action = com.example.app.presentation.home.view.HomeFragmentDirections
                    .actionHomeToMealList(category.getStrCategory());
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });

        binding.categoriesRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.categoriesRecyclerView.setAdapter(categoryAdapter);

        recommendedAdapter = new MealListAdapter(new ArrayList<>(), new MealListAdapter.OnMealClickListener() {
            @Override
            public void onMealClick(Meal meal) {
                com.example.app.presentation.home.view.HomeFragmentDirections.ActionHomeToMealDetails action = com.example.app.presentation.home.view.HomeFragmentDirections
                        .actionHomeToMealDetails(meal.getIdMeal());
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }

            @Override
            public void onAddToFavorite(Meal meal) {

            }
        }, true);
        binding.recyclerView
                .setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerView.setAdapter(recommendedAdapter);
    }

    @Override
    public void showRandomMeal(Meal meal) {
        this.currentRandomMeal = meal;
        binding.mealOfDayTitle.setText(meal.getStrMeal());
        binding.mealOfDayCategory.setText(meal.getStrCategory());
        Glide.with(this)
                .load(meal.getStrMealThumb())
                .into(binding.mealOfDayImage);
    }

    @Override
    public void showCategories(List<Category> categories) {
        if (categoryAdapter != null) {
            categoryAdapter.updateCategories(categories);
        }
    }

    @Override
    public void showRecommendedMeals(List<Meal> meals) {
        if (recommendedAdapter != null) {
            recommendedAdapter.updateMeals(meals);
        }
    }

    @Override
    public void showLoading() {
        // TODO: Add progress bar to layout and show it here
    }

    @Override
    public void hideLoading() {
        // TODO: Add progress bar to layout and hide it here
    }

    @Override
    public void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.onDestroy();
        }
        binding = null;
    }
}
