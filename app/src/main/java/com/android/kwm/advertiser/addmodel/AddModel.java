package com.android.kwm.advertiser.addmodel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.kwm.Injection;
import com.android.kwm.R;
import com.android.kwm.data.advertising_time_options.AdvertisingTimeRepository;
import com.android.kwm.data.categories.CategoriesRepository;
import com.android.kwm.data.modelItems.ModelItemsRepository;
import com.android.kwm.model.AdvertisingTimeOption;
import com.android.kwm.model.Category;
import com.android.kwm.model.ModelItem;

import java.util.ArrayList;
import java.util.Locale;

public class AddModel extends AppCompatActivity implements AddModelContract.View, ModelItemsRepository.ModelItemsInsertionCallback {

    private AddModelContract.Presenter mPresenter;
    private Spinner categorySpinner;
    private EditText modelNameEditText, descEditText, buyingPriceEditText, sellingPriceEditText;
    private RadioGroup advertisingTimeRadioGroup;
    private Button addImagesButton;
    private ImageButton increaseMonthButton, decreaseMonthButton, increaseDayButton, decreaseDayButton;
    private TextView monthValueTextView, dayValueTextView, totalMonthsCostTextView, totalDaysCostTextView;
    private int daysValue;
    private int monthsValue;
    private double daysUnitPrice;
    private double monthsUnitPrice;
    private static final int PICK_MULTIPLE_IMAGE = 2;
    private ArrayList<String> mSelectedImageArrayUri;
    private String mSelectedCategory;
    private ModelItem.AdvertisingTimeType mAdvertisingTimeType = ModelItem.AdvertisingTimeType.Month;
    private ProgressBar spinnerProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_model);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPresenter = new AddModelPresenter(this, Injection.provideAddModelUseCaseHandler());

        initializeViews();
        initializeCategorySpinner();
        initializeAdvertisingTimeOptions();
    }

    private void initializeAdvertisingTimeOptions() {
        advertisingTimeRadioGroup = findViewById(R.id.advertising_time_radio_group);
        final RadioButton monthsRadioButton = findViewById(R.id.months);
        final RadioButton daysRadioButton = findViewById(R.id.days);

        mPresenter.retrieveAdvertisingTimeOptions(new AdvertisingTimeRepository.AdvertisingTimeCallback() {
            @Override
            public void onAdvertisingTimeRetrieved(ArrayList<AdvertisingTimeOption> options) {
                for (AdvertisingTimeOption option : options) {
                    if (ModelItem.AdvertisingTimeType.valueOf(option.getName()) == ModelItem.AdvertisingTimeType.Month) {
                        monthsUnitPrice = option.getUnitPrice();
                        monthsRadioButton.setText(String.format(Locale.US,"Month (%.01f KWD)", monthsUnitPrice));

                    }
                    if (ModelItem.AdvertisingTimeType.valueOf(option.getName()) == ModelItem.AdvertisingTimeType.Day) {
                        daysUnitPrice = option.getUnitPrice();
                        daysRadioButton.setText(String.format(Locale.US, "Day (%.01f KWD)", daysUnitPrice));
                    }
                }
            }

            @Override
            public void onAdvertisingTimesRetrievedFailed(String errmsg) {

            }
        });
        advertisingTimeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.months :
                        mAdvertisingTimeType = ModelItem.AdvertisingTimeType.Month;
                        break;
                    case R.id.days :
                        mAdvertisingTimeType = ModelItem.AdvertisingTimeType.Day;
                }
            }
        });

    }

    private void initializeCategorySpinner() {
        mPresenter.retrieveCategories(new CategoriesRepository.CategoriesCallback() {
            @Override
            public void onCategoriesRetrieved(ArrayList<Category> categories) {
                Spinner categorySpinner = findViewById(R.id.category_spinner);
                spinnerProgressBar.setVisibility(View.INVISIBLE);
                categorySpinner.setVisibility(View.VISIBLE);

                final ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(AddModel.this, android.R.layout.simple_spinner_dropdown_item);
                for (Category category: categories) {
                    categoriesAdapter.add(category.getName());
                }

                categorySpinner.setAdapter(categoriesAdapter);
                categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mSelectedCategory = categoriesAdapter.getItem(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(AddModel.this, "no thing selected", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCategoriesRetrievedFailed(String errmsg) {
                spinnerProgressBar.setVisibility(View.INVISIBLE);
                categorySpinner.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initializeViews() {
        spinnerProgressBar = findViewById(R.id.spinner_progressbar);
        modelNameEditText = findViewById(R.id.model_name_edittext);
        descEditText = findViewById(R.id.desc_edittext);
        buyingPriceEditText = findViewById(R.id.buying_price_edittext);
        sellingPriceEditText = findViewById(R.id.selling_price_edittext);

        addImagesButton = findViewById(R.id.add_images_button);
        addImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        final View monthsValueView = findViewById(R.id.month_value_layout);
        final View daysValueView = findViewById(R.id.days_value_layout);

        monthValueTextView = monthsValueView.findViewById(R.id.value_textview);
        increaseMonthButton = monthsValueView.findViewById(R.id.increase_value_button);
        increaseMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthValueTextView.setText(++monthsValue + "");
                totalMonthsCostTextView.setText(String.format(Locale.US, "Total: %.01f KWD", monthsValue * monthsUnitPrice));
            }
        });
        decreaseMonthButton = monthsValueView.findViewById(R.id.decrease_value_button);
        decreaseMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (monthsValue > 0) {
                    monthValueTextView.setText(--monthsValue + "");
                    totalMonthsCostTextView.setText(String.format(Locale.US, "Total: %.01f KWD", monthsValue * monthsUnitPrice));
                }
            }
        });


        dayValueTextView = daysValueView.findViewById(R.id.value_textview);
        increaseDayButton = daysValueView.findViewById(R.id.increase_value_button);
        increaseDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayValueTextView.setText(++daysValue + "");
                totalDaysCostTextView.setText(String.format(Locale.US, "Total: %.01f KWD", daysValue * daysUnitPrice));
            }
        });
        decreaseDayButton = daysValueView.findViewById(R.id.decrease_value_button);
        decreaseDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (daysValue > 0) {
                    dayValueTextView.setText(++daysValue + "");
                    totalDaysCostTextView.setText(String.format(Locale.US, "Total: %.01f KWD", daysValue * daysUnitPrice));
                }
            }
        });


        totalMonthsCostTextView = findViewById(R.id.total_months_textview);
        totalDaysCostTextView = findViewById(R.id.totals_days_textview);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_model_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.done) {
            mPresenter.addNewModelItem(getModelItemData(), this);
        }
        return true;
    }

    private ModelItem getModelItemData() {
        ModelItem modelItem = new ModelItem();
        modelItem.setName(modelNameEditText.getText().toString());
        modelItem.setDesc(descEditText.getText().toString());
        try {
            modelItem.setBuyingPrice(Double.parseDouble(buyingPriceEditText.getText().toString()));
        } catch (NumberFormatException ex) {
            modelItem.setBuyingPrice(0);
        }
        try {
            modelItem.setSellingPrice(Double.parseDouble(sellingPriceEditText.getText().toString()));
        } catch (NumberFormatException ex) {
            modelItem.setSellingPrice(0);
        }
        modelItem.setAdvertisingTimeType(mAdvertisingTimeType);
        if (mAdvertisingTimeType == ModelItem.AdvertisingTimeType.Month) {
            modelItem.setAdvertisingTime(Integer.parseInt(monthValueTextView.getText().toString()));
        } else if (mAdvertisingTimeType == ModelItem.AdvertisingTimeType.Day) {
            modelItem.setAdvertisingTime(Integer.parseInt(dayValueTextView.getText().toString()));
        }
        modelItem.setCategory(mSelectedCategory);
        modelItem.setModelImages(mSelectedImageArrayUri);
        return modelItem;
    }

    @Override
    public void setPresenter(AddModelContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_MULTIPLE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            mSelectedImageArrayUri = new ArrayList<>();
            // When an Image is picked
            if (requestCode == PICK_MULTIPLE_IMAGE && resultCode == RESULT_OK && null != data) {
                // Get the Image from data
                if (data.getData() != null) {
                    Uri mImageUri = data.getData();
                    mSelectedImageArrayUri.add(mImageUri.toString());
                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();

                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mSelectedImageArrayUri.add(uri.toString());
                        }
                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setSelectCategoryErrorMessage() {
        Toast.makeText(this, "You must select category", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setModelNameErrorMessage() {
        modelNameEditText.setError("Model Name can't be empty");
    }

    @Override
    public void setDescErrorMessage() {
        descEditText.setError("Description can't be empty");
    }

    @Override
    public void setBuyingPriceErrorMessage() {
        buyingPriceEditText.setError("Buying price can't be empty");
    }

    @Override
    public void setSellingPriceErrorMessage() {
        sellingPriceEditText.setError("Selling price can't be empty");
    }

    @Override
    public void setAdvertisingTimeErrorMessage() {
        Toast.makeText(this, "You must select Advertising Time", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setModelImagesErrorMessage() {
        Toast.makeText(this, "You must select images for the model to be added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessfullyAddingModelItem() {
        Toast.makeText(this, "The model is added successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onAddingModelItemFailed(String errmsg) {
        Toast.makeText(this, errmsg, Toast.LENGTH_SHORT).show();
    }
}

