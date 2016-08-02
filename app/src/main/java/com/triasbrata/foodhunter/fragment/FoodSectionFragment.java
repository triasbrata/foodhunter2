package com.triasbrata.foodhunter.fragment;

import android.content.Context;
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
import com.triasbrata.foodhunter.DashboardActivity;
import com.triasbrata.foodhunter.R;
import com.triasbrata.foodhunter.adapter.FoodListAdapter;
import com.triasbrata.foodhunter.adapter.interfaces.FoodListOnClickListener;
import com.triasbrata.foodhunter.etc.Config;
import com.triasbrata.foodhunter.fragment.dialogs.DialogFoodDetailFragment;
import com.triasbrata.foodhunter.fragment.interfaces.RecyclerAdapterRefresh;
import com.triasbrata.foodhunter.model.FoodModel;

import java.util.ArrayList;

/**
 * Created by triasbrata on 08/07/16.
 */
public class FoodSectionFragment extends Fragment implements RecyclerAdapterRefresh {
    private final String TAG = "FoodSectionFragment";
    private final ArrayList<FoodModel> mFoodModel = new ArrayList<FoodModel>();
    private FoodListAdapter mAdapter = null;
    protected final FoodListOnClickListener viewListener = new CardViewListener() , btnBrowseListener = new BtnBrowserListener(), btnLikeListener = new BtnLikeListener();
    private Context mContext;

    private final FutureCallback<JsonArray> mFuture =new FutureCallback<JsonArray>() {
        @Override
        public void onCompleted(Exception e, JsonArray result) {
            mFoodModel.clear();
            if(e != null){
                Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_LONG).show();
                return;
            }
            try {
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
                        model.addListenerBtnBrowse(btnBrowseListener);
                        model.addListenerBtnLike(btnLikeListener);
                        model.addListenerCardView(viewListener);
                        mFoodModel.add(model);
                    }
                }
            }catch (Exception err){
                Log.d(TAG, "onCompleted: "+err.getMessage(), err.getCause());
            }
        }
    };
    

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if(mContext == null) mContext = getContext();
        super.onCreate(savedInstanceState);
    }

    public FoodSectionFragment(){}
    public static FoodSectionFragment newInstance() {
        return new FoodSectionFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refresh(view);
    }



    public void refresh(View view) {

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv_search);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new FoodListAdapter(getFetchFoodModel(), mContext);
        dataRefresher();
        rv.setAdapter(mAdapter);

    }
    private ArrayList<FoodModel> getFetchFoodModel() {
        String url = Config.base_url + "food";
        Ion.with(mContext)
                .load(url)
                .asJsonArray()
                .setCallback(mFuture);
        return mFoodModel;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search,container,false);
        return v;
    }

    @Override
    public void dataRefresher() {
        mAdapter.swap(getFetchFoodModel());

    }

    private class BtnLikeListener implements FoodListOnClickListener {

        protected View mView;
        protected final FutureCallback<JsonObject> callbackViewListener = new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e != null || result.equals(new JsonObject())) {
                    Toast.makeText(mContext, "Makanan tidak ditemukan", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d(TAG, "onCompleted: result: "+result.toString());
                DashboardActivity mActivity = (DashboardActivity) mContext;
//                            mActivity.getFragmentManager().beginTransaction().replace()
            }
        };
        @Override
        public void onClickListener(final View v, final String idItem) {
            mView = v;
            String url = Config.base_url+"like/"+idItem;
            Log.d(TAG, "onClickListener: "+url);
            Ion.with(mContext)
                    .load(url)
                    .asJsonObject()
                    .setCallback(callbackViewListener);
            Toast.makeText(mContext, "Button Like clicked", Toast.LENGTH_SHORT).show();
        }
    }

    private class BtnBrowserListener implements FoodListOnClickListener {
        protected View mView;
        protected final FutureCallback<JsonObject> callbackViewListener = new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
//                if (e != null || result.equals(new JsonObject())) {
//                    Toast.makeText(mView.mContext, "Makanan tidak ditemukan", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                ((DashboardActivity) getActivity()).loadStore();
                Log.d(TAG, "onCompleted: result: "+result.toString());
            }
        };
        @Override
        public void onClickListener(final View v, final String idItem) {
            mView = v;
            String url = Config.URL.store_detail(idItem);
            Ion.with(mContext)
                    .load(url)
                    .asJsonObject()
                    .setCallback(callbackViewListener);
            Toast.makeText(mContext, "Button Browser clicked", Toast.LENGTH_SHORT).show();
        }
    }

    private class CardViewListener implements FoodListOnClickListener {
        protected View mView;
        protected final FutureCallback<JsonObject> callbackViewListener = new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e != null || result.equals(new JsonObject())) {
                    Toast.makeText(mContext, "Makanan tidak ditemukan", Toast.LENGTH_SHORT).show();
                    return;
                }
                DialogFoodDetailFragment f =  DialogFoodDetailFragment.newInstance(result.toString());
                f.show(((DashboardActivity) mContext).getSupportFragmentManager(),"");
            }
        };
        @Override
        public void onClickListener(final View v, final String idItem) {
            mView = v;
            String url = Config.base_url+"food/"+idItem;
            Log.d(TAG, "onClickListener: "+url);
            Ion.with(mContext)
                    .load(url)
                    .asJsonObject()
                    .setCallback(callbackViewListener);
        }
    }

}
