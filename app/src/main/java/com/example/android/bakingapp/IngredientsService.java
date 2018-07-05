package com.example.android.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;

import android.content.Context;
import android.content.Intent;


import com.example.android.bakingapp.model.Ingredient;

import java.util.ArrayList;

public class IngredientsService extends IntentService {

    public static final String ACTION_WATER_PLANT = "com.example.android.mygarden.action.water_plant";
    public static final String ACTION_UPDATE_PLANT_WIDGETS = "com.example.android.mygarden.action.update_plant_widgets";

    public static final String NAME = "com.example.android.bakingapp.extra.name";
    public static final String INGREDIENTS = "com.example.android.bakingapp.extra.ingredients";


    private ArrayList<Ingredient> ingredients = new ArrayList<>();

    public IngredientsService() {
        super("IngredientsService");
    }

    /**
     * Starts this service to perform WaterPlant action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionAddIngredients(Context context, String name, ArrayList<Ingredient> ingredients) {
        Intent intent = new Intent(context, IngredientsService.class);
        intent.setAction(ACTION_WATER_PLANT);
        intent.putExtra(NAME, name);
        intent.putParcelableArrayListExtra(INGREDIENTS, ingredients);
        context.startService(intent);
    }

    /**
     * Starts this service to perform UpdatePlantWidgets action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionUpdatePlantWidgets(Context context) {
        Intent intent = new Intent(context, IngredientsService.class);
        intent.setAction(ACTION_UPDATE_PLANT_WIDGETS);
        context.startService(intent);
    }

    /**
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            ArrayList<Ingredient> ingredients = new ArrayList<>();
            ingredients = intent.getParcelableArrayListExtra(INGREDIENTS);
            String name = intent.getStringExtra(NAME);
                handleActionUpdatePlantWidgets(ingredients, name);
        }
    }

    /**
     * Handle action WaterPlant in the provided background thread with the provided
     * parameters.
     */
    private void handleActionWaterPlant(long plantId) {
    }


    /**
     * Handle action UpdatePlantWidgets in the provided background thread
     */
    private void handleActionUpdatePlantWidgets(ArrayList<Ingredient> ingredients, String name) {
        // Extract the plant details

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidgetProvider.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingredients_widget_linear_layout);
        //Now update all widgets
        IngredientsWidgetProvider.updateIngredientsWidgets(this, appWidgetManager, ingredients, name,appWidgetIds);
    }
}