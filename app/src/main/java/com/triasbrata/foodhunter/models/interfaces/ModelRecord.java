package com.triasbrata.foodhunter.models.interfaces;

import com.google.gson.JsonObject;

/**
 * Created by triasbrata on 23/08/16.
 */
public interface ModelRecord {
    public void setRec(JsonObject jsonObject);
    public JsonObject getRec();
}
