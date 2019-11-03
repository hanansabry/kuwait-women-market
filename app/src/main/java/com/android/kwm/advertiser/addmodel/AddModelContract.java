package com.android.kwm.advertiser.addmodel;

import com.android.kwm.BasePresenter;
import com.android.kwm.BaseView;
import com.android.kwm.data.advertising_time_options.AdvertisingTimeRepository;
import com.android.kwm.data.categories.CategoriesRepository;
import com.android.kwm.data.modelItems.ModelItemsRepository;
import com.android.kwm.model.ModelItem;

public interface AddModelContract {

    interface View extends BaseView<Presenter> {

        void setSelectCategoryErrorMessage();

        void setModelNameErrorMessage();

        void setDescErrorMessage();

        void setBuyingPriceErrorMessage();

        void setSellingPriceErrorMessage();

        void setAdvertisingTimeErrorMessage();

        void setModelImagesErrorMessage();
    }

    interface Presenter extends BasePresenter {

        void addNewModelItem(ModelItem modelItem, ModelItemsRepository.ModelItemsInsertionCallback callback);

        void retrieveCategories(CategoriesRepository.CategoriesCallback callback);

        void retrieveAdvertisingTimeOptions(AdvertisingTimeRepository.AdvertisingTimeCallback callback);
    }
}
