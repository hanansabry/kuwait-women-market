package com.android.kwm.usecase;

import com.android.kwm.data.advertising_time_options.AdvertisingTimeRepository;
import com.android.kwm.data.categories.CategoriesRepository;
import com.android.kwm.data.modelItems.ModelItemsRepository;
import com.android.kwm.model.ModelItem;

public class AddModelUseCaseHandler {

    private final CategoriesRepository categoriesRepository;
    private final ModelItemsRepository modelItemsRepository;
    private final AdvertisingTimeRepository advertisingTimeRepository;

    public AddModelUseCaseHandler(CategoriesRepository categoriesRepository,
                                  ModelItemsRepository modelItemsRepository,
                                  AdvertisingTimeRepository advertisingTimeRepository) {
        this.categoriesRepository = categoriesRepository;
        this.modelItemsRepository = modelItemsRepository;
        this.advertisingTimeRepository = advertisingTimeRepository;
    }

    public void addNewModelItem(ModelItem modelItem, ModelItemsRepository.ModelItemsInsertionCallback callback) {
        modelItemsRepository.addNewModelItem(modelItem, callback);
    }

    public void retrieveCategories(CategoriesRepository.CategoriesCallback callback) {
        categoriesRepository.retrieveCategories(callback);
    }

    public void retrieveAdvertisingTimeOptions(AdvertisingTimeRepository.AdvertisingTimeCallback callback) {
        advertisingTimeRepository.retrieveAdvertisingTimeOptions(callback);
    }
}
