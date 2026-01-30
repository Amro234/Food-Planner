package com.example.app.presentation.category.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.app.databinding.FragmentMealitemBinding;
import com.example.app.data.model.Meal;
import com.example.app.presentation.category.presenter.MealListContract;
import com.example.app.presentation.category.presenter.MealListPresenter;
import com.example.app.presentation.category.view.CategoryFragmentArgs;
import com.example.app.presentation.category.view.CategoryFragmentDirections;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment implements MealListContract.View {

    private FragmentMealitemBinding binding;
    private MealListContract.Presenter presenter;
    private MealListAdapter adapter;
    private String categoryName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryName = CategoryFragmentArgs.fromBundle(getArguments()).getCategory();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentMealitemBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new MealListPresenter(this, requireContext());
        setupRecyclerView();
        if (categoryName != null) {
            presenter.getMealsByCategory(categoryName);
        }
    }

    private void setupRecyclerView() {
        adapter = new MealListAdapter(new ArrayList<>(), new MealListAdapter.OnMealClickListener() {
            @Override
            public void onMealClick(Meal meal) {
                CategoryFragmentDirections.ActionCategoryToDetails action = CategoryFragmentDirections
                        .actionCategoryToDetails(meal.getIdMeal());
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }

            @Override
            public void onAddToFavorite(Meal meal) {
                presenter.addToFavorites(meal);
                Toast.makeText(requireContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
            }
        });
        binding.mealsRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.mealsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showMeals(List<Meal> meals) {
        adapter.updateMeals(meals);
    }

    @Override
    public void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
        binding = null;
    }
}
