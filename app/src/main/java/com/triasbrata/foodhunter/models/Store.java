package com.triasbrata.foodhunter.models;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by triasbrata on 03/08/16.
 */
public class Store{
    private final String TAG = getClass().getSimpleName();
    private String name,addresss;
    private int id;
    private String city;
    private Operation operation = new Operation();
    private ArrayList<Food> food_list = new ArrayList<>();

    public Store(@Nullable  JsonObject record) {
        if(!new JsonObject().equals(record) && record != null){

            if(record.has("address")) setAddress(record.get("address").getAsString());
            if(record.has("name"))setName(record.get("name").getAsString());
            if(record.has("id"))setId(record.get("id").getAsInt());
            if(record.has("city"))setCity(record.get("city").getAsString());
            if(record.has("operation"))setOperation(new Operation(record.get("operation").getAsJsonObject()));
            if(record.has("food")){
                Food food;
                for (JsonElement rec :record.get("food").getAsJsonArray()) {
                    food = new Food((JsonObject) rec);
                    setFoodList(food);
                }
            }
            Log.d(TAG, "Store: success casting");
        }
    }

    public Store() {}

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;

    }

    public void setFoodList(Food food) {
        this.food_list.add(food);
    }

    public void setAddress(String addresss) {
        this.addresss = addresss;
    }

    public ArrayList<Food> getFoodList() {
        return food_list;
    }

    public String getCity() {
        return city;
    }

    public String getAddresss() {
        return addresss;
    }

    public Operation getOperation() {
        return operation;
    }

    public class Operation {

        private Date open;
        private Date close;

        public Operation(@Nullable JsonObject record) {
            if (record != null && !record.equals(new JsonObject())) {
                @SuppressLint("SimpleDateFormat")
                 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H:m");
                try {
                    if (record.has("open"))
                        setOpen(simpleDateFormat.parse(record.get("open").getAsString()));
                    if (record.has("close"))
                        setClose(simpleDateFormat.parse(record.get("close").getAsString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Log.d(TAG, "Operation: success casting");
        }
        public Operation() {
            this(null);
        }


        public Date getOpen() {
            return open;
        }

        public void setOpen(Date open) {
            this.open = open;
        }

        public Date getClose() {
            return close;
        }

        public void setClose(Date close) {
            this.close = close;
        }
    }
}
