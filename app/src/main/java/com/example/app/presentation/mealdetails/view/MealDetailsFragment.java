package com.example.app.presentation.mealdetails.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.app.R;
import com.example.app.databinding.FragmentMealdetailsBinding;
import com.example.app.data.model.Meal;
import com.example.app.presentation.mealdetails.presenter.MealDetailsContract;
import com.example.app.presentation.mealdetails.presenter.MealDetailsPresenter;

public class MealDetailsFragment extends Fragment implements MealDetailsContract.View {

    private FragmentMealdetailsBinding binding;
    private MealDetailsContract.Presenter presenter;
    private String mealId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mealId = MealDetailsFragmentArgs.fromBundle(getArguments()).getMealId();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentMealdetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new MealDetailsPresenter(this, requireContext());
        if (mealId != null) {
            presenter.getMealById(mealId);
        }
    }

    @Override
    public void showMealDetails(Meal meal) {
        binding.mealName.setText(meal.getStrMeal());
        binding.mealCategory.setText(meal.getStrCategory());
        binding.mealArea.setText(meal.getStrArea());
        binding.mealInstructions.setText(meal.getStrInstructions());

        Glide.with(this)
                .load(meal.getStrMealThumb())
                .into(binding.mealImage);

        binding.addToFavButton.setOnClickListener(v -> presenter.addToFavorites(meal));
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
