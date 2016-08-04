package com.triasbrata.foodhunter.etc;

import com.triasbrata.foodhunter.adapters.interfaces.RecycleViewItemOnClick;

/**
 * Created by triasbrata on 11/07/16.
 */
public class idItemAndListener {
    public String mId;
    public RecycleViewItemOnClick mListener;
    public idItemAndListener(String i, RecycleViewItemOnClick l) {
        mId = i;
        mListener = l;
    }

}
