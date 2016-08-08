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
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.triasbrata.foodhunter.R;
import com.triasbrata.foodhunter.adapters.UserFavAdapter;
import com.triasbrata.foodhunter.models.Food;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.triasbrata.foodhunter.etc.Config.URL;

/**
 * Created by triasbrata on 24/07/16.
 */
public class LikeFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "LikeFragment";
    @BindView(R.id.recycle_view) RecyclerView mRecyclerView;
    private Context mContext;

    final ArrayList<ArrayList<Food>> mData = new ArrayList<>();
    final FutureCallback<JsonArray> fetchingDataFuture = new FutureCallback<JsonArray>() {
        @Override
        public void onCompleted(Exception e, JsonArray result) {
            if(e != null){
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                return;
            }
            ArrayList<Food> temp = new ArrayList<>();
            int x = 0;
            for (JsonElement rec : result) {
                Food model = new Food( rec.getAsJsonObject());
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

    private ArrayList<ArrayList<Food>> fetchingData() {
        String url = URL.userLike();
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
