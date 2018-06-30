package com.example.android.bakingapp;

import timber.log.Timber;


//for release purposes crash library can be used (https://github.com/JakeWharton/timber/tree/master/sample/src/main/java/com/example/timber)

//No logging in release
public class ReleaseTree extends Timber.Tree {
    @Override
    protected void log(final int priority, final String tag, final String message, final Throwable throwable) {
    }
}