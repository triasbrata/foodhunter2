package com.triasbrata.foodhunter.models;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.triasbrata.foodhunter.adapters.interfaces.RecycleViewItemOnClick;
import com.triasbrata.foodhunter.models.interfaces.ModelRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by triasbrata on 09/07/16.
 */
public class Food implements ModelRecord {
    private String TAG = getClass().getSimpleName();

    private String name;
    private int price = 0,id;
    private Image image = new Image();
    private boolean userLike = true;
    private Store store = new Store();
    private RecycleViewItemOnClick listenerBtnLike,listenerBtnBrowse, listenerCardView;
    private View.OnClickListener listenerHolder;
    private JsonObject rec = new JsonObject();

    public Food(@Nullable  JsonObject record) {
        if(!new JsonObject().equals(record) && record != null){
            setRec(record);
            if(record.has("id"))setId(record.get("id").getAsInt());
            if(record.has("name"))setName(record.get("name").getAsString());
            if(record.has("price"))setPrice(record.get("price").getAsInt());
            if(record.has("image"))setImage(new Image(record.get("image").getAsJsonObject()));
            if(record.has("store"))setStore(new Store(record.get("store").getAsJsonObject()));
            Log.d(TAG, "Food: success casting");
        }
    }

    public Food() {
        this(null);

    }


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public RecycleViewItemOnClick getListenerBtnLike() {
        return listenerBtnLike;
    }

    public RecycleViewItemOnClick getListenerCardView() {

        return listenerCardView;
    }

    public RecycleViewItemOnClick getListenerBtnBrowse() {
        return listenerBtnBrowse;
    }

    public void addListenerBtnBrowse(RecycleViewItemOnClick listenerBtnBrowse) {
        this.listenerBtnBrowse = listenerBtnBrowse;
    }

    public void addListenerCardView(RecycleViewItemOnClick listenerCardView) {
        this.listenerCardView = listenerCardView;
    }

    public void addListenerBtnLike(RecycleViewItemOnClick listenerBtnLike) {
        this.listenerBtnLike = listenerBtnLike;
    }

    public View.OnClickListener getListenerHolder() {
        return listenerHolder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImage() {
        return image;
    }

    public Store getStore() {
        return store;
    }


    public boolean isUserLike() {
        return userLike;
    }

    public void setUserLike(boolean userLike) {
        this.userLike = userLike;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStore(Store tmpStore) {
        this.store = tmpStore;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public static Food parseJson(String string) {
        return new  Food(new JsonParser().parse(string).getAsJsonObject());
    }

    public JsonObject getRec() {
        return rec;
    }

    public void setRec(JsonObject rec) {
        this.rec = rec;
    }


    public class Image implements ModelRecord {
        private JsonObject rec = new JsonObject();
        private String preview;
        private List<String> slide = new ArrayList<>();

        public Image(@Nullable  JsonObject record) {
            if(!new JsonObject().equals(record) && record != null){
                setRec(record);
                if(record.has("preview"))setPreview(record.get("preview").getAsString());
                if(record.has("slide")){
                    for (JsonElement slide: record.get("slide").getAsJsonArray()) {
                        setSlide(slide.getAsString());
                    }
                }
                Log.d(TAG, "Image: success casting");
            }
        }

        public Image() {
            this(null);
        }

        public String getPreview() {
            return preview;
        }

        public void setPreview(String preview) {
            this.preview = preview;
        }

        public List<String> getSlide() {
            return slide;
        }

        public void setSlide(String slide) {
            this.slide.add(slide);
        }

        public JsonObject getRec() {
            return rec;
        }

        public void setRec(JsonObject rec) {
            this.rec = rec;
        }
    }
}
