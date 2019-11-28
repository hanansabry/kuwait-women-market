package com.android.kwm.shopping.itemdetails;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.kwm.Injection;
import com.android.kwm.R;
import com.android.kwm.model.ModelItem;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class ModelItemDetails extends AppCompatActivity implements ModelItemDetailsContract.View {

    private TextView categoryNameTitleTextView, modelNameTitleTextView, priceTitleTextView, categoryNameDetailsTextView
            , modelNameDetailsTextView, priceDetailsView, modelDescTextView,sellingShopNameTextView
            , shopPhoneTextView, shopEmailTextView;
    private ModelItemDetailsContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_item_details);

        mPresenter = new ItemDetailsPresenter(this, Injection.provideAdvertiserRepository(), Injection.provideModelItemsRepository());

        ModelItem modelItem = (ModelItem) getIntent().getExtras().get(ModelItem.MODEL);
        initializeViews();
        bindModelDetailsToViews(modelItem);
        setupSliderView(modelItem.getModelImages());
        mPresenter.updateNumberOfViews(modelItem.getId(), modelItem.getNumberOfViews());
    }

    private void initializeViews() {
        categoryNameTitleTextView = findViewById(R.id.category_name_textview);
        modelNameTitleTextView = findViewById(R.id.model_name_textview);
        priceTitleTextView = findViewById(R.id.price_textview);

        categoryNameDetailsTextView = findViewById(R.id.category_name_details_textview);
        modelNameDetailsTextView = findViewById(R.id.model_name_details__textview);
        priceDetailsView = findViewById(R.id.model_price_details_textview);
        modelDescTextView = findViewById(R.id.model_description_textview);

        sellingShopNameTextView = findViewById(R.id.selling_shop_name_textview);
        shopPhoneTextView = findViewById(R.id.shop_phone_textview);
        shopEmailTextView = findViewById(R.id.shop_email_textview);
    }

    private void bindModelDetailsToViews(ModelItem modelItem) {
        categoryNameTitleTextView.setText(modelItem.getCategory().getName());

        modelNameTitleTextView.setText(modelItem.getName());

        priceTitleTextView.setText(String.format("%s KWD", modelItem.getSellingPrice()));

        categoryNameDetailsTextView.setText(String.format(getResources().getString(R.string.category_name_value),
                modelItem.getCategory().getName()));

        modelNameDetailsTextView.setText(String.format(getResources().getString(R.string.model_name_value),
                modelItem.getName()));

        priceDetailsView.setText(String.format(getResources().getString(R.string.price_value),
                modelItem.getSellingPrice()));

        modelDescTextView.setText(modelItem.getDesc());

        sellingShopNameTextView.setText(String.format(getResources().getString(R.string.selling_shop_name_value),
                modelItem.getAdvertiser().getName()));

        shopPhoneTextView.setText(String.format(getResources().getString(R.string.shop_phone_value),
                modelItem.getAdvertiser().getPhone()));

        shopEmailTextView.setText(String.format(getResources().getString(R.string.shop_email_value),
                modelItem.getAdvertiser().getEmail()));
    }

    private void setupSliderView(ArrayList<String> uris) {
        SliderView sliderView = findViewById(R.id.imageSlider);
        ItemImageSliderAdapter adapter = new ItemImageSliderAdapter(mPresenter);
        sliderView.setSliderAdapter(adapter);
        sliderView.startAutoCycle();
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        adapter.bindItemImages(uris);
    }

    public void onBackClicked(View view) {
        onBackPressed();
    }

    @Override
    public void setPresenter(ModelItemDetailsContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
