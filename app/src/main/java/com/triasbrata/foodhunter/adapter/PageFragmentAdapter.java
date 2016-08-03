package com.triasbrata.foodhunter.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;


import com.triasbrata.foodhunter.fragment.StoreSectionFrament;
import com.triasbrata.foodhunter.fragment.interfaces.RecyclerAdapterRefresh;

import java.util.ArrayList;

/**
 * Created by triasbrata on 08/07/16.
 */
public class PageFragmentAdapter extends FragmentPagerAdapter {
    private static final String TAG = "PageFragmentAdapter";
    private final ArrayList<Fragment> mFragments;
    public PageFragmentAdapter(ArrayList<Fragment> mFragments, FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
        this.mFragments = mFragments;
    }
    @Override
    public Fragment getItem(int pos) {
        return mFragments.get(pos);
    }
    @Override
    public int getCount() {
        return mFragments.size();
    }

}
