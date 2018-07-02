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

public class MasterListFragment extends Fragment {


    // Final Strings to store state information about the steps
    private static final String STEPS = "steps";
    private static final String RECIPE = "recipe";

    // Tag for logging
    private static final String TAG = "MaterListFragment";

    // Variables to store an array of steps
    private ArrayList<Step> mSteps = new ArrayList<>();

    private Recipe mRecipe;

    // Define a new interface OnImageClickListener that triggers a callback in the host activity
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
                    + " must implement OnImageClickListener");
        }
    }


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
           mSteps = mRecipe.getSteps();
        }

        final View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);

        // Get a reference to the linear layout in the fragment layout
        //FINAL?
        final LinearLayout stepsInfoLayout = (LinearLayout) rootView.findViewById(R.id.steps_linear_layout);
        final TextView ingredientsLabelTextView = (TextView) rootView.findViewById(R.id.ingredients_label);
        ingredientsLabelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onItemSelected(0);
            }
        });


        if (mSteps != null) {

            for (final Step step : mSteps) {
                if (step != null) {

                    View mStepItem = LayoutInflater.from(getActivity()).inflate(
                            R.layout.step_list_item, null);

                    TextView mMovieTrailerTitle = (TextView) mStepItem.findViewById(R.id.step_short_description);
                    mMovieTrailerTitle.setText(step.getShortDescription());
                    Log.v(TAG, "Step Name: " + step.getShortDescription());

                    mStepItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mCallback.onItemSelected(step.getStepId() + 1);
                        }
                    });

                    if (stepsInfoLayout != null) {
                        stepsInfoLayout.addView(mStepItem);
                    }
                }
            }
        }

        else {
            Log.v(TAG, "This fragment has a null list of steps");
        }

        // Return the root view
        return rootView;
    }


    public void setSteps(ArrayList<Step> trailers) {
        mSteps = trailers;
    }

    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
    }

    /*Save the current state of this fragment*/
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelableArrayList(STEPS, mSteps);
        currentState.putParcelable(RECIPE, mRecipe);
    }
}

