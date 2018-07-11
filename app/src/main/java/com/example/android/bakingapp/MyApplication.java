package com.example.android.bakingapp;

import android.app.Application;

import timber.log.Timber;

class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG)
            Timber.plant(new BakingDebugTree());
        else
            Timber.plant(new ReleaseTree());
    }
}