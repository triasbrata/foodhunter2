package com.triasbrata.foodhunter.models;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.util.Log;

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
    private String name, address,background,logo,city ="string";
    private int id = 0;
    private Operation operation = new Operation();
    private ArrayList<Food> food_list = new ArrayList<>();
    private JsonObject rec = new JsonObject();

    public Store(@Nullable  JsonObject record) {
        if(!new JsonObject().equals(record) && record != null){
            setRec(record);
            if(record.has("address")) setAddress(record.get("address").getAsString());
            if(record.has("name"))setName(record.get("name").getAsString());
            if(record.has("background"))setBackground(record.get("background").getAsString());
            if(record.has("logo"))setLogo(record.get("logo").getAsString());
            if(record.has("id"))setId(record.get("id").getAsInt());
            if(record.has("city"))setCity(record.get("city").getAsString());
            if(record.has("operation"))setOperation(new Operation(record.get("operation").getAsJsonObject()));
            if(record.has("foods")){
                Food food;
                Store tmp = this;
                for (JsonElement rec :record.get("foods").getAsJsonArray()) {
                    food = new Food((JsonObject) rec);
                    food.setStore(tmp);
                    setFoodList(food);
                }
            }

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
        this.address = addresss;
    }

    public ArrayList<Food> getFoodList() {
        return food_list;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public Operation getOperation() {
        return operation;
    }

    public JsonObject getRec() {
        return rec;
    }

    private void setRec(JsonObject rec) {
        this.rec = rec;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public class Operation {

        private Date open;
        private Date close;
        private JsonObject rec = new JsonObject();
        public Operation(@Nullable JsonObject record) {

            if (record != null && !record.equals(new JsonObject())) {
                setRec(record);
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

        public JsonObject getRec() {
            return rec;
        }

        public void setRec(JsonObject rec) {
            this.rec = rec;
        }
    }
}
