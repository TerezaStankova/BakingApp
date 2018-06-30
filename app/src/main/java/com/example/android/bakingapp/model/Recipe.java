package com.example.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


//A Parcelable class with a Parcelable object as one of the data field
// The parcelable object has to be the first one in the read and write

public class Recipe implements Parcelable{
    private String name;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private ArrayList<Step> steps = new ArrayList<>();
    private int servings;
    private String image;


    /* No args constructor */
    public Recipe() {
    }

    public Recipe(String name, ArrayList<Ingredient> ingredients, ArrayList<Step> steps, int servings, String image) {
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    public String getName() {
        return name;
    }
    public int getServings() {
        return servings;
    }
    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }
    public ArrayList<Step> getSteps() {
        return steps;
    }
    public String getImage() {
        return image;
    }

    private Recipe(Parcel in){
        name = in.readString();
        in.readTypedList(this.ingredients, Ingredient.CREATOR);
        in.readTypedList(this.steps, Step.CREATOR);
        servings = in.readInt();
        image = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeTypedList(ingredients);
        parcel.writeTypedList(steps);
        parcel.writeInt(servings);
        parcel.writeString(image);
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel parcel) {
            return new Recipe(parcel);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }

    };
}
