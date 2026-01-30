package com.example.app.presentation.plan.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app.databinding.ItemMealBinding;
import com.example.app.Database.MealEntity;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {

    private List<MealEntity> meals;
    private final OnPlanClickListener listener;

    public interface OnPlanClickListener {
        void onMealClick(MealEntity meal);

        void onRemoveClick(MealEntity meal);
    }

    public PlanAdapter(List<MealEntity> meals, OnPlanClickListener listener) {
        this.meals = meals;
        this.listener = listener;
    }

    public void updateMeals(List<MealEntity> newMeals) {
        this.meals = newMeals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMealBinding binding = ItemMealBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new PlanViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        holder.bind(meals.get(position));
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    class PlanViewHolder extends RecyclerView.ViewHolder {
        private final ItemMealBinding binding;

        PlanViewHolder(ItemMealBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(MealEntity meal) {
            binding.mealName.setText(meal.getStrMeal() + " (" + meal.getPlannedDay() + ")");
            Glide.with(binding.getRoot().getContext())
                    .load(meal.getStrMealThumb())
                    .into(binding.mealImage);

            binding.getRoot().setOnClickListener(v -> listener.onMealClick(meal));
        }
    }
}
