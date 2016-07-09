package com.triasbrata.foodhunter.model;

import android.graphics.Bitmap;

/**
 * Created by triasbrata on 09/07/16.
 */
public class FoodModel {
    private Bitmap foodImage;
    private String foodName,storeName,storeAdress,storeId;
    private int foodPrice;
    private boolean userLike;

    public Bitmap getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(Bitmap foodImage) {
        this.foodImage = foodImage;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAdress() {
        return storeAdress;
    }

    public void setStoreAdress(String storeAdress) {
        this.storeAdress = storeAdress;
    }

    public int getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(int foodPrice) {
        this.foodPrice = foodPrice;
    }

    public boolean isUserLike() {
        return userLike;
    }

    public void setUserLike(boolean userLike) {
        this.userLike = userLike;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreId() {
        return storeId;
    }

    public FoodModel getModel() {
        return this;
    }
}
