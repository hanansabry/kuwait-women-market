package com.android.kwm.advertiser.store;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.kwm.EmptyRecyclerView;
import com.android.kwm.Injection;
import com.android.kwm.R;
import com.android.kwm.advertiser.addmodel.AddModel;
import com.android.kwm.data.modelItems.ModelItemsRepository;
import com.android.kwm.intro.IntroActivity;
import com.android.kwm.model.ModelItem;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class AdvertiserStoreActivity extends AppCompatActivity implements AdvertiserStoreContract.View, ModelItemsRepository.RetrieveModelItemsCallback {

    private AdvertiserStoreContract.Presenter mPresenter;
    private StoreItemsAdapter storeItemsAdapter;
    private ProgressBar progressBar;
    private EmptyRecyclerView storeItemsRecyclerView;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertiser_store);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPresenter = new AdvertiserStorePresenter(this, Injection.provideModelItemsRepository());
        progressBar = findViewById(R.id.progress_bar);

        initializeRecylerView();
        mPresenter.retrieveStoreItems(this);
    }

    private void initializeRecylerView() {
        emptyView = findViewById(R.id.empty_view);
        storeItemsRecyclerView = findViewById(R.id.store_items_recycler_view);
        storeItemsAdapter = new StoreItemsAdapter(mPresenter);

        storeItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        storeItemsRecyclerView.setAdapter(storeItemsAdapter);
    }


    public void onAddModelItemFabClicked(View view) {
        Toast.makeText(this, "Adding new model item", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, AddModel.class));
    }

    @Override
    public void setPresenter(AdvertiserStoreContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent introIntent = new Intent(this, IntroActivity.class);
            introIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(introIntent);
        }
        return true;
    }

    @Override
    public void onModelItemsRetrieved(ArrayList<ModelItem> modelItems) {
        progressBar.setVisibility(View.INVISIBLE);
        storeItemsAdapter.bindStoreItems(modelItems);
        storeItemsRecyclerView.setEmptyView(emptyView);
    }

    @Override
    public void onModelItemsRetrievedFailed(String errmsg) {
        progressBar.setVisibility(View.INVISIBLE);
        storeItemsRecyclerView.setEmptyView(emptyView);
        Toast.makeText(this, "Can't retrieve store items", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.signout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
