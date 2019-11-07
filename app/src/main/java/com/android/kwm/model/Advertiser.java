package com.android.kwm.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

public class Advertiser implements Parcelable {

    private String id;
    private String name;
    private String userName;
    private String password;
    private String email;
    private String phone;

    public Advertiser() {
    }

    protected Advertiser(Parcel in) {
        id = in.readString();
        name = in.readString();
        userName = in.readString();
        password = in.readString();
        email = in.readString();
        phone = in.readString();
    }

    public static final Creator<Advertiser> CREATOR = new Creator<Advertiser>() {
        @Override
        public Advertiser createFromParcel(Parcel in) {
            return new Advertiser(in);
        }

        @Override
        public Advertiser[] newArray(int size) {
            return new Advertiser[size];
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(userName);
        dest.writeString(password);
        dest.writeString(email);
        dest.writeString(phone);
    }
}
