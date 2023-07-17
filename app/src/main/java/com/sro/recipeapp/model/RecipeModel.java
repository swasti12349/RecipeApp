package com.sro.recipeapp.model;

import java.util.List;

public class RecipeModel {
    String id;
    String title;
    String image;
    String ingredients;
    String instruction;
    List<String> ingredientImage;

    public List<String> getIngredientImage() {
        return ingredientImage;
    }

    public void setIngredientImage(List<String> ingredientImage) {
        this.ingredientImage = ingredientImage;
    }

    public RecipeModel(String id, String title, String image, String ingredients, String instruction, List<String> ingredientImage) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.ingredients = ingredients;
        this.instruction = instruction;
        this.ingredientImage = ingredientImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
