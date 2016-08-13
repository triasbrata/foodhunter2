package com.triasbrata.foodhunter.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.triasbrata.foodhunter.DashboardActivity;
import com.triasbrata.foodhunter.R;
import com.triasbrata.foodhunter.adapters.interfaces.RecycleViewItemOnClick;
import com.triasbrata.foodhunter.etc.Config;
import com.triasbrata.foodhunter.models.Store;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by triasbrata on 03/08/16.
 */
public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {
    private ArrayList<Store> mStore;
    private Context mContext;

    public StoreAdapter(ArrayList<Store> fetchStore, Context context) {
        mStore = fetchStore;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =LayoutInflater.from(parent.getContext()).inflate(R.layout.store_list_item,parent,false);
        return  new ViewHolder((CardView) v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Store store = mStore.get(position);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("H:m");
        holder.mIdFood = String.valueOf(store.getId());
        holder.mTxtCity.setText(store.getCity());
        holder.mTxtName.setText(store.getName());
        holder.mTxtAddress.setText(store.getAddress());
        holder.mTxtOperation.setText(sdf.format(store.getOperation().getOpen())+ " - " + sdf.format(store.getOperation().getClose()));
        Picasso.with(mContext).load(store.getLogo()).centerCrop().resize(100,100).into(holder.mStoreImgHolder);
        holder.mCardViewListener = new RecycleViewItemOnClick() {
            @Override
            public void onClickListener(View v, String idItem) {
                Ion.with(mContext)
                        .load(Config.URL.store_detail(idItem))
                        .asJsonObject()
                        .setCallback(new ResponseStoreDetail(mContext));
            }
        };
    }

    @Override
    public int getItemCount() {
        return mStore.size();
    }

    public void swap(ArrayList<Store> fetchStore) {
        mStore = fetchStore;
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        protected ImageView mStoreImgHolder;
        protected TextView mTxtName;
        protected TextView mTxtAddress;
        protected TextView mTxtCity;
        protected TextView mTxtOperation;
        protected String mIdFood;
        protected RecycleViewItemOnClick mCardViewListener;
        public ViewHolder(CardView v) {
            super(v);
            mStoreImgHolder = (ImageView) v.findViewById(R.id.image);
            mTxtName = (TextView) v.findViewById(R.id.name);
            mTxtCity = (TextView) v.findViewById(R.id.city);
            mTxtAddress = (TextView) v.findViewById(R.id.address);
            mTxtOperation = (TextView) v.findViewById(R.id.operation);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCardViewListener.onClickListener(v,mIdFood);
                }
            });
        }
    }
    class ResponseStoreDetail implements FutureCallback<JsonObject>{

        private final String TAG = ResponseStoreDetail.class.getSimpleName();
        private DashboardActivity mActivity;

        public ResponseStoreDetail(Context context) {
            this.mActivity = (DashboardActivity) context;
        }

        @Override
        public void onCompleted(Exception e, JsonObject result) {
            try {

                if( !result.equals(new JsonObject()) ){
                    Store store  = new Store(result);
                    mActivity.loadStore(store);
                }

            } catch (NullPointerException err){
                Log.d(TAG, "onCompleted: "+err.getMessage(),err.getCause());
                Toast.makeText(this.mActivity,R.string.notif_detail_store_fail,Toast.LENGTH_SHORT).show();
            }

        }
    }

}
