package com.triasbrata.foodhunter;

import android.graphics.Bitmap;
import android.graphics.Color;

import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;



import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gigamole.navigationtabbar.ntb.NavigationTabBar;
import com.nineoldandroids.animation.Animator;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.triasbrata.foodhunter.adapter.PageFragmentAdapter;
import com.triasbrata.foodhunter.etc.BitmapOperation;


import java.util.ArrayList;

public class DashboardActivity extends FragmentActivity{

    ViewPager aViewPage;
    Drawable userImage;
    private TextView textLoading;
    private LinearLayout loadingLayout;
    private ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
    public NavigationTabBar navigationTabBar;
    private Target target = new Target() {


        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            models.add(
                    new NavigationTabBar.Model.Builder(new BitmapDrawable(getResources(),BitmapOperation.getRoundedCornerBitmap(bitmap,Color.WHITE,100,5,DashboardActivity.this)),Color.parseColor("#ffffff")).build()
            );
            navigationTabBar.setModels(models);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };
    private boolean isLogged = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        textLoading = (TextView) findViewById(R.id.text_loading);
        textLoading.setTypeface(Typeface.createFromAsset(getAssets(),"font/avenir-next-lt-pro/AvenirNextLTPro-Demi.otf"));


        aViewPage = (ViewPager) findViewById(R.id.view_pager);
        final PageFragmentAdapter pAdapter = new PageFragmentAdapter(getSupportFragmentManager());
        aViewPage.setAdapter(pAdapter);
        textLoading.setText("Preparing for you...");
        makeNavigationBottom();
        if(isLogged){
            userNavigate();
        }

    }

    private void userNavigate() {
        String url = "https://scontent-sit4-1.xx.fbcdn.net/v/t1.0-1/p320x320/13428361_1323574151004020_555344963131837310_n.jpg?oh=a416a796466d3dd0ea90f3a34bb0a993&oe=57F4FDFC";
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.like),
                        Color.parseColor("#ffffff")
                )
                        .selectedIcon(getResources().getDrawable(R.drawable.like_selected))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.path),
                        Color.parseColor("#ffffff")
                )
                        .selectedIcon(getResources().getDrawable(R.drawable.path_selected))
                        .build()
        );
        Picasso.with(DashboardActivity.this).load(url).into(target);
    }

    private void makeNavigationBottom() {
     try{
         navigationTabBar = (NavigationTabBar) findViewById(R.id.navigation);
         models.add(
                 new NavigationTabBar.Model.Builder(
                         getResources().getDrawable(R.drawable.search),
                         Color.parseColor("#ffffff")
                 )
                         .selectedIcon(getResources().getDrawable(R.drawable.search_selected))
                         .build()
         );
         navigationTabBar.setModels(models);
         navigationTabBar.setIsBadged(false);
         navigationTabBar.setIsTitled(false);
         navigationTabBar.setIsTinted(false);
         navigationTabBar.setIsBadgeUseTypeface(false);
         navigationTabBar.setViewPager(aViewPage);
         navigationTabBar.postDelayed(new Runnable() {
             @Override
             public void run() {
                 textLoading.setText("Done!");
                 pushOutLoading();
                 navigationTabBar.show();
             }
         },2000);

     }catch (NullPointerException e) {
         System.out.println(e.getMessage());
     }
    }

    private void pushOutLoading() {
        loadingLayout = (LinearLayout) findViewById(R.id.loading_layout);
        YoYo.with(Techniques.FadeOut)
                .delay(1000)
                .duration(1000)
                .withListener(new Animator.AnimatorListener() {

                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        loadingLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .playOn(loadingLayout);

    }

    @Override
    public void onBackPressed() {

    }

}
