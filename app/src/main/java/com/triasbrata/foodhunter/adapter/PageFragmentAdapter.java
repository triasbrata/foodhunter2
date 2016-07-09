package com.triasbrata.foodhunter.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.triasbrata.foodhunter.fragment.LikeFragment;
import com.triasbrata.foodhunter.fragment.PopularFragment;
import com.triasbrata.foodhunter.fragment.SearchFragment;
import com.triasbrata.foodhunter.fragment.UserFragment;

/**
 * Created by triasbrata on 08/07/16.
 */
public class PageFragmentAdapter extends FragmentPagerAdapter {
    private final int PAGE_COUNT= 4;
    public PageFragmentAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }
    @Override
    public Fragment getItem(int pos) {
        switch(pos) {

            case 0:
            default:
                return SearchFragment.newInstance();
            case 1: return LikeFragment.newInstance();
            case 2: return PopularFragment.newInstance();
            case 3: return UserFragment.newInstance();

        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
