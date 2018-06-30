package com.example.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Step implements Parcelable{
    private String shortDescription;
    private String description;
    private String videoURL;


    /* No args constructor */
    public Step() {
    }

    public Step(String shortDescription, String description, String videoURL) {
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
    }

    public String getShortDescription() {
        return shortDescription;
    }
    public String getDescription() {
        return description;
    }
    public String getName() {
        return videoURL;
    }

    private Step(Parcel in){
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(shortDescription);
        parcel.writeString(description);
        parcel.writeString(videoURL);
    }

    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel parcel) {
            return new Step(parcel);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }

    };
}
