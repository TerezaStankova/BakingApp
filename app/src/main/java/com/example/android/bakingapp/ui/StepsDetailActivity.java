package com.example.android.bakingapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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

            // Retrieve list index values that were sent through an intent
            // Use setListindex(int index) to set the list index for detailStepFragment

            int listIndex = getIntent().getIntExtra("stepIndex", 0);
            Timber.d("stepIndexIntent%s", listIndex);
            ArrayList<Step> steps = getIntent().getParcelableArrayListExtra("steps");
            ArrayList<Ingredient> ingredients = getIntent().getParcelableArrayListExtra("ingredients");

            DetailStepsFragment newFragment = new DetailStepsFragment();
            newFragment.setTwoPane(false);

            newFragment.setListIndex(listIndex);
            newFragment.setSteps(steps);
            newFragment.setIngredients(ingredients);

            // Replace the old fragment with a new one
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_description_container, newFragment)
                    .commit();
        }
    }
}
