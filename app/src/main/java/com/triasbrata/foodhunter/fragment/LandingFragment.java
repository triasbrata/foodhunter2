package com.triasbrata.foodhunter.fragment;



import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;

import com.triasbrata.foodhunter.DashboardActivity;
import com.triasbrata.foodhunter.LandingActivity;
import com.triasbrata.foodhunter.R;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class LandingFragment extends Fragment implements View.OnClickListener, Animation.AnimationListener {

    Button btnLogin, btnTryApp, btnLoginFb;
    LandingActivity mActivity;
    public LandingFragment() {
        // Required empty public constructor
    }
    public static LandingFragment newInstance() {
        return  new LandingFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = (LandingActivity) getActivity();
        btnLoginFb = (Button) view.findViewById(R.id.btnLogInFb);
        btnLogin = (Button) view.findViewById(R.id.btnLogIn);
        btnTryApp = (Button) view.findViewById(R.id.btnTryApp);
        btnLogin.setTypeface(getTypeFaceDemi());
        btnLoginFb.setTypeface(getTypeFaceDemi());
        btnTryApp.setTypeface(getTypeFaceDemi());
        btnLogin.setOnClickListener(this);
        btnTryApp.setOnClickListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_landing, container, false);
    }



    protected Typeface getTypeFaceDemi(){
        return  Typeface.createFromAsset(getActivity().getAssets(),"font/avenir-next-lt-pro/AvenirNextLTPro-DemiCn.otf");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogInFb:
                break;
            case R.id.btnTryApp:
                btnTryAppListener();
                break;
            case R.id.btnLogIn:
                btnLoginListener();
                break;
        }
    }

    private void btnTryAppListener() {
        startActivity(new Intent(mActivity,DashboardActivity.class));
        mActivity.finish();


    }

    private void btnLoginListener() {


        if(!mActivity.isMovedTopLayout()){
            mActivity.translateFoodLayout();
            mActivity.setMovedTopLayout(true);
            mActivity.loadLoginFragment();
            mActivity.setLoginViewed(true);
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        mActivity.loadLoginFragment();
        mActivity.setLoginViewed(true);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
