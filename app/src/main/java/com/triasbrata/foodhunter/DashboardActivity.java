package com.triasbrata.foodhunter;

import android.graphics.Bitmap;
import android.graphics.Color;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.gigamole.navigationtabbar.ntb.NavigationTabBar;
import com.triasbrata.foodhunter.adapter.PageFragmentAdapter;
import com.triasbrata.foodhunter.etc.BitmapOperation;

import java.util.ArrayList;

public class DashboardActivity extends FragmentActivity implements Response.Listener<Bitmap> {

    ViewPager aViewPage;
    Drawable userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);
        String url = "https://scontent-sit4-1.xx.fbcdn.net/v/t1.0-1/p320x320/13428361_1323574151004020_555344963131837310_n.jpg?oh=a416a796466d3dd0ea90f3a34bb0a993&oe=57F4FDFC";
        aViewPage = (ViewPager) findViewById(R.id.view_pager);
        aViewPage.setAdapter(new PageFragmentAdapter(getSupportFragmentManager()));
        RequestQueue queue = Volley.newRequestQueue(this);
        ImageRequest request = new ImageRequest(url,this, 0, 0, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        queue.add(request);
        queue.start();

    }
    @Override
    public void onResponse(Bitmap bitmap) {
        if(bitmap != null){
            try{
                bitmap = BitmapOperation.getRoundedCornerBitmap(bitmap, Color.parseColor("#ECF0F1"), 100, 10, getApplicationContext());
                makeNavigationBottom(new BitmapDrawable(getResources(),bitmap));
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }

    private void makeNavigationBottom(BitmapDrawable bitmapDrawable) {
     try{

         final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.navigation);
         final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
         models.add(
                 new NavigationTabBar.Model.Builder(
                         getResources().getDrawable(R.drawable.search),
                         Color.parseColor("#ffffff")
                 )
                         .selectedIcon(getResources().getDrawable(R.drawable.search_selected))
                         .build()
         );
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
         models.add(
                 new NavigationTabBar.Model.Builder(bitmapDrawable,Color.parseColor("#ffffff")).build()
         );

         navigationTabBar.setModels(models);
         navigationTabBar.setIsBadged(false);
         navigationTabBar.setIsTitled(false);
         navigationTabBar.setIsTinted(false);
         navigationTabBar.setIsBadgeUseTypeface(false);
         navigationTabBar.setViewPager(aViewPage, 0);
     }catch (NullPointerException e) {
         System.out.println(e.getMessage());
     }
    }
    @Override
    public void onBackPressed() {

    }

}
