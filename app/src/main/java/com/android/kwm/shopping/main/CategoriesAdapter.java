package com.android.kwm.shopping.main;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.kwm.R;
import com.android.kwm.model.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {

    private final HomeContract.Presenter mPresenter;

    public CategoriesAdapter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void bindCategories(ArrayList<Category> categories) {
        mPresenter.bindCategories(categories);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item_layout, parent, false);

        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        mPresenter.onBindCategoryItemRowViewAtPosition(position, holder);
    }

    @Override
    public int getItemCount() {
        return mPresenter.getCategoriesSize();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder implements CategoryItemRowView, View.OnClickListener {

        private ImageView categoryImageView;
        private TextView categoryNameTextView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryImageView = itemView.findViewById(R.id.category_imageview);
            categoryNameTextView = itemView.findViewById(R.id.category_name_textview);

            itemView.setOnClickListener(this);
        }

        @Override
        public void setCategoryImage(String uri) {
            if (uri == null) {
                uri = "path";
            }
            Picasso.get()
                    .load(uri)
                    .placeholder(R.drawable.category_placeholder)
                    .into(categoryImageView);
        }

        @Override
        public void setCategoryName(String name) {
            categoryNameTextView.setText(name);
        }

        @Override
        public void onClick(View v) {
            mPresenter.onCategoryClicked(getAdapterPosition());
        }
    }
}
