package com.example.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable{
    private double quantity;
    private String measure;
    private String ingredientName;

    /* No args constructor */
    public Ingredient() {
    }

    public Ingredient(double quantity, String measure, String name) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredientName = name;
    }

    public double getQuantity() {
        return quantity;
    }
    public String getMeasure() {
        return measure;
    }
    public String getName() {
        return ingredientName;
    }

    private Ingredient(Parcel in){
        quantity = in.readDouble();
        measure = in.readString();
        ingredientName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredientName);
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel parcel) {
            return new Ingredient(parcel);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }

    };
}
