package com.triasbrata.foodhunter.fragment;

import android.content.Context;
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
import com.triasbrata.foodhunter.adapters.FoodListAdapter;
import com.triasbrata.foodhunter.adapters.interfaces.RecycleViewItemOnClick;
import com.triasbrata.foodhunter.etc.Config;
import com.triasbrata.foodhunter.fragment.dialogs.DialogFoodDetailFragment;
import com.triasbrata.foodhunter.fragment.interfaces.RecyclerAdapterRefresh;
import com.triasbrata.foodhunter.models.Food;
import com.triasbrata.foodhunter.models.Store;

import java.util.ArrayList;

/**
 * Created by triasbrata on 08/07/16.
 */
public class FoodSectionFragment extends Fragment implements RecyclerAdapterRefresh {
    private final String TAG = "FoodSectionFragment";
    private FoodListAdapter mAdapter = null;
    protected final RecycleViewItemOnClick viewListener = new CardView() , btnBrowseListener = new BtnBrowser(), btnLikeListener = new BtnLike();
    private Context mContext;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if(mContext == null) mContext = getContext();
        super.onCreate(savedInstanceState);
    }

    public FoodSectionFragment(){}
    public static FoodSectionFragment newInstance() {
        return new FoodSectionFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv_layout);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        getFetchFoodModel(getContext(),rv);
    }


    private void getFetchFoodModel(Context context, RecyclerView rv) {
        String url = Config.URL.food_all();
        FutureCallback<JsonArray> f = new CallbackFetchingModel(context,rv);
        Log.d(TAG, "getFetchFoodModel: "+url);
        Ion.with(mContext)
                .load(url)
                .asJsonArray()
                .setCallback(f);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_food_section,container,false);
    }

    @Override
    public void dataRefresher() {
        getFetchFoodModel(getContext(), (RecyclerView) getView().findViewById(R.id.rv_layout));

    }

    private class BtnLike implements RecycleViewItemOnClick {
        private final String TAG = BtnLike.class.getSimpleName();

        protected View mView;
        protected final FutureCallback<JsonObject> callbackViewListener = new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e != null || result.equals(new JsonObject())) {
                    Toast.makeText(mContext, "Makanan tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
        };

        @Override
        public void onClickListener(final View v, final String idItem) {
            mView = v;
            String url = Config.URL.like_food(idItem);
            Log.d(TAG, "onClickListener: "+url);
            Ion.with(mContext)
                    .load(url)
                    .asJsonObject()
                    .setCallback(callbackViewListener);

        }
    }

    private class BtnBrowser implements RecycleViewItemOnClick {
        private final String TAG = BtnBrowser.class.getSimpleName();
        protected View mView;
        protected final FutureCallback<JsonObject> callbackViewListener = new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e != null || result.equals(new JsonObject())) {
                    Toast.makeText(mContext, "Makanan tidak ditemukan", Toast.LENGTH_SHORT).show();
                    return;
                }
                if( !result.equals(new JsonObject()) ){
                    Store store = new Store(result);
                    ((DashboardActivity) getActivity()).loadStore(store);

                }
            }
        };
        @Override
        public void onClickListener(final View v, final String idItem) {
            mView = v;
            String url = Config.URL.store_detail(idItem);
            Log.d(TAG, "onClickListener: "+url);
            Ion.with(mContext)
                    .load(url)
                    .asJsonObject()
                    .setCallback(callbackViewListener);
        }
    }

    private class CardView implements RecycleViewItemOnClick {
        private final String TAG = getClass().getSimpleName();
        protected final FutureCallback<JsonObject> callbackViewListener = new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e != null || result.equals(new JsonObject())) {
                    Toast.makeText(mContext, "Makanan tidak ditemukan", Toast.LENGTH_SHORT).show();
                    return;
                }
                DialogFoodDetailFragment f =  DialogFoodDetailFragment.newInstance(result.toString());
                f.show(((DashboardActivity) mContext).getSupportFragmentManager(),"");
            }
        };
        @Override
        public void onClickListener(final View v, final String idItem) {
            String url = Config.URL.food_detail(idItem);
            Log.d(TAG, "onClickListener: "+url);
            Ion.with(mContext)
                    .load(url)
                    .setLogging(TAG,0)
                    .asJsonObject()
                    .setCallback(callbackViewListener);
        }
    }

    private class CallbackFetchingModel implements FutureCallback<JsonArray> {
        private final Context context;
        private final RecyclerView rv;

        public CallbackFetchingModel(Context context, RecyclerView rv) {
            this.context = context;
            this.rv = rv;
        }
        @Override
        public void onCompleted(Exception e, JsonArray result) {
            ArrayList<Food> foods = new ArrayList<>();
            if(e != null){
                Toast.makeText(mContext,R.string.notif_all_food_fail,Toast.LENGTH_LONG).show();
                e.printStackTrace();
                return;
            }
            try {
                if( result.size() > 0 ){
                    for (int i = 0; i < result.size(); i++){
                        Food model = new Food(result.get(i).getAsJsonObject());
                        model.addListenerBtnBrowse(btnBrowseListener);
                        model.addListenerBtnLike(btnLikeListener);
                        model.addListenerCardView(viewListener);
                        foods.add(model);
                    }
                }
            }catch (Exception err){
                Log.w(TAG, "onCompleted: Error  "+err.getMessage());
                err.printStackTrace();
            }
            mAdapter = new FoodListAdapter(foods, this.context);
            this.rv.setAdapter(mAdapter);
            this.rv.getAdapter().notifyDataSetChanged();
        }
    }
}
