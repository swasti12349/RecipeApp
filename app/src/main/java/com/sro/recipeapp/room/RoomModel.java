package com.sro.recipeapp.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.List;

@Entity(tableName = "RecipeTable")
public class RoomModel {

    @PrimaryKey(autoGenerate = true)
    int id;

    String itemid;
    String title;
    String image;
    String ingredients;
    String instruction;
    @TypeConverters(ListConverter.class)
    List<String> ingredientImage;

    public RoomModel(String itemid, String title, String image, String ingredients, String instruction, List<String> ingredientImage) {
        this.itemid = itemid;
        this.title = title;
        this.image = image;
        this.ingredients = ingredients;
        this.instruction = instruction;
        this.ingredientImage = ingredientImage;
    }

    public List<String> getIngredientImage() {
        return ingredientImage;
    }

    public void setIngredientImage(List<String> ingredientImage) {
        this.ingredientImage = ingredientImage;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
