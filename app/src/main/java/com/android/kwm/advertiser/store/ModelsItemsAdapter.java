package com.android.kwm.advertiser.store;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.kwm.R;
import com.android.kwm.model.ModelItem;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ModelsItemsAdapter extends RecyclerView.Adapter<ModelsItemsAdapter.StoreItemViewHolder> {

    private final AdvertiserStoreContract.Presenter mPresenter;

    public ModelsItemsAdapter(AdvertiserStoreContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void bindStoreItems(ArrayList<ModelItem> storeItems) {
        mPresenter.bindStoreItems(storeItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StoreItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_item_layout, parent, false);

        return new StoreItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreItemViewHolder holder, int position) {
        mPresenter.onBindStoreItemRowViewAtPosition(position, holder);
    }

    @Override
    public int getItemCount() {
        return mPresenter.getStoreItemsListCount();
    }


    class StoreItemViewHolder extends RecyclerView.ViewHolder implements ModelItemRowView, View.OnClickListener {
        private TextView modelNameTextView, categoryNameTextView, activeInActiveTextView
                , shopNameTextView, itemPriceTextView;
        private SwitchCompat activeSwitch;
        private Context context;
        private View categoryView, shopPriceView;

        public StoreItemViewHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();
            modelNameTextView = itemView.findViewById(R.id.model_name_textview);
            categoryNameTextView = itemView.findViewById(R.id.category_name_textview);
            activeInActiveTextView = itemView.findViewById(R.id.item_status_textview);
            activeSwitch = itemView.findViewById(R.id.active_switch);
            shopNameTextView = itemView.findViewById(R.id.selling_shop_name_textview);
            itemPriceTextView = itemView.findViewById(R.id.item_price_textview);

            categoryView = itemView.findViewById(R.id.category_view);
            shopPriceView = itemView.findViewById(R.id.advertiser_price_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void setModelItemName(String modelName) {
            modelNameTextView.setText(modelName);
        }

        @Override
        public void setModelCategoryName(String categoryName) {
            categoryNameTextView.setText(categoryName);
        }

        @Override
        public void setModelItemSwitchActiveInaActive(boolean active) {
            activeSwitch.setChecked(active);
            activeInActiveTextView.setText(active? "Active" : "Inactive");
            if (active) {
                activeInActiveTextView.setTextColor(context.getResources().getColor(R.color.colorAccent));
            } else {
                activeInActiveTextView.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            }
        }

        @Override
        public void setSellingShopName(String name) {
            shopNameTextView.setText(name);
        }

        @Override
        public void setModelItemPrice(String price) {
            itemPriceTextView.setText(price);
        }

        @Override
        public void setCategoryViewVisibility(boolean visible) {
            categoryView.setVisibility(visible? View.VISIBLE : View.INVISIBLE);
        }

        @Override
        public void setShopNameAndPriceVisibility(boolean visible) {
            shopPriceView.setVisibility(visible? View.VISIBLE : View.INVISIBLE);
        }

        @Override
        public void onClick(View v) {
            mPresenter.onModelItemClicked(getAdapterPosition());
        }
    }
}
