package com.triasbrata.foodhunter.model;

import android.graphics.Bitmap;
import android.view.View;

import com.triasbrata.foodhunter.adapter.interfaces.FoodListOnClickListener;

/**
 * Created by triasbrata on 09/07/16.
 */
public class FoodModel {

    private String foodImage,foodName,storeName,storeAdress,storeId,FoodId = "";
    private int foodPrice = 0;
    private boolean userLike = true;
    private FoodListOnClickListener listenerBtnLike,listenerBtnBrowse, listenerCardView;

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
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

    public FoodListOnClickListener getListenerBtnLike() {
        if(listenerBtnLike == null) return new FoodListOnClickListener() {
            @Override
            public void onClickListener(View v,final String idItem) {

            }
        };
        return listenerBtnLike;
    }

    public FoodListOnClickListener getListenerCardView() {
        if(listenerCardView == null) return new FoodListOnClickListener() {
            @Override
            public void onClickListener(View v,final String idItem) {

            }
        };
        return listenerCardView;
    }

    public FoodListOnClickListener getListenerBtnBrowse() {
        if(listenerBtnBrowse == null) return new FoodListOnClickListener() {
            @Override
            public void onClickListener(View v,final String idItem) {

            }
        };
        return listenerBtnBrowse;
    }

    public void addListenerBtnBrowse(FoodListOnClickListener listenerBtnBrowse) {
        this.listenerBtnBrowse = listenerBtnBrowse;
    }

    public void addListenerCardView(FoodListOnClickListener listenerCardView) {
        this.listenerCardView = listenerCardView;
    }

    public void addListenerBtnLike(FoodListOnClickListener listenerBtnLike) {
        this.listenerBtnLike = listenerBtnLike;
    }

    public String getFoodId() {
        return FoodId;
    }

    public void setFoodId(String foodId) {
        FoodId = foodId;
    }
}
