package com.android.kwm.advertiser.store;

import com.android.kwm.data.modelItems.ModelItemsRepository;
import com.android.kwm.model.Category;
import com.android.kwm.model.ModelItem;

import java.util.ArrayList;

public class AdvertiserStorePresenter implements AdvertiserStoreContract.Presenter {

    private final AdvertiserStoreContract.View mView;
    private ArrayList<ModelItem> mStoreItems = new ArrayList<>();
    private final ModelItemsRepository mModelItemsRepository;

    public AdvertiserStorePresenter(AdvertiserStoreContract.View view, ModelItemsRepository modelItemsRepository) {
        mView = view;
        mModelItemsRepository = modelItemsRepository;
        mView.setPresenter(this);
    }


    @Override
    public void start() {

    }

    @Override
    public int getStoreItemsListCount() {
        return mStoreItems.size();
    }

    @Override
    public void onBindStoreItemRowViewAtPosition(int position, ModelItemRowView holder) {
        ModelItem modelItem = mStoreItems.get(position);
        holder.setModelItemName(modelItem.getName());
        holder.setModelCategoryName(modelItem.getCategory().getName());
        holder.setModelItemSwitchActiveInaActive(modelItem.isActive());
        holder.setCategoryViewVisibility(true);
        holder.setShopNameAndPriceVisibility(false);
        holder.setActiveButtonVisibility(true);
    }

    @Override
    public void bindStoreItems(ArrayList<ModelItem> storeItems) {
        mStoreItems = storeItems;
    }

    @Override
    public void retrieveStoreItems(ModelItemsRepository.RetrieveModelItemsCallback callback) {
        mModelItemsRepository.retrieveModelItemsByAdvertiser(callback);
    }

    @Override
    public void retrieveCategoryModelsItems(String categoryId, ModelItemsRepository.RetrieveModelItemsCallback callback) {

    }

    @Override
    public void onModelItemClicked(int position) {

    }

    @Override
    public void applyPriceFilterOnCategoryModels(Category category, double minPriceFilterValue, double maxPriceFilterValue, ModelItemsRepository.RetrieveModelItemsCallback callback) {

    }
}
