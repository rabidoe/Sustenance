package com.enhanced.sustenance.ui.recipe_fragment;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipeObject implements Parcelable {
    private String name;
    private String description;
    private String directions;
    private String notes;
    private String ingredients;
    private String favourite;

    public RecipeObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(directions);
        dest.writeString(notes);
        dest.writeString(ingredients);
        dest.writeString(favourite);
    }

    public RecipeObject(Parcel in) {
        name = in.readString();
        description = in.readString();
        directions = in.readString();
        notes = in.readString();
        ingredients = in.readString();
        favourite = in.readString();
    }

    public static final Parcelable.Creator<RecipeObject> CREATOR =
            new Parcelable.Creator<RecipeObject>() {
                public RecipeObject createFromParcel(Parcel in) {
                    return new RecipeObject(in);
                }

                public RecipeObject[] newArray(int size) {
                    return new RecipeObject[size];
                }
            };
}
