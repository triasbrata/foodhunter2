package com.triasbrata.foodhunter.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
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

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.triasbrata.foodhunter.R;
import com.triasbrata.foodhunter.adapters.FoodListAdapter;
import com.triasbrata.foodhunter.adapters.StoreAdapter;
import com.triasbrata.foodhunter.etc.Config;
import com.triasbrata.foodhunter.etc.MapMaker;
import com.triasbrata.foodhunter.fragment.interfaces.RecyclerAdapterRefresh;
import com.triasbrata.foodhunter.models.Food;
import com.triasbrata.foodhunter.models.Store;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by triasbrata on 08/07/16.
 */
public class StoreSectionFragment extends Fragment
        implements RecyclerAdapterRefresh,MapMaker.DataFetcher{
    private final String TAG = "StoreSectionFragment";;
    private RecyclerView rv;
    private HashMap<Long, Store> mLinkerMarkerAndStore = new HashMap<>();


    public StoreSectionFragment(){}

    public static StoreSectionFragment newInstance() {
        return new StoreSectionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store,container,false);

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = (RecyclerView) view.findViewById(R.id.rv_layout);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        new MapMaker(getChildFragmentManager(),this.getClass().getSimpleName(),this, R.id.store_header_layout)
                .invoke();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private void getFetchStore(RecyclerView rv, MapboxMap mapboxMap) {
        String url = Config.URL.store_all();
        Ion.with(getContext())
                .load(url)
                .asJsonArray()
                .setCallback(new StoreFetchCallback(rv,mapboxMap));;
    }

    @Override
    public void dataRefresher() {

    }

    public void changeViewToDetailStore(Store store) {

        View v = getView().findViewById(R.id.store_detail_header) != null ?
                         getView().findViewById(R.id.store_detail_header):
                    getActivity().getLayoutInflater().inflate(R.layout.store_detail_header, (ViewGroup) getView().findViewById(R.id.store_header_layout));
        new HandlerStoreDetail(v,getView(),store);

    }

    @Override
    public void getDataFetcher(MapboxMap mapboxMap) {
        getFetchStore(rv,mapboxMap);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    class HandlerStoreDetail {
        TextView mStoreName,
                mStoreAddress,
                mStoreCity,
                mStoreOperation;
        KenBurnsView mBackground;
        CircleImageView mLogo;
        boolean islogoLoaded = false, isBackgroundLoaded = false;

        public HandlerStoreDetail(View v,View rootView,Store store) {
            mStoreName = (TextView) v.findViewById(R.id.store_name);
            mStoreAddress = (TextView) v.findViewById(R.id.store_address);
            mStoreCity = (TextView) v.findViewById(R.id.store_city);
            mStoreOperation = (TextView) v.findViewById(R.id.store_operation);
            mLogo = (CircleImageView) v.findViewById(R.id.store_logo);
            mBackground = (KenBurnsView) v.findViewById(R.id.store_background);
            changeFont();
            bindDataToView(rootView,store);

        }

        private void bindDataToView(View rootView, Store store) {
            try {
                Log.d(TAG, "bindDataToView: "+store.getRec().toString());
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat sdf = new SimpleDateFormat("H:m");
                mStoreName.setText(store.getName());
                mStoreAddress.setText(store.getAddress());
                mStoreCity.setText(store.getCity());
                mStoreOperation.setText(sdf.format(store.getOperation().getOpen())+ " - " + sdf.format(store.getOperation().getClose()));
                Picasso pica = Picasso.with(getContext());
                pica.load(store.getLogo())
                        .centerCrop()
                        .resize(100,100)
                        .noFade()
                        .into(new TargetCallBack(mLogo,store.getBackground()));

            }catch (NullPointerException e){
                e.printStackTrace();
            }
            updateRecycleView((RecyclerView) rootView.findViewById(R.id.rv_layout),store.getFoodList());

        }

        private void updateRecycleView(RecyclerView rv, ArrayList<Food> foodList) {
            rv.setAdapter(new FoodListAdapter(foodList,getContext()));
        }

        private void changeFont() {
            Typeface tfD = Typeface.createFromAsset(getContext().getAssets(),"font/avenir-next-lt-pro/AvenirNextLTPro-Demi.otf");
            Typeface tfR = Typeface.createFromAsset(getContext().getAssets(),"font/avenir-next-lt-pro/AvenirNextLTPro-Regular.otf");
            mStoreName.setTypeface(tfD);
            mStoreAddress.setTypeface(tfR);
            mStoreCity.setTypeface(tfR);
            mStoreOperation.setTypeface(tfR);
        }

        private class TargetCallBack implements Target {

            private final ImageView v;
            private final String b
                    ;

            public TargetCallBack(ImageView view,String bg) {
                v = view;
                b = bg;

            }

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                if(b != "") {
                    v.setImageBitmap(bitmap);
                    islogoLoaded = true;
                    Picasso.with(getContext()).load(b).into(new TargetCallBack(mBackground,""));
                }else{
                    v.setImageBitmap(bitmap);
                    isBackgroundLoaded = true;
                }
//                if(isBackgroundLoaded && islogoLoaded){
//                    ((DashboardActivity) getActivity()).pushOutLoading();
//                }

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        }
    }
    private class StoreFetchCallback implements FutureCallback<JsonArray> {
        private final RecyclerView rv;
        private final MapboxMap mMapboxMap;


        public StoreFetchCallback(RecyclerView rv, MapboxMap mapboxMap) {
            this.rv = rv;
            mMapboxMap = mapboxMap;
        }

        @Override
        public void onCompleted(Exception e, JsonArray result) {
            if(e != null){
                Toast.makeText(getContext(), R.string.notif_all_store_fail, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }
            if(result.size() > 0){
                ArrayList<Store> stores = new ArrayList<>();
                mLinkerMarkerAndStore.clear();
                for (JsonElement rec:result) {
                    try {
                        Store store = new Store((JsonObject) rec);
                        stores.add(store);
                        String alamatDanOperation = store.getOperation().toString()+"\n"+store.getAddress();
                        Marker marker = mMapboxMap.addMarker(new MarkerOptions()
                                .position(store.getLocation())
                                .snippet(alamatDanOperation)
                                .title(store.getName()));
                        mLinkerMarkerAndStore = new HashMap<>();
                        mLinkerMarkerAndStore.put(marker.getId(),store);
                    }catch (Exception error){
                        error.printStackTrace();
                    }
                }
//                if(rv.getAdapter() == null){
//                    rv.setAdapter(new StoreAdapter(stores,getContext()));
//                }else {
//                    rv.swapAdapter(new StoreAdapter(stores, getContext()),false);
//                }
                    rv.setAdapter(new StoreAdapter(stores,getContext()));

            }
        }
    }
}