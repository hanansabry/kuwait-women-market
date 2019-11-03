package com.android.kwm.data.categories;

import com.android.kwm.model.Category;

import java.util.ArrayList;

public interface CategoriesRepository {

    interface CategoriesCallback {
        void onCategoriesRetrieved(ArrayList<Category> categories);

        void onCategoriesRetrievedFailed(String errmsg);
    }

    void retrieveCategories(CategoriesCallback callback);
}
