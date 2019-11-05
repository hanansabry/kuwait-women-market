package com.android.kwm.shopping.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.kwm.Injection;
import com.android.kwm.R;
import com.android.kwm.data.categories.CategoriesRepository;
import com.android.kwm.model.Category;
import com.android.kwm.shopping.category_models.CategoryModels;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements HomeContract.View, CategoriesRepository.CategoriesCallback {

    private HomeContract.Presenter mPresenter;
    private ProgressBar prgoressBar;
    private CategoriesAdapter mCategoriesAdapter;
    private RecyclerView mCategoriesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prgoressBar = findViewById(R.id.progress_bar);

        mPresenter = new HomePresenter(this, Injection.provideCategoriesRepository());
        initializeCategoriesRecylerView();
        mPresenter.retrieveCategories(this);
    }

    private void initializeCategoriesRecylerView() {
        mCategoriesAdapter = new CategoriesAdapter(mPresenter);

        mCategoriesRecyclerView = findViewById(R.id.categories_recylerview);
        mCategoriesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mCategoriesRecyclerView.setAdapter(mCategoriesAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCategoriesRetrieved(ArrayList<Category> categories) {
        prgoressBar.setVisibility(View.INVISIBLE);
        mCategoriesAdapter.bindCategories(categories);
    }

    @Override
    public void onCategoriesRetrievedFailed(String errmsg) {
        prgoressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void openCategoryModels(Category selectedCategory) {
        Intent intent = new Intent(this, CategoryModels.class);
        intent.putExtra(Category.CATEGORY, selectedCategory);
        startActivity(intent);
    }
}
