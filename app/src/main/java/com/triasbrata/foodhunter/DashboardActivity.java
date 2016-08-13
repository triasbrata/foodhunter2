package com.triasbrata.foodhunter;

import android.graphics.Bitmap;
import android.graphics.Color;

import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;



import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gigamole.navigationtabbar.ntb.NavigationTabBar;
import com.nineoldandroids.animation.Animator;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.triasbrata.foodhunter.adapters.PageFragmentAdapter;
import com.triasbrata.foodhunter.etc.BitmapOperation;
import com.triasbrata.foodhunter.fragment.FoodSectionFragment;
import com.triasbrata.foodhunter.fragment.StoreSectionFragment;
import com.triasbrata.foodhunter.fragment.PopularFragment;
import com.triasbrata.foodhunter.fragment.UserSectionFragment;
import com.triasbrata.foodhunter.models.Store;


import java.util.ArrayList;

public class DashboardActivity extends FragmentActivity{

    private static final String TAG = "DashboardActivity";
    private final int mSelectedBackground = Color.parseColor("#2C3E50");
    ViewPager aViewPage;
    private TextView textLoading;
    private LinearLayout loadingLayout;
    private ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
    public NavigationTabBar navigationTabBar;
    private boolean isLogged = true;
    private boolean IsDetailStoreView = false;
    private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
    private Target target = new Target() {

        private void build(Drawable drawable, boolean isImage){
            NavigationTabBar.Model.Builder builder =  new NavigationTabBar.Model.Builder(drawable,mSelectedBackground);
            if(!isImage){
                builder.selectedIcon(getResources().getDrawable(R.drawable.nav_user_section_normal));
            }
            models.add(builder.build());
            showNTB();
            laodView();
        }
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Drawable userImage = new BitmapDrawable(getResources(),BitmapOperation.getRoundedCornerBitmap(bitmap,Color.WHITE,100,5,DashboardActivity.this));
            build(userImage,true);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            build(errorDrawable,false);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {}
    };
    private PageFragmentAdapter pAdapter;
    private Store mStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        textLoading = (TextView) findViewById(R.id.text_loading);
        textLoading.setTypeface(Typeface.createFromAsset(getAssets(),"font/avenir-next-lt-pro/AvenirNextLTPro-Demi.otf"));
        loadingLayout = (LinearLayout) findViewById(R.id.loading_layout);


        aViewPage = (ViewPager) findViewById(R.id.view_pager);
        mFragments.add(0, FoodSectionFragment.newInstance());
        mFragments.add(1, StoreSectionFragment.newInstance());
        mFragments.add(2, PopularFragment.newInstance());
        mFragments.add(3, UserSectionFragment.newInstance());
        pAdapter = new PageFragmentAdapter(mFragments,getSupportFragmentManager()){

            @Override
            public int getItemPosition(Object object) {
                if(object instanceof StoreSectionFragment && isDetailStoreView()){
                    StoreSectionFragment f = ((StoreSectionFragment) object);
                    f.changeViewToDetailStore(mStore);
                    setDetailStoreView(false);
                    return super.getItemPosition(object);
                }
                return POSITION_UNCHANGED;
            }
        };
        aViewPage.setAdapter(pAdapter);
        textLoading.setText("Preparing for you...");
        makeNavigationBottom();

    }
    private void userNavigate() {
        String url = "https://scontent-sit4-1.xx.fbcdn.net/v/t1.0-1/p320x320/13428361_1323574151004020_555344963131837310_n.jpg?oh=a416a796466d3dd0ea90f3a34bb0a993&oe=57F4FDFC";
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.nav_love_section_normal),mSelectedBackground)
                        .selectedIcon(getResources().getDrawable(R.drawable.nav_love_section))
                        .build()
        );
        Picasso.with(DashboardActivity.this)
                .load(url)
                .error(R.drawable.nav_user_section)
                .into(target);
    }

    private void makeNavigationBottom() {
     try{
         navigationTabBar = (NavigationTabBar) findViewById(R.id.navigation);
         navigationTabBar.setIsBadged(false);
         navigationTabBar.setIsTitled(false);
         navigationTabBar.setIsTinted(false);
         navigationTabBar.setIsBadgeUseTypeface(false);
         navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

             @Override
             public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

             }

             @Override
             public void onPageSelected(int position) {
                 pAdapter.notifyDataSetChanged();
             }

             @Override
             public void onPageScrollStateChanged(int state) {

             }
         });
         models.add(
                 new NavigationTabBar.Model.Builder(
                         getResources().getDrawable(R.drawable.nav_food_section_normal),mSelectedBackground)
                         .selectedIcon(getResources().getDrawable(R.drawable.nav_food_section))
                         .build()
         );
         models.add(
                 new NavigationTabBar.Model.Builder(
                         getResources().getDrawable(R.drawable.nav_shop_section_normal), mSelectedBackground)
                         .selectedIcon(getResources().getDrawable(R.drawable.nav_shop_section))
                         .build()
         );
         showNTB();
         if(isLogged) {
             userNavigate();
         }else{
             laodView();
         }




     }catch (NullPointerException e) {
         System.out.println(e.getMessage());
     }
    }

    private void showNTB() {
        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(aViewPage,0);
        navigationTabBar.show();

    }

    private void laodView() {
        aViewPage.setCurrentItem(0);
        pushOutLoading();

    }

    public void pushOutLoading() {
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
        super.onBackPressed();
    }

    public void loadStore(Store store) {
        setDetailStoreView(true);
        mStore = store;
        aViewPage.setCurrentItem(1);

    }

    public boolean isDetailStoreView() {
        return IsDetailStoreView;
    }

    public void setDetailStoreView(boolean detailStoreView) {
        IsDetailStoreView = detailStoreView;
    }
}
