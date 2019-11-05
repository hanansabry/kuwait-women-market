package com.android.kwm.shopping.main;

import com.android.kwm.BasePresenter;
import com.android.kwm.BaseView;
import com.android.kwm.advertiser.store.ModelItemRowView;
import com.android.kwm.data.categories.CategoriesRepository;
import com.android.kwm.model.Category;
import com.android.kwm.model.ModelItem;

import java.util.ArrayList;

public interface HomeContract {

    interface View extends BaseView<Presenter> {

        void openCategoryModels(Category selectedCategory);
    }

    interface Presenter extends BasePresenter {

        int getCategoriesSize();

        void onBindCategoryItemRowViewAtPosition(int position, CategoryItemRowView holder);

        void bindCategories(ArrayList<Category> categories);

        void retrieveCategories(CategoriesRepository.CategoriesCallback callback);

        void onCategoryClicked(int position);
    }
}
