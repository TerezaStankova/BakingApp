package com.example.android.bakingapp.ui;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Movie;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.MainActivity;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private ArrayList<Recipe> mRecipeData = new ArrayList();

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final RecipeAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe singleRecipe);
    }

    /**
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public RecipeAdapter(RecipeAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    /**
     * Cache of the children views for a movie list item.
     */
    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_recipe_title) TextView mTitleTextView;
        @BindView(R.id.tv_servings) TextView mServingsTextView;
        @BindView(R.id.tv_image) ImageView mPosterImageView;


        public RecipeAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Recipe singleRecipe = mRecipeData.get(adapterPosition);
            mClickHandler.onClick(singleRecipe);
        }
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @return A new ForecastAdapterViewHolder that holds the View for each list item
     */
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_card;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new RecipeAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the movie
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param recipeAdapterViewHolder The ViewHolder which should be updated to represent the
     *                               contents of the item at the given position in the data set.
     * @param position               The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder recipeAdapterViewHolder, int position) {
        Recipe singleRecipe = mRecipeData.get(position);

        Timber.d("recipe %s", singleRecipe.getName());
        Log.d("onBind", "recipe " + singleRecipe.getName());
        recipeAdapterViewHolder.mTitleTextView.setText(singleRecipe.getName());
        recipeAdapterViewHolder.mServingsTextView.setText(singleRecipe.getServings() + " servings");

        ArrayList<Step> steps = singleRecipe.getSteps();
        Step lastStep = steps.get(steps.size() - 1);
        Log.d("onBind Step", "description " + lastStep.getVideoURL() + lastStep.getShortDescription());
        String path = lastStep.getVideoURL();

        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path,
                MediaStore.Images.Thumbnails.MINI_KIND);

        /*
        Bitmap frame = null;

        try {
            frame = retriveVideoFrameFromVideo(path);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }*/

        Uri thumbo;
        if (thumb != null){
            Log.d("onBind", "ThumbNotNull ");
        thumbo = getImageUri(recipeAdapterViewHolder.itemView.getContext(), thumb);}else {
           thumbo = null;
            Log.d("onBind", "ThumbNull ");
        }
/*
        Uri thumbo;
        if (frame != null){
            Log.d("onBind", "ThumbNotNull ");
            thumbo = getImageUri(recipeAdapterViewHolder.itemView.getContext(), frame);}else {
            thumbo = null;
            Log.d("onBind", "ThumbNull ");
        }*/

        //lastStep.getVideoURL()
        //(singleRecipe.getImage().isEmpty())

        if (lastStep.getVideoURL().isEmpty()) {
            if (thumbo != null) {
                Picasso.with(recipeAdapterViewHolder.itemView.getContext())
                        .load(thumbo)
                        .resize(15, 15)
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(recipeAdapterViewHolder.mPosterImageView);
            } else {
                recipeAdapterViewHolder.mPosterImageView.setImageResource(R.mipmap.ic_launcher);}
        } else{
            Picasso.with(recipeAdapterViewHolder.itemView.getContext())
                    .load(lastStep.getVideoURL())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(recipeAdapterViewHolder.mPosterImageView);
        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (null == mRecipeData) return 0;
        return mRecipeData.size();
    }


    public void setRecipeData(ArrayList<Recipe> recipeData) {
        mRecipeData = recipeData;
        notifyDataSetChanged();
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath)throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)"+ e.getMessage());
        }
        finally
        {
            if (mediaMetadataRetriever != null)
            {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

}

