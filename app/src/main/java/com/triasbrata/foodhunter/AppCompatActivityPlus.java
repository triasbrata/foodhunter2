/**
 * Created by triasbrata on 05/07/16.
 */
package com.triasbrata.foodhunter;

        import android.content.SharedPreferences;
        import android.graphics.Typeface;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AppCompatActivity;


/**
 * Created by triasbrata on 16/06/16.
 */
abstract class AppCompatActivityPlus extends AppCompatActivity{
    protected SharedPreferences setting;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private Typeface tf;

    protected Typeface getTypeFace(String name){
        this.tf = Typeface.createFromAsset(getAssets(),"font/avenir-next-lt-pro/AvenirNextLTPro-"+name+".otf");
        return tf;
    }
    protected Typeface getTypeFace(){
        return  this.tf;
    }
    protected Typeface getTypeFaceDemi(){
        return  this.getTypeFace("DemiCn");
    }
    protected Typeface getTypeFaceRegular(){
        return  this.getTypeFace("Regular");
    }

}

