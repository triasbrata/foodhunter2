package com.triasbrata.foodhunter.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.triasbrata.foodhunter.fragment.LikeFragment;
import com.triasbrata.foodhunter.fragment.PopularFragment;
import com.triasbrata.foodhunter.fragment.SearchFragment;
import com.triasbrata.foodhunter.fragment.UserFragment;
import com.triasbrata.foodhunter.model.FoodModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by triasbrata on 08/07/16.
 */
public class PageFragmentAdapter extends FragmentPagerAdapter {
    private final int PAGE_COUNT= 4;
    private final HashMap mFragmentTags = new HashMap<Integer,String>();
    private final FragmentManager mFragmentManager;

    public PageFragmentAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
        mFragmentManager = supportFragmentManager;
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

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        Fragment f = (Fragment) obj;
        String tag = f.getTag();
        mFragmentTags.put(position,tag);
        return obj;
    }
}
