package com.android.kwm.model;

import android.net.Uri;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

public class ModelItem {

    public enum AdvertisingTimeType {
        Month, Day
    }

    private String id;
    private String name;
    private String desc;
    private String category;
    private double buyingPrice;
    private double sellingPrice;
    private AdvertisingTimeType advertisingTimeType;
    private int advertisingTime;
    private boolean active;
    private ArrayList<String> modelImages;
    private String advertiseStartDate;
    private String advertiseEndDate;
    private String advertiserId;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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

    public String getAdvertiserId() {
        return advertiserId;
    }

    public void setAdvertiserId(String advertiserId) {
        this.advertiserId = advertiserId;
    }
}
