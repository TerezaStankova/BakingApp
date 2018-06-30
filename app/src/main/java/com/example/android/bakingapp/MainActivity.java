package com.example.android.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ButterKnife.bind(this);

        //Timber.tag("LifeCycles");
        //Set type of log - Tag is provided for you - more info https://medium.com/@caueferreira/timber-enhancing-your-logging-experience-330e8af97341
        Timber.d("Activity Created");



    }
}
