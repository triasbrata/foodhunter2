package com.triasbrata.foodhunter.adapter;

import android.content.Context;
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
import com.triasbrata.foodhunter.model.FoodModel;

import java.util.ArrayList;

/**
 * Created by triasbrata on 25/07/16.
 */
public class UserFavAdapter extends RecyclerView.Adapter<UserFavAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = UserFavAdapter.class.getSimpleName() ;
    ArrayList<ArrayList<FoodModel>> mModel = null;
    Context mContext = null;

    public UserFavAdapter(ArrayList<ArrayList<FoodModel>> model) {
        mModel = model;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_food_fav,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int p) {

        for (ArrayList<FoodModel> arrayModel: mModel) {
            for (int i = 0; i < arrayModel.size(); i++) {
                FoodModel model = arrayModel.get(i);
                h.onClickListener = model.getListenerHolder();
                h.txtFoodName.get(i).setText(model.getFoodName());
                Picasso.with(mContext).load(model.getFoodImage()).into(h.imgViewHolder.get(i));
            }
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+mModel.size());
        return mModel.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ArrayList<TextView> txtFoodName = new ArrayList<>();
        public ArrayList<ImageView> imgViewHolder = new ArrayList<>();
        public View.OnClickListener onClickListener;
        public ViewHolder(LinearLayout v) {

            super(v);
//            v.findViewById(R.id.holder_1).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onClickListener.onClick(v);
//                }
//            });
//            v.findViewById(R.id.holder_2).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onClickListener.onClick(v);
//                }
//            });
//
//            txtFoodName.add(0,(TextView) v.findViewById(R.id.food_name_1));
//            txtFoodName.add(1,(TextView) v.findViewById(R.id.food_name_2));
//            imgViewHolder.add(0,(ImageView) v.findViewById(R.id.image_food_holder_1));
//            imgViewHolder.add(1,(ImageView) v.findViewById(R.id.image_food_holder_2));
        }
    }
}
