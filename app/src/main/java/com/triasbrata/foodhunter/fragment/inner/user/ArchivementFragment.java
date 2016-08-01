package com.triasbrata.foodhunter.fragment.inner.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.triasbrata.foodhunter.R;

/**
 * Created by triasbrata on 24/07/16.
 */
public class ArchivementFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "ArchivementFragment";
    public ArchivementFragment(){}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: archivementFragment loaded");
        return  inflater.inflate(R.layout.fragment_inner_user_archivement,container,false);
    }
}
