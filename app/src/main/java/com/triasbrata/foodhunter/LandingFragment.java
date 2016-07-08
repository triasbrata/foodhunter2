package com.triasbrata.foodhunter;



import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class LandingFragment extends Fragment implements View.OnClickListener, Animation.AnimationListener {

    Button btnLogin, btnTryApp, btnLoginFb;
    LandingActivity mActivity;
    View v;
    public LandingFragment() {
        // Required empty public constructor
    }
    public static LandingFragment newInstance() {
        return  new LandingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        mActivity = (LandingActivity) getActivity();
        v = inflater.inflate(R.layout.fragment_landing, container, false);
        btnLoginFb = (Button) v.findViewById(R.id.btnLogInFb);
        btnLogin = (Button) v.findViewById(R.id.btnLogIn);
        btnTryApp = (Button) v.findViewById(R.id.btnTryApp);
        btnLogin.setTypeface(getTypeFaceDemi());
        btnLoginFb.setTypeface(getTypeFaceDemi());
        btnTryApp.setTypeface(getTypeFaceDemi());
        btnLogin.setOnClickListener(this);
        btnTryApp.setOnClickListener(this);
        return v;
    }



    protected Typeface getTypeFaceDemi(){
        return  Typeface.createFromAsset(getActivity().getAssets(),"font/avenir-next-lt-pro/AvenirNextLTPro-DemiCn.otf");
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnLogin.getId()) {
            btnLoginListener();
            return;
        }
        if(v.getId() == btnTryApp.getId()) {
            btnTryAppListener();
            return;
        }
    }

    private void btnTryAppListener() {


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
