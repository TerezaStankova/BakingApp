package com.example.android.bakingapp.ui;

import android.content.Context;
import android.net.Uri;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

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
     * Cache of the children views for a recipe list item.
     */
    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_recipe_title) TextView mTitleTextView;
        @BindView(R.id.tv_servings) TextView mServingsTextView;
        @BindView(R.id.tv_image) ImageView mPosterImageView;


        RecipeAdapterViewHolder(View view) {
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

        recipeAdapterViewHolder.mTitleTextView.setText(singleRecipe.getName().toUpperCase());
        String servingsText = singleRecipe.getServings() + " servings";
        recipeAdapterViewHolder.mServingsTextView.setText(servingsText);

        ArrayList<Step> steps = singleRecipe.getSteps();
        Step lastStep = steps.get(steps.size() - 1);

        Uri uri = Uri.parse(lastStep.getVideoURL());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.ic_launcher_foreground);
        requestOptions.error(R.mipmap.ic_launcher_foreground);

        if (singleRecipe.getImage().isEmpty()) {
            if (uri != null) {
                Glide.with(recipeAdapterViewHolder.itemView.getContext())
                        .setDefaultRequestOptions(requestOptions)
                        .load(uri)
                        .into(recipeAdapterViewHolder.mPosterImageView);

            } else {
                recipeAdapterViewHolder.mPosterImageView.setImageResource(R.mipmap.ic_launcher);}
        } else{
            Glide.with(recipeAdapterViewHolder.itemView.getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(singleRecipe.getImage())
                    .into(recipeAdapterViewHolder.mPosterImageView);
        }
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
}

