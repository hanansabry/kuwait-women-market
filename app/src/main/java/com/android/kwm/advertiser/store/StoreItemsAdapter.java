package com.android.kwm.advertiser.store;

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

public class StoreItemsAdapter extends RecyclerView.Adapter<StoreItemsAdapter.StoreItemViewHolder> {

    private final AdvertiserStoreContract.Presenter mPresenter;

    public StoreItemsAdapter(AdvertiserStoreContract.Presenter presenter) {
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
                .inflate(R.layout.store_model_item_layout, parent, false);

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


    class StoreItemViewHolder extends RecyclerView.ViewHolder implements ModelItemRowView{
        private TextView modelNameTextView, categoryNameTextView, activeInActiveTextView;
        private SwitchCompat activeSwitch;

        public StoreItemViewHolder(@NonNull View itemView) {
            super(itemView);

            modelNameTextView = itemView.findViewById(R.id.model_name_textview);
            categoryNameTextView = itemView.findViewById(R.id.category_name_textview);
            activeInActiveTextView = itemView.findViewById(R.id.item_status_textview);
            activeSwitch = itemView.findViewById(R.id.active_switch);
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
            activeInActiveTextView.setText(active? "active" : "inactive");
        }
    }
}
