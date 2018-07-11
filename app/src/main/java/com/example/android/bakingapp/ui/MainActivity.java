package com.example.android.bakingapp.ui;

//icon: Photo by Artur Rutkowski on Unsplash

import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bakingapp.IdlingResource.SimpleIdlingResource;
import com.example.android.bakingapp.IngredientsService;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.ui.RecipeAdapter.RecipeAdapterOnClickHandler;
import com.example.android.bakingapp.utilities.JSONUtils;
import com.example.android.bakingapp.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements RecipeAdapterOnClickHandler {

    private RecipeAdapter mRecipeAdapter;

    private ArrayList<Recipe> mRecipes = new ArrayList();
    private GridLayoutManager layoutManager;
    private Parcelable mListState;

    // Final String to store state information about the movies
    private static final String RECIPES = "recipes";
    private static final String LIST_STATE_KEY = "list_state";

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @BindView(R.id.recyclerview_recipes)
    RecyclerView mRecyclerView;
    /* This TextView is used to display errors and will be hidden if there are no errors */
    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageDisplay;
    /*
     * The ProgressBar that will indicate to the user that we are loading data. It will be
     * hidden when no data is loading.
     */
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //Set type of log - Tag is provided for you
        // More info https://medium.com/@caueferreira/timber-enhancing-your-logging-experience-330e8af97341
        Timber.d("Activity Created");

        final int columns = getResources().getInteger(R.integer.gallery_columns);

        layoutManager = new GridLayoutManager(this, columns, GridLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
            /*
             * The mRecipeAdapter is responsible for linking data with the Views that
             * will end up displaying the data.
             */
            mRecipeAdapter = new RecipeAdapter(this);
            /* Setting the adapter attaches it to the RecyclerView in our layout. */
            mRecyclerView.setAdapter(mRecipeAdapter);

            if (savedInstanceState != null) {
                // Load the saved state (the array of trailers) if there is one
                mRecipes = savedInstanceState.getParcelableArrayList(RECIPES);
                showRecipeDataView();
                mRecipeAdapter.setRecipeData(mRecipes);
            }

            // Get the IdlingResource instance
            getIdlingResource();
        }

            @Override
            protected void onStart() {
                super.onStart();
                loadRecipeData();
            }


            /**
             * This method will tell some background method to get the data in the background.
             */
            private void loadRecipeData() {
                if (isConnected()) {
                    showRecipeDataView();
                    new FetchRecipeTask().execute();
                } else {
                    showErrorMessage();
                }
            }

            /**Check for internet connection before making the actual request to the website,
             * so the device can save one unneeded network call given that we know it will
             * fail to fetch the recipes.
             */

            private boolean isConnected() {
                ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            }

            /**
             * This method is overridden by our MainActivity class in order to handle RecyclerView item
             * clicks.
             *
             * @param singleRecipe The recipe for the image that was clicked
             */
            @Override
            public void onClick(Recipe singleRecipe) {
                Context context = this;
                Recipe recipe;
                recipe = singleRecipe;
                IngredientsService.startActionAddIngredients(this, recipe.getName(), recipe.getIngredients());
                Timber.d("sent%s", recipe.getName());
                Class destinationClass = DetailActivity.class;
                Intent intentToStartDetailActivity = new Intent(context, destinationClass);
                intentToStartDetailActivity.putExtra("parcel_data", recipe);
                startActivity(intentToStartDetailActivity);
            }

            /**
             * This method will make the View for the movie data visible and
             * hide the error message
             */
            private void showRecipeDataView() {
                /* First, make sure the error is invisible */
                mErrorMessageDisplay.setVisibility(View.INVISIBLE);
                /* Then, make sure the movie data is visible */
                mRecyclerView.setVisibility(View.VISIBLE);
            }

            /**
             * This method will make the error message visible and hide the movie
             * View.
             */
            private void showErrorMessage() {
                /* First, hide the currently visible data */
                mRecyclerView.setVisibility(View.INVISIBLE);
                /* Then, show the error */
                mErrorMessageDisplay.setVisibility(View.VISIBLE);
            }

            class FetchRecipeTask extends AsyncTask<String, Void, ArrayList<Recipe>> {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    mLoadingIndicator.setVisibility(View.VISIBLE);

                    if (mIdlingResource != null) {
                        mIdlingResource.setIdleState(false);
                    }
                }

                @Override
                protected ArrayList<Recipe> doInBackground(String... params) {

                    URL recipeRequestUrl = NetworkUtils.buildUrl();

                    try {
                        String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(recipeRequestUrl);

                        return JSONUtils.getRecipeDataFromJson(MainActivity.this, jsonMovieResponse);

                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(ArrayList<Recipe> recipeData) {
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                    if (recipeData != null) {
                        mRecipes = recipeData;
                        showRecipeDataView();
                        mRecipeAdapter.setRecipeData(recipeData);
                    } else {
                        showErrorMessage();
                    }

                    if (mIdlingResource != null) {
                        mIdlingResource.setIdleState(true);
                    }
                }
            }



            @Override
            public void onSaveInstanceState(Bundle savedInstanceState)
            {   super.onSaveInstanceState(savedInstanceState);
                savedInstanceState.putParcelableArrayList(RECIPES, (ArrayList<Recipe>) mRecipes);
                // Save list state
                mListState = layoutManager.onSaveInstanceState();
                savedInstanceState.putParcelable(LIST_STATE_KEY, mListState);
            }

            @Override
            protected void onRestoreInstanceState(Bundle state) {
                super.onRestoreInstanceState(state);

                // Retrieve list state and list/item positions
                if(state != null)
                    mListState = state.getParcelable(LIST_STATE_KEY);
            }

            @Override
            protected void onResume() {
                super.onResume();

                if (mListState != null) {
                    layoutManager.onRestoreInstanceState(mListState);
                }
            }
}

