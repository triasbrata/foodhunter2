package com.triasbrata.foodhunter.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.triasbrata.foodhunter.DashboardActivity;
import com.triasbrata.foodhunter.R;
import com.triasbrata.foodhunter.adapter.PageFragmentAdapter;
import com.triasbrata.foodhunter.fragment.inner.user.ArchivementFragment;
import com.triasbrata.foodhunter.fragment.inner.user.LikeFragment;
import com.triasbrata.foodhunter.fragment.interfaces.RecyclerAdapterRefresh;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * Created by triasbrata on 08/07/16.
 */
public class UserSectionFragment extends Fragment implements RecyclerAdapterRefresh, View.OnClickListener, ViewPager.OnPageChangeListener {

    private boolean mNavIsClicked = true;
    private final String TAG = "UserSectionFragment";
    @BindView(R.id.view_pager_inner) ViewPager mViewPager;
    private Context mContex;
    @BindView(R.id.txtNamaUser) TextView mTxtNamaUser;
    @BindView(R.id.txtCityUser) TextView mTxtCityUser;
    @BindView(R.id.txtQuoteUser) TextView mTxtQuoteUser;
    @BindView(R.id.btn_nav_arc) RelativeLayout mBtnNavArc;
    @BindView(R.id.btn_nav_fav) RelativeLayout mBtnNavFav;


    public UserSectionFragment(){

    }

    public static UserSectionFragment newInstance() {
        return  new UserSectionFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            mContex = container.getContext();
        }
        return inflater.inflate(R.layout.fragment_user_section,container,false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new LikeFragment());
        fragments.add(new ArchivementFragment());
        PageFragmentAdapter pfa = new PageFragmentAdapter(fragments,getChildFragmentManager());
        mViewPager.setAdapter(pfa);
        mViewPager.setCurrentItem(0,true);
        mViewPager.addOnPageChangeListener(this);
        changeFont(view);
        registerListener();
    }

    private void registerListener() {
        mBtnNavFav.setOnClickListener(this);
        mBtnNavArc.setOnClickListener(this);

    }

    private void changeFont(final View v) {
        Typeface tfR = Typeface.createFromAsset(getResources().getAssets(),"font/avenir-next-lt-pro/AvenirNextLTPro-Regular.otf");
        Typeface tfD = Typeface.createFromAsset(getResources().getAssets(),"font/avenir-next-lt-pro/AvenirNextLTPro-DemiCn.otf");
        ((TextView) v.findViewById(R.id.nav_arch)).setTypeface(tfD);
        ((TextView) v.findViewById(R.id.nav_fav)).setTypeface(tfD);
        mTxtNamaUser.setTypeface(tfD);
        mTxtCityUser.setTypeface(tfR);
        mTxtQuoteUser.setTypeface(tfR);
    }

    @Override
    public void dataRefresher() {

    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.btn_nav_arc:
               mViewPager.setCurrentItem(1);
               break;
           case R.id.btn_nav_fav :
               mViewPager.setCurrentItem(0);
               break;
       }
    }

    private void listenerNav(final int id) {

        switch (id){
            case 0:
                    mBtnNavArc.setBackgroundColor(Color.parseColor("#00000000"));
                    mBtnNavFav.setBackgroundColor(Color.parseColor("#FFFFFF"));
            break;

            case 1:
                    mBtnNavFav.setBackgroundColor(Color.parseColor("#00000000"));
                    mBtnNavArc.setBackgroundColor(Color.parseColor("#FFFFFF"));
            break;
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        listenerNav(position);
    }

    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "onPageSelected: ");
        listenerNav(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}