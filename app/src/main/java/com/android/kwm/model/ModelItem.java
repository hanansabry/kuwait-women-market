package com.android.kwm.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

public class ModelItem implements Parcelable {

    public static final String MODEL = "MODEL";

    public enum AdvertisingTimeType {
        Month, Day
    }

    private String id;
    private String name;
    private String desc;
    private Category category;
    private double buyingPrice;
    private double sellingPrice;
    private AdvertisingTimeType advertisingTimeType;
    private int advertisingTime;
    private boolean active;
    private ArrayList<String> modelImages;
    private String advertiseStartDate;
    private String advertiseEndDate;
    private Advertiser advertiser;

    public ModelItem() {
    }

    protected ModelItem(Parcel in) {
        id = in.readString();
        name = in.readString();
        desc = in.readString();
        category = in.readParcelable(Category.class.getClassLoader());
        buyingPrice = in.readDouble();
        sellingPrice = in.readDouble();
        advertisingTime = in.readInt();
        active = in.readByte() != 0;
        modelImages = in.createStringArrayList();
        advertiseStartDate = in.readString();
        advertiseEndDate = in.readString();
        advertiser = in.readParcelable(Advertiser.class.getClassLoader());
    }

    public static final Creator<ModelItem> CREATOR = new Creator<ModelItem>() {
        @Override
        public ModelItem createFromParcel(Parcel in) {
            return new ModelItem(in);
        }

        @Override
        public ModelItem[] newArray(int size) {
            return new ModelItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(desc);
        dest.writeParcelable(category, flags);
        dest.writeDouble(buyingPrice);
        dest.writeDouble(sellingPrice);
        dest.writeInt(advertisingTime);
        dest.writeByte((byte) (active ? 1 : 0));
        dest.writeStringList(modelImages);
        dest.writeString(advertiseStartDate);
        dest.writeString(advertiseEndDate);
        dest.writeParcelable(advertiser, flags);
    }

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Exclude
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ArrayList<String> getModelImages() {
        return modelImages;
    }

    public void setModelImages(ArrayList<String> modelImages) {
        this.modelImages = modelImages;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public AdvertisingTimeType getAdvertisingTimeType() {
        return advertisingTimeType;
    }

    public void setAdvertisingTimeType(AdvertisingTimeType advertisingTimeType) {
        this.advertisingTimeType = advertisingTimeType;
    }

    public int getAdvertisingTime() {
        return advertisingTime;
    }

    public void setAdvertisingTime(int advertisingTime) {
        this.advertisingTime = advertisingTime;
    }

    public String getAdvertiseStartDate() {
        return advertiseStartDate;
    }

    public void setAdvertiseStartDate(String advertiseStartDate) {
        this.advertiseStartDate = advertiseStartDate;
    }

    public String getAdvertiseEndDate() {
        return advertiseEndDate;
    }

    public void setAdvertiseEndDate(String advertiseEndDate) {
        this.advertiseEndDate = advertiseEndDate;
    }

    public Advertiser getAdvertiser() {
        return advertiser;
    }

    public void setAdvertiser(Advertiser advertiser) {
        this.advertiser = advertiser;
    }
}
