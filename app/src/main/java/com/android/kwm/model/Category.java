package com.android.kwm.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {

    public static final String CATEGORY = "category";
    private String id;
    private String name;
    private String image;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    protected Category(Parcel in) {
        id = in.readString();
        name = in.readString();
        image = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(image);
    }
}
