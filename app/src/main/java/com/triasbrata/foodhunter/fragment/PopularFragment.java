package com.triasbrata.foodhunter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.triasbrata.foodhunter.R;
import com.triasbrata.foodhunter.adapters.FoodListAdapter;
import com.triasbrata.foodhunter.etc.Config;
import com.triasbrata.foodhunter.fragment.interfaces.RecyclerAdapterRefresh;
import com.triasbrata.foodhunter.models.Food;

import java.util.ArrayList;

/**
 * Created by triasbrata on 08/07/16.
 */
public class PopularFragment extends Fragment implements RecyclerAdapterRefresh {

    private final String TAG = "PopularFragment";
    private final ArrayList<Food> mFoods = new ArrayList<Food>();
    private FoodListAdapter mAdapter = null;
    private final FutureCallback<JsonArray> mFuture =new FutureCallback<JsonArray>() {
        @Override
        public void onCompleted(Exception e, JsonArray result) {

            if(e != null){
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                return;
            }
            try {
                mFoods.clear();
                if( result.size() > 0 ){
                    for (int i = 0; i < result.size(); i++){
                        mFoods.add(new Food(result.get(i).getAsJsonObject()));
                    }
                }
            }catch (NullPointerException err){
                Log.d(TAG, "onCompleted: "+err.getMessage(),err.getCause());
            }
        }
    };


    public void refresh(View view) {
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv_search);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        if(mAdapter == null){
            mAdapter = new FoodListAdapter(getFetchFoodModel(), getContext());
        }
        dataRefresher();
        rv.setAdapter(mAdapter);

    }
    private ArrayList<Food> getFetchFoodModel() {
        String url = Config.base_url + "food";
        Ion.with(getActivity())
                .load(url)
                .asJsonArray()
                .setCallback(mFuture);
        return mFoods;
    }


    @Override
    public void dataRefresher() {
        mAdapter.swap(getFetchFoodModel());
    }

    public PopularFragment(){}

    public static PopularFragment newInstance() {
        return new PopularFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_popular,container,false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refresh(view);
    }
}
