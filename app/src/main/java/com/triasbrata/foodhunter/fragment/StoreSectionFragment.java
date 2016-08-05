package com.triasbrata.foodhunter.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;
import com.triasbrata.foodhunter.R;
import com.triasbrata.foodhunter.adapters.FoodListAdapter;
import com.triasbrata.foodhunter.adapters.StoreAdapter;
import com.triasbrata.foodhunter.etc.Config;
import com.triasbrata.foodhunter.fragment.interfaces.RecyclerAdapterRefresh;
import com.triasbrata.foodhunter.models.Food;
import com.triasbrata.foodhunter.models.Store;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by triasbrata on 08/07/16.
 */
public class StoreSectionFragment extends Fragment implements RecyclerAdapterRefresh{
    private final String TAG = "StoreSectionFragment";
    private final ArrayList<Store> mStores = new ArrayList<Store>();
    private StoreAdapter mAdapter = null;
    private Context mContext = null;


    public StoreSectionFragment(){}

    public static StoreSectionFragment newInstance() {
        return new StoreSectionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mContext == null ){
            mContext = getContext();
        }
        return inflater.inflate(R.layout.fragment_store,container,false);

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refresh(view);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if(mContext == null ){
            mContext = getContext();
        }
        super.onCreate(savedInstanceState);
    }

    public void refresh(View view) {
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv_layout);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        if(mAdapter == null){
            mAdapter = new StoreAdapter(getFetchStore(), mContext);
        }
        dataRefresher();
        rv.setAdapter(mAdapter);

    }
    private ArrayList<Store> getFetchStore() {
        String url = Config.URL.store_all();
        ArrayList<Store> out = new ArrayList<>();
        Ion.with(mContext)
                .load(url)
                .asJsonArray()
                .setCallback(mCallback);
        return mStores;
    }

    @Override
    public void dataRefresher() {

    }

    public void changeViewToDetailStore(Store store) {
        new HandlerStoreDetail(getActivity().getLayoutInflater().inflate(R.layout.store_detail_header, (ViewGroup) getView().findViewById(R.id.store_frame_layout)),getView(),store);

    }


    class HandlerStoreDetail {
        TextView mStoreName,
                mStoreAddress,
                mStoreCity,
                mStoreOperation;
        ImageView mBackground;
        CircleImageView mLogo;

        public HandlerStoreDetail(View v,View rootView,Store store) {
            mStoreName = (TextView) v.findViewById(R.id.store_name);
            mStoreAddress = (TextView) v.findViewById(R.id.store_address);
            mStoreCity = (TextView) v.findViewById(R.id.store_city);
            mStoreOperation = (TextView) v.findViewById(R.id.store_operation);
            mLogo = (CircleImageView) v.findViewById(R.id.store_logo);
            mBackground = (ImageView) v.findViewById(R.id.store_background);
            changeFont();
            bindDataToView(rootView,store);

        }

        private void bindDataToView(View rootView, Store store) {
            try {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat("H:m");
                mStoreName.setText(store.getName());
                mStoreAddress.setText(store.getAddress());
                mStoreCity.setText(store.getCity());
                mStoreOperation.setText(sdf.format(store.getOperation().getOpen())+ " - " + sdf.format(store.getOperation().getClose()));
                Picasso pica = Picasso.with(getContext());
                pica.load(store.getBackground()).into(mBackground);
                pica.load(store.getLogo())
                        .centerCrop()
                        .resize(100,100)
                        .noFade()
                        .into(mLogo);

            }catch (NullPointerException e){
                e.printStackTrace();
            }
            updateRecycleView((RecyclerView) rootView.findViewById(R.id.rv_layout),store.getFoodList());

        }

        private void updateRecycleView(RecyclerView rv, ArrayList<Food> foodList) {
            rv.swapAdapter(new FoodListAdapter(foodList,getContext()),false);
        }

        private void changeFont() {
            Typeface tfD = Typeface.createFromAsset(mContext.getAssets(),"font/avenir-next-lt-pro/AvenirNextLTPro-Demi.otf");
            Typeface tfR = Typeface.createFromAsset(mContext.getAssets(),"font/avenir-next-lt-pro/AvenirNextLTPro-Regular.otf");
            mStoreName.setTypeface(tfD);
            mStoreAddress.setTypeface(tfR);
            mStoreCity.setTypeface(tfR);
            mStoreOperation.setTypeface(tfR);
        }
    }

    private FutureCallback<JsonArray> mCallback = new FutureCallback<JsonArray>() {
        ArrayList<Store> mStores = new ArrayList<>();
        @Override
        public void onCompleted(Exception e, JsonArray result) {
            if(e != null){
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }
            mStores.clear();
            try {

                if( result.size() > 0  ){
                    for (JsonElement row: result) {
                        Store store = new Store((JsonObject) row);
                        mStores.add(store);

                    }
                }
            } catch (Exception err){
                Log.d(TAG, "onCompleted: "+err.getMessage(),err.getCause());
            }
        }
    };
}