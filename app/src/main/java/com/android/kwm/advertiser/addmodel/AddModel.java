package com.android.kwm.advertiser.addmodel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
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

import com.android.kwm.BuildConfig;
import com.android.kwm.Injection;
import com.android.kwm.R;
import com.android.kwm.data.advertising_time_options.AdvertisingTimeRepository;
import com.android.kwm.data.categories.CategoriesRepository;
import com.android.kwm.data.modelItems.ModelItemsRepository;
import com.android.kwm.model.AdvertisingTimeOption;
import com.android.kwm.model.Category;
import com.android.kwm.model.ModelItem;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

public class AddModel extends AppCompatActivity implements AddModelContract.View, ModelItemsRepository.ModelItemsInsertionCallback {

    private static final String TAG = AddModel.class.getName();

    private static final int PICK_MULTIPLE_IMAGE = 2;

    private static final int CAMERA_REQUEST_CODE = 1;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private AddModelContract.Presenter mPresenter;
    private Spinner categorySpinner;
    private EditText modelNameEditText, descEditText, buyingPriceEditText, sellingPriceEditText;
    private RadioGroup advertisingTimeRadioGroup;
    private Button addImagesButton;
    private ImageButton increaseMonthButton, decreaseMonthButton, increaseDayButton, decreaseDayButton;
    private TextView monthValueTextView, dayValueTextView, totalMonthsCostTextView, totalDaysCostTextView, imagesUploadedTextView;
    private int daysValue;
    private int monthsValue;
    private double daysUnitPrice;
    private double weekUnitPrice;
    private ArrayList<String> mSelectedImageArrayUri;
    private Category mSelectedCategory;
    private ModelItem.AdvertisingTimeType mAdvertisingTimeType = ModelItem.AdvertisingTimeType.Week;
    private ProgressBar spinnerProgressBar, imagesUploadingProgressBar;
    private ProgressDialog dialog;
    private PickCaptureDialog pickCaptureDialog;
    private String[] permissions;
    private String cameraFilePath;

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
        final RadioButton weekRadioButton = findViewById(R.id.weeks);
        final RadioButton daysRadioButton = findViewById(R.id.days);

        mPresenter.retrieveAdvertisingTimeOptions(new AdvertisingTimeRepository.AdvertisingTimeCallback() {
            @Override
            public void onAdvertisingTimeRetrieved(ArrayList<AdvertisingTimeOption> options) {
                for (AdvertisingTimeOption option : options) {
                    if (ModelItem.AdvertisingTimeType.valueOf(option.getName()) == ModelItem.AdvertisingTimeType.Week) {
                        weekUnitPrice = option.getUnitPrice();
                        weekRadioButton.setText(String.format(Locale.US, "Week (%.01f KWD)", weekUnitPrice));

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
                    case R.id.weeks:
                        mAdvertisingTimeType = ModelItem.AdvertisingTimeType.Week;
                        break;
                    case R.id.days:
                        mAdvertisingTimeType = ModelItem.AdvertisingTimeType.Day;
                }
            }
        });

    }

    private void initializeCategorySpinner() {
        mPresenter.retrieveCategories(new CategoriesRepository.CategoriesCallback() {
            @Override
            public void onCategoriesRetrieved(final ArrayList<Category> categories) {
                Spinner categorySpinner = findViewById(R.id.category_spinner);
                spinnerProgressBar.setVisibility(View.INVISIBLE);
                categorySpinner.setVisibility(View.VISIBLE);

                final ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(AddModel.this, android.R.layout.simple_spinner_dropdown_item);
                for (Category category : categories) {
                    categoriesAdapter.add(category.getName());
                }

                categorySpinner.setAdapter(categoriesAdapter);
                categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mSelectedCategory = categories.get(position);
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

        imagesUploadedTextView = findViewById(R.id.images_uploaded_textview);
        imagesUploadingProgressBar = findViewById(R.id.images_uploading_progressbar);

        addImagesButton = findViewById(R.id.add_images_button);
        addImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                pickImage();
                pickCaptureDialog = new PickCaptureDialog(AddModel.this);
                pickCaptureDialog.show();
                imagesUploadingProgressBar.setVisibility(View.VISIBLE);
                imagesUploadedTextView.setVisibility(View.INVISIBLE);
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
                totalMonthsCostTextView.setText(String.format(Locale.US, "Total: %.01f KWD", monthsValue * weekUnitPrice));
            }
        });
        decreaseMonthButton = monthsValueView.findViewById(R.id.decrease_value_button);
        decreaseMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (monthsValue > 0) {
                    monthValueTextView.setText(--monthsValue + "");
                    totalMonthsCostTextView.setText(String.format(Locale.US, "Total: %.01f KWD", monthsValue * weekUnitPrice));
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
            showProgressDialog();
            mPresenter.addNewModelItem(getModelItemData(), this);
        }
        return true;
    }

    private void showProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Adding new model, Please wait..");
        dialog.setCancelable(false);
        dialog.show();
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
        if (mAdvertisingTimeType == ModelItem.AdvertisingTimeType.Week) {
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

    @SuppressLint("DefaultLocale")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            mSelectedImageArrayUri = new ArrayList<>();
            // When an Image is picked
            if (resultCode == RESULT_OK) {
                if (requestCode == PickCaptureDialog.PICK_MULTIPLE_IMAGE && null != data) {
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
                } else if (requestCode == PickCaptureDialog.CAMERA_REQUEST_CODE) {
                    mSelectedImageArrayUri.add(cameraFilePath);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
        imagesUploadingProgressBar.setVisibility(View.INVISIBLE);
        imagesUploadedTextView.setVisibility(View.VISIBLE);
        imagesUploadedTextView.setText(String.format("Selected Images : %d", mSelectedImageArrayUri.size()));
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PickCaptureDialog.REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission granted.");
                captureImage();
            } else {
                showPermissionDeniedSnackbar();
                Log.i(TAG, "Permission denied explanation.");
            }
        }
    }

    @Override
    public void setSelectCategoryErrorMessage() {
        dialog.dismiss();
        Toast.makeText(this, "You must select category", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setModelNameErrorMessage() {
        dialog.dismiss();
        modelNameEditText.setError("Model Name can't be empty");
    }

    @Override
    public void setDescErrorMessage() {
        dialog.dismiss();
        descEditText.setError("Description can't be empty");
    }

    @Override
    public void setBuyingPriceErrorMessage() {
        dialog.dismiss();
        buyingPriceEditText.setError("Buying price can't be empty");
    }

    @Override
    public void setSellingPriceErrorMessage() {
        dialog.dismiss();
        sellingPriceEditText.setError("Selling price can't be empty");
    }

    @Override
    public void setAdvertisingTimeErrorMessage() {
        dialog.dismiss();
        Toast.makeText(this, "You must select Advertising Time", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setModelImagesErrorMessage() {
        dialog.dismiss();
        Toast.makeText(this, "You must select images for the model to be added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessfullyAddingModelItem() {
        Toast.makeText(this, "The model is added successfully", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
        finish();
    }

    @Override
    public void onAddingModelItemFailed(String errmsg) {
        dialog.dismiss();
        Toast.makeText(this, errmsg, Toast.LENGTH_SHORT).show();
    }

    public void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_MULTIPLE_IMAGE);
    }

    public void captureImage() {
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            try {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean checkPermissions() {
        permissions = new String[2];
        permissions[0] = Manifest.permission.CAMERA;
        permissions[1] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(AddModel.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    public void showPermissionDeniedSnackbar() {
        showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Build intent that displays the App settings screen.
                        Intent intent = new Intent();
                        intent.setAction(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                        intent.setData(uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //This is the directory in which the file will be created. This is the default location of Camera photos
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for using again
        cameraFilePath = "file://" + image.getAbsolutePath();
        return image;
    }
}

