package com.triasbrata.foodhunter.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
import com.koushikdutta.ion.ImageViewBitmapInfo;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.triasbrata.foodhunter.DashboardActivity;
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
        getFetchStore(rv);
        rv.setAdapter(mAdapter);

    }
    private void getFetchStore(RecyclerView rv) {
        String url = Config.URL.store_all();
        Ion.with(mContext)
                .load(url)
                .asJsonArray()
                .setCallback(new StoreFetchCallback(rv));;
    }

    @Override
    public void dataRefresher() {

    }

    public void changeViewToDetailStore(Store store) {

        View v = (getView().findViewById(R.id.store_detail_header) != null)?
                         getView().findViewById(R.id.store_detail_header):
                    getActivity().getLayoutInflater().inflate(R.layout.store_detail_header, (ViewGroup) getView().findViewById(R.id.store_header_layout));
        new HandlerStoreDetail(v,getView(),store);

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
                Log.d(TAG, "onBitmapLoaded: Background"+ isBackgroundLoaded);
                Log.d(TAG, "onBitmapLoaded: Logo : "+ islogoLoaded);
                if(isBackgroundLoaded && islogoLoaded){
                    ((DashboardActivity) getActivity()).pushOutLoading();
                }

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

        public StoreFetchCallback(RecyclerView rv) {
            this.rv = rv;
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
                for (JsonElement rec:result) {
                    try {
                        stores.add(new Store((JsonObject) rec));
                    }catch (Exception error){
                        error.printStackTrace();
                    }
                }
//                if(rv.getAdapter() == null){
//                    rv.setAdapter(new StoreAdapter(stores,getContext()));
//                }else {
//                    rv.swapAdapter(new StoreAdapter(stores, getContext()),false);
//                }
                rv.swapAdapter(new StoreAdapter(stores, getContext()),false);
            }
        }
    }
}