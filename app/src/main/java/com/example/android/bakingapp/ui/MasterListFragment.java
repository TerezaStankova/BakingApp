package com.example.android.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v4.app.Fragment;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;

import java.util.ArrayList;

import timber.log.Timber;

public class MasterListFragment extends Fragment {

    // Define a new interface OnItemClickListener that triggers a callback in the host activity
    OnItemClickListener mCallback;

    // OnImageClickListener interface, calls a method in the host activity named onImageSelected
    public interface OnItemClickListener {
        void onItemSelected(int position);
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnItemClickListener");
        }
    }


    // Final Strings to store state information about the steps
    private static final String STEPS = "steps";
    private static final String RECIPE = "recipe";

    // Tag for logging
    private static final String TAG = "MasterListFragment";

    // Variables to store an array of steps
    private ArrayList<Step> mSteps = new ArrayList<>();

    private Recipe mRecipe;


    // Mandatory empty constructor
    public MasterListFragment() {
    }

    // Inflates the GridView of all AndroidMe images
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        // Load the saved state (the array of trailers) if there is one
        if (savedInstanceState != null) {
           mRecipe = (Recipe) savedInstanceState.getParcelable(RECIPE);
           //mSteps = (ArrayList<Step>) savedInstanceState.getParcelableArrayListExtra(STEPS);
            mSteps = mRecipe.getSteps();
        }

        final View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);

        // Get a reference to the linear layout in the fragment layout
        //FINAL?
        final LinearLayout stepsInfoLayout = (LinearLayout) rootView.findViewById(R.id.steps_linear_layout);
        //stepsInfoLayout.removeAllViews();

        View IngredientLabel = LayoutInflater.from(getActivity()).inflate(
                R.layout.step_list_item, null);

        TextView mIngredientLabelText = (TextView) IngredientLabel.findViewById(R.id.step_short_description);
        mIngredientLabelText.setText(" List of Ingredients ");
        IngredientLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onItemSelected(0);
            }
        });

        Log.v("Log", "Step Name: " + (stepsInfoLayout != null));


        //if (stepsInfoLayout != null) {
            stepsInfoLayout.addView(IngredientLabel);
        //}


        if (mSteps != null) {

            for (final Step step : mSteps) {
                if (step != null) {
                    View mStepItem = LayoutInflater.from(getActivity()).inflate(
                            R.layout.step_list_item, null);

                    TextView mStepName = (TextView) mStepItem.findViewById(R.id.step_short_description);

                    mStepName.setText(" " + step.getShortDescription());
                    Timber.v("Step Name: " + step.getShortDescription());
                    Log.v("Log", "Step Name: " + step.getShortDescription());

                    mStepItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mCallback.onItemSelected(step.getStepId() + 1);
                        }
                    });

                    stepsInfoLayout.addView(mStepItem);

                }
            }
        }

        else {
            Log.v(TAG, "This fragment has a null list of steps");
        }

        // Return the root view
        return rootView;
    }


    //public void setSteps(ArrayList<Step> trailers) {
       // mSteps = trailers;
    //}

    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
        mSteps = recipe.getSteps();
    }

    /*Save the current state of this fragment*/
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        //currentState.putParcelableArrayListEXTRA(STEPS, mSteps);
        currentState.putParcelable(RECIPE, mRecipe);
    }
}

