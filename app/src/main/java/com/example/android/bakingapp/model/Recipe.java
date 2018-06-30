package com.example.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

@Parcel
public class Recipe {
    private String name;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Step> steps;
    private int servings;
    private String image;


    /* No args constructor */
    public Recipe() {
    }

    public Recipe(String name, int servings, String image) {
        this.name = name;
        this.servings = servings;
        this.image = image;
    }

    public String getName() {
        return name;
    }
    public int getServings() {
        return servings;
    }
    public String getImage() {
        return image;
    }

    private Recipe(Parcel in){
        name = in.readString();
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
