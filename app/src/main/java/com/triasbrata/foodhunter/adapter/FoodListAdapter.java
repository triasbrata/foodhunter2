package com.triasbrata.foodhunter.adapter;

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
import com.triasbrata.foodhunter.etc.idItemAndListener;
import com.triasbrata.foodhunter.model.FoodModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by triasbrata on 08/07/16.
 */
public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> implements View.OnClickListener {
    private final String TAG = "FoodListAdapter";
    private ArrayList<FoodModel> mDataset;
    private Context mContext;
    private HashMap<String,idItemAndListener> mListenerList = new HashMap<>();

    public FoodListAdapter(ArrayList<FoodModel> fm, Context context) {
        mDataset = fm;
        mContext = context;

    }
    public void swap(ArrayList<FoodModel> fm){
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
        if(mDataset.isEmpty())
            return;
        FoodModel dataFM = mDataset.get(position);
        holder.mTxtFoodName.setText(dataFM.getFoodName());
        holder.mTxtStoreName.setText(dataFM.getStoreName());
        holder.mTxtStoreAddress.setText(dataFM.getStoreAdress());
        holder.mTxtPriceTag.setText("Rp. "+String.valueOf(dataFM.getFoodPrice()));
        Drawable likeIcon = dataFM.isUserLike() ?
                mContext.getResources().getDrawable(R.drawable.like_small_icon):
                mContext.getResources().getDrawable(R.drawable.like_small_icon_selected);
        holder.mIconBtnLike.setImageDrawable(likeIcon);
        Picasso.with(mContext).load(dataFM.getFoodImage()).centerCrop().resize(100,100).error(R.drawable.food).placeholder(R.drawable.food).into(holder.mImgFoodImage);
        registerListene(position,holder,dataFM);
        cahngeFont(holder);
    }

    private void registerListene(int pos,ViewHolder holder,FoodModel fm) {
        holder.mCardView.setOnClickListener(this);
        mListenerList.put("Listener:"+holder.mCardView.getId(),new idItemAndListener(fm.getFoodId(),fm.getListenerCardView()));
        holder.mBtnLike.setOnClickListener(this);
        mListenerList.put("Listener:"+holder.mBtnLike.getId(),new idItemAndListener(fm.getFoodId(),fm.getListenerBtnLike()));
        holder.mBtnBrowse.setOnClickListener(this);
        mListenerList.put("Listener:"+holder.mBtnBrowse.getId(),new idItemAndListener(fm.getFoodId(),fm.getListenerBtnBrowse()));

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

    @Override
    public void onClick(View v) {
        idItemAndListener c =  mListenerList.get("Listener:"+v.getId());
        if( c == null){
            Log.d(TAG, "onClick: listener gone");
            return;
        }
        Log.d(TAG, "onClick: "+c.mId);
        c.mListener.onClickListener(v,c.mId);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected CardView mCardView;
        protected ImageView mImgFoodImage,mIconBtnLike;
        protected TextView mTxtFoodName,
                        mTxtStoreName,
                        mTxtStoreAddress,
                        mTxtPriceTag;
        protected LinearLayout mBtnBrowse, mBtnLike;
        public ViewHolder(CardView v) {
            super(v);
            mCardView  = (CardView) v;
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
