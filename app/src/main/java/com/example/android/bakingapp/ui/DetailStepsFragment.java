package com.example.android.bakingapp.ui;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.support.v4.app.Fragment;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Step;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;

import com.google.android.exoplayer2.SimpleExoPlayer;

import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;


import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;

import com.google.android.exoplayer2.ui.PlayerView;

import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class DetailStepsFragment extends Fragment {

    // Final Strings to store state information about the list of images and list index
    public static final String STEPS_LIST = "steps";
    public static final String INGREDIENTS_LIST = "ingredients";
    public static final String LIST_INDEX = "list_index";
    public static final String TWOPANE = "two_pane";
    public static final String PLAYBACK_POSITION = "playbackPosition";
    public static final String CURRENT_WINDOW = "currentWindow";
    public static final String PLAY_WHEN_READY = "playWhenReady";

    // Tag for logging
    private static final String TAG = "DetailStepsFragment";

    // Variables to store a list of recipe's resources and the index of the image that this fragment displays
    private ArrayList<Step> mSteps;
    private ArrayList<Ingredient> mIngredients;
    private int mListIndex;

    //Variables for VideoPlayer
    private String path;
    private SimpleExoPlayer player;
    private static PlayerView playerView;

    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady;


    //Boolean for tablet(two pane)/mobile mode
    private boolean TwoPane;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public DetailStepsFragment() {
    }

    /**
     * Inflates the fragment layout file and sets the correct resource for the image to display
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Load the saved state (the list of images and list index) if there is one
        if (savedInstanceState != null) {
            mSteps = savedInstanceState.getParcelableArrayList(STEPS_LIST);
            mIngredients = savedInstanceState.getParcelableArrayList(INGREDIENTS_LIST);
            mListIndex = savedInstanceState.getInt(LIST_INDEX);
            TwoPane = savedInstanceState.getBoolean(TWOPANE);

            playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION);
            currentWindow = savedInstanceState.getInt(CURRENT_WINDOW);
            playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
        }

        // Inflate the Android-Me fragment layout
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);

        playerView = (PlayerView) rootView.findViewById(R.id.player_view);
        //playerView.setMinimumHeight(getScreenHeightInDPs(getContext())/5);

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
            setIngredientsView(ingredientsInfoLayout, longDescriptionView, nextButtonView, previousButtonView);

        } else {
            // If index is not 0, show steps
            // if steps are not null, show them, otherwise, create a Log statement that indicates that the list was not found
            setStepsView(ingredientsInfoLayout, longDescriptionView, nextButtonView, previousButtonView);

        }

        if (mSteps != null){
        // Set a click listener on the next button
        nextButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Increment position as long as the index remains <= the size of the steps list
                if (mListIndex < mSteps.size()) {
                    mListIndex++;
                }
                // Set the image resource to the new list item
                if (mListIndex == 0) {
                    setIngredientsView(ingredientsInfoLayout, longDescriptionView, nextButtonView, previousButtonView);
                }
                else{
                    setStepsView(ingredientsInfoLayout, longDescriptionView, nextButtonView, previousButtonView);
                }
            }
        });

        // Set a click listener on the previous button
        previousButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Increment position as long as the index remains <= the size of the image ids list
                if (mListIndex > 0) {
                    mListIndex--;
                }
                // Set the image resource to the new list item
                if (mListIndex == 0) {
                    setIngredientsView(ingredientsInfoLayout, longDescriptionView, nextButtonView, previousButtonView);
                }
                else{
                    setStepsView(ingredientsInfoLayout, longDescriptionView, nextButtonView, previousButtonView);}
            }
        });
        }

            // Return the rootView
            return rootView;
        }



    // Setter methods for keeping track of the list images this fragment can display and which image
    // in the list is currently being displayed

    public void setSteps(ArrayList<Step> steps) {
        mSteps = steps;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        mIngredients = ingredients;
    }

    public void setListIndex(int index) {
        mListIndex = index;
    }

    public void setTwoPane(boolean twoPane) {TwoPane = twoPane; }


    public void setStepsView(LinearLayout ingredientsInfoLayout, TextView longDescriptionView, Button nextButtonView, Button previousButtonView){
        playerView.setVisibility(View.GONE);

        if (mSteps != null){
            /*
            DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            String path = mSteps.get(mListIndex -1).getVideoURL();
            //Extractor MediaSource - MP3/MP4
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"),
                    mediaDataSourceFactory, extractorsFactory, null, null);
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);*/

            //Check if there is a video resource URL
            path = mSteps.get(mListIndex -1).getVideoURL();

            longDescriptionView.setText(mSteps.get(mListIndex -1).getDescription());
            longDescriptionView.setVisibility(View.VISIBLE);
            ingredientsInfoLayout.setVisibility(View.GONE);
            previousButtonView.setVisibility(View.VISIBLE);

            if (path.length() > 8){

                Log.d("URL", "URL substring: " + path.substring(path.length()-4) + path.length());
                if(path.substring(path.length()-4).equals(".mp4")){
                    Log.d("URL for video2", "URL" + path);
                    Timber.d(path);
                    Log.d("Log", "path" + path);
                    initializeVideoPlayer();}

                int orientation = this.getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    hideSystemUi();
                } else
                {showSystemUI();}
            }

        if (mListIndex == mSteps.size()) {
            nextButtonView.setVisibility(View.INVISIBLE);
        } else {
            nextButtonView.setVisibility(View.VISIBLE);
        }

        } else {
            Log.v(TAG, "This fragment has a null list of steps");
        }

    }

    public void setIngredientsView(LinearLayout ingredientsInfoLayout, TextView longDescriptionView, Button nextButtonView, Button previousButtonView) {
        playerView.setVisibility(View.GONE);
        longDescriptionView.setVisibility(View.GONE);

        ingredientsInfoLayout.setVisibility(View.VISIBLE);
        previousButtonView.setVisibility(View.INVISIBLE);
        int childCount = ingredientsInfoLayout.getChildCount();

        if (childCount < 2) {

        if (mIngredients != null) {
            int a = 1;
            for (final Ingredient ingredient : mIngredients) {
                if (ingredient != null) {

                    View mIngredientItem = LayoutInflater.from(getActivity()).inflate(
                            R.layout.ingredient_list_item, null);

                    TextView mIngredientName = (TextView) mIngredientItem.findViewById(R.id.ingredient_name);
                    Double quantityDouble = ingredient.getQuantity();
                    mIngredientName.setText(a + ". " + ingredient.getName().toUpperCase() + ": " + quantityDouble.toString()
                            + " " + ingredient.getMeasure().toLowerCase());

                    Log.v(TAG, "Quantity: " + String.valueOf((ingredient.getQuantity())));
                    Timber.d("Quantity");

                    ingredientsInfoLayout.addView(mIngredientItem);
                    a++;
                }
            }
        } else {
            Log.v(TAG, "This fragment has a null list of ingredients");
        }
    }
    }

    public void initializeVideoPlayer(){
        if (path != null) {
            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());

            playerView.setPlayer(player);
            Log.d("OnStart", "is " + playbackPosition);
            player.setPlayWhenReady(playWhenReady);


            Uri uri = Uri.parse(path);
            MediaSource mediaSource = buildMediaSource(uri);
            player.prepare(mediaSource, true, true);
            player.seekTo(currentWindow, playbackPosition);
            playerView.setVisibility(View.VISIBLE);
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("BakingApp")).
                createMediaSource(uri);
    }


    /*@SuppressLint("InlinedApi")
    private void hideSystemUi() {
        if (TwoPane == false) {
            playerView.setSystemUiVisibility(
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Hide the nav bar and status bar
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }*/


    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        if (TwoPane == false) {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }}

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        if (TwoPane == false) {
            playerView.setSystemUiVisibility(0);
            //View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            //   | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            // | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (Util.SDK_INT > 23) {
            if (player != null) {
            Log.d("OnStart", "is " + playbackPosition + player.getCurrentPosition());}
            initializeVideoPlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSystemUi();
        } else
        {showSystemUI();}
        if ((Util.SDK_INT <= 23 || player == null)) {
            if (player != null) {
            Log.d("OnResume", "is " + playbackPosition + player.getCurrentPosition());}
            initializeVideoPlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        showSystemUI();
        if (Util.SDK_INT <= 23) {
            Log.d("OnPause", "is " + playbackPosition + player.getCurrentPosition());
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        showSystemUI();
        if (Util.SDK_INT > 23) {
            if (player != null) {
            Log.d("OnStop", "is " + player.getCurrentPosition());}
            releasePlayer();
        }
    }

   /* @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }*/

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            if (player != null) {
            Log.d("OnCurrentRelease", "is " + player.getCurrentPosition());}
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (TwoPane == false) {

            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                hideSystemUi();
                Log.e("On Config Change", "LANDSCAPE");
            } else {
                Log.e("On Config Change", "PORTRAIT");
                showSystemUI();
            }
        }
    }
    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelableArrayList(STEPS_LIST, (ArrayList<Step>) mSteps);
        currentState.putParcelableArrayList(INGREDIENTS_LIST, (ArrayList<Ingredient>) mIngredients);
        currentState.putInt(LIST_INDEX, mListIndex);
        currentState.putBoolean(TWOPANE, TwoPane);
        if (player != null) {
        currentState.putInt(CURRENT_WINDOW, player.getCurrentWindowIndex());
        currentState.putBoolean(PLAY_WHEN_READY, player.getPlayWhenReady());
        currentState.putLong(PLAYBACK_POSITION, player.getCurrentPosition());
        Log.d("OnSave", "is " + player.getCurrentPosition());}
    }

    /*
    public static int getScreenHeightInDPs(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        /*
            In this example code we converted the float value
            to nearest whole integer number. But, you can get the actual height in dp
            by removing the Math.round method. Then, it will return a float value, you should
            also make the necessary changes.
        */

        /*
            public int heightPixels
                The absolute height of the display in pixels.

            public float density
             The logical density of the display.
        */
        /*int heightInDP = Math.round(dm.heightPixels / dm.density);
        return heightInDP;
    }*/
}