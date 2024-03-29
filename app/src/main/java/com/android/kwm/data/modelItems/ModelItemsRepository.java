package com.android.kwm.data.modelItems;

import com.android.kwm.model.Category;
import com.android.kwm.model.ModelItem;

import java.util.ArrayList;

public interface ModelItemsRepository {

    interface ModelItemsInsertionCallback {
        void onSuccessfullyAddingModelItem();

        void onAddingModelItemFailed(String errmsg);
    }

    interface RetrieveModelItemsCallback {
        void onModelItemsRetrieved(ArrayList<ModelItem> modelItems);

        void onModelItemsRetrievedFailed(String errmsg);
    }

    void addNewModelItem(ModelItem modelItem, ModelItemsInsertionCallback callback);

    void retrieveModelItemsByAdvertiser(RetrieveModelItemsCallback callback);

    void retrieveModelItemsByCategory(String categoryId, ModelItemsRepository.RetrieveModelItemsCallback callback);

    void retrieveFilteredModelItemsByPrice(Category category, double minPrice, double maxPricem, RetrieveModelItemsCallback callback);

    void updateModelNumberOfViews(String modelId, int currentViews);
}
