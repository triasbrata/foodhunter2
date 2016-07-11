package com.triasbrata.foodhunter.adapter.interfaces;

import android.view.View;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;

/**
 * Created by triasbrata on 11/07/16.
 */
public interface FoodListOnClickListener {
    void onClickListener(final View v,final String idItem);
}
