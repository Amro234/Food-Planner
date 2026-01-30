package com.example.app.data.model;

import java.io.Serializable;
import java.util.List;

import com.example.app.data.model.Meal;
import com.google.gson.annotations.SerializedName;

public class MealResponse implements Serializable {

    @SerializedName("meals")
    private List<Meal> meals;

    public MealResponse() {
    }

    public MealResponse(List<Meal> meals) {
        super();
        this.meals = meals;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public MealResponse withMeals(List<Meal> meals) {
        this.meals = meals;
        return this;
    }

}