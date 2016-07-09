package com.triasbrata.foodhunter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.gigamole.navigationtabbar.ntb.NavigationTabBar;
import com.google.android.gms.common.api.GoogleApiClient;
import com.triasbrata.foodhunter.adapter.PageFragmentAdapter;

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

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int color, int cornerDips, int borderDips, Context context) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int borderSizePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) borderDips,
                context.getResources().getDisplayMetrics());
        final int cornerSizePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) cornerDips,
                context.getResources().getDisplayMetrics());
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        // prepare canvas for transfer
        paint.setAntiAlias(true);
        paint.setColor(0xFFFFFFFF);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);

        // draw bitmap
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        // draw border
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) borderSizePx);
        canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);

        return output;
    }

    private void makeNavigationBottom(BitmapDrawable bitmapDrawable) {
     try{
         aViewPage.setAdapter(new PageFragmentAdapter(getSupportFragmentManager()));
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


    @Override
    public void onResponse(Bitmap bitmap) {
//        if(bitmap != null){
//            bitmap = getRoundedCornerBitmap(bitmap, Color.parseColor("#ECF0F1"), 100, 10, getApplicationContext());
//            makeNavigationBottom(new BitmapDrawable(getResources(),bitmap));
//        }
//        makeNavigationBottom(null);
    }
}
