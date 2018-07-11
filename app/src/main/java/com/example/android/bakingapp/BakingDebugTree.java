package com.example.android.bakingapp;

import timber.log.Timber;

class BakingDebugTree extends Timber.DebugTree {
@Override
protected String createStackElementTag(StackTraceElement element) {
        return String.format("[%s#%s:%s]",
        element.getLineNumber(),
        element.getMethodName(),super.createStackElementTag(element));
        }
        }