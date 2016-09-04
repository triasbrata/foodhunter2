package com.triasbrata.foodhunter.etc;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;

import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;
import com.triasbrata.foodhunter.fragment.inner.MapBoxFragment;

/**
 * Created by triasbrata on 24/08/16.
 */
public class MapMaker {
    private int mViewId;

    public MapMaker setmCameraChangeListener(MapboxMap.OnCameraChangeListener mCameraChangeListener) {
        this.mCameraChangeListener = mCameraChangeListener;
        return this;
    }

    public MapMaker setmMarkerClickListener(MapboxMap.OnMarkerClickListener mMarkerClickListener) {
        this.mMarkerClickListener = mMarkerClickListener;
        return this;
    }

    public interface  DataFetcher extends LocationListener {
        void getDataFetcher(MapboxMap mapboxMap);
    }

    private LocationManager mLocationManager;
    private String mProviderLocationManager;
    private FragmentManager mFragmentMeneger;
    private String mTag;
    private SupportMapFragment mapFragment;
    private DataFetcher mFragmentHandler;
    private LatLng mDefaultLocation;
    private MapboxMap.OnCameraChangeListener mCameraChangeListener;
    private MapboxMap.OnMarkerClickListener mMarkerClickListener;

    public MapMaker(FragmentManager fragmentManager, String tag, int mViewId, DataFetcher fragmentHandler, LocationManager locationManager, String providerLocationManager, LatLng defaultLocation) {
        mFragmentMeneger = fragmentManager;
        this.mViewId = mViewId;
        mTag = tag+".com.mapbox.mapview";
        mLocationManager = locationManager;
        mProviderLocationManager = providerLocationManager;
        mDefaultLocation = defaultLocation;
        mFragmentHandler = fragmentHandler;
    }
    public MapMaker(FragmentManager fragmentManager, String tag, DataFetcher fragmentHandler, @Nullable LocationManager locationManager, @Nullable String providerLocationManager, int mViewId) {
        this(fragmentManager, tag, mViewId, fragmentHandler, locationManager, providerLocationManager,new LatLng(-0.470402,117.151568));
    }
    public MapMaker(FragmentManager fragmentManager, String tag, DataFetcher fragmentHandler, int mViewId) {
        this(fragmentManager, tag, fragmentHandler, null, null, mViewId);
        mLocationManager = (LocationManager) ((Fragment) fragmentHandler).getActivity().getSystemService(Context.LOCATION_SERVICE);
        mProviderLocationManager = mLocationManager.getBestProvider(new Criteria(), true);
    }
    public void invoke(){
        mapFragment = (SupportMapFragment) mFragmentMeneger.findFragmentByTag(mTag);
        if (mapFragment == null) {
            FragmentTransaction transaction = mFragmentMeneger.beginTransaction();

            Location position = mLocationManager.getLastKnownLocation(mProviderLocationManager);
            LatLng userLocation;
            if(position == null){
                userLocation = mDefaultLocation;
            }else{
                userLocation = new LatLng(position);
            }

            MapboxMapOptions options = new MapboxMapOptions();
            options.styleUrl(Style.LIGHT);
            options.camera(new CameraPosition.Builder()
                    .target(userLocation)
                    .zoom(14)
                    .build());
            mapFragment = MapBoxFragment.newInstance(options);
            transaction.add(mViewId,mapFragment,mTag);
            transaction.commit();
        }
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mapboxMap.setOnCameraChangeListener(mCameraChangeListener);
                mapboxMap.setOnMarkerClickListener(mMarkerClickListener);
                mFragmentHandler.getDataFetcher(mapboxMap);
            }
        });
    }
}