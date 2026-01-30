package com.example.app.data.model;

import com.example.app.data.model.Category;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class CategoryResponse implements Serializable {

    @SerializedName("categories")
    private List<Category> categories;

    public CategoryResponse() {
    }

    public CategoryResponse(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
