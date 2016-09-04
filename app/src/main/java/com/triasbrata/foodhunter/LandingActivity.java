package com.triasbrata.foodhunter;


import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.parse.ParseObject;
import com.triasbrata.foodhunter.etc.CheckingGPS;
import com.triasbrata.foodhunter.fragment.LandingFragment;
import com.triasbrata.foodhunter.fragment.LoginFragment;
import com.triasbrata.foodhunter.fragment.RegisterFragment;

public class LandingActivity extends FragmentActivity {

    private boolean ismovedTopLayout = false;
    private boolean isExit = true;
    private boolean isLoginViewed,isRegisterViewed = false;
    private LandingFragment aLandingFragment;


    public void setLoginViewed(boolean loginViewed) {
        isLoginViewed = loginViewed;
    }

    public boolean isMovedTopLayout() {
        return ismovedTopLayout;
    }

    public void setMovedTopLayout(boolean movedTopLayout) {
        this.ismovedTopLayout = movedTopLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        aLandingFragment = LandingFragment.newInstance();
        openFragment(aLandingFragment);
        //new CheckingGPS((LocationManager) getSystemService(Context.LOCATION_SERVICE),this).nextRule();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //new CheckingGPS((LocationManager) getSystemService(Context.LOCATION_SERVICE),this).nextRule();
    }
    private void openFragment(final Fragment fragment)   {
        System.out.println(fragment.getClass());
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(!isExit){
            if(isLoginViewed || isRegisterViewed){
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
            }else{
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        }
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

    }
    public void loadLoginFragment() {
        isExit = false;
        openFragment(LoginFragment.newInstance());
    }

    @Override
    public void onBackPressed() {
        if(isRegisterViewed){
            openFragment(LoginFragment.newInstance());
            isRegisterViewed = false;
            return;
        }
        if(isLoginViewed){
            openFragment(aLandingFragment);
            isExit = true;
            isLoginViewed = false;
        }else{
            if(isExit){
                finish();
            }else{
                super.onBackPressed();
                isExit = true;
            }
        }
        if(ismovedTopLayout){
            translateFoodLayout();
            setMovedTopLayout(false);
        }


    }
    public void translateFoodLayout() {
        RelativeLayout layoutTop = (RelativeLayout) findViewById(R.id.layout_top);
        int topStrat = (ismovedTopLayout) ? -350 : 0;
        int topEnd = (!ismovedTopLayout) ? -350 : 0;
        TranslateAnimation anim = new TranslateAnimation(0,0,topStrat,topEnd);
        anim.setDuration(1000);
        anim.setFillAfter(true);
        layoutTop.startAnimation(anim);
    }

    public void loadRegisterFragment() {
        isExit = false;
        isRegisterViewed = true;
        openFragment(RegisterFragment.newInstance());
    }

}
