package com.example.android.bakingapp.ui;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private String path;
    private SimpleExoPlayer player;
    private PlayerView playerView;


    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady;

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
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);

        playerView = (PlayerView) rootView.findViewById(R.id.player_view);

        /*
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

        playerView.setPlayer(player);
*/

        /*
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

        playerView.setPlayer(player);

        player.setPlayWhenReady(shouldAutoPlay);

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"),
                mediaDataSourceFactory, extractorsFactory, null, null);
        player.prepare(mediaSource);*/

        /*
        mainHandler = new Handler();
        userAgent = Util.getUserAgent(getContext(), "BakingApp");
        mediaDataSourceFactory = buildDataSourceFactory();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter());
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getActivity()), trackSelector, new DefaultLoadControl());
        playerView.setPlayer(player);
        playerView.requestFocus();
        playVideoInit();*/

/*
        // 1. Create a default TrackSelector
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        DefaultTrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

// 2. Create the player
        SimpleExoPlayer player =
                ExoPlayerFactory.newSimpleInstance(context, trackSelector);
*/



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


    public void initializeVideoPlayer(){
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);

        Uri uri = Uri.parse(path);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);

    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-bakingapp")).
                createMediaSource(uri);
    }

    public void setStepsView(LinearLayout ingredientsInfoLayout, TextView longDescriptionView, Button nextButtonView, Button previousButtonView){

        if (mSteps != null){
            playerView.setVisibility(View.GONE);
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

            if (path.length() > 8){

                Log.d("URL", "URL substring: " + path.substring(path.length()-4) + path.length());
                if(path.substring(path.length()-4).equals(".mp4")){
                    Log.d("URL for video2", "URL" + path);
                    Timber.d(path);
                    initializeVideoPlayer();
                    playerView.setVisibility(View.VISIBLE);}
            }

            longDescriptionView.setText(mSteps.get(mListIndex -1).getDescription());
            longDescriptionView.setVisibility(View.VISIBLE);
            ingredientsInfoLayout.setVisibility(View.GONE);
            previousButtonView.setVisibility(View.VISIBLE);

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
        longDescriptionView.setVisibility(View.GONE);
        playerView.setVisibility(View.GONE);
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
                    mIngredientName.setText(a + ". " + ingredient.getName().toUpperCase() + ": " + quantityDouble.toString() + " " + ingredient.getMeasure().toLowerCase());



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

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializeVideoPlayer();
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializeVideoPlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }
}