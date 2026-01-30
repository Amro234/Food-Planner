package com.example.app.data.model;

public class Ingredient {

    private String name;
    private String measure;

    public Ingredient(String name, String measure) {
        this.name = name;
        this.measure = measure;
    }

    public String getName() {
        return name;
    }

    public String getMeasure() {
        return measure;
    }

    public String getImageUrl() {
        return "https://www.themealdb.com/images/ingredients/" + name + "-small.png";
    }
}
