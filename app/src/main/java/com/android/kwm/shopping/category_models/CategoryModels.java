package com.android.kwm.shopping.category_models;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.kwm.BaseView;
import com.android.kwm.EmptyRecyclerView;
import com.android.kwm.Injection;
import com.android.kwm.R;
import com.android.kwm.advertiser.store.ModelsItemsAdapter;
import com.android.kwm.data.modelItems.ModelItemsRepository;
import com.android.kwm.model.Category;
import com.android.kwm.model.ModelItem;
import com.android.kwm.shopping.itemdetails.ModelItemDetails;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class CategoryModels extends AppCompatActivity implements BaseView<CategoryModelsPresenter>, ModelItemsRepository.RetrieveModelItemsCallback {

    private View emptyView;
    private EmptyRecyclerView modelsItemsRecyclerView;
    private ModelsItemsAdapter modelsItemsAdapter;
    private CategoryModelsPresenter mPresenter;
    private ProgressBar progressBar;
    private BottomSheetDialog filterBottomSheetDialog;
    private CheckBox priceFilterCheckbox;
    private ImageView increaseMinPriceValue, decreaseMinPriceValue, increaseMaxPriceValue, decreaseMaxPriceValue;
    private EditText minPriceEditText, maxPriceEditText;
    private Button doneButton;
    private double minPriceFilterValue, maxPriceFilterValue;
    private Category currentCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_models);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentCategory = (Category) getIntent().getExtras().get(Category.CATEGORY);
        setTitle(currentCategory.getName());
        progressBar = findViewById(R.id.progress_bar);

        mPresenter = new CategoryModelsPresenter(this, Injection.provideModelItemsRepository());
        initializeRecyclerView();
        initializeBottomSheetDialog();
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
            filterBottomSheetDialog.show();
        }
        return true;
    }

    public void goToModelItemDetailsScreen(ModelItem modelItem) {
        Toast.makeText(this, modelItem.getName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ModelItemDetails.class);
        intent.putExtra(ModelItem.MODEL, modelItem);
        startActivity(intent);
    }

    private void initializeBottomSheetDialog() {
        final View dialogView = getLayoutInflater().inflate(R.layout.filter_bottom_sheet, null);
        filterBottomSheetDialog = new BottomSheetDialog(CategoryModels.this);

        priceFilterCheckbox = dialogView.findViewById(R.id.price_filter_checkbox);
        priceFilterCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dialogView.findViewById(R.id.price_values_layout).setVisibility(View.VISIBLE);
                } else {
                    dialogView.findViewById(R.id.price_values_layout).setVisibility(View.GONE);
                }
            }
        });
        increaseMinPriceValue = dialogView.findViewById(R.id.increase_min_value_button);
        decreaseMinPriceValue = dialogView.findViewById(R.id.decrease_min_value_button);
        minPriceEditText = dialogView.findViewById(R.id.min_price_edittext);

        increaseMaxPriceValue = dialogView.findViewById(R.id.increase_max_value_button);
        decreaseMaxPriceValue = dialogView.findViewById(R.id.decrease_max_value_button);
        maxPriceEditText = dialogView.findViewById(R.id.max_price_edittext);

        doneButton = dialogView.findViewById(R.id.done);
        filterBottomSheetDialog.setContentView(dialogView);

        setFilterPriceListeners();
    }

    private void setFilterPriceListeners() {
        minPriceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    minPriceFilterValue = Double.parseDouble(s.toString());
                } else {
                    minPriceFilterValue = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        minPriceEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (minPriceFilterValue == 0) {
                        minPriceEditText.setText("0.0");
                    }
                }
            }
        });
        increaseMinPriceValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minPriceEditText.setText(++minPriceFilterValue + "");
            }
        });

        decreaseMinPriceValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minPriceEditText.setText(--minPriceFilterValue + "");
            }
        });


        maxPriceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    maxPriceFilterValue = Double.parseDouble(s.toString());
                } else {
                    minPriceFilterValue = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        maxPriceEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (maxPriceFilterValue == 0) {
                        maxPriceEditText.setText("0.0");
                    }
                }
            }
        });
        increaseMaxPriceValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maxPriceEditText.setText(++maxPriceFilterValue + "");
            }
        });
        decreaseMaxPriceValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maxPriceEditText.setText(--maxPriceFilterValue + "");
            }
        });
    }

    public void onDoneClicked(View view) {
        filterBottomSheetDialog.dismiss();
        if (priceFilterCheckbox.isChecked()) {
            progressBar.setVisibility(View.VISIBLE);
            mPresenter.applyPriceFilterOnCategoryModels(currentCategory, minPriceFilterValue, maxPriceFilterValue, this);
        } else {
            mPresenter.retrieveCategoryModelsItems(currentCategory.getId(), this);
        }
    }
}
