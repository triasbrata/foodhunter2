package com.triasbrata.foodhunter.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.triasbrata.foodhunter.R;
import com.triasbrata.foodhunter.adapters.interfaces.RecycleViewItemOnClick;
import com.triasbrata.foodhunter.models.Food;

import java.util.ArrayList;

/**
 * Created by triasbrata on 08/07/16.
 */
public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> implements View.OnClickListener {




    private final String TAG = "FoodListAdapter";
    private ArrayList<Food> mDataset;
    private Context mContext;

    public FoodListAdapter(ArrayList<Food> fm, Context context) {
        Log.d(TAG, "FoodListAdapter: "+fm.size());
        mDataset = fm;
        mContext = context;

    }
    public void swap(ArrayList<Food> fm){
        mDataset = fm;
        this.notifyDataSetChanged();
        Log.d(TAG, "swap: Fired");
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list_item,parent,false);
        ViewHolder vh = new ViewHolder((CardView) v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {

            if(mDataset.isEmpty()) return;

            Food dataFM = mDataset.get(position);
            holder.mTxtFoodName.setText(dataFM.getName());
            holder.mTxtStoreName.setText(dataFM.getStore().getName());
            holder.mTxtStoreAddress.setText(dataFM.getStore().getAddress());
            holder.mTxtPriceTag.setText("Rp. "+String.valueOf(dataFM.getPrice()));
            Drawable likeIcon = dataFM.isUserLike() ?
                    mContext.getResources().getDrawable(R.drawable.like_small_icon):
                    mContext.getResources().getDrawable(R.drawable.like_small_icon_selected);
            holder.mIconBtnLike.setImageDrawable(likeIcon);
            Picasso.with(mContext).load(dataFM.getImage().getPreview()).centerCrop().resize(100,100).error(R.drawable.food).placeholder(R.drawable.food).into(holder.mImgFoodImage);
            cahngeFont(holder);
            holder.mBtnLikeListener = dataFM.getListenerBtnLike();
            holder.mBtnBrowseListener = dataFM.getListenerBtnBrowse();
            holder.mCardViewListener = dataFM.getListenerCardView();
            holder.mIdFood = String.valueOf(dataFM.getId());
            holder.mIdStore = String.valueOf(dataFM.getStore().getId());
        } catch (Exception ignored){
            Log.d(TAG, "onBindViewHolder: "+ignored.getMessage(),ignored.getCause());
            ignored.printStackTrace();
        }
    }


    private void cahngeFont(ViewHolder holder) {
        Typeface tfM =  Typeface.createFromAsset(mContext.getAssets(),"font/avenir-next-lt-pro/AvenirNextLTPro-MediumCn.otf");
        holder.mTxtStoreAddress.setTypeface(tfM);
        holder.mTxtStoreName.setTypeface(tfM);
        holder.mTxtFoodName.setTypeface(tfM);
        holder.mTxtPriceTag.setTypeface(tfM);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+mDataset.size());
        return mDataset.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        protected CardView mCardView;
        protected ImageView mImgFoodImage,mIconBtnLike;
        protected TextView mTxtFoodName,
                        mTxtStoreName,
                        mTxtStoreAddress,
                        mTxtPriceTag;
        protected String mIdFood,mIdStore;
        protected LinearLayout mBtnBrowse, mBtnLike;
        protected RecycleViewItemOnClick mBtnBrowseListener, mCardViewListener,mBtnLikeListener;

        public ViewHolder(CardView v) {
            super(v);
            mCardView  = v;
            mIconBtnLike = (ImageView) v.findViewById(R.id.iconBtnLike);
            mImgFoodImage = (ImageView) v.findViewById(R.id.image_food);
            mTxtFoodName = (TextView) v.findViewById(R.id.title_food);
            mTxtStoreAddress = (TextView) v.findViewById(R.id.address_store);
            mTxtStoreName = (TextView) v.findViewById(R.id.store_food);
            mTxtPriceTag = (TextView) v.findViewById(R.id.harga_food);
            mBtnBrowse = (LinearLayout) v.findViewById(R.id.btnBrowse);
            mBtnLike= (LinearLayout) v.findViewById(R.id.btnLike);
            mBtnBrowse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBtnBrowseListener.onClickListener(v,mIdStore);
                }
            });
            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCardViewListener.onClickListener(v,mIdFood);
                }
            });
            mBtnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBtnLikeListener.onClickListener(v,mIdFood);
                }
            });


        }
    }
}
