package com.android.kwm.advertiser.store;

public interface ModelItemRowView {

    void setModelItemName(String modelName);

    void setModelCategoryName(String categoryName);

    void setModelItemSwitchActiveInaActive(boolean active);

    void setSellingShopName(String name);

    void setModelItemPrice(String price);

    void setCategoryViewVisibility(boolean visible);

    void setShopNameAndPriceVisibility(boolean visible);
}
