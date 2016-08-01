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

    protected final FoodListOnClickListener
        viewListener = new FoodListOnClickListener() {
            protected View mView;
            protected final FutureCallback<JsonObject> callbackViewListener = new FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject result) {
                    if (e != null || result.equals(new JsonObject())) {
                        Toast.makeText(mView.getContext(), "Makanan tidak ditemukan", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    DialogFoodDetailFragment f =  DialogFoodDetailFragment.newInstance(result.toString());
                    f.show(((DashboardActivity) mView.getContext()).getSupportFragmentManager(),"");
                }
            };
            @Override
            public void onClickListener(final View v, final String idItem) {
                mView = v;
                String url = Config.base_url+"food/"+idItem;
                Log.d(TAG, "onClickListener: "+url);
                Ion.with(v.getContext())
                    .load(url)
                    .asJsonObject()
                    .setCallback(callbackViewListener);
            }
        },
        btnLikeListener = new FoodListOnClickListener() {
            protected View mView;
            protected final FutureCallback<JsonObject> callbackViewListener = new FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject result) {
                    if (e != null || result.equals(new JsonObject())) {
                        Toast.makeText(mView.getContext(), "Makanan tidak ditemukan", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.d(TAG, "onCompleted: result: "+result.toString());
                    DashboardActivity mActivity = (DashboardActivity) mView.getContext();
//                            mActivity.getFragmentManager().beginTransaction().replace()
                }
            };
            @Override
            public void onClickListener(final View v, final String idItem) {
                mView = v;
                String url = Config.base_url+"like/"+idItem;
                Log.d(TAG, "onClickListener: "+url);
                Ion.with(v.getContext())
                        .load(url)
                        .asJsonObject()
                        .setCallback(callbackViewListener);
                Toast.makeText(v.getContext(), "Button Like clicked", Toast.LENGTH_SHORT).show();
            }
        },
        btnBrowseListener = new FoodListOnClickListener() {
            protected View mView;
            protected final FutureCallback<JsonObject> callbackViewListener = new FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject result) {
                    if (e != null || result.equals(new JsonObject())) {
                        Toast.makeText(mView.getContext(), "Makanan tidak ditemukan", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.d(TAG, "onCompleted: result: "+result.toString());
//                    DashboardActivity mActivity = (DashboardActivity) mView.getContext();
//                    ((RelativeLayout) mActivity.findViewById(R.id.backdrop)).setVisibility(View.VISIBLE);
//                    mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_backdrop,DialogFoodDetailFragment.newInstance()).commit();
////                            mActivity.getFragmentManager().beginTransaction().replace()
                }
            };
            @Override
            public void onClickListener(final View v, final String idItem) {
                mView = v;
                String url = Config.base_url+"browse/"+idItem;
                Log.d(TAG, "onClickListener: "+url);
                Ion.with(v.getContext())
                        .load(url)
                        .asJsonObject()
                        .setCallback(callbackViewListener);
                Toast.makeText(v.getContext(), "Button Browser clicked", Toast.LENGTH_SHORT).show();
            }
        };

    private final FutureCallback<JsonArray> mFuture =new FutureCallback<JsonArray>() {
        @Override
        public void onCompleted(Exception e, JsonArray result) {
            mFoodModel.clear();
            if(e != null){
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                return;
            }
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
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new FoodListAdapter(getFetchFoodModel(), getContext());
        dataRefresher();
        rv.setAdapter(mAdapter);

    }
    private ArrayList<FoodModel> getFetchFoodModel() {
        String url = Config.base_url + "food";
        Ion.with(getContext())
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
}
