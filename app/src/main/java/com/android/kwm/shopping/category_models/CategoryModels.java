package com.android.kwm.shopping.category_models;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.kwm.BaseView;
import com.android.kwm.EmptyRecyclerView;
import com.android.kwm.Injection;
import com.android.kwm.R;
import com.android.kwm.advertiser.store.AdvertiserStorePresenter;
import com.android.kwm.advertiser.store.ModelsItemsAdapter;
import com.android.kwm.data.modelItems.ModelItemsRepository;
import com.android.kwm.model.Category;
import com.android.kwm.model.ModelItem;
import com.android.kwm.shopping.itemdetails.ModelItemDetails;

import java.util.ArrayList;

public class CategoryModels extends AppCompatActivity implements BaseView<CategoryModelsPresenter>, ModelItemsRepository.RetrieveModelItemsCallback {

    private View emptyView;
    private EmptyRecyclerView modelsItemsRecyclerView;
    private ModelsItemsAdapter modelsItemsAdapter;
    private CategoryModelsPresenter mPresenter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_models);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Category currentCategory = (Category) getIntent().getExtras().get(Category.CATEGORY);
        setTitle(currentCategory.getName());
        progressBar = findViewById(R.id.progress_bar);

        mPresenter = new CategoryModelsPresenter(this, Injection.provideModelItemsRepository());
        initializeRecyclerView();
        mPresenter.retrieveCategoryModelsItems(currentCategory.getId(), this);
    }

    private void initializeRecyclerView() {
        emptyView = findViewById(R.id.empty_view);
        modelsItemsRecyclerView = findViewById(R.id.store_items_recycler_view);
        modelsItemsAdapter = new ModelsItemsAdapter(mPresenter);

        modelsItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        modelsItemsRecyclerView.setAdapter(modelsItemsAdapter);
    }


    @Override
    public void setPresenter(CategoryModelsPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onModelItemsRetrieved(ArrayList<ModelItem> modelItems) {
        progressBar.setVisibility(View.INVISIBLE);
        modelsItemsAdapter.bindStoreItems(modelItems);
        modelsItemsRecyclerView.setEmptyView(emptyView);
    }

    @Override
    public void onModelItemsRetrievedFailed(String errmsg) {
        progressBar.setVisibility(View.INVISIBLE);
        modelsItemsRecyclerView.setEmptyView(emptyView);
        Toast.makeText(this, "Can't retrieve store items", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.filter){
            Toast.makeText(this, "filter is clicked", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public void goToModelItemDetailsScreen(ModelItem modelItem) {
        Toast.makeText(this, modelItem.getName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ModelItemDetails.class);
        intent.putExtra(ModelItem.MODEL, modelItem);
        startActivity(intent);
    }
}
