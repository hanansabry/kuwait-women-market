package com.android.kwm.shopping.itemdetails;

import com.android.kwm.data.advertisers.AdvertiserRepository;

import java.util.ArrayList;

public class ItemDetailsPresenter implements ModelItemDetailsContract.Presenter {

    private ArrayList<String> imagesUris = new ArrayList<>();
    private final ModelItemDetailsContract.View mView;
    private final AdvertiserRepository mAdvertiserRepository;

    public ItemDetailsPresenter(ModelItemDetailsContract.View view, AdvertiserRepository advertiserRepository) {
        mView = view;
        mAdvertiserRepository = advertiserRepository;
        mView.setPresenter(this);
    }

    @Override
    public int getItemImagesSize() {
        return imagesUris.size();
    }

    @Override
    public void onBindItemImageAtPosition(ItemImageRowView holder, int position) {
        holder.setImageIntoSlider(imagesUris.get(position));
    }

    @Override
    public void bindItemImagesUris(ArrayList<String> uris) {
        imagesUris = uris;
    }

    @Override
    public void getAdvertiserDetails(String id, AdvertiserRepository.RetrieveAdvertiserCallback callback) {
        mAdvertiserRepository.retrieveAdvertiserById(id, callback);
    }

    @Override
    public void start() {

    }
}
