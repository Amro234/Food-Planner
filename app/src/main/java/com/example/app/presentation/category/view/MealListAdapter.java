package com.example.app.presentation.category.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app.databinding.ItemMealBinding;
import com.example.app.data.model.Meal;

import java.util.List;

public class MealListAdapter extends RecyclerView.Adapter<MealListAdapter.MealViewHolder> {

    private List<Meal> meals;
    private final OnMealClickListener listener;
    private final boolean isHorizontal;

    public interface OnMealClickListener {
        void onMealClick(Meal meal);

        void onAddToFavorite(Meal meal);
    }

    public MealListAdapter(List<Meal> meals, OnMealClickListener listener) {
        this(meals, listener, false);
    }

    public MealListAdapter(List<Meal> meals, OnMealClickListener listener, boolean isHorizontal) {
        this.meals = meals;
        this.listener = listener;
        this.isHorizontal = isHorizontal;
    }

    public void updateMeals(List<Meal> meals) {
        this.meals.clear();
        if (meals != null) {
            this.meals.addAll(meals);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = isHorizontal ? com.example.app.R.layout.item_meal_horizontal
                : com.example.app.R.layout.item_meal;
        android.view.View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        ItemMealBinding binding = ItemMealBinding.bind(view);
        return new MealViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        holder.bind(meals.get(position));
    }

    @Override
    public int getItemCount() {
        return meals == null ? 0 : meals.size();
    }

    class MealViewHolder extends RecyclerView.ViewHolder {
        private final ItemMealBinding binding;

        MealViewHolder(ItemMealBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Meal meal) {
            binding.mealName.setText(meal.getStrMeal());
            Glide.with(binding.getRoot().getContext())
                    .load(meal.getStrMealThumb())
                    .into(binding.mealImage);

            binding.getRoot().setOnClickListener(v -> listener.onMealClick(meal));
            binding.LoveId.setOnClickListener(v -> listener.onAddToFavorite(meal));
        }
    }
}
