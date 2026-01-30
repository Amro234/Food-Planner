package com.example.logic_mvp.presentation.Home;

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
import com.example.logic_mvp.R;
import com.example.logic_mvp.databinding.FragmentHomeBinding;
import com.example.logic_mvp.data.model.Category;
import com.example.logic_mvp.data.model.Meal;
import com.example.app.presentation.category.view.CategoryAdapter;
import com.example.app.presentation.category.presenter.categoryPresenter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeContract.View {

    private FragmentHomeBinding binding;
    private categoryPresenter presenter;
    private CategoryAdapter categoryAdapter;
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

        presenter = new categoryPresenter(this, requireContext());
        setupRecyclerView();
        presenter.loadRandomMeal();
        presenter.loadCategories();

        binding.mealOfDayCard.setOnClickListener(v -> {
            if (currentRandomMeal != null) {
                HomeFragmentDirections.ActionHomeToMealDetails action = HomeFragmentDirections
                        .actionHomeToMealDetails(currentRandomMeal.getIdMeal());
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }
        });
    }

    private void setupRecyclerView() {
        categoryAdapter = new CategoryAdapter(new ArrayList<>(), category -> {
            HomeFragmentDirections.ActionHomeToMealList action = HomeFragmentDirections
                    .actionHomeToMealList(category.getStrCategory());
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });

        binding.categoriesRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.categoriesRecyclerView.setAdapter(categoryAdapter);
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
        categoryAdapter.updateCategories(categories);
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
