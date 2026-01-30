package com.example.app.presentation.search.view;

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

import com.example.app.data.model.Meal;
import com.example.app.databinding.FragmentSearchedMealBinding;
import com.example.app.presentation.category.view.MealListAdapter;
import com.example.app.presentation.search.presenter.SearchContract;
import com.example.app.presentation.search.presenter.SearchPresenter;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements SearchContract.View {

    private FragmentSearchedMealBinding binding;
    private SearchContract.Presenter presenter;
    private MealListAdapter adapter;
    private String initialQuery;
    private String initialArea;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            SearchFragmentArgs args = SearchFragmentArgs.fromBundle(getArguments());
            initialQuery = args.getQuery();
            initialArea = args.getArea();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchedMealBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new SearchPresenter(this, requireContext());
        setupRecyclerView();
        setupToolbar();

        if (initialQuery != null) {
            presenter.searchMeals(initialQuery, initialArea);
        }
    }

    private void setupToolbar() {
        binding.searchToolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        if (initialArea != null && !initialArea.equalsIgnoreCase("Unknown")) {
            binding.searchToolbar.setTitle("Results in " + initialArea);
        }
    }

    private void setupRecyclerView() {
        adapter = new MealListAdapter(new ArrayList<>(), new MealListAdapter.OnMealClickListener() {
            @Override
            public void onMealClick(Meal meal) {
                SearchFragmentDirections.ActionSearchToDetails action = SearchFragmentDirections
                        .actionSearchToDetails(meal.getIdMeal());
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }

            @Override
            public void onAddToFavorite(Meal meal) {
                presenter.addToFavorites(meal);
                Toast.makeText(requireContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
            }
        });

        binding.searchRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.searchRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showSearchResults(List<Meal> meals) {
        binding.emptyStateLayout.setVisibility(View.GONE);
        binding.searchRecyclerView.setVisibility(View.VISIBLE);
        adapter.updateMeals(meals);
    }

    @Override
    public void showLoading() {
        binding.searchProgressBar.setVisibility(View.VISIBLE);
        binding.emptyStateLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        binding.searchProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyState() {
        binding.searchRecyclerView.setVisibility(View.GONE);
        binding.emptyStateLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
        binding = null;
    }
}
