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
import com.triasbrata.foodhunter.adapter.FoodListAdapter;
import com.triasbrata.foodhunter.etc.Config;
import com.triasbrata.foodhunter.fragment.interfaces.RecyclerAdapterRefresh;
import com.triasbrata.foodhunter.model.FoodModel;

import java.util.ArrayList;

/**
 * Created by triasbrata on 08/07/16.
 */
public class PopularFragment extends Fragment implements RecyclerAdapterRefresh {

    private final String TAG = "PopularFragment";
    private final ArrayList<FoodModel> mFoodModel = new ArrayList<FoodModel>();
    private FoodListAdapter mAdapter = null;
    private final FutureCallback<JsonArray> mFuture =new FutureCallback<JsonArray>() {
        @Override
        public void onCompleted(Exception e, JsonArray result) {

            if(e != null){
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                return;
            }
            try {
                mFoodModel.clear();
                if( result.size() > 0 ){
                    for (int i = 0; i < result.size(); i++){
                        JsonObject data = result.get(i).getAsJsonObject();
                        FoodModel model = new FoodModel();
                        model.setFoodId(data.get("id").getAsString());
                        model.setFoodName(data.get("food_name").getAsString());
                        model.setFoodPrice(data.get("food_price").getAsInt());
                        JsonObject dataStore = data.get("store").getAsJsonObject();
                        model.setStoreAdress(dataStore.get("name").getAsString());
                        model.setStoreName(dataStore.get("address").getAsString());
                        model.setStoreId(dataStore.get("id").getAsString());
                        model.setFoodImage(data.get("food_image").getAsString());
                        mFoodModel.add(model);
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
    private ArrayList<FoodModel> getFetchFoodModel() {
        String url = Config.base_url + "food";
        Ion.with(getActivity())
                .load(url)
                .asJsonArray()
                .setCallback(mFuture);
        return mFoodModel;
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
