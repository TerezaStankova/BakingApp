package com.example.android.bakingapp.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Step;

import java.util.ArrayList;

import timber.log.Timber;

public class StepsDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_detail);

        // Only create new fragments when there is no previously saved state
        if(savedInstanceState == null) {

            // Retrieve list index values that were sent through an intent; use them to display the desired Android-Me body part image
            // Use setListindex(int index) to set the list index for all BodyPartFragments

            int listIndex = getIntent().getIntExtra("stepIndex", 0);
            Timber.e("stepIndexIntent" + listIndex);
            Log.d("StepsDetail", "stepIndexIntent: " + listIndex);
            ArrayList<Step> steps = getIntent().getParcelableArrayListExtra("steps");
            ArrayList<Ingredient> ingredients = getIntent().getParcelableArrayListExtra("ingredients");
            Log.d("StepsDetail", "step1: " + steps.get(steps.size()-1).getShortDescription());

            StepsFragment newFragment = new StepsFragment();
            //newFragment.setImageIds(steps);
            newFragment.setListIndex(listIndex);
            newFragment.setSteps(steps);
            newFragment.setIngredients(ingredients);

            // Replace the old head fragment with a new one
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_description_container, newFragment)
                    .commit();
        }

    }
}
