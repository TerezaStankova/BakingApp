package com.example.android.bakingapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.support.v4.app.Fragment;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

public class StepsFragment extends Fragment {

    // Final Strings to store state information about the list of images and list index
    public static final String IMAGE_ID_LIST = "image_ids";
    public static final String STEPS_LIST = "steps";
    public static final String INGREDIENTS_LIST = "ingredients";
    public static final String LIST_INDEX = "list_index";

    // Tag for logging
    private static final String TAG = "StepsFragment";

    // Variables to store a list of image resources and the index of the image that this fragment displays
    private List<Integer> mStepIds;
    private ArrayList<Step> mSteps;
    private ArrayList<Ingredient> mIngredients;
    private int mListIndex;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public StepsFragment() {
    }

    /**
     * Inflates the fragment layout file and sets the correct resource for the image to display
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Load the saved state (the list of images and list index) if there is one
        if (savedInstanceState != null) {
            mStepIds = savedInstanceState.getIntegerArrayList(IMAGE_ID_LIST);
            mSteps = savedInstanceState.getParcelableArrayList(STEPS_LIST);
            mIngredients = savedInstanceState.getParcelableArrayList(INGREDIENTS_LIST);
            mListIndex = savedInstanceState.getInt(LIST_INDEX);
        }

        // Inflate the Android-Me fragment layout
        View rootView = inflater.inflate(R.layout.fragment_step_description, container, false);

        // Get a reference to the ImageView in the fragment layout
        //final ImageView imageView = (ImageView) rootView.findViewById(R.id.body_part_image_view);

        // Get a reference to the ImageView in the fragment layout
        final TextView longDescriptionView = (TextView) rootView.findViewById(R.id.step_long_description);

        // Get a reference to the ImageView in the fragment layout
        final Button nextButtonView = (Button) rootView.findViewById(R.id.next_button2);

        // Get a reference to the ImageView in the fragment layout
        final Button previousButtonView = (Button) rootView.findViewById(R.id.previous_button);

        final LinearLayout ingredientsInfoLayout = (LinearLayout) rootView.findViewById(R.id.ingredients_linear_layout);

        if (mListIndex == 0) {
            longDescriptionView.setVisibility(View.GONE);
            ingredientsInfoLayout.setVisibility(View.VISIBLE);
            setIngredientsView(ingredientsInfoLayout);

        } else {

            longDescriptionView.setVisibility(View.VISIBLE);
            ingredientsInfoLayout.setVisibility(View.GONE);

            // If a list of image ids exists, set the image resource to the correct item in that list
            // Otherwise, create a Log statement that indicates that the list was not found
            if (mSteps != null) {
                // Set the image resource to the list item at the stored index
                //imageView.setImageResource(mStepIds.get(mListIndex));
                longDescriptionView.setText(mSteps.get(mListIndex).getDescription());

            } else {
                Log.v(TAG, "This fragment has a null list of steps");
            }

        }

        if (mSteps != null){
        // Set a click listener on the image view
        nextButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Increment position as long as the index remains <= the size of the image ids list
                if (mListIndex < mSteps.size()) {
                    mListIndex++;
                } else {
                    // The end of list has been reached, so return to beginning index
                    mListIndex = 0;
                }
                // Set the image resource to the new list item
                if (mListIndex == 0) {
                    longDescriptionView.setVisibility(View.GONE);
                    ingredientsInfoLayout.setVisibility(View.VISIBLE);
                    setIngredientsView(ingredientsInfoLayout);
                }
                else{
                    longDescriptionView.setVisibility(View.VISIBLE);
                    ingredientsInfoLayout.setVisibility(View.GONE);
                    longDescriptionView.setText(mSteps.get(mListIndex -1).getDescription());}
            }
        });

        // Set a click listener on the image view
        previousButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Increment position as long as the index remains <= the size of the image ids list
                if (mListIndex > 0) {
                    mListIndex--;
                } else {
                    // The begin of list has been reached, so return to end index
                    mListIndex = mSteps.size();
                }
                // Set the image resource to the new list item
                if (mListIndex == 0) {
                    longDescriptionView.setVisibility(View.GONE);
                    ingredientsInfoLayout.setVisibility(View.VISIBLE);
                    setIngredientsView(ingredientsInfoLayout);
                }
                else{
                    longDescriptionView.setVisibility(View.VISIBLE);
                    ingredientsInfoLayout.setVisibility(View.GONE);
                    longDescriptionView.setText(mSteps.get(mListIndex -1).getDescription());}
            }
        });
        }

            // Return the rootView
            return rootView;
        }



    // Setter methods for keeping track of the list images this fragment can display and which image
    // in the list is currently being displayed

    public void setImageIds(List<Integer> stepIds) {
        mStepIds = stepIds;
    }

    public void setSteps(ArrayList<Step> steps) {
        mSteps = steps;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        mIngredients = ingredients;
    }

    public void setListIndex(int index) {
        mListIndex = index;
    }

    public void setIngredientsView(LinearLayout ingredientsInfoLayout) {

        if (mIngredients != null) {

            for (final Ingredient ingredient : mIngredients) {
                if (ingredient != null) {

                    View mIngredientItem = LayoutInflater.from(getActivity()).inflate(
                            R.layout.ingredient_list_item, null);

                    TextView mIngredientName = (TextView) mIngredientItem.findViewById(R.id.ingredient_name);
                    mIngredientName.setText(ingredient.getName());

                    TextView mIngredientValue = (TextView) mIngredientItem.findViewById(R.id.ingredient_value);
                    mIngredientValue.setText(String.valueOf((ingredient.getQuantity())) + " ");

                    TextView mIngredientMeasure = (TextView) mIngredientItem.findViewById(R.id.ingredient_measure);
                    mIngredientMeasure.setText(ingredient.getMeasure());


                    Log.v(TAG, "Quantity: " + String.valueOf((ingredient.getQuantity())));

                    if (ingredientsInfoLayout != null) {
                        ingredientsInfoLayout.addView(mIngredientItem);
                    }
                }
            }
        } else {
            Log.v(TAG, "This fragment has a null list of ingredients");
        }




    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putIntegerArrayList(IMAGE_ID_LIST, (ArrayList<Integer>) mStepIds);
        currentState.putParcelableArrayList(STEPS_LIST, (ArrayList<Step>) mSteps);
        currentState.putParcelableArrayList(INGREDIENTS_LIST, (ArrayList<Ingredient>) mIngredients);
        currentState.putInt(LIST_INDEX, mListIndex);
    }


}
