package com.triasbrata.foodhunter.fragment;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;
import com.triasbrata.foodhunter.DashboardActivity;
import com.triasbrata.foodhunter.R;
import com.triasbrata.foodhunter.adapters.FoodListAdapter;
import com.triasbrata.foodhunter.adapters.interfaces.RecycleViewItemOnClick;
import com.triasbrata.foodhunter.etc.Config;
import com.triasbrata.foodhunter.fragment.dialogs.DialogDetailFoodFragment;
import com.triasbrata.foodhunter.fragment.inner.MapBoxFragment;
import com.triasbrata.foodhunter.fragment.interfaces.RecyclerAdapterRefresh;
import com.triasbrata.foodhunter.models.Food;
import com.triasbrata.foodhunter.models.Store;
import com.triasbrata.foodhunter.etc.MapMaker;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by triasbrata on 08/07/16.
 */
public class FoodSectionFragment extends Fragment implements
        RecyclerAdapterRefresh,
        MapMaker.DataFetcher {
    private final String TAG = "FoodSectionFragment";
    private FoodListAdapter mAdapter = null;
    protected final RecycleViewItemOnClick viewListener = new CardView(), btnBrowseListener = new BtnBrowser(), btnLikeListener = new BtnLike();
    private Context mContext;
    protected SupportMapFragment mapFragment;
    private  LocationManager gpsService;
    private String gpsProvider;
    private HashMap<Long, Food> mListLinkerMarkerAndFood = new HashMap<>();
    private LatLng userLocaltion;
    private RecyclerView rv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (mContext == null) mContext = getContext();

        super.onCreate(savedInstanceState);
    }

    public FoodSectionFragment() {
    }

    public static FoodSectionFragment newInstance() {
        return new FoodSectionFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gpsService = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        gpsProvider = gpsService.getBestProvider(new Criteria(), true);
        rv = (RecyclerView) view.findViewById(R.id.rv_layout);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        String tagMap = this.getClass().getSimpleName();

        new MapMaker(getChildFragmentManager(),tagMap,this,gpsService,gpsProvider, R.id.map_view)
                .setmMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        Food food = mListLinkerMarkerAndFood.get(marker.getId());
                        DialogDetailFoodFragment f =  DialogDetailFoodFragment.newInstance(food.getRec().toString());
                        f.show(((FragmentActivity) mContext).getSupportFragmentManager(),"com.triasbrata.foodhunter.DialogDetailFoodFragment");
                        return false;
                    }
                })
                .invoke();
//        new CheckingGPS((LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE),getActivity()).nextRule();
    }

    @Override
    public void onResume() {
        super.onResume();
        gpsService.requestLocationUpdates(gpsProvider, 400, 1, this);
        if(mapFragment != null) mapFragment.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        gpsService.removeUpdates(this);
    }

    private void makeMap(Bundle savedInstanceState, final RecyclerView rv) {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentByTag("com.mapbox.map");
        if (mapFragment == null) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            Location position = gpsService.getLastKnownLocation(gpsProvider);
            //            if(position == null){
                userLocaltion = new LatLng(-0.470402,117.151568);
//            }else{
//                location = new LatLng(position);
//            }

            MapboxMapOptions options = new MapboxMapOptions();
            options.styleUrl(Style.LIGHT);
            options.camera(new CameraPosition.Builder()
                    .target(userLocaltion)
                    .zoom(14)
                    .build());
            mapFragment = MapBoxFragment.newInstance(options);
            transaction.add(R.id.map_view,mapFragment,"com.mapbox.map");
            transaction.commit();
        }
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mapboxMap.setOnCameraChangeListener(new MapboxMap.OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition position) {
                        Log.d(TAG, "onCameraChange: called");
                    }
                });
                mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        Log.d(TAG, "onMarkerClick: called");
                        Food food = mListLinkerMarkerAndFood.get(marker.getId());
                        DialogDetailFoodFragment f =  DialogDetailFoodFragment.newInstance(food.getRec().toString());
                        f.show(((DashboardActivity) mContext).getSupportFragmentManager(),"");
                        return false;
                    }
                });
                mapboxMap.setOnCameraChangeListener(new MapboxMap.OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition position) {
                        Log.d(TAG, "onCameraChange: "+position.target.toString());
                    }
                });
                getFetchFoodModel(getContext(), rv,mapboxMap);
            }
        });
    }


    private void getFetchFoodModel(Context context, RecyclerView rv, MapboxMap mapboxMap) {
        String url = Config.URL.food_all();
        FutureCallback<JsonArray> f = new CallbackFetchingModel(context,rv, mapboxMap);
        Log.d(TAG, "getFetchFoodModel: "+url);
        Ion.with(context)
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
        //getFetchFoodModel(getContext(), (RecyclerView) getView().findViewById(R.id.rv_layout), mapboxMap);

    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "onProviderEnabled: "+provider);
        new MaterialDialog.Builder(getActivity()).content("GPS Aktif dengan" + provider).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, "onProviderDisabled: "+provider);
        new MaterialDialog.Builder(getActivity()).content("GPS Aktif dengan" + provider).show();
    }

    @Override
    public void getDataFetcher(MapboxMap mapboxMap) {
        getFetchFoodModel(getContext(),rv,mapboxMap);
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
                DialogDetailFoodFragment f =  DialogDetailFoodFragment.newInstance(result.toString());
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
        private MapboxMap mapboxMap;

        public CallbackFetchingModel(Context context, RecyclerView rv, MapboxMap mapboxMap) {
            this.context = context;
            this.rv = rv;
            this.mapboxMap = mapboxMap;
        }
        @Override
        @UiThread
        public void onCompleted(Exception e, JsonArray result) {
            final ArrayList<Food> foods = new ArrayList<>();
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
            if(!foods.isEmpty()){
                Marker m;
                mListLinkerMarkerAndFood.clear();
                for (Food food : foods) {
                    String storeAndAddress = food.getStore().getName()+"\n"+food.getStore().getAddress();
                    m = mapboxMap.addMarker( new MarkerOptions()
                            .position( food.getStore().getLocation())
                            .title(food.getName())
                            .snippet(storeAndAddress));
                    mListLinkerMarkerAndFood.put(m.getId(),food);
                }
            }
            this.rv.setAdapter(mAdapter);
            this.rv.getAdapter().notifyDataSetChanged();
        }
    }
}
