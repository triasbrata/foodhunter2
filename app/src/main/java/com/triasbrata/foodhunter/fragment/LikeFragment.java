package com.triasbrata.foodhunter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.triasbrata.foodhunter.R;

/**
 * Created by triasbrata on 08/07/16.
 */
public class LikeFragment extends Fragment{
    public LikeFragment(){}

    public static LikeFragment newInstance() {
        return new LikeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         return inflater.inflate(R.layout.fragment_like,container,false);
    }
}