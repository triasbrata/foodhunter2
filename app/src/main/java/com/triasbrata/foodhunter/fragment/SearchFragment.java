package com.triasbrata.foodhunter.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;

import com.triasbrata.foodhunter.R;
import com.triasbrata.foodhunter.adapter.FoodListAdapter;
import com.triasbrata.foodhunter.etc.Config;
import com.triasbrata.foodhunter.model.FoodModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by triasbrata on 08/07/16.
 */
public class SearchFragment extends Fragment {
    private View v;
    private RecyclerView rv;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Map map = null;
    private MapFragment mapFragment = null;
    private RequestQueue requestQueue;


    public SearchFragment(){

    }
    public static SearchFragment newInstance() {
        System.out.println("Search Fragment Load");
        return new SearchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_search,container,false);
        rv = (RecyclerView) v.findViewById(R.id.rv_search);
        mLayoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(mLayoutManager);
        ArrayList<FoodModel> fm = getFetchFoodModel();
        mAdapter = new FoodListAdapter(fm);
        rv.setAdapter(mAdapter);
        mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapfragment);
        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(
                    OnEngineInitListener.Error error)
            {
                if (error == OnEngineInitListener.Error.NONE) {
                    // retrieve a reference of the map from the map fragment
                    map = mapFragment.getMap();
                    // Set the map center to the Vancouver region (no animation)
                    map.setCenter(new GeoCoordinate(49.196261, -123.004773, 0.0),
                            Map.Animation.NONE);
                    // Set the zoom level to the average between min and max
                    map.setZoomLevel(
                            (map.getMaxZoomLevel() + map.getMinZoomLevel()) / 2);
                } else {
                    System.out.println("ERROR: Cannot initialize Map Fragment");
                }
            }
        });
        return  v;
    }

    private ArrayList<FoodModel> getFetchFoodModel() {
        final ArrayList<FoodModel> aFM = new ArrayList<FoodModel>();
        String url = Config.base_url + "food/list";
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject data = response.getJSONObject(i);
                        FoodModel model = new FoodModel();
                        model.setFoodName(data.getString("food_name"));
                        model.setFoodPrice(data.getInt("food_price"));
                        JSONObject dataStore = data.getJSONObject("store");
                        model.setStoreAdress(dataStore.getString("name"));
                        model.setStoreName(dataStore.getString("address"));
                        model.setStoreId(dataStore.getString("id"));
                        String foodImageUrl = Config.base_url + data.getString("food_image");
                        final FoodModel finalModel = model;
                        ImageRequest imageRequest = new ImageRequest(foodImageUrl, new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                finalModel.setFoodImage(response);
                            }
                        }, 0, 0, null, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        requestQueue.add(imageRequest);
                        requestQueue.start();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    aFM.add(finalModel.getModel());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
        requestQueue.start();
        return  aFM;
    }
}
