package com.example.app.data.model;

public class Ingredient {
    private String idIngredient;
    private String strIngredient;
    private String strDescription;
    private String strType;

    public Ingredient() {
    }

    public Ingredient(String strIngredient, String measure) {
        this.strIngredient = strIngredient;
        this.measure = measure;
    }

    public String getIdIngredient() {
        return idIngredient;
    }

    public String getStrIngredient() {
        return strIngredient;
    }

    public String getStrDescription() {
        return strDescription;
    }

    public String getStrType() {
        return strType;
    }

    public String getImageUrl() {
        return "https://www.themealdb.com/images/ingredients/" + strIngredient + "-small.png";
    }

    // For backward compatibility with meal details if needed
    public String getName() {
        return strIngredient;
    }

    // Measure is only relevant in context of a meal's recipe, not in the master
    // list
    private String measure;

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }
}
