package com.triasbrata.foodhunter.adapters;

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
import com.triasbrata.foodhunter.models.Food;

import java.util.ArrayList;

/**
 * Created by triasbrata on 25/07/16.
 */
public class UserFavAdapter extends RecyclerView.Adapter<UserFavAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = UserFavAdapter.class.getSimpleName() ;
    ArrayList<ArrayList<Food>> mModel = null;
    Context mContext = null;

    public UserFavAdapter(ArrayList<ArrayList<Food>> model) {
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

        for (ArrayList<Food> arrayModel: mModel) {
            for (int i = 0; i < arrayModel.size(); i++) {
                Food model = arrayModel.get(i);
                h.onClickListener = model.getListenerHolder();
                h.txtFoodName.get(i).setText(model.getName());
                Picasso.with(mContext).load(model.getImage().getPreview()).into(h.imgViewHolder.get(i));
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
        }
    }
}
