package com.sro.recipeapp.model;

public class CartModel {
    String itemName;
    String itemImg;

    public CartModel(String itemName, String itemImg) {
        this.itemName = itemName;
        this.itemImg = itemImg;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemImg() {
        return itemImg;
    }

    public void setItemImg(String itemImg) {
        this.itemImg = itemImg;
    }
}
