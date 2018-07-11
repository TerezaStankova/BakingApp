package com.example.android.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MasterListFragment extends Fragment {

    // Define a new interface OnItemClickListener that triggers a callback in the host activity
    private OnItemClickListener mCallback;

    // Final Strings to store state information about the steps
    private static final String RECIPE = "recipe";

    // Tag for logging
    private static final String TAG = "MasterListFragment";

    // Variables to store an array of steps
    private ArrayList<Step> mSteps = new ArrayList<>();
    private Recipe mRecipe;

    // Get a reference to the linear layout in the fragment layout
    @BindView(R.id.steps_linear_layout)
    private LinearLayout stepsInfoLayout;

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


    // Mandatory empty constructor
    public MasterListFragment() {
    }

    // Inflates the View of all steps short description
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        // Load the saved state (the array of trailers) if there is one
        if (savedInstanceState != null) {
           mRecipe = (Recipe) savedInstanceState.getParcelable(RECIPE);
           mSteps = mRecipe.getSteps();
        }

        final View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);

        // Get a reference to the views in the fragment layout
        ButterKnife.bind(this, rootView);

        View IngredientLabel = LayoutInflater.from(getActivity()).inflate(
                R.layout.step_list_item, null);

        TextView mIngredientLabelText = (TextView) IngredientLabel.findViewById(R.id.step_short_description);
        mIngredientLabelText.setText(R.string.list_of_ingredients);
        IngredientLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onItemSelected(0);
            }
        });

        stepsInfoLayout.addView(IngredientLabel);



        if (mSteps != null) {

            for (final Step step : mSteps) {
                if (step != null) {
                    View mStepItem = LayoutInflater.from(getActivity()).inflate(
                            R.layout.step_list_item, null);

                    TextView mStepName = (TextView) mStepItem.findViewById(R.id.step_short_description);
                    String nameText = " " + step.getShortDescription();
                    mStepName.setText(nameText);

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
            Timber.tag(TAG).v("This fragment has a null list of steps");
        }

        // Return the root view
        return rootView;
    }


    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
        mSteps = recipe.getSteps();
    }

    /*Save the current state of this fragment*/
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelable(RECIPE, mRecipe);
    }
}

