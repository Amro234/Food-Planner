package com.example.app.presentation.favorites.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app.databinding.ItemMealBinding;
import com.example.app.Database.MealEntity;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {

    private List<MealEntity> meals;
    private final OnFavoriteClickListener listener;

    public interface OnFavoriteClickListener {
        void onMealClick(MealEntity meal);

        void onRemoveClick(MealEntity meal);
    }

    public FavoritesAdapter(List<MealEntity> meals, OnFavoriteClickListener listener) {
        this.meals = meals;
        this.listener = listener;
    }

    public void updateMeals(List<MealEntity> newMeals) {
        this.meals = newMeals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMealBinding binding = ItemMealBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new FavoriteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        holder.bind(meals.get(position));
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        private final ItemMealBinding binding;

        FavoriteViewHolder(ItemMealBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(MealEntity meal) {
            binding.mealName.setText(meal.getStrMeal());
            Glide.with(binding.getRoot().getContext())
                    .load(meal.getStrMealThumb())
                    .into(binding.mealImage);

            binding.getRoot().setOnClickListener(v -> listener.onMealClick(meal));
            // Assuming there's a delete icon or similar in ItemMealBinding or handling it
            // via long press
            // For now, simple click
        }
    }
}
