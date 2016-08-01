package com.triasbrata.foodhunter.fragment.inner.user;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.triasbrata.foodhunter.R;
import com.triasbrata.foodhunter.adapter.UserFavAdapter;
import com.triasbrata.foodhunter.model.FoodModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.triasbrata.foodhunter.etc.Config.URL;

/**
 * Created by triasbrata on 24/07/16.
 */
public class LikeFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "LikeFragment";
    @BindView(R.id.recyvle_view) RecyclerView mRecyclerView;
    private Context mContext;

    final ArrayList<ArrayList<FoodModel>> mData = new ArrayList<>();
    final FutureCallback<JsonArray> fetchingDataFuture = new FutureCallback<JsonArray>() {
        ArrayList<FoodModel> temp = new ArrayList<>();
        @Override
        public void onCompleted(Exception e, JsonArray result) {
            int x = 0;
            for (JsonElement rec : result) {
                JsonObject data = rec.getAsJsonObject();
                FoodModel model = new FoodModel();
                model.setFoodName(data.get("food_name").getAsString());
                model.setFoodId(data.get("id").getAsString());
                model.setFoodImage(data.get("food_image").getAsString());
                temp.add(x,model);
                if(x>0){
                    mData.add(temp);
                    temp.clear();
                }
                x = x < 1 ? x+1 : 0;
            }
        }
    };


    public LikeFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = container.getContext();
        return  inflater.inflate(R.layout.fragment_inner_user_like,container,false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this,view);
        makeRecyclerView();
    }

    private ArrayList<ArrayList<FoodModel>> fetchingData() {
        String url = URL.makeUrl(URL.user_like);
        Log.d(TAG, "fetchingData: "+url);
        Ion.with(mContext)
                .load(url)
                .asJsonArray()
                .setCallback(fetchingDataFuture);
        return mData;
    }

    private void makeRecyclerView() {
        mData.clear();
        UserFavAdapter adapter = new UserFavAdapter(fetchingData());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(adapter);
    }
}
