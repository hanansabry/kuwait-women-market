package com.android.kwm.advertiser.store;

import com.android.kwm.BasePresenter;
import com.android.kwm.BaseView;
import com.android.kwm.data.modelItems.ModelItemsRepository;
import com.android.kwm.model.ModelItem;

import java.util.ArrayList;

public interface AdvertiserStoreContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        int getStoreItemsListCount();

        void onBindStoreItemRowViewAtPosition(int position, ModelItemRowView holder);

        void bindStoreItems(ArrayList<ModelItem> storeItems);

        void retrieveStoreItems(ModelItemsRepository.RetrieveModelItemsCallback callback);

        void retrieveCategoryModelsItems(String categoryId, ModelItemsRepository.RetrieveModelItemsCallback callback);

        void onModelItemClicked(int position);
    }
}
