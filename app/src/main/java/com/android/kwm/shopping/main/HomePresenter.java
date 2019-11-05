package com.android.kwm.shopping.main;

import com.android.kwm.advertiser.store.ModelItemRowView;
import com.android.kwm.data.categories.CategoriesRepository;
import com.android.kwm.model.Category;
import com.android.kwm.model.ModelItem;

import java.util.ArrayList;

public class HomePresenter implements HomeContract.Presenter {

    private final HomeContract.View mView;
    private final CategoriesRepository mCategoriesRepository;
    private ArrayList<Category> mCategories = new ArrayList<>();

    public HomePresenter(HomeContract.View view, CategoriesRepository categoriesRepository) {
        mView = view;
        mCategoriesRepository = categoriesRepository;
        mView.setPresenter(this);
    }


    @Override
    public int getCategoriesSize() {
        return mCategories.size();
    }

    @Override
    public void onBindCategoryItemRowViewAtPosition(int position, CategoryItemRowView holder) {
        Category category = mCategories.get(position);
        holder.setCategoryName(category.getName());
        holder.setCategoryImage(category.getImage());
    }

    @Override
    public void bindCategories(ArrayList<Category> categories) {
        mCategories = categories;
    }

    @Override
    public void retrieveCategories(CategoriesRepository.CategoriesCallback callback) {
        mCategoriesRepository.retrieveCategories(callback);
    }

    @Override
    public void onCategoryClicked(int position) {
        Category selectedCategory = mCategories.get(position);
        mView.openCategoryModels(selectedCategory);
    }

    @Override
    public void start() {

    }
}
