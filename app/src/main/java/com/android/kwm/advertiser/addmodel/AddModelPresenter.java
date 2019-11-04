package com.android.kwm.advertiser.addmodel;

import com.android.kwm.data.advertising_time_options.AdvertisingTimeRepository;
import com.android.kwm.data.categories.CategoriesRepository;
import com.android.kwm.data.modelItems.ModelItemsRepository;
import com.android.kwm.model.ModelItem;
import com.android.kwm.usecase.AddModelUseCaseHandler;

public class AddModelPresenter implements AddModelContract.Presenter {

    private final AddModelContract.View mView;
    private final AddModelUseCaseHandler mAddModelUseCaseHandler;

    public AddModelPresenter(AddModelContract.View view, AddModelUseCaseHandler addModelUseCaseHandler) {
        this.mView = view;
        mAddModelUseCaseHandler = addModelUseCaseHandler;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    private boolean validateModelData(ModelItem modelItem) {
        boolean isValid = true;
        if (modelItem.getCategory() == null) {
            isValid = false;
            mView.setSelectCategoryErrorMessage();
        }
        if (modelItem.getName() == null || modelItem.getName().isEmpty()) {
            isValid = false;
            mView.setModelNameErrorMessage();
        }
        if (modelItem.getDesc() == null || modelItem.getDesc().isEmpty()) {
            isValid = false;
            mView.setDescErrorMessage();
        }
        if (modelItem.getBuyingPrice() == 0) {
            isValid = false;
            mView.setBuyingPriceErrorMessage();
        }
        if (modelItem.getSellingPrice() == 0) {
            isValid = false;
            mView.setSellingPriceErrorMessage();
        }
        if (modelItem.getAdvertisingTimeType() == null) {
            isValid = false;
            mView.setAdvertisingTimeErrorMessage();
        }
        if (modelItem.getAdvertisingTime() <= 0) {
            isValid = false;
            mView.setAdvertisingTimeErrorMessage();
        }
        if (modelItem.getModelImages() == null || modelItem.getModelImages().size() == 0) {
            isValid = false;
            mView.setModelImagesErrorMessage();
        }
        return isValid;
    }

    @Override
    public void addNewModelItem(ModelItem modelItem, ModelItemsRepository.ModelItemsInsertionCallback callback) {
        if (validateModelData(modelItem)) {
            mAddModelUseCaseHandler.addNewModelItem(modelItem, callback);
        }
    }

    @Override
    public void retrieveCategories(CategoriesRepository.CategoriesCallback callback) {
        mAddModelUseCaseHandler.retrieveCategories(callback);
    }

    @Override
    public void retrieveAdvertisingTimeOptions(AdvertisingTimeRepository.AdvertisingTimeCallback callback) {
        mAddModelUseCaseHandler.retrieveAdvertisingTimeOptions(callback);
    }
}
