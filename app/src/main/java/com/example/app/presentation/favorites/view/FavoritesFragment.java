package com.example.app.presentation.favorites.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.app.Database.MealEntity;
import com.example.app.databinding.FragmentFavBinding;
import com.example.app.presentation.favorites.presenter.FavoritesContract;
import com.example.app.presentation.favorites.presenter.FavoritesPresenter;
import com.example.app.data.repository.UserRepositoryImp;
import androidx.navigation.Navigation;
import com.example.app.presentation.favorites.view.FavoritesFragmentDirections;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment implements FavoritesContract.View {

    private FragmentFavBinding binding;
    private FavoritesContract.Presenter presenter;
    private FavoritesAdapter adapter;
    // Assuming a FavoritesAdapter exists or using a generic one
    // For now, focusing on the MVP structure

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentFavBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new FavoritesPresenter(this, requireContext());
        setupRecyclerView();

        if (UserRepositoryImp.getInstance(requireContext()).isGuestMode()) {
            binding.favRecyclerView.setVisibility(View.GONE);
            binding.emptyStateText.setVisibility(View.VISIBLE);
            binding.emptyStateText.setText("You must sign-in to see your Favorites!");
        } else {
            presenter.getFavoriteMeals();
        }
    }

    private void setupRecyclerView() {
        adapter = new FavoritesAdapter(new ArrayList<>(), new FavoritesAdapter.OnFavoriteClickListener() {
            @Override
            public void onMealClick(MealEntity meal) {
                FavoritesFragmentDirections.ActionFavToDetails action = FavoritesFragmentDirections
                        .actionFavToDetails(meal.getIdMeal());
                Navigation.findNavController(requireView()).navigate(action);
            }

            @Override
            public void onRemoveClick(MealEntity meal) {
                presenter.removeFromFavorites(meal.getIdMeal());
            }
        });
        binding.favRecyclerView
                .setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(requireContext()));
        binding.favRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showFavoriteMeals(List<MealEntity> meals) {
        binding.favRecyclerView.setVisibility(View.VISIBLE);
        binding.emptyStateText.setVisibility(View.GONE);
        adapter.updateMeals(meals);
    }

    @Override
    public void showEmptyState() {
        binding.favRecyclerView.setVisibility(View.GONE);
        binding.emptyStateText.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
        binding = null;
    }
}
