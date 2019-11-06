package com.android.kwm.shopping.itemdetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.kwm.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemImageSliderAdapter extends SliderViewAdapter<ItemImageSliderAdapter.SliderAdapterVH> {

    private ModelItemDetailsContract.Presenter mPresenter;

    public ItemImageSliderAdapter(ModelItemDetailsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void bindItemImages(ArrayList<String> uris) {
        mPresenter.bindItemImagesUris(uris);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        mPresenter.onBindItemImageAtPosition(viewHolder, position);
    }

    @Override
    public int getCount() {
        return mPresenter.getItemImagesSize();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder implements ItemImageRowView{

        ImageView imageViewBackground;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
        }

        @Override
        public void setImageIntoSlider(String uri) {
            Picasso.get().load(uri).into(imageViewBackground);
        }
    }
}