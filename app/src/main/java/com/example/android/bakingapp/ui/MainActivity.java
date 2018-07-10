package com.example.android.bakingapp.ui;

//icon: Photo by Artur Rutkowski on Unsplash

import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bakingapp.IngredientsService;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.ui.RecipeAdapter.RecipeAdapterOnClickHandler;
import com.example.android.bakingapp.utilities.JSONUtils;
import com.example.android.bakingapp.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements RecipeAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    private TextView mErrorMessageDisplay;
    public ProgressBar mLoadingIndicator;
    private ArrayList<Recipe> mRecipes = new ArrayList();
    private GridLayoutManager layoutManager;
    private Parcelable mListState;

    // Final String to store state information about the movies
    private static final String RECIPES = "recipes";
    private static final String LIST_STATE_KEY = "list_state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ButterKnife.bind(this);

        //Timber.tag("LifeCycles");
        //Set type of log - Tag is provided for you - more info https://medium.com/@caueferreira/timber-enhancing-your-logging-experience-330e8af97341
        Timber.d("Activity Created");


            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_recipes);
            /* This TextView is used to display errors and will be hidden if there are no errors */
            mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

            final int columns = getResources().getInteger(R.integer.gallery_columns);

            layoutManager = new GridLayoutManager(this, columns, GridLayoutManager.VERTICAL, false);

            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            /*
             * The mMovieAdapter is responsible for linking data with the Views that
             * will end up displaying the data.
             */
            mRecipeAdapter = new RecipeAdapter(this);
            /* Setting the adapter attaches it to the RecyclerView in our layout. */
            mRecyclerView.setAdapter(mRecipeAdapter);
            /*
             * The ProgressBar that will indicate to the user that we are loading data. It will be
             * hidden when no data is loading.
             */
            mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);


            if (savedInstanceState != null) {
                // Load the saved state (the array of trailers) if there is one
                //mRecipes = (ArrayList<Recipe>) savedInstanceState.getParcelableArrayList(RECIPES);
            }

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

            /**Check for internet connection before making the actual request to the API,
             * so the device can save one unneeded network call given that we know it will
             * fail to fetch the movies.
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
                Log.d("MAIN", "sent" + recipe.getName());
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
                }

                @Override
                protected ArrayList<Recipe> doInBackground(String... params) {

                    URL movieRequestUrl = NetworkUtils.buildUrl();

                    try {
                        String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

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
                }
            }



            @Override
            public void onSaveInstanceState(Bundle savedInstanceState)
            {
                super.onSaveInstanceState(savedInstanceState);

                // Save list state
                mListState = layoutManager.onSaveInstanceState();
                savedInstanceState.putParcelable(LIST_STATE_KEY, mListState);
                savedInstanceState.putParcelableArrayList(RECIPES, (ArrayList<Recipe>) mRecipes);

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

