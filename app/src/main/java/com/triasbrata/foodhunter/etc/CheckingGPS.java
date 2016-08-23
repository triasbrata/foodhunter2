package com.triasbrata.foodhunter.etc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.triasbrata.foodhunter.R;

/**
 * Created by triasbrata on 23/08/16.
 */
public class CheckingGPS {
    private final LocationManager locationManager;
    private final Activity context;

    public CheckingGPS(LocationManager locationManager, Activity context){
        this.locationManager = locationManager;
        this.context = context;
    }
    public void invoke() {
        boolean isGps = false;
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) isGps = true;
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) isGps = true;
        if(locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) isGps = true;
        if(isGps){
            new MaterialDialog.Builder(context)
                    .title("Aktifkan GPS")
                    .content("GPS anda belum aktif silahkan aktifkan terlebih dahulu")
                    .backgroundColorRes(R.color.white)
                    .contentColorRes(R.color.textDark)
                    .titleColor(R.color.textDark)
                    .positiveText("Aktifkan")
                    .negativeText("Tutup Aplikasi")
                    .canceledOnTouchOutside(false)
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            context.finish();
                        }
                    })
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .show();
        }
    }
}
