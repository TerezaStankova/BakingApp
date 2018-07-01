package com.example.android.bakingapp.utilities;

import android.content.Context;
import android.graphics.Movie;
import android.util.Log;

import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class JSONUtils {

    //Parse Recipe data from Json
    public static ArrayList<Recipe> getRecipeDataFromJson(Context context, String recipeJsonStr)
            throws JSONException {

        /* Movie information. Each movie's info is an element of the "results" array */
        final String OWM_ID = "id";
        final String OWN_NAME = "name";

        final String OWM_INGREDIENTS = "ingredients";
        final String OWM_QUANTITY = "quantity";
        final String OWM_MEASURE = "measure";
        final String OWN_INGREDIENT = "ingredient";

        final String OWN_STEPS = "steps";
        //CAN BE WRONG - BETTER TO UPLOAD NEW ID's
        final String OWM_STEP_ID = "id";
        final String OWN_SHORT_DESCRIPTION = "shortDescription";
        final String OWN_DESCRIPTION = "description";
        final String OWN_VIDEO_URL = "videoURL";
        final String OWN_THUMBNAIL_URL = "thumbnailURL";
        final String OWN_SERVINGS = "servings";
        final String OWN_IMAGE = "image";

        /* Movie array to hold each movie's info */
        // Recipe[] parsedRecipeData;

        // Gson gson = new Gson();
        //Type recipeListType = new TypeToken<ArrayList<Recipe>>(){}.getType();
        //ArrayList<Recipe> recipeList = gson.fromJson(recipeJsonStr, recipeListType);

        ArrayList<Recipe> recipeList = new ArrayList();

        JSONArray recipeJson = new JSONArray(recipeJsonStr);


        for (int a = 0; a < recipeJson.length(); a++) {
            String name;
            ArrayList<Ingredient> ingredientsList = new ArrayList<>();
            ArrayList<Step> stepList = new ArrayList<>();
            int servings;
            String image;

            JSONObject recipeInfo = recipeJson.getJSONObject(a);

            name = recipeInfo.getString(OWN_NAME);

            JSONArray ingredientsArray = recipeInfo.getJSONArray(OWM_INGREDIENTS);

            for (int i = 0; i < ingredientsArray.length(); i++) {
                double quantity;
                String measure;
                String ingredient;

                JSONObject ingredientInfo = ingredientsArray.getJSONObject(i);

                quantity = ingredientInfo.getDouble(OWM_QUANTITY);
                measure = ingredientInfo.getString(OWM_MEASURE);
                ingredient = ingredientInfo.getString(OWN_INGREDIENT);

                ingredientsList.add(new Ingredient(quantity, measure, ingredient));
            }

            JSONArray stepsArray = recipeInfo.getJSONArray(OWN_STEPS);

            for (int i = 0; i < stepsArray.length(); i++) {
                int stepId;
                String shortDescription;
                String description;
                String videoURL;

                JSONObject stepInfo = stepsArray.getJSONObject(i);

                stepId = i;
                shortDescription = stepInfo.getString(OWN_SHORT_DESCRIPTION);
                description = stepInfo.getString(OWN_DESCRIPTION);
                if (stepInfo.getString(OWN_VIDEO_URL) != null) {
                    videoURL = stepInfo.getString(OWN_VIDEO_URL);
                } else {
                    videoURL = stepInfo.getString(OWN_THUMBNAIL_URL);
                }

                stepList.add(new Step(stepId, shortDescription, description, videoURL));
            }

            servings = recipeInfo.getInt(OWN_SERVINGS);
            image = recipeInfo.getString(OWN_IMAGE);

            recipeList.add(new Recipe(name, ingredientsList, stepList, servings, image));
        }

        return recipeList;

    }
}







                /* Get the JSON object representing the movie
                JSONObject movieInfo = ingredientsArray.getJSONObject(i);

                //checkout GSON library for JSON parsing. Checkout this link : https://github.com/google/gson

                title = movieInfo.getString(OWN_TITLE);
                originalTitle = movieInfo.getString(OWM_ORIGINAL_TITLE);
                voteAverage = movieInfo.getDouble(OWM_VOTE_AVERAGE);
                releaseDate = movieInfo.getString(OWM_RELEASE_DATE);
                plot = movieInfo.getString(OWM_PLOT);
                poster_path = movieInfo.getString(OWM_POSTER);
                poster =  "http://image.tmdb.org/t/p/w185" + poster_path;
                id = movieInfo.getInt(OWM_ID);

                parsedMovieData[i] = new Movie(title, originalTitle, releaseDate, voteAverage, poster, plot, id);


















            for (int i = 0; i < recipeList.size(); i++ ) {
                Timber.d("JSON recipe:" +recipeList.get(i).getName());
                Log.d("JSON", "JSON recipe:" + recipeList.get(i).getName());
            }

            return recipeList;
        }


}


            // parsedRecipeData = gson.fromJson(recipeJson, Recipe[].class);

            /*JSONObject recipeJson = new JSONObject(recipeJsonStr);
            JSONArray ingredientsArray = movieJson.getJSONArray(OWM_RESULTS);

            parsedMovieData = new Recipe[ingredientsArray.length()];

            for (int i = 0; i < ingredientsArray.length(); i++) {
                String title;
                String originalTitle;
                double voteAverage;

                String releaseDate;
                String plot;
                String poster_path;
                String poster;
                int id;

                /* Get the JSON object representing the movie
                JSONObject movieInfo = ingredientsArray.getJSONObject(i);

                //checkout GSON library for JSON parsing. Checkout this link : https://github.com/google/gson

                title = movieInfo.getString(OWN_TITLE);
                originalTitle = movieInfo.getString(OWM_ORIGINAL_TITLE);
                voteAverage = movieInfo.getDouble(OWM_VOTE_AVERAGE);
                releaseDate = movieInfo.getString(OWM_RELEASE_DATE);
                plot = movieInfo.getString(OWM_PLOT);
                poster_path = movieInfo.getString(OWM_POSTER);
                poster =  "http://image.tmdb.org/t/p/w185" + poster_path;
                id = movieInfo.getInt(OWM_ID);

                parsedMovieData[i] = new Movie(title, originalTitle, releaseDate, voteAverage, poster, plot, id);
            }

            return parsedMovieData;
        }

        //Parse data from Json for trailers
        public static Trailer[] getTrailerDataFromJson(Context context, String trailerJsonStr)
                throws JSONException {

            /* Trailer information. Each trailer's info is an element of the "results" array
            /*
            final String OWM_RESULTS = "results";
           /* final String OWN_NAME = "name";
            final String OWM_TYPE = "type";
            final String OWM_KEY = "key";*/

            /* Trailer array to hold each trailer's info
            Trailer[] parsedTrailerData;

            JSONObject trailerJson = new JSONObject(trailerJsonStr);
            JSONArray trailerArray = trailerJson.getJSONArray(OWM_RESULTS);

            int a = 0;

            //Get number of trailers
            for (int i = 0; i < trailerArray.length(); i++) {
                String type;

                /* Get the JSON object representing the trailer's type
                JSONObject trailerInfo = trailerArray.getJSONObject(i);
                type = trailerInfo.getString(OWM_TYPE);
                Log.d("Type", " is " + type);

                /*Get number of trailers in videos
                if (type.equals("Trailer")){
                    a++;
                }
            }

            if (a > 0) {

                parsedTrailerData = new Trailer[a];

                int b = 0;
                for (int i = 0; b < parsedTrailerData.length; i++) {
                    Log.d("ParsedData length", " is " + parsedTrailerData.length);
                    String name;
                    String key;
                    String whole_key;
                    String type;

                    /* Get the JSON object representing the trailer */
                  /*  JSONObject trailerInfo = trailerArray.getJSONObject(i);
                      type = trailerInfo.getString(OWM_TYPE);

                    if (type.equals("Trailer")){
                        name = trailerInfo.getString(OWN_NAME);
                        Log.d("Name", " is " + i + name);
                        key = trailerInfo.getString(OWM_KEY);
                        whole_key =  "https://m.youtube.com/watch?v=" + key;
                        parsedTrailerData[b] = new Trailer(name, whole_key);
                        b++;
                    }
                    else {
                        name = trailerInfo.getString(OWN_NAME);
                        Log.d("Type is not Trailer", " is " + type + name);
                    }
                }
            } else {parsedTrailerData = null;}

            return parsedTrailerData;
        }

        */