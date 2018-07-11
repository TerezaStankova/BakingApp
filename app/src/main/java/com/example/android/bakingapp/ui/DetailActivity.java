package com.example.android.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.widget.Toast;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;

import java.util.ArrayList;

import timber.log.Timber;

public class DetailActivity extends AppCompatActivity implements MasterListFragment.OnItemClickListener{

    //Fields for recipe's info
    private String name;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private ArrayList<Step> steps = new ArrayList<>();
    private Recipe recipe;

    // Track whether to display a two-pane or single-pane UI
    // A single-pane display refers to phone screens, and two-pane to larger tablet screens
    private boolean mTwoPane;
    private String RECIPE = "recipe";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            recipe = (Recipe) getIntent().getParcelableExtra("parcel_data");
        }

        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable(RECIPE);
        }

        if (recipe == null) {
            // Recipe data unavailable
            closeOnError();
            return;
        }

        steps = recipe.getSteps();
        //Set recipe´s info
        name = recipe.getName();
        ingredients = recipe.getIngredients();
        steps = recipe.getSteps();
        setTitle(name);
        Timber.d("onCreateDetailActivity" + name);


        if (savedInstanceState == null) {
            if (steps != null) {
                // Create a new MasterListFragment
                MasterListFragment masterListFragment = new MasterListFragment();
                //masterListFragment.setSteps(steps);
                masterListFragment.setRecipe(recipe);
                // Add the fragment to its container using a FragmentManager and a Transaction
                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .add(R.id.master_list_container, masterListFragment)
                        .commit();
            } else {
                // Steps data unavailable
                closeOnError();
                return;
            }
        }

        // Determine if you're creating a two-pane or single-pane display
        if (findViewById(R.id.small_divider) != null) {
            Timber.d("Name " + name);
            // This LinearLayout will only initially exist in the two-pane tablet case
            mTwoPane = true;

            if (savedInstanceState == null) {
                Timber.d("Name " + name);
                // In two-pane mode, add initial DetailStepFragments to the screen
                DetailStepsFragment newFragment = new DetailStepsFragment();

                newFragment.setSteps(steps);
                newFragment.setIngredients(ingredients);
                newFragment.setListIndex(0);
                newFragment.setTwoPane(mTwoPane);
                // Replace the old fragment with a new one
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_description_container, newFragment)
                        .commit();
            }
        } else {
            // We're in single-pane mode and displaying fragments on a phone in separate activities
            mTwoPane = false;
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }


    // Define the behavior for onItemSelected
    public void onItemSelected(int position) {

        // Handle the two-pane case and replace existing fragments right when a new image is selected from the master list
        if (mTwoPane) {
            // Create two=pane interaction

            DetailStepsFragment newFragment = new DetailStepsFragment();
            //newFragment.setImageIds(steps);
            newFragment.setSteps(steps);
            newFragment.setIngredients(ingredients);
            newFragment.setTwoPane(mTwoPane);
            newFragment.setListIndex(position);
            // Replace the old head fragment with a new one
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_description_container, newFragment)
                    .commit();
            }
            else {

            // Handle the single-pane phone case by passing information in a Bundle attached to an Intent
            // Put this information in a Bundle and attach it to an Intent that will launch an StepsDetailActivity
            Bundle b = new Bundle();
            b.putInt("stepIndex", position);
            Timber.d("nextStepClicked" + position);
            b.putParcelableArrayList("steps", steps);
            b.putParcelableArrayList("ingredients", ingredients);

            // Attach the Bundle to an intent
            final Intent intent = new Intent(this, StepsDetailActivity.class);
            intent.putExtras(b);
            startActivity(intent);

        }

    }


    //Save info about recipe
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(RECIPE, recipe);
    }
}
