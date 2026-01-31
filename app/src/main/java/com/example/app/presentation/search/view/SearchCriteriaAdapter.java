package com.example.app.presentation.search.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.app.R;
import com.example.app.databinding.ItemCategoryBinding;
import java.util.ArrayList;
import java.util.List;

public class SearchCriteriaAdapter extends RecyclerView.Adapter<SearchCriteriaAdapter.ViewHolder> {

    private List<SearchCriteria> items = new ArrayList<>();
    private final OnCriteriaClickListener listener;

    public interface OnCriteriaClickListener {
        void onCriteriaClick(SearchCriteria criteria);
    }

    public static class SearchCriteria {
        private final String name;
        private final String imageUrl;
        private final Type type;

        public enum Type {
            CATEGORY, INGREDIENT, AREA
        }

        public SearchCriteria(String name, String imageUrl, Type type) {
            this.name = name;
            this.imageUrl = imageUrl;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public Type getType() {
            return type;
        }
    }

    public SearchCriteriaAdapter(OnCriteriaClickListener listener) {
        this.listener = listener;
    }

    public void updateList(List<SearchCriteria> newList) {
        this.items = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding binding = ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchCriteria item = items.get(position);
        holder.binding.categoryName.setText(item.getName());
        Glide.with(holder.binding.categoryImage.getContext())
                .load(item.getImageUrl())
                .placeholder(R.drawable.image_holder)
                .into(holder.binding.categoryImage);

        holder.itemView.setOnClickListener(v -> listener.onCriteriaClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemCategoryBinding binding;

        public ViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
