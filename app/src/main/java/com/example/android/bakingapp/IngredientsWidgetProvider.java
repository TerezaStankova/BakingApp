package com.example.android.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.ui.MainActivity;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,ArrayList<Ingredient> mIngredients, String name,
                                int appWidgetId) {

        Log.v("widget Updated", "This name" + name);
        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_provider);
        views.setTextViewText(R.id.appwidget_text, name);

        if (mIngredients != null) {
            views.removeAllViews (R.id.ingredients_widget_linear_layout);
            int a = 1;
            for (final Ingredient ingredient : mIngredients) {
                if (ingredient != null) {

                    RemoteViews mIngredientItem = new RemoteViews(context.getPackageName(), R.layout.widget_ingredient_item);

                    Double quantityDouble = ingredient.getQuantity();
                    mIngredientItem.setTextViewText(R.id.ingredient_name_widget,a + ". " + ingredient.getName().toUpperCase() + ": " + quantityDouble.toString() + " " + ingredient.getMeasure().toLowerCase());

                    Log.v("widget", "Quantity: " + String.valueOf((ingredient.getQuantity())));
                    Timber.d("Quantity");


                    views.addView(R.id.ingredients_widget_linear_layout, mIngredientItem);
                    a++;

                }
            }
        } else {
            Log.v("widget", "This fragment has a null list of ingredients");
        }

        //Create an Intent to launch MainActivity when clicked
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        //Widgets allow click handlers to only launch pending intents
        views.setOnClickPendingIntent(R.id.ingredients_linear_layout, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateIngredientsWidgets(Context context, AppWidgetManager appWidgetManager,
                                                ArrayList<Ingredient> ingredients, String name, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, ingredients, name, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //Start the intent service update widget action, the service takes care of updating the widgets UI
        IngredientsService.startActionUpdatePlantWidgets(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

