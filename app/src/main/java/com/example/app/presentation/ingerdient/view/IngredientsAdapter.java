package com.example.app.presentation.ingerdient.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.app.R;
import com.example.app.data.model.Ingredient;
import com.example.app.databinding.ItemIngredientBinding;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private List<Ingredient> ingredientList;

    public IngredientsAdapter(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public void setList(List<Ingredient> list) {
        this.ingredientList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemIngredientBinding binding = ItemIngredientBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = ingredientList.get(position);
        holder.binding.tvIngredientName.setText(ingredient.getName());
        holder.binding.tvIngredientMeasure.setText(ingredient.getMeasure());

        Glide.with(holder.binding.imgIngredient.getContext())
                .load(ingredient.getImageUrl())
                .placeholder(R.drawable.image_holder)
                .into(holder.binding.imgIngredient);
    }

    @Override
    public int getItemCount() {
        return ingredientList == null ? 0 : ingredientList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemIngredientBinding binding;

        public ViewHolder(ItemIngredientBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}