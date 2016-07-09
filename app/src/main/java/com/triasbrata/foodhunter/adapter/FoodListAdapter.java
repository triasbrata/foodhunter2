package com.triasbrata.foodhunter.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.triasbrata.foodhunter.R;
import com.triasbrata.foodhunter.etc.imagefromurl.ImageLoader;
import com.triasbrata.foodhunter.model.FoodModel;

import java.util.ArrayList;

/**
 * Created by triasbrata on 08/07/16.
 */
public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {
    private ArrayList<FoodModel> mDataset;
    private Context mContext;

    public FoodListAdapter(ArrayList<FoodModel> fm, Context context) {
        mDataset = fm;
        mContext = context;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list_item,parent,false);
        System.out.println(parent.getContext().getClass());
        ViewHolder vh = new ViewHolder((CardView) v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mDataset.isEmpty())
            return;
        FoodModel dataFM = mDataset.get(position);
        holder.mTxtFoodName.setText(dataFM.getFoodName());
        holder.mTxtStoreName.setText(dataFM.getStoreName());
        holder.mTxtStoreAddress.setText(dataFM.getStoreAdress());
        holder.mTxtPriceTag.setText("Rp. "+String.valueOf(dataFM.getFoodPrice()));
        Drawable likeIcon = dataFM.isUserLike() ?
                mContext.getResources().getDrawable(R.drawable.like_small_selected):
                mContext.getResources().getDrawable(R.drawable.like_small);
        holder.mIconBtnLike.setImageDrawable(likeIcon);
        Picasso.with(mContext).load(dataFM.getFoodImage()).centerCrop().resize(100,100).error(R.drawable.food).placeholder(R.drawable.food).into(holder.mImgFoodImage);
        cahngeFont(holder);
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
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView mImgFoodImage,mIconBtnLike;
        protected TextView mTxtFoodName,
                        mTxtStoreName,
                        mTxtStoreAddress,
                        mTxtPriceTag;
        protected LinearLayout mBtnBrowse, mBtnLike;
        public ViewHolder(CardView v) {
            super(v);
            mIconBtnLike = (ImageView) v.findViewById(R.id.iconBtnLike);
            mImgFoodImage = (ImageView) v.findViewById(R.id.image_food);
            mTxtFoodName = (TextView) v.findViewById(R.id.title_food);
            mTxtStoreAddress = (TextView) v.findViewById(R.id.address_store);
            mTxtStoreName = (TextView) v.findViewById(R.id.store_food);
            mTxtPriceTag = (TextView) v.findViewById(R.id.harga_food);
            mBtnBrowse = (LinearLayout) v.findViewById(R.id.btnBrowse);
            mBtnLike= (LinearLayout) v.findViewById(R.id.btnLike);
        }
    }
}
