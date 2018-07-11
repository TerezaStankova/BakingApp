package com.example.android.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;

import android.content.Context;
import android.content.Intent;


import com.example.android.bakingapp.model.Ingredient;

import java.util.ArrayList;

public class IngredientsService extends IntentService {

    private static final String NAME = "com.example.android.bakingapp.extra.name";
    private static final String INGREDIENTS = "com.example.android.bakingapp.extra.ingredients";


    private ArrayList<Ingredient> ingredients = new ArrayList<>();

    public IngredientsService() {
        super("IngredientsService");
    }


    public static void startActionAddIngredients(Context context, String name, ArrayList<Ingredient> ingredients) {
        Intent intent = new Intent(context, IngredientsService.class);
        intent.putExtra(NAME, name);
        intent.putParcelableArrayListExtra(INGREDIENTS, ingredients);
        context.startService(intent);
    }

    /**
     * Starts this service to perform UpdateWidgets action with the given parameters. If
     * the service is already performing a task this action will be queued.
     */
    public static void startActionUpdatePlantWidgets(Context context) {
        Intent intent = new Intent(context, IngredientsService.class);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            ArrayList<Ingredient> ingredients = new ArrayList<>();
            ingredients = intent.getParcelableArrayListExtra(INGREDIENTS);
            String name = intent.getStringExtra(NAME);
            handleActionUpdateWidgets(ingredients, name);
        }
    }

    /**
     * Handle action UpdateWidgets in the provided background thread
     */
    private void handleActionUpdateWidgets(ArrayList<Ingredient> ingredients, String name) {
        // Extract the plant details

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidgetProvider.class));
        //Trigger data update
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingredients_widget_linear_layout);
        //Now update all widgets
        IngredientsWidgetProvider.updateIngredientsWidgets(this, appWidgetManager, ingredients, name,appWidgetIds);
    }
}