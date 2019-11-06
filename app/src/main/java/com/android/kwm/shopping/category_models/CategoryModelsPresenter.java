package com.android.kwm.shopping.category_models;

import com.android.kwm.BaseView;
import com.android.kwm.advertiser.store.AdvertiserStoreContract;
import com.android.kwm.advertiser.store.ModelItemRowView;
import com.android.kwm.data.modelItems.ModelItemsRepository;
import com.android.kwm.model.ModelItem;

import java.util.ArrayList;

public class CategoryModelsPresenter implements AdvertiserStoreContract.Presenter {

    private final CategoryModels mView;
    private final ModelItemsRepository mModelItemRepository;

    private ArrayList<ModelItem> mModelItems = new ArrayList<>();

    public CategoryModelsPresenter(CategoryModels view, ModelItemsRepository modelItemRepository) {
        mView = view;
        mModelItemRepository = modelItemRepository;
    }

    @Override
    public int getStoreItemsListCount() {
        return mModelItems.size();
    }

    @Override
    public void onBindStoreItemRowViewAtPosition(int position, ModelItemRowView holder) {
        ModelItem modelItem = mModelItems.get(position);
        if (modelItem.isActive()) {
            holder.setModelItemName(modelItem.getName());
            holder.setModelCategoryName(modelItem.getCategory().getName());
            holder.setModelItemSwitchActiveInaActive(modelItem.isActive());
        }
    }

    @Override
    public void bindStoreItems(ArrayList<ModelItem> modelItems) {
        mModelItems = modelItems;
    }

    @Override
    public void retrieveStoreItems(ModelItemsRepository.RetrieveModelItemsCallback callback) {

    }

    @Override
    public void retrieveCategoryModelsItems(String categoryId, ModelItemsRepository.RetrieveModelItemsCallback callback) {
        mModelItemRepository.retrieveModelItemsByCategory(categoryId, callback);
    }

    @Override
    public void onModelItemClicked(int position) {
        mView.goToModelItemDetailsScreen(mModelItems.get(position));
    }

    @Override
    public void start() {

    }
}
