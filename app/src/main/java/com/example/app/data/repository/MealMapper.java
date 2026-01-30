package com.example.app.data.repository;

import com.example.app.Database.MealEntity;
import com.example.app.data.model.Meal;

public class MealMapper {
    public static MealEntity toEntity(Meal meal) {
        MealEntity entity = new MealEntity();
        entity.setIdMeal(meal.getIdMeal());
        entity.setStrMeal(meal.getStrMeal());
        entity.setStrCategory(meal.getStrCategory());
        entity.setStrArea(meal.getStrArea());
        entity.setStrInstructions(meal.getStrInstructions());
        entity.setStrMealThumb(meal.getStrMealThumb());
        entity.setStrTags(meal.getStrTags());
        entity.setStrYoutube(meal.getStrYoutube());

        // Ingredients and Measures
        entity.setStrIngredient1(meal.getStrIngredient1());
        entity.setStrIngredient2(meal.getStrIngredient2());
        entity.setStrIngredient3(meal.getStrIngredient3());
        entity.setStrIngredient4(meal.getStrIngredient4());
        entity.setStrIngredient5(meal.getStrIngredient5());
        entity.setStrIngredient6(meal.getStrIngredient6());
        entity.setStrIngredient7(meal.getStrIngredient7());
        entity.setStrIngredient8(meal.getStrIngredient8());
        entity.setStrIngredient9(meal.getStrIngredient9());
        entity.setStrIngredient10(meal.getStrIngredient10());
        entity.setStrIngredient11(meal.getStrIngredient11());
        entity.setStrIngredient12(meal.getStrIngredient12());
        entity.setStrIngredient13(meal.getStrIngredient13());
        entity.setStrIngredient14(meal.getStrIngredient14());
        entity.setStrIngredient15(meal.getStrIngredient15());
        entity.setStrIngredient16(meal.getStrIngredient16());
        entity.setStrIngredient17(meal.getStrIngredient17());
        entity.setStrIngredient18(meal.getStrIngredient18());
        entity.setStrIngredient19(meal.getStrIngredient19());
        entity.setStrIngredient20(meal.getStrIngredient20());

        entity.setStrMeasure1(meal.getStrMeasure1());
        entity.setStrMeasure2(meal.getStrMeasure2());
        entity.setStrMeasure3(meal.getStrMeasure3());
        entity.setStrMeasure4(meal.getStrMeasure4());
        entity.setStrMeasure5(meal.getStrMeasure5());
        entity.setStrMeasure6(meal.getStrMeasure6());
        entity.setStrMeasure7(meal.getStrMeasure7());
        entity.setStrMeasure8(meal.getStrMeasure8());
        entity.setStrMeasure9(meal.getStrMeasure9());
        entity.setStrMeasure10(meal.getStrMeasure10());
        entity.setStrMeasure11(meal.getStrMeasure11());
        entity.setStrMeasure12(meal.getStrMeasure12());
        entity.setStrMeasure13(meal.getStrMeasure13());
        entity.setStrMeasure14(meal.getStrMeasure14());
        entity.setStrMeasure15(meal.getStrMeasure15());
        entity.setStrMeasure16(meal.getStrMeasure16());
        entity.setStrMeasure17(meal.getStrMeasure17());
        entity.setStrMeasure18(meal.getStrMeasure18());
        entity.setStrMeasure19(meal.getStrMeasure19());
        entity.setStrMeasure20(meal.getStrMeasure20());

        entity.setStrSource(meal.getStrSource());
        entity.setStrImageSource(meal.getStrImageSource());

        return entity;
    }

    public static Meal fromEntity(MealEntity entity) {
        Meal meal = new Meal();
        meal.setIdMeal(entity.getIdMeal());
        meal.setStrMeal(entity.getStrMeal());
        meal.setStrCategory(entity.getStrCategory());
        meal.setStrArea(entity.getStrArea());
        meal.setStrInstructions(entity.getStrInstructions());
        meal.setStrMealThumb(entity.getStrMealThumb());
        meal.setStrTags(entity.getStrTags());
        meal.setStrYoutube(entity.getStrYoutube());

        // Ingredients and Measures (simplified, add all if needed)
        meal.setStrIngredient1(entity.getStrIngredient1());
        // ... (can add more if required in UI)

        return meal;
    }
}
