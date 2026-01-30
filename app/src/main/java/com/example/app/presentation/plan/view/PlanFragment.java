package com.example.app.presentation.plan.view;

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
import com.example.app.databinding.FragmentPlanBinding;
import com.example.app.presentation.plan.presenter.PlanContract;
import com.example.app.presentation.plan.presenter.PlanPresenter;

import java.util.ArrayList;
import java.util.List;

public class PlanFragment extends Fragment implements PlanContract.View {

    private FragmentPlanBinding binding;
    private PlanContract.Presenter presenter;
    private PlanAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentPlanBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new PlanPresenter(this, requireContext());
        setupRecyclerView();
        presenter.getPlannedMeals();
    }

    private void setupRecyclerView() {
        adapter = new PlanAdapter(new ArrayList<>(), new PlanAdapter.OnPlanClickListener() {
            @Override
            public void onMealClick(MealEntity meal) {
                // Navigate to details
            }

            @Override
            public void onRemoveClick(MealEntity meal) {
                presenter.removeFromPlan(meal.getIdMeal());
            }
        });
        binding.planRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.planRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showPlannedMeals(List<MealEntity> meals) {
        adapter.updateMeals(meals);
        binding.planRecyclerView.setVisibility(View.VISIBLE);
        binding.emptyPlanText.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyState() {
        binding.planRecyclerView.setVisibility(View.GONE);
        binding.emptyPlanText.setVisibility(View.VISIBLE);
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
