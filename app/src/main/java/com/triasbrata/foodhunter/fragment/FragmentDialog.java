package com.triasbrata.foodhunter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.squareup.picasso.Picasso;
import com.triasbrata.foodhunter.R;

import java.util.HashMap;



/**
 * Created by triasbrata on 11/07/16.
 */
public class FragmentDialog extends Fragment implements ViewPagerEx.OnPageChangeListener {
    private SliderLayout mDemoSlider;
    private String TAG = "FragmentDialog";

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mDemoSlider = (SliderLayout)view.findViewById(R.id.slider);

        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");
        HashMap<String,Integer> gambar = new HashMap<String, Integer>();
        gambar.put("gambar 1",R.drawable.background1);
        gambar.put("gambar 2",R.drawable.gambar2);
        gambar.put("gambar 3",R.drawable.gambar3);
        gambar.put("gambar 4",R.drawable.gambar4);



        for(String name : gambar.keySet()){

            DefaultSliderView slideModel = new DefaultSliderView(getContext());
            // initialize a SliderLayout
            slideModel
//                    .image(url_maps.get(name))
                    .image(gambar.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop);
//                    .setPicasso(Picasso.with(getContext()));

            mDemoSlider.addSlider(slideModel);
        }
        PagerIndicator pdi = (PagerIndicator) view.findViewById(R.id.custom_indicator);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setCustomIndicator(pdi);
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_dialog,container,false);
    }

    public FragmentDialog() {
    }

    public static FragmentDialog newInstance() {
        return  new FragmentDialog();

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
