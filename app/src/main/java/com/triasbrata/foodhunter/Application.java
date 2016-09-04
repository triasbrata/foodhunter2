package com.triasbrata.foodhunter;

import android.support.multidex.MultiDexApplication;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.triasbrata.foodhunter.etc.Config;


/**
 * Created by triasbrata on 24/08/16.
 */
public class Application extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("Iw9DRS3UyNpZbAzQcbGcDIPFKqNiajxPpHffSZtz")
                .server("https://parseapi.back4app.com/")
        .build()
        );
    }
}
