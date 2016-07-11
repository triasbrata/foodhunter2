package com.triasbrata.foodhunter.etc;

import android.support.v7.widget.CardView;
import android.view.View;

import com.triasbrata.foodhunter.adapter.interfaces.FoodListOnClickListener;

import java.util.HashMap;

/**
 * Created by triasbrata on 11/07/16.
 */
public class idItemAndListener {
    public String mId;
    public FoodListOnClickListener mListener;
    public idItemAndListener(String i, FoodListOnClickListener l) {
        mId = i;
        mListener = l;
    }

}
