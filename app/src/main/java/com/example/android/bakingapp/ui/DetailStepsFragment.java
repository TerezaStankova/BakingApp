package com.example.android.bakingapp.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class DetailStepsFragment extends Fragment {

    // Final Strings to store state information
    private static final String STEPS_LIST = "steps";
    private static final String INGREDIENTS_LIST = "ingredients";
    private static final String LIST_INDEX = "list_index";
    private static final String TWO_PANE = "two_pane";
    private static final String PLAYBACK_POSITION = "playbackPosition";
    private static final String CURRENT_WINDOW = "currentWindow";
    private static final String PLAY_WHEN_READY = "playWhenReady";


    // Variables to store a list of recipe's resources and the index of the image that this fragment displays
    private ArrayList<Step> mSteps;
    private ArrayList<Ingredient> mIngredients;
    private int mListIndex;

    //Variables for VideoPlayer
    private String path;
    private SimpleExoPlayer player;

    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady;


    //Boolean for tablet(two pane)/mobile mode
    private boolean TwoPane;

    // Get a reference to the nextButton in the fragment layout
    @BindView(R.id.next_button2)
    private Button nextButtonView;
    @BindView(R.id.previous_button)
    private Button previousButtonView;
    @BindView(R.id.detail_description_layout)
    private LinearLayout textLayout;
    @BindView(R.id.buttons_layout)
    private LinearLayout buttonsLayout;
    @BindView(R.id.ingredients_linear_layout)
    private LinearLayout ingredientsInfoLayout;
    @BindView(R.id.step_long_description)
    private TextView longDescriptionView;

    @BindView(R.id.player_view)
    private PlayerView playerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public DetailStepsFragment() {
    }

    /**
     * Inflates the fragment layout file
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Load the saved state if there is one
        if (savedInstanceState != null) {
            mSteps = savedInstanceState.getParcelableArrayList(STEPS_LIST);
            mIngredients = savedInstanceState.getParcelableArrayList(INGREDIENTS_LIST);
            mListIndex = savedInstanceState.getInt(LIST_INDEX);
            TwoPane = savedInstanceState.getBoolean(TWO_PANE);

            playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION);
            currentWindow = savedInstanceState.getInt(CURRENT_WINDOW);
            playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
        }

        // Inflate the step fragment layout
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);

        // Get a reference to the views in the fragment layout
        ButterKnife.bind(this, rootView);

        // Show ingredient if first option was selected or nothing was selected
        if (mListIndex == 0) {
            setIngredientsView(ingredientsInfoLayout, longDescriptionView, nextButtonView, previousButtonView);

        } else {
            //Show step with detail description instead
            // if steps are not null, show them, otherwise, create a Log statement that indicates that the list was not found
            setStepsView(ingredientsInfoLayout, longDescriptionView, nextButtonView, previousButtonView);

        }

        if (mSteps != null) {
            // Set a click listener on the next button
            nextButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Increment position as long as the index remains < the size of the steps list
                    if (mListIndex < (mSteps.size())) {
                        mListIndex++;
                    }
                    // Set the view accordingly to the next item
                    if (mListIndex == 0) {
                        setIngredientsView(ingredientsInfoLayout, longDescriptionView, nextButtonView, previousButtonView);
                    } else {
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
                    // Set the view accordingly to the previous item
                    if (mListIndex == 0) {
                        setIngredientsView(ingredientsInfoLayout, longDescriptionView, nextButtonView, previousButtonView);
                    } else {
                        setStepsView(ingredientsInfoLayout, longDescriptionView, nextButtonView, previousButtonView);
                    }
                }
            });
        }

        // Return the rootView
        return rootView;
    }


    // Setter methods for keeping track of the steps and ingredients of current recipe
    public void setSteps(ArrayList<Step> steps) {
        mSteps = steps;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        mIngredients = ingredients;
    }

    public void setListIndex(int index) {
        mListIndex = index;
    }

    // Setter method for keeping track of type of the device (mobile/tablet mode)
    public void setTwoPane(boolean twoPane) {
        TwoPane = twoPane;
    }


    private void setStepsView(LinearLayout ingredientsInfoLayout, TextView longDescriptionView, Button nextButtonView, Button previousButtonView) {
        playerView.setVisibility(View.GONE);
        ingredientsInfoLayout.setVisibility(View.GONE);

        if (mSteps != null) {
            //Check if there is a video resource URL
            path = mSteps.get(mListIndex - 1).getVideoURL();

            //Set detail description of the selected step
            longDescriptionView.setText(mSteps.get(mListIndex - 1).getDescription());
            longDescriptionView.setVisibility(View.VISIBLE);

            //Set visibility of previous button
            previousButtonView.setVisibility(View.VISIBLE);

            //If the video URL exists, ends with ".mp4" and the device is connected to Internet, initialize the video player
            if (path.length() > 8 && isConnected()) {
                Timber.d("URL substring: %s", path.substring(path.length() - 4));
                if (path.substring(path.length() - 4).equals(".mp4")) {
                    Timber.d(path);
                    initializeVideoPlayer();
                }
            } else {
                if (!TwoPane) {
                    int orientation = this.getResources().getConfiguration().orientation;
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        // Set detail description to be visible and hide unnecessary UI if there is no video to show (phone, landscape)
                        textLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                        textLayout.setVisibility(View.VISIBLE);
                    }
                }
            }

            //Set nextButton invisible if this is the last step
            if (mListIndex == mSteps.size()) {
                nextButtonView.setVisibility(View.INVISIBLE);
            } else {
                nextButtonView.setVisibility(View.VISIBLE);
            }

        } else {
            Timber.d("This fragment has a null list of steps");
        }

    }

    private void setIngredientsView(LinearLayout ingredientsInfoLayout, TextView longDescriptionView, Button nextButtonView, Button previousButtonView) {

        //Hide playerView, previousButton and view for step description
        playerView.setVisibility(View.GONE);
        longDescriptionView.setVisibility(View.GONE);
        previousButtonView.setVisibility(View.INVISIBLE);

        // Set Layout for ingredients to be visible and hide unnecessary UI if there is no video to show (phone, landscape)
        if (!TwoPane) {
            int orientation = this.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                textLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                textLayout.setVisibility(View.VISIBLE);
            }
        }

        ingredientsInfoLayout.setVisibility(View.VISIBLE);

        //Check if there are already ingredients listed
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
                        String mIngredientsInfo = a + ". " + ingredient.getName().toUpperCase() + ": " + quantityDouble.toString()
                                + " " + ingredient.getMeasure().toLowerCase();
                        mIngredientName.setText(mIngredientsInfo);
                        ingredientsInfoLayout.addView(mIngredientItem);
                        a++;
                    }
                }
            } else {
                Timber.d("This fragment has a null list of ingredients");
            }
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

    private void initializeVideoPlayer() {
        if (path != null && isConnected()) {
            try {
                player = ExoPlayerFactory.newSimpleInstance(
                        new DefaultRenderersFactory(getContext()),
                        new DefaultTrackSelector(), new DefaultLoadControl());

                playerView.setPlayer(player);
                player.setPlayWhenReady(playWhenReady);

                Uri uri = Uri.parse(path);
                MediaSource mediaSource = buildMediaSource(uri);
                player.prepare(mediaSource, true, true);
                player.seekTo(currentWindow, playbackPosition);


                // Hide unnecessary UI if there is a video to show (phone, landscape)
                if (!TwoPane) {
                    int orientation = this.getResources().getConfiguration().orientation;
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        hideSystemUi();
                    }
                }

                playerView.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                Timber.d("Error with Media Player");
            }
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("BakingApp")).
                createMediaSource(uri);
    }


    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        if (!TwoPane) {
            textLayout.setVisibility(View.GONE);

            playerView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LOW_PROFILE |
                            View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

            int scale2 = getActivity().getResources().getDisplayMetrics().widthPixels;
            int scale3 = getContext().getResources().getDisplayMetrics().heightPixels;
            playerView.setPadding(0, 0, 0, 0);

            //The minimum height of buttons, in pixels
            int heightButtonsMin = buttonsLayout.getMinimumHeight();

            //The minimum height the view will try to be, in pixels
            playerView.setMinimumHeight(scale3 - heightButtonsMin);
            playerView.setMinimumWidth(scale2);
        }
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

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList(STEPS_LIST, (ArrayList<Step>) mSteps);
        savedInstanceState.putParcelableArrayList(INGREDIENTS_LIST, (ArrayList<Ingredient>) mIngredients);
        savedInstanceState.putInt(LIST_INDEX, mListIndex);
        savedInstanceState.putBoolean(TWO_PANE, TwoPane);
        if (player != null) {
            savedInstanceState.putInt(CURRENT_WINDOW, player.getCurrentWindowIndex());
            savedInstanceState.putBoolean(PLAY_WHEN_READY, player.getPlayWhenReady());
            savedInstanceState.putLong(PLAYBACK_POSITION, player.getCurrentPosition());
        }
    }
}