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

    // Track whether to display a two-pane or single-pane UI
    // A single-pane display refers to phone screens, and two-pane to larger tablet screens
    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Recipe recipe = (Recipe) getIntent().getParcelableExtra("parcel_data");

        if (recipe == null) {
            // Recipe data unavailable
            closeOnError();
            return;
        }

        //Set recipe´s info
        name = recipe.getName();
        ingredients = recipe.getIngredients();
        steps = recipe.getSteps();
        setTitle(name);
        Log.d("Name ", "onCreateDetailActivity" + name);

        // Determine if you're creating a two-pane or single-pane display
        if(findViewById(R.id.small_divider) != null) {
            Log.d("TwoPane","Name " + name);
            // This LinearLayout will only initially exist in the two-pane tablet case
            mTwoPane = true;

            if(savedInstanceState == null) {
                Log.d("InstanceNull","Name " + name);
                // In two-pane mode, add initial DetailStepFragments to the screen
                DetailStepsFragment newFragment = new DetailStepsFragment();
                //newFragment.setImageIds(steps);
                newFragment.setSteps(steps);
                newFragment.setIngredients(ingredients);
                newFragment.setListIndex(0);
                // Replace the old head fragment with a new one
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
        // Create a Toast that displays the position that was clicked
        Toast.makeText(this, "Position clicked = " + position, Toast.LENGTH_SHORT).show();

        // Store the correct list index no matter where in the image list has been clicked
        // This ensures that the index will always be a value between 0-11

        // Handle the two-pane case and replace existing fragments right when a new image is selected from the master list
        if (mTwoPane) {
            // Create two=pane interaction

            DetailStepsFragment newFragment = new DetailStepsFragment();
            //newFragment.setImageIds(steps);
            newFragment.setSteps(steps);
            newFragment.setIngredients(ingredients);
            newFragment.setListIndex(position);
            // Replace the old head fragment with a new one
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_description_container, newFragment)
                    .commit();
            }
            else {

            // Handle the single-pane phone case by passing information in a Bundle attached to an Intent
            // Put this information in a Bundle and attach it to an Intent that will launch an AndroidMeActivity
            Bundle b = new Bundle();
            b.putInt("stepIndex", position);
            Timber.e("clicked");
            Log.d("clicked", "nextButtonClicked" + position);
            b.putParcelableArrayList("steps", steps);
            b.putParcelableArrayList("ingredients", ingredients);

            // Attach the Bundle to an intent
            final Intent intent = new Intent(this, StepsDetailActivity.class);
            intent.putExtras(b);
            startActivity(intent);


            /*
            // The "Next" button launches a new AndroidMeActivity
            Button nextButton = (Button) findViewById(R.id.next_button);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Timber.e("clicked");
                   Log.d("clicked", "nextButtonClicked");
                    startActivity(intent);
                }
            });*/
        }

    }


/*
    private void setButton(boolean isFavourite){
        if (isFavourite){
            mButton.setText(R.string.my_favourite);}
        else if (!isFavourite) {mButton.setText(R.string.new_favourite);
        }
    }




    private void loadMovieDetailData() {
        if (isConnected()) {
            new FetchDetailTrailerTask2().execute();
            new FetchDetailReviewTask().execute();
        }
    }

    /**Check for internet connection before making the actual request to the API,
     * so the device can save one unneeded network call given that we know it will
     * fail to fetch the movies.
     */

/*
    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /*
    private void populateUI(Movie movie) {

        //findViewById calls - Try Butterknife library https://github.com/JakeWharton/butterknife
        TextView originalTitleView = (TextView) findViewById(R.id.original_title_tv);
        originalTitleView.setText(movie.getOriginalTitle());

        TextView releaseDateView = (TextView) findViewById(R.id.release_date_tv);
        releaseDateView.setText(movie.getReleaseDate());

        TextView voteView = (TextView) findViewById(R.id.vote_tv);
        voteView.setText(movie.getVoteAverage());

        TextView plotView = (TextView) findViewById(R.id.plot_tv);
        plotView.setText(movie.getPlot());
    }
*/
    /*
    class FetchDetailTrailerTask2 extends AsyncTask<String, Void, Trailer[]> {


        /*It is recommended to have this AsyncTask class into a separate file to make your code more maintainable.
        https://xelsoft.wordpress.com/2014/11/28/asynctask-implementation-using-callback-interface/
        */
    /*

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Trailer[] doInBackground(String... params) {

            URL trailerRequestUrl = NetworkUtils.buildVideoUrl(id);

            try {
                String jsonTrailerResponse = NetworkUtils.getResponseFromHttpUrl(trailerRequestUrl);

                return JSONUtils.getTrailerDataFromJson(DetailActivity.this, jsonTrailerResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Trailer[] trailerData) {

            if (trailerData != null) {
                // Create a new head  TrailerFragment
                TrailerFragment trailerFragment = new TrailerFragment();

                // Set the trailers for the head fragment and set the position to the second image in the list
                trailerFragment.setTrailers(trailerData);

                // Add the fragment to its container using a FragmentManager and a Transaction
                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .add(R.id.trailer_container, trailerFragment)
                        .commit();
            }
        }
    }


    class FetchDetailReviewTask extends AsyncTask<String, Void, Review[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Review[] doInBackground(String... params) {

            URL reviewRequestUrl = NetworkUtils.buildReviewUrl(id);

            try {
                String jsonReviewResponse = NetworkUtils.getResponseFromHttpUrl(reviewRequestUrl);

                return JSONUtils.getReviewDataFromJson(DetailActivity.this, jsonReviewResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Review[] reviewData) {
            if (reviewData != null) {
                // Create a new head  ReviewFragment
                ReviewFragment reviewFragment = new ReviewFragment();

                // Set the reviews for the head fragment
                reviewFragment.setReviews(reviewData);

                // Add the fragment to its container using a FragmentManager and a Transaction
                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .add(R.id.review_container, reviewFragment)
                        .commit();
            }
        }
    }


    //Define what happens when "favourite" button is clicked
    private void onSaveButtonClicked() {

        if (!isFavourite) {
            final MovieEntry movieEntry = new MovieEntry(id, title, originalTitle, releaseDate, voteAverage, poster, plot);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    // insert new task
                    mDb.movieDao().insertMovie(movieEntry);
                    Log.d("set task", "new favourite movie" + mDb.movieDao().titleById(id));
                    //You should checkout Timber library (https://github.com/JakeWharton/timber) for easier logging.
                }
            });
            mButton.setText(R.string.my_favourite);
            isFavourite = true;
        }
        else {
            // Delete from favourites
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.movieDao().deleteById(id);
                    Log.d("delete task","deleted movie: " + mDb.movieDao().titleById(id));
                }
            });

            mButton.setText(R.string.new_favourite);
            isFavourite = false;
        }

    }
*/

    //Save info about "is favourite?"
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        //savedInstanceState.putBoolean(IS_FAVOURITE, isFavourite);
    }
}
